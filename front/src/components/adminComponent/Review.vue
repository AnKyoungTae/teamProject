<template>
  <div class="review">
    <!-- 노티피케이션 -->
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div class="review-left">
      <!-- 평점 -->
      <span style="font-weight: 600; font-size: 35px">{{ averageScore }}</span
      ><span style="color: lightgray; font-size: 28px"> / 5</span>
      <div style="font-size: 13px; margin-top: -10px">
        <!-- 별 -->
        <i
          class="fas fa-star"
          v-for="index of parseInt(averageScore)"
          :key="index"
        ></i>
      </div>
    </div>
  </div>
  <div class="review2">
    <div style="float: left; width: 70%">
      <!-- 글의 수 카운팅 -->
      <p class="review2-p">
        리뷰 <span style="font-weight: 600">{{ totalCount }}</span
        >개 · 사장님 댓글
        <span style="font-weight: 600">{{ totalCountHasReply }}</span
        >개
      </p>
    </div>
    <!-- 리뷰쓰기버튼 -->
    <div
      style="float: right; width: 20%; margin-top: 5px; margin-right: 20px"
      @click="popReviewModal(food)"
    >
      <div class="review2-div reviewWrite" style="height: 40px">
        <div>
          <i class="fas fa-pencil-alt"></i>
          <span class="m-1">리뷰쓰기</span>
        </div>
      </div>
    </div>
  </div>
  <div class="row">
    <!-- 맨 윗줄 카테고리영역 -->
    <div
      class="col btn btn-light spread-underline"
      @click="setStatusOption('recent')"
    >
      <span :class="[this.showOption === 'recent' ? 'choiced' : '']">
        최신순보기
      </span>
    </div>
    <div
      class="col btn btn-light spread-underline"
      @click="setStatusOption('score')"
    >
      <span :class="[this.showOption === 'score' ? 'choiced' : '']">
        별점순보기
      </span>
    </div>
  </div>
  <hr style="margin-top: 0px" />
  <!-- 리뷰로우 -->
  <div v-if="!pageLoaded">
    <!-- 페이지 로딩되지않았을 때 -->
    <div class="spinner-border" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
  <div v-else>
    <div v-for="(review, index) of reviewList" :key="index" class="m-2">
      <review-row :data="review" :isOwner="isOwner"></review-row>
    </div>
  </div>
  <hr />
  <!-- 리뷰끝 -->
  <!-- 페이지네이션 -->
  <nav aria-label="Page navigation">
    <ul class="pagination justify-content-center">
      <li class="page-item" :class="{ disabled: !hasPreviousPage }">
        <a
          class="page-link"
          aria-disabled="true"
          @click="requestPage(firstPageOfthisIndex - 1)"
          :class="{ 'available-link': hasPreviousPage }"
          >이전목록</a
        >
        <!-- 현재 단락의 가장 첫번째 페이지 -1을 요청해야함. -->
      </li>
      <!-- 페이지순번 -->
      <div v-for="(index, i) in maxIndex" :key="i">
        <div v-if="index + (currentIndex - 1) * maxIndex <= totalPages">
          <li
            class="page-item"
            v-if="
              (this.currentIndex - 1) * this.showindex + index != currentPage
            "
          >
            <a
              class="page-link available-link"
              @click="requestPage(index + (currentIndex - 1) * maxIndex)"
              >{{ index + (currentIndex - 1) * maxIndex }}</a
            >
          </li>
          <li class="page-item active" v-else>
            <span class="page-link">{{
              index + (currentIndex - 1) * maxIndex
            }}</span>
          </li>
        </div>
      </div>

      <li class="page-item" :class="{ disabled: !hasNextPage }">
        <a
          class="page-link"
          @click="requestPage(lastPageOfthisIndex + 1)"
          :class="{ 'available-link': hasNextPage }"
          >다음목록</a
        >
      </li>
    </ul>
  </nav>
  <!-- 모달 -->
  <review-modal
    :data="reviewModalData"
    v-if="this.$store.state.reviewModal == true"
    :storeId="storeId"
  ></review-modal>
</template>

<script>
import ReviewModal from "@/components/modal/Review.vue";
import { normal, error, success } from "@/api/notification";
import http from "@/api/http";
import ReviewRow from "@/components/adminComponent/ReviewRow.vue";
import { mapGetters } from "vuex";

