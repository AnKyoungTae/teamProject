<template>
  <WriteQuestion v-if="toggle == 'write'" :articleId='articleId' @changeToggle="changeToggle"></WriteQuestion>

  <div v-else  style="width: 1000px">
    <div class="qna-select">
      <div style="padding-right:50px;">
        <h3>공개 여부</h3>
        전체<input type="radio" name="hidden" value="ALL" @click="changeStatus('ALL')" checked>
        비공개<input type="radio" name="hidden" value="Hidden"  @click="changeStatus('Hiden')"/>
      </div>
      <div>
        <h3>댓글 여부</h3>
        있음<input type="radio" name="comment" value="ALL" @click="changechildren('ON')" >
        없음<input type="radio" name="comment" value="Hidden"  @click="changechildren('OFF')" checked />
      </div>
    </div>

    <table class="table table-striped" style="text-align: center; vertical-align: middle;">
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
          <td class="col-1">
            <div v-if="qn.boardId == 4">주문</div>
            <div v-else-if="qn.boardId == 5">딜리버리 주문</div>
            <div v-else-if="qn.boardId == 6">제품/품질/서비스</div>
            <div v-else-if="qn.boardId == 7">답글 <i class="fas fa-long-arrow-alt-right"></i></div>
            <div v-else>기타</div>
          </td>

          <td class="col-5" style="text-align: left; padding-left:20px; padding-right:20px;">
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
      class="mt-5 position-relative .center-block"
    >
      <ul class="pagination QNA-btn">
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
.QNA-btn {
  position:absolute;
  left:50%;
  transform: translate(-50%, 30%);
}
.qna-select {
  display: flex;
  justify-content: center;
  padding:30px;
}
</style>