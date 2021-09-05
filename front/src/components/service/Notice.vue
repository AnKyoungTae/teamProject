<template>
  <div class="container" style="width: 1000px">
    <div class="notice col-12 h-50">
      <ul
        class="list-group list-group-horizontal"
        style="border-top: 2px solid gray"
      >
        <li class="list-group-item noticeTitle" style="width: 100px">
          <strong>No.</strong>
        </li>
        <li class="list-group-item noticeTitle" style="width: 700px">
          <strong>제목</strong>
        </li>
        <li class="list-group-item noticeTitle" style="width: 200px">
          <strong>시간</strong>
        </li>
      </ul>
      <!-- 리스트 시작 -->
      <ul
        class="list-group list-group-horizontal"
        style="cursor: pointer; border-top: 2px solid gray"
        @click="listPage(notice.articleId)"
        v-for="notice in this.notices"
        :key="notice.id"
      >
        <li
          class="list-group-item noticeList"
          style="border: 0px; width: 100px; justify-content: center"
        >
          <span> {{ notice.nickname }}. </span>
        </li>
        <li
          class="list-group-item noticeList"
          style="border: 0px; width: 700px"
        >
          <strong style="font-size: 18px">
            {{ notice.title }}
          </strong>
        </li>
        <li
          class="list-group-item noticeList"
          style="border: 0px; width: 200px; justify-content: center"
        >
          <span>
            {{ notice.regDate }}
          </span>
        </li>
        <li v-if="notice.length == 0" style="font-weight: 700; font-size: 2vh">
          글이 없습니다.
        </li>
      </ul>
      <!-- 끝 -->
      <div style="border-top: 2px solid gray"></div>
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
            @click="prevBotton(paging.range, this.rangeSize, this.listSize)"
          >
            <a class="page-link" href="#" aria-label="Previous">
              <span aria-hidden="true">&laquo;</span>
            </a>
          </li>
          <!-- 숫자 순서 버튼 -->
          <li class="page-item" v-for="num in this.pageList" :key="num">
            <a
              class="page-link"
              href="#"
              @click="
                numPage(num, this.paging.range, this.rangeSize, this.listSize)
              "
              v-if="paging.page != num"
              >{{ num }}</a
            >
            <a
              class="page-link"
              href="#"
              @click="
                numPage(num, this.paging.range, this.rangeSize, this.listSize)
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
            @click="nextBotton(paging.range, this.rangeSize, this.listSize)"
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
      notices: [],
      pageList: [],
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
      this.numPage(page, range, rangeSize, listSize);
    },
    nextBotton(range1, rangeSize, listSize) {
      let page = parseInt(range1 * rangeSize) + 1;
      let range = range1 + 1;

      this.numPage(page, range, rangeSize, listSize);
    },
    //페이지 번호로 이동 axios
    numPage(page, range, rangeSize, listSize) {
      authAPI.getBoardList(2, page, range, rangeSize, listSize).then((res) => {
        this.paging = res.data.paging;
        this.notices = res.data.list.reverse();
        this.pageList = [];
        this.pageLists(res.data.paging.startPage, res.data.paging.endPage);
        //리스트 페이지 처리
        let maxnum = res.data.paging.page == 1 ? 10 : 10 * res.data.paging.page;
        if (res.data.paging.total - 10 * page < 0) {
          maxnum =
            res.data.paging.startList +
            10 -
            -(res.data.paging.total - 10 * page);
        }
        let num = 0;

        for (let i = res.data.paging.startList; i < maxnum; i++) {
          if (i < this.notices.length) {
            this.notices[num].nickname = num + 1;
          } else {
            this.notices[num].nickname = i + 1;
          }
          num++;
        }
      });
    },
    //Q&A 리스트 불러오기
    async downAllList(boardId, page, range) {
      //리스트 axios 통신 query = boardId  page:페이지  range: 범위  boardId
      await authAPI
        .getBoardList(boardId, page, range)
        .then((res) => {
          //페이징
          this.paging = res.data.paging;
          //리스트
          this.notices = res.data.list.reverse();
          //페이징 리스트
          this.pageList = [];
          this.pageLists(res.data.paging.startPage, res.data.paging.endPage);
          //리스트 페이지 처리
          let maxnum =
            res.data.paging.page == 1 ? 10 : 10 * res.data.paging.page;
          if (res.data.paging.total - 10 * page < 0) {
            maxnum =
              res.data.paging.startList +
              10 -
              -(res.data.paging.total - 10 * page);
          }
          let num = 0;

          for (let i = res.data.paging.startList; i < maxnum; i++) {
            if (i < this.notices.length) {
              this.notices[num].nickname = num + 1;
            } else {
              this.notices[num].nickname = i + 1;
            }
            num++;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //페이지 리스트
    pageLists(start, end) {
      for (let i = start; i <= end; i++) {
        this.pageList.push(i);
      }
    },
  },
  //고객센터통해 접근할 경우
  mounted() {
    this.downAllList(2, 1, 1);
  },
  //버튼 클릭시
  updated() {},
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
