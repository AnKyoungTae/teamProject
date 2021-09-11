<template>
  <div style="width: 100%; margin-top: 50px;">
    <h1 class="nearShopTitle" style="margin-right: 50px;">
      <p>이벤트 페이지</p>
    </h1>
  </div>
  <div class="container">
    <div class="m-4">
      <!-- 노티피케이션 -->
      <notifications
        group="notifyApp"
        position="bottom right"
        style="margin-right: 30vh"
      />
      <div class="row">
        <!-- 맨 윗줄 카테고리영역 -->
        <div
          class="col btn btn-light spread-underline"
          @click="setStatusOption('ALL')"
        >
          <span :class="[this.statusOption === 'ALL' ? 'choiced' : '']">
            전체 이벤트
          </span>
        </div>
        <div
          class="col btn btn-light spread-underline"
          @click="setStatusOption('Y')"
        >
          <span :class="[this.statusOption === 'Y' ? 'choiced' : '']">
            진행중인 이벤트
          </span>
        </div>
        <div
          class="col btn btn-light spread-underline"
          @click="setStatusOption('N')"
        >
          <span :class="[this.statusOption === 'N' ? 'choiced' : '']">
            만료된 이벤트
          </span>
        </div>
      </div>
      <hr />
      <!-- 관리자, 판매자일경우 추가해야함 -->
      <div v-if="!pageLoaded">
        <!-- 페이지 로딩되지않았을 때 -->
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <div v-else>
        <!-- 페이지 로딩되었을때 -->
        <div class="row" v-for="event in eventList" :key="event">
          <eventRow :data="event"></eventRow>

          <hr />
        </div>
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
                    (this.currentIndex - 1) * this.showindex + index !=
                    currentPage
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
      </div>
    </div>
  </div>
</template>

<script>
import { normal, error, success } from "@/api/notification";
import http from "@/api/http";
import eventRow from "@/components/event/EventRow.vue";

export default {
  components: {
    eventRow,
  },
  data() {
    return {
      eventList: [],
      currentPage: 1, // 현재 페이지
      listPerPage: 10, // 한번에 보여줄 리스트숫자
      totalCount: 0, // 총 게시글 수
      showindex: 5, // 번호로 표시될 페이지 총 갯수
      statusOption: "Y",
      selectedEvent: "",
      pageLoaded: false,
    };
  },
  computed: {
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
  mounted() {
    normal("정보를 불러옵니다. 잠시만 기다려주세요..", this);
    // 정보를 호출한다.
    this.requestPage(1);
  },
  methods: {
    requestPage(request) {
      this.pageLoaded = false;
      console.log("요청페이지 : " + request);
      this.requestListCount();
      const data = {
        listPerPage: this.listPerPage,
        currentPage: request,
        statusOption: this.statusOption,
      };
      http
        .post("/event/getEventList", data)
        .then((response) => {
          if (response.status === 200) {
            this.eventList = response.data;
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
      http
        .get("/event/getEventListCount", {
          params: {
            option: this.statusOption,
          },
        })
        .then((response) => {
          this.totalCount = response.data;
          console.log("등록된 이벤트의 총 갯수 : " + this.totalCount);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    setStatusOption(value) {
      this.statusOption = value;
      this.requestPage(1);
    },
  },
};
</script>

<style scoped>
.container {
  width: 1000px;
  margin-left: 200px;
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

.nearShopTitle {
  font-weight: bolder;
  text-align: end;
  margin-bottom: 20px;
  color: #91afba;
  font-family: BMDOHYEON;
  user-select: none;
  display: flex;
  justify-content: end;
}
.nearShopTitle > p {
  width: 30%;
  border-bottom: 8px solid #ffda77;
  padding-bottom: 10px;
}

@font-face {
  font-family: "BMDOHYEON";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
</style>
