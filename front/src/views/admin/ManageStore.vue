<template>
  <div>
    <h1>가게관리페이지</h1>
    <!-- 노티피케이션 -->
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />

    <!-- 설정 -->
    <div
      class="container-fluid mt-2"
      id="optionWrapper"
      :class="{ detailIsOn: storeDetail }"
    >
      <div class="row align-items-center">
        <div class="col-7">
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio1"
              value="ALL"
              checked
              @click="setStatusOption('ALL')"
            />
            <label class="form-check-label" for="inlineRadio1">전체보기</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio2"
              @click="setStatusOption('S')"
              value="S"
            />
            <label class="form-check-label" for="inlineRadio2"
              >신청대기중</label
            >
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio3"
              @click="setStatusOption('Y')"
              value="Y"
            />
            <label class="form-check-label" for="inlineRadio3">영업중</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio4"
              @click="setStatusOption('N')"
              value="N"
            />
            <label class="form-check-label" for="inlineRadio4">영업정지</label>
          </div>
        </div>
        <div class="col-5">
          <!-- 검색 -->
          <div class="input-group w-100">
            <button
              class="btn btn-outline-secondary dropdown-toggle"
              type="button"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              선택하세요
            </button>
            <ul class="dropdown-menu">
              <li><a class="dropdown-item">이름</a></li>
              <li><a class="dropdown-item">주소</a></li>
              <li><a class="dropdown-item">신청자</a></li>
            </ul>
            <input
              type="text"
              class="form-control"
              aria-label="Text input with dropdown button"
            />
          </div>
        </div>
      </div>
    </div>
    <!-- 테이블시작 -->
    <div
      id="wrapper"
      class="container-fluid mw-100 mt-2"
      :class="{ detailIsOn: storeDetail }"
    >
      <table class="table table-bordered align-self-center table-striped">
        <thead>
          <tr class="bg-secondary">
            <th scope="col fw-bold">#</th>
            <th scope="col fw-bold">등록일</th>
            <th scope="col fw-bold">가게이름</th>
            <th scope="col fw-bold">상태</th>
            <th scope="col fw-bold">주소</th>
            <th scope="col fw-bold">신청자</th>
            <!-- 클릭시 자세하게 볼 수 있도록 -->
          </tr>
        </thead>
        <!-- 테이블 내용 -->
        <tbody>
          <tr v-if="totalCount === 0">
            <td class="table-danger" colspan="6">등록된 가게가 없습니다.</td>
          </tr>
          <tr
            v-for="(list, index) in storeList"
            :key="index"
            class="eachStoreRow"
            @click="selectStore(list)"
          >
            <td class="table-light">{{ startListNum + index + 1 }}</td>
            <td class="table-light text-wrap fw-light" style="width: 6rem">
              {{
                list.regDate[2] +
                "일 " +
                list.regDate[3] +
                ":" +
                list.regDate[4]
              }}
            </td>
            <td class="table-light">{{ list.storename }}</td>
            <td class="table-success" v-if="list.status == 'Y'">
              {{ list.status }}
            </td>
            <td class="table-warning" v-else-if="list.status == 'S'">
              {{ list.status }}
            </td>
            <td class="table-danger" v-else-if="list.status == 'N'">
              {{ list.status }}
            </td>
            <td class="table-dark" v-else>?</td>
            <td class="table-light">{{ list.address }}</td>
            <td class="table-light">{{ list.membername }}</td>
          </tr>
        </tbody>
      </table>
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
    <!-- 가게 상세페이지 -->
    <div class="container detailWrapper" :class="{ detailIsOn: !storeDetail }">
      <div class="row mb-2 p-3">
        <div
          class="col-3 btn btn-success align-align-self-auto m-1"
          @click="approveStore"
        >
          승인하기
        </div>
        <div
          class="col-3 btn btn-danger align-align-self-auto m-1"
          @click="denyStore"
        >
          등록거절
        </div>
        <div class="col-2"></div>
        <div
          class="btn btn-primary col-3 align-self-end m-1"
          @click="clearDetail"
        >
          목록으로 돌아가기
        </div>
      </div>
      <Detail :data="selectedStore"></Detail>
    </div>
  </div>
</template>

<script>
import { normal, error, success } from "@/api/notification";
import http from "@/api/http";
import Detail from "@/components/admin/storeDetail.vue";

export default {
  components: {
    Detail,
  },
  data() {
    return {
      storeList: [],
      currentPage: 1, // 현재 페이지
      listPerPage: 10, // 한번에 보여줄 리스트숫자
      totalCount: 0, // 총 게시글 수
      showindex: 5, // 번호로 표시될 페이지 총 갯수
      statusOption: "ALL",
      selectedStore: "",
      storeDetail: false,
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
      console.log("요청페이지 : " + request);
      this.requestListCount();
      const data = {
        listPerPage: this.listPerPage,
        currentPage: request,
        statusOption: this.statusOption,
      };
      http
        .post("/admin/getStoreList", data)
        .then((response) => {
          if (response.status === 200) {
            console.log(response.data);
            this.storeList = response.data;
            this.currentPage = request;
            console.log("현재페이지 : " + this.currentPage);
          }
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
        });
    },
    requestListCount() {
      http
        .get("/admin/getStoreListCount", {
          params: {
            option: this.statusOption,
          },
        })
        .then((response) => {
          this.totalCount = response.data;
          console.log("등록된 가게의 총 갯수 : " + this.totalCount);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    requestChangeStoreStatus(storeId, status) {
      const data = {
        storeId: storeId,
        status: status,
      };
      http
        .post("/admin/updateStoreStatus", data)
        .then((response) => {
          if (response.status === 200) {
            console.log(response.data);
            success("가게 상태변경 완료!", this);
            this.clearDetail();
          }
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
        });
    },
    setStatusOption(value) {
      this.statusOption = value;
      this.requestPage(1);
    },
    selectStore(list) {
      this.selectedStore = list;
      this.storeDetail = true;
    },
    clearDetail() {
      this.storeDetail = false;
      this.requestPage(this.currentPage);
    },
    approveStore() {
      if (this.selectedStore.status === "Y") {
        error("이미 승인된 가게입니다!", this);
        return;
      }
      success("가게를 승인합니다!", this);
      // 가게 status를 바꿔주는 mvc패턴을 만들고, storename과 membername으로 가게를찾아야해
      this.requestChangeStoreStatus(this.selectedStore.storeId, "Y");
    },
    denyStore() {
      if (this.selectedStore.status === "N") {
        error("이미 취소된 가게입니다!", this);
        return;
      }
      error("가게를 등록취소합니다!", this);
      this.requestChangeStoreStatus(this.selectedStore.storeId, "N");
    },
  },
};
</script>

<style scoped>
* {
  align-content: center;
}
.available-link {
  cursor: pointer;
}
.detailIsOn {
  visibility: hidden;
}
.eachStoreRow:hover {
  cursor: pointer;
  outline: 2px solid rgba(0, 26, 255, 0.4);
  transition: 0.3s;
}
.detailWrapper {
  z-index: 2;
  position: absolute;
  top: 4rem;
  left: 5rem;
  width: 100%;
  height: 100%;
  background-color: aliceblue;
  padding: 0.2rem;
}
</style>
