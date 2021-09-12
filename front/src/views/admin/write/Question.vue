<template>
  <WriteQuestion v-if="toggle == 'write'" :articleId='articleId' @changeToggle="changeToggle"></WriteQuestion>

  <div v-else>
    <div>
      전체<input type="radio" id="all" name="hidden" value="ALL" @click="changeStatus('ALL')" checked>
      비공개<input type="radio" name="hidden" value="Hidden"  @click="changeStatus('Hiden')"/>
    </div>
    <hr>
    <div>
      <h2>댓글 여부</h2>
      있음<input type="radio"  name="comment" value="ALL" @click="changechildren('ON')" >
      없음<input type="radio" id="comment" name="comment" value="Hidden"  @click="changechildren('OFF')" checked />
    </div>

    <table class="table table-striped">
        <tbody>
          <tr>
            <th>번호</th>
            <th>구분</th>
            <th>제목</th>
            <th>날짜</th>
            <th>조회수</th>
            <th>공개 여부</th>
          </tr>
          <tr
            class="listRow"
            style="cursor: pointer"
            v-for="(qn, index) in this.queAn"
            :key="index"
            @click="listPage(qn.articleId)"
          >
            <td class="col-1">{{ startListNum + index +1 }}</td>
            <td class="col-1" v-if="qn.boardId == 4">주문</td>
            <td class="col-1" v-else-if="qn.boardId == 5">딜리버리 주문</td>
            <td class="col-1" v-else-if="qn.boardId == 6">제품/품질/서비스</td>
            <td class="col-1" v-else-if="qn.boardId == 7">
              답글 <i class="fas fa-long-arrow-alt-right"></i>
            </td>
            <td class="col-1" v-else>기타</td>

            <td class="col-5 text-left">
              <span>
                {{ qn.title }}
              </span>
              <span v-if="qn.children > 0"> [{{ qn.children }}] </span>
            </td>
            <td class="col-2">{{ qn.regDate }}</td>
            <td class="col-1">{{ qn.hit }}</td>
            <td class="col-1" v-if="qn.status != 'H'">
              <i class="fas fa-lock-open"></i>
            </td>
            <td class="col-1" v-else>
              <i class="fas fa-lock"></i>
            </td>
          </tr>
          <tr v-if="queAn.length == 0">
            <td colspan="6" style="font-weight: 700; font-size: 2vh">
              글이 없습니다
            </td>
          </tr>
        </tbody>
      </table>


      <!-- 순서 버튼 -->
      <nav
        aria-label="Page navigation example "
        class="mt-5  .center-block"
        
        style="margin: 0 auto; width: 5vw;"
      >
        <ul class="pagination " style="left: 30vw">
          <!-- 이전 순서 버튼 -->
          <li
            class="page-item"
            v-if="this.paging.prev == true"
            @click="prevBotton(paging.range, rangeSize, listSize)"
          >
            <a class="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <!-- 숫자 순서 버튼 -->
          <li
            class="page-item"
            v-for="num in pageList"
            :key="num"
            :style="{ ontSize: '20px' }"
          >
            <a
              class="page-link"
              href="#"
              @click="
                downAllList(
                  num,
                  this.paging.range,
                )
              "
              v-if="paging.page != num"
              >{{ num }}</a
            >
            <a
              class="page-link"
              href="#"
              @click="
                downAllList(
                  num,
                  this.paging.range,
                )
              "
              style="background-color: #0d6efd; color: #fff"
              v-else
              >{{ num }}</a
            >
          </li>

          <!-- 다음 순서 버튼 -->
          <li
            class="page-item"
            v-if="this.paging.next == true"
            @click="nextBotton(paging.range, rangeSize, listSize)"
          >
            <a class="page-link" href="#" aria-label="Next">
              <span aria-hidden="true">&raquo;</span>
            </a>
          </li>
        </ul>
      </nav>

  </div>
</template>

<script>
import http from "@/api/http";
import WriteQuestion from "./WriteQuestion.vue"

export default {
  components: {WriteQuestion},

  data() {
    return{
      total: 0,
      
      paging: [], //페이징
      queAn: [], //Q&A리스트
      pageList: [],
      listSize: 10, //리스트 수
      rangeSize: 5, //버튼 수,

      status: 'ALL',  //글 상태
      children: 'OFF', //댓글 여부
      search: null, //검색


      toggle: 'none',
      articleId: 0
    }
  },
  computed:{
    startListNum() {
      return (this.paging.page - 1) * this.paging.listSize;
    }
  },
  methods: {
    changeToggle(word){
      this.toggle = word
      if(word == "change"){
        this.status = 'ALL',  //글 상태
        this.children = 'OFF', //댓글 여부
        this.downAllList(1,1)
      }
      
    },
    //통신
    downAllList(page, range) {
      http.post("/article/deleteAdminQuestion", {
        params: {
          page: page,
          range: range,
          listSize: this.listSize,
          rangeSize: this.rangeSize,

          status: this.status,
          children: this.children,
          search: this.search
        }
      })
      .then(res=> {
        console.log(res.data)
        this.paging =res.data.paging
        this. queAn = res.data.list
        this.total = res.data.total

        this.pageList = []
        this.pageLists(res.data.paging.startPage, res.data.paging.endPage);
      })
    },
    //글 공개 여부
    changeStatus(word){
      this.status= 'ALL',  //글 상태
      this.children= 'OFF', //댓글 여부
      this.search= null, //검색

      this.status = word
      this.downAllList(1,1)
    },
    //댓글 여부
    changechildren(word){
      this.children = word
      this.downAllList(this.paging.page, this.paging.range)
    },

    prevBotton(range1, rangeSize, listSize) {
      var page = (range1 - 2) * rangeSize + 1;
      var range = range1 - 1;
      this.numPage(page, range, rangeSize, listSize);
    },
    nextBotton(range1, rangeSize, listSize) {
      let page = parseInt(range1 * rangeSize) + 1;
      let range = range1 + 1;

      this.numPage(page, range, rangeSize, listSize);
    },
    //페이지 이동
    listPage(articleId) {
      this.articleId = articleId
      this.changeToggle('write')
    },
    //페이지 리스트
    pageLists(start, end) {
      for (let i = start; i <= end; i++) {
        this.pageList.push(i);
      }
    },
  },
  mounted(){
    this.downAllList(1,1)
    
  }
}
</script>

<style>

</style>