import { mapMutations } from "vuex";
export default {
  props: ["storeId"],
  components: {
    ReviewModal,
    ReviewRow,
  },
  data() {
    return {
      reviewModalData: null,
      reviewList: [],
      currentPage: 1, // 현재 페이지
      listPerPage: 10, // 한번에 보여줄 리스트숫자
      totalCount: 0, // 총 게시글 수
      showindex: 5, // 번호로 표시될 페이지 총 갯수
      showOption: "recent",
      pageLoaded: false,
      averageScore: null,
      totalCountHasReply: null,
      isOwner: false,
    };
  },
  computed: {
    ...mapGetters({
      getUserId: "auth/getUserId",
      getMyStore: "auth/getMyStore",
    }),
    startListNum() {
      return (this.currentPage - 1) * this.listPerPage;
    },
    hasNextPage() {
      return this.currentIndex < this.totalIndex;
    },
    hasPreviousPage() {
      return this.currentIndex > 1;
    },
    totalPages() {
      let remains = this.totalCount % this.listPerPage;
      // 나머지가 있으면 1을 더하고, 0으로 떨어지면 그냥 준다.
      if (remains == 0) {
        return parseInt(this.totalCount / this.listPerPage);
      }
      return parseInt(this.totalCount / this.listPerPage) + 1;
    },
    maxIndex() {
      // 최대 표시 번호 수
      if (this.totalPages < this.showindex) {
        return this.totalPages;
      }
      return this.showindex;
    },
    currentIndex() {
      // 현재있는 단락
      return this.currentPage % this.maxIndex == 0
        ? parseInt(this.currentPage / this.maxIndex)
        : parseInt(this.currentPage / this.maxIndex) + 1;
    },
    totalIndex() {
      // 최대 단락
      return this.totalPages % this.maxIndex == 0
        ? parseInt(this.totalPages / this.maxIndex)
        : parseInt(this.totalPages / this.maxIndex) + 1;
    },
    firstPageOfthisIndex() {
      return (this.currentIndex - 1) * this.maxIndex + 1;
    },
    lastPageOfthisIndex() {
      return this.currentIndex * this.maxIndex;
    },
  },
  methods: {
    ...mapMutations(["SET_MODAL_REVIEW"]),
    popReviewModal(food) {
      this.reviewModalData = food;
      this.SET_MODAL_REVIEW(true);
    },
    requestPage(request) {
      this.pageLoaded = false;
      console.log("요청페이지 : " + request);
      this.requestListCount();
      let storeId = parseInt(this.storeId);

      const data = {
        listPerPage: this.listPerPage,
        currentPage: request,
        storeId: storeId,
        showOption: this.showOption,
      };
      http
        .post("/review/getReviewList", data)
        .then((response) => {
          if (response.status === 200) {
            console.log(response.data);
            this.reviewList = response.data;
            this.currentPage = request;
            console.log("현재페이지 : " + this.currentPage);
            this.pageLoaded = true;
          }
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
        });
    },

    requestListCount() {
      const storeId = parseInt(this.storeId);
      http
        .get("/review/getReviewListCount", {
          params: {
            storeId: storeId,
          },
        })
        .then((response) => {
          this.totalCount = response.data;
          console.log("등록된 리뷰의 총 갯수 : " + this.totalCount);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    requestListHasReplyCount() {
      const storeId = parseInt(this.storeId);
      http
        .get("/review/getReviewListHasReplyCount", {
          params: {
            storeId: storeId,
          },
        })
        .then((response) => {
          this.totalCountHasReply = response.data;
          console.log(
            "등록된 사장님댓글의 총 갯수 : " + this.totalCountHasReply
          );
        })
        .catch((err) => {
          console.log(err);
        });
    },
    setStatusOption(value) {
      this.showOption = value;
      this.requestPage(1);
    },
    getAverageScore() {
      const storeId = parseInt(this.storeId);
      http
        .get("/review/getAverageScore", {
          params: {
            storeId: storeId,
          },
        })
        .then((response) => {
          this.averageScore = response.data;
          console.log("점수 평균값 : " + this.averageScore);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  mounted() {
    normal("정보를 불러옵니다. 잠시만 기다려주세요..", this);
    // 정보를 호출한다.
    this.requestPage(1);
    this.getAverageScore();

    this.requestListHasReplyCount();
    if (this.getMyStore.owner_id == this.getUserId) {
      console.log("주인이 맞습니다용");
      this.isOwner = true;
    }
  },
};
</script>

<style scoped>
.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}
.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.review {
  width: 100%;
  border: 1px solid lightgray;
}
.review-left {
  font-size: 40px;
}

.review-2 {
  margin-left: 10px;
}

.review2 {
  width: 100%;
  text-align: left;
  background-color: lightgray;
  height: 50px;
  line-height: 40px;
  padding-left: 20px;
}
.review2-p {
  line-height: middle;
}

.review2-div {
  border: 2px solid rgb(170, 170, 170);
  background-color: #fafafa;
  text-align: center;
  border-radius: 25px;
  padding: -5px;
}
.available-link {
  cursor: pointer;
}
.choiced {
  text-decoration-line: underline;
  text-underline-position: under;
}
.spread-underline {
  color: #333;
  text-decoration: none;
  display: inline-block;
  padding: 10px 0;
  position: relative;
}
.spread-underline:after {
  background: none repeat scroll 0 0 transparent;
  bottom: 0;
  content: "";
  display: block;
  height: 4px;
  left: 50%;
  position: absolute;
  background: #ffb000;
  transition: width 0.3s ease 0s, left 0.3s ease 0s;
  width: 0;
}
.spread-underline:hover:after {
  width: 100%;
  left: 0;
}
.reviewWrite:hover {
  cursor: pointer;
  background-color: tomato;
}


.review2-p {
  margin: 0px;
  height: 60px;
  line-height: 50px;
}
</style>
