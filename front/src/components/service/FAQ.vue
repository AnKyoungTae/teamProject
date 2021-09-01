<template>
<div class="container" style="width: 1000px;">
  <div class="notice col-12 h-50">
    <ul class="list-group list-group-horizontal" style="border-top: 2px solid gray;">
      <li class="list-group-item noticeTitle" style="width: 100px;"><strong>No.</strong></li>
      <li class="list-group-item noticeTitle" style="width: 700px;"><strong>제목</strong></li>
      <li class="list-group-item noticeTitle" style="width: 200px;"><strong>시간</strong></li>
    </ul>
    <!-- 리스트 시작 -->
    <ul class="list-group list-group-horizontal" 
        style="cursor: pointer; border-top: 2px solid gray;"
        @click="listPage(notice.articleId)"
        v-for="(notice, index) in this.notices"
        :key="notice.id">
      <li class="list-group-item noticeList" style="border: 0px; width: 100px; justify-content: center;">
        <span>
          {{ ++index }}.
        </span>
      </li>
      <li class="list-group-item noticeList" style="border: 0px; width: 700px;">
        <strong style="font-size: 18px;">
          {{ notice.title }}
        </strong>
      </li>
      <li class="list-group-item noticeList" style="border: 0px; width: 200px; justify-content: center;">
        <span>
          {{ notice.regDate }}
        </span>
      </li>
      <li v-if="notice.length < 0" style="font-weight: 700; font-size: 2vh;"> 글이 없습니다.</li>
    </ul>
    <!-- 끝 -->
    <div style="border-top: 2px solid gray;"></div>
    <!-- 순서 버튼 -->
    <nav
      aria-label="Page navigation example"
      class="mt-5 position-relative .center-block"
      style=""
    >
      <ul class="pagination position-absolute" style="left: 30vw">
        <!-- 이전 순서 버튼 -->
        <li
          class="page-item"
          v-if="this.paging.prev == true"
          @click="prevBotton(paging.range, paging.rangeSize, this.listSize)"
        >
          <a class="page-link" href="#" aria-label="Previous">
            <span aria-hidden="true">&laquo;</span>
          </a>
        </li>
        <!-- 숫자 순서 버튼 -->
        <li class="page-item" v-for="num in this.pageList" :key="num">
          <a class="page-link" href="#" @click="numPage(num, this.paging.range, this.listSize,this.rangeSize)" v-if="paging.page != num">{{num}}</a>
          <a class="page-link" href="#" @click="numPage(num, this.paging.range, this.listSize,this.rangeSize)" style="background-color: #0d6efd; color: #fff;" v-else>{{num}}</a>
        </li>

        <!-- 다음 순서 버튼 -->
        <li
          class="page-item"
          v-if="this.paging.next == true"
          @click="nextBotton(paging.range, paging.rangeSize, this.listSize)"
        >
          <a class="page-link" href="#" aria-label="Next">
            <span aria-hidden="true">&raquo;</span>
          </a>
        </li>
      </ul>
    </nav>
    <!-- 끝 -->
  </div>
</div>
</template>

<script>
import * as authAPI from "@/api/article.js";


export default {
  data() {
    return {
      paging: [],
      notices:[],

      pageList:[],

      listSize: 10, //리스트 수
      rangeSize: 5, //버튼 수
    };
  },
  
  methods: {
    
    listPage(articleId) {
      //페이지 이동
      console.log(articleId);
      return (this.$router.push({name: 'BoardList', query: { "board": "notice", "articleId": articleId}}))
      //location.href = "/boardList?board=notice&articleId=" + articleId;
    },
    prevBotton(range1, rangeSize, listSize) {
      var page = (range1 - 2) * rangeSize + 1;
      var range = range1 - 1;

      this.numPage(page, range, listSize, rangeSize);
    },
    nextBotton(range1, rangeSize, listSize) {

      let page = parseInt(range1 * rangeSize) + 1;
      let range = range1 + 1;
 
      this.numPage(page, range, listSize, rangeSize);
    },

    //페이지 번호로 이동 axios
    numPage(page, range, listSize, rangeSize) {
      
      authAPI.getBoardList(1, page, range, listSize, rangeSize).then((res) => {
        this.paging = res.data.paging

        this.notices = res.data.list
      });
    },

    //Q&A 리스트 불러오기
    async downAllList(boardId, page, range) {
      //리스트 axios 통신 query = boardId  page:페이지  range: 범위  boardId
      await authAPI
        .getBoardList(boardId, page, range)
        .then((res) => {
          console.log(res)
          //페이징
          this.paging = res.data.paging
          //리스트
          this.notices = res.data.list
           //페이징 리스트
          this.pageLists()
        })
        .catch((err) => {
          console.log(err);
        });
    },

    //페이지 리스트
    pageLists() {
      for(let i = this.paging.startPage; i <= this.paging.endPage; i++){
        this.pageList.push(i)
      }
    }

  },
  //고객센터통해 접근할 경우
  mounted() {
    this.downAllList(1,1,1)
    
  },
  //버튼 클릭시
  updated() {
    
  },
  
};
</script>

<style scoped>
.noticeList {
  padding-top: 30px;
  padding-bottom: 30px;
  display: flex;
  align-items: center;
}
.noticeTitle {
  border: 0px;
  padding-top: 15px;
  padding-bottom: 15px;
  font-size: 18px;
}
</style>
