<template>
  <div>
    <h1 class="mt-4 mb-4">가게메뉴</h1>
    <!-- 노티피케이션 -->
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <!-- 음식이 있을 때 -->
    <div class="row align-items-center">
      <div class="col-10">
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
            @click="setStatusOption('1')"
            value="1"
          />
          <label class="form-check-label" for="inlineRadio2">판매중</label>
        </div>
        <div class="form-check form-check-inline">
          <input
            class="form-check-input"
            type="radio"
            name="inlineRadioOptions"
            id="inlineRadio3"
            @click="setStatusOption('2')"
            value="2"
          />
          <label class="form-check-label" for="inlineRadio3">품절</label>
        </div>
        <div class="form-check form-check-inline">
          <input
            class="form-check-input"
            type="radio"
            name="inlineRadioOptions"
            id="inlineRadio4"
            @click="setStatusOption('3')"
            value="3"
          />
          <label class="form-check-label" for="inlineRadio4">판매중단</label>
        </div>
      </div>
    </div>
    <!-- 검색메뉴? -->
    <div id="tableWrapper">
      <no-menu class="d-block"></no-menu>
      <div v-if="foodList">
        <div
          v-for="(food, index) in foodList"
          :key="index"
          @click="selectFood(food)"
        >
          <menu-box class="d-block" :foodInfo="food"></menu-box>
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
import MenuBox from "@/components/store/StoreMenu.vue";
import NoMenu from "@/components/store/StoreMenuNone.vue";
import { normal, error, success } from "@/api/notification";
import http from "@/api/http";
import { mapGetters } from "vuex";

export default {
  components: {
    MenuBox,
    NoMenu,
  },
  data() {
    return {
      foodList: [],
      currentPage: 1, // 현재페이지
      listPerPage: 10, // 한번에 보여줄 리스트 숫자
      totalCount: 0, // 총 게시글 수
      showindex: 5, // 페이지네이션? 표시될 총 페이지 개수
      statusOption: "ALL",
      selectedFood: "",
      foodDetail: "",
      storeId: null,
    };
  },
  computed: {
    ...mapGetters({ myStore: "auth/getMyStore" }),
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
    if (!this.myStore) {
      this.$router.push({ path: "/store" });
      console.log("storeId 정보가 없어서 다시 홈으로");
    }
    this.storeId = this.myStore.storeId;

    this.requestPage(1);
  },
  beforeUpdate() {
    if (!this.myStore) {
      this.$router.push({ path: "/store" });
      console.log("storeId 정보가 없어서 다시 홈으로");
    }
    this.storeId = this.myStore.storeId;
  },
  methods: {
    requestPage(page) {
      this.requestListCount(this.storeId);
      const data = {
        listPerPage: this.listPerPage,
        currentPage: page,
        statusOption: this.statusOption,
        storeId: this.storeId,
      };
      http
        .post("/store/getFoodList", data)
        .then((response) => {
          if (response.status === 200) {
            console.log(response.data);
            success("데이터 로딩 완료!", this);
            this.foodList = response.data;
            this.currentPage = page;
            console.log("현재페이지 : " + this.currentPage);
          }
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
        });
    },
    requestListCount(storeId) {
      http
        .get("/store/getFoodListCount", {
          params: {
            option: this.statusOption,
            storeId: storeId,
          },
        })
        .then((response) => {
          this.totalCount = response.data;
          console.log("등록된 음식의 총 갯수 : " + this.totalCount);
        })
        .catch((error) => {
          console.log(error);
        });
    },
    setStatusOption(value) {
      this.statusOption = value;
      this.requestPage(1);
    },
    selectFood(list) {
      this.selectedFood = list;
      this.foodDetail = true;
    },
    clearDetail() {
      this.foodDetail = false;
      this.requestPage(this.currentPage);
    },
    // approveFood() {
    //   if (this.selectedStore.status === "1") {
    //     error("이미 판매중인 음식입니다!", this);
    //     return;
    //   }
    //   success("가게를 승인합니다!", this);
    //   // 가게 status를 바꿔주는 mvc패턴을 만들고, storename과 membername으로 가게를찾아야해
    //   this.requestChangeStoreStatus(this.selectedStore.storeId, "1");
    // },
    // denyStore() {
    //   if (this.selectedStore.status === "3") {
    //     error("이미 판매중지된 음식입니다!", this);
    //     return;
    //   }
    //   error("가게를 등록취소합니다!", this);
    //   this.requestChangeStoreStatus(this.selectedStore.storeId, "3");
    // },
    // requestChangeStoreStatus(storeId, status) {
    //   const data = {
    //     storeId: storeId,
    //     status: status,
    //   };
    //   http
    //     .post("http://localhost:8083/admin/updateStoreStatus", data)
    //     .then((response) => {
    //       if (response.status === 200) {
    //         console.log(response.data);
    //         success("가게 상태변경 완료!", this);
    //         this.clearDetail();
    //       }
    //     })
    //     .catch((error) => {
    //       console.log(error);
    //       error("오류가 발생했습니다. 다시 시도해주세요", this);
    //     });
    // },
  },
};
</script>

<style scoped>
#tableWrapper {
  width: 100%;
  height: 100%;
  display: table;
  margin: 1rem;
}
.available-link {
  cursor: pointer;
}
.col-10 {
  width: 100%;
  text-align: right;
}
</style>
