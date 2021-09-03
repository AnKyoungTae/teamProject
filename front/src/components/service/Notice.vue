<template>
  <div class="container" style="width: 1000px">
    <div class="notice col-12 h-50">
      <!-- 리스트 시작 -->
      <div class="accordion accordion-flush" id="accordionFlushExample">
        <div
          class="accordion-item"
          style="border-top: 2px solid gray"
          v-for="(notice, index) in this.FAQ"
          :key="notice.id"
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading' + index">
            <button
              class="accordion-button collapsed faqButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse' + index"
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse' + index"
            >
              <strong style="font-size: 18px">{{ notice.title }}</strong>
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse' + index"
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-heading' + index"
            data-bs-parent="#accordionFlushExample"
          >
            <div class="accordion-body">{{ notice.body }}</div>
          </div>
        </div>
      </div>
      <!-- 리스트 끝 -->
      <div style="border-top: 2px solid gray"></div>
    </div>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";
export default {
  data() {
    return {
      paging: [], //페이징
      FAQ: [], //리스트
      listSize: 10, //리스트 수
      rangeSize: 5, //버튼 수
    };
  },
  methods: {
    listPage(articleId) {
      //페이지 이동
      return this.$router.push({
        name: "BoardList",
        query: { board: "notice", articleId: articleId },
      });
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
    numPage(page, range, listSize, rangeSize) {
      //페이지 번호로 이동 axios
      authAPI.getBoardList(1, page, range, listSize, rangeSize).then((res) => {
        this.setPagingNotices(res.data.paging);

        res.data.list.forEach((element) => {
          //vuex에 데이터 넣기
          this.setCenternotices(element);
        });
      });
    },
    //공지사항 리스트 불러오기
    async downAllList(boardId, page, range) {
      //리스트 axios 통신 query = boardId  page:페이지  range: 범위  boardId
      await authAPI
        .getBoardList(boardId, page, range)
        .then((res) => {
          //페이징
          this.paging = res.data.paging;
          //리스트
          this.FAQ = res.data.list.reverse();
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  //고객센터통해 접근할 경우
  mounted() {
    this.downAllList(1, 1, 1);
  },
  //버튼 클릭시
  updated() {
    //(this.listSize = this.paging.listSize),
    //this.rangeSize = this.paging.rangeSize
  },
};
</script>

<style scoped>
.faqButton {
  padding: 20px;
}
</style>
