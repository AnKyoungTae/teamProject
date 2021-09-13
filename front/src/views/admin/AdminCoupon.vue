<template>
  <div>
    <div style="margin-top: 50px">
      <h1 class="nearShopTitle" style="margin-right: 50px">
        <p>이벤트관리</p>
      </h1>
    </div>
    <!-- 노티피케이션 -->
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />

    <div class="col-7">
      <div class="form-check form-check-inline">
        <input
          class="form-check-input"
          type="radio"
          name="inlineRadioOptions"
          id="inlineRadio2"
          value="ALL"
          checked
          @click="setStatusOption('S')"
        />
        <label class="form-check-label" for="inlineRadio2">대기중</label>
      </div>
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
          id="inlineRadio3"
          @click="setStatusOption('Y')"
          value="Y"
        />
        <label class="form-check-label" for="inlineRadio3">진행중</label>
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
        <label class="form-check-label" for="inlineRadio4">종료</label>
      </div>
    </div>

    <div class="container" style="width: 1000px">
      <div class="notice col-12 h-50">
        <!-- 리스트 시작 -->
        <div class="accordion accordion-flush" id="accordionFlushExample">
          <div
            class="accordion-item"
            style="border-top: 2px solid gray"
            v-for="(list, index) in couponList"
            :key="index"
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
                <strong style="font-size: 18px">
                  <!-- 이벤트  -->
                  <table
                    class="table table-bordered align-self-center table-striped"
                  >
                    <thead>
                      <tr class="bg-secondary">
                        <th scope="col fw-bold">#</th>
                        <th scope="col fw-bold">이벤트발행일자</th>
                        <th scope="col fw-bold">사용종료일</th>
                        <th scope="col fw-bold">제목</th>
                        <th scope="col fw-bold">내용</th>
                        <th scope="col fw-bold">상태</th>
                        <th scope="col fw-bold">할인금액</th>

                        <!-- 클릭시 자세하게 볼 수 있도록 -->
                      </tr>
                    </thead>
                    <!-- 테이블 내용 -->
                    <tbody>
                      <tr v-if="totalCount === 1">
                        <td class="table-danger" colspan="7">
                          등록된 이벤트가 없습니다.
                        </td>
                      </tr>
                      <tr class="eachcouponRow" @click="selectcoupon(list)">
                        <td class="table-light">
                          {{ startListNum + index + 1 }}
                        </td>
                        <td
                          class="table-light text-wrap fw-light"
                          style="width: 10rem"
                        >
                          {{ list.eventRegDate }}
                        </td>
                        <td
                          class="table-light text-wrap fw-light"
                          style="width: 10rem"
                        >
                          {{ list.eventDueDate }}
                        </td>
                        <td class="table-light" style="width: 8rem">
                          {{ list.title }}
                        </td>
                        <td class="table-light" style="width: 12rem">
                          {{ list.body }}
                        </td>

                        <td class="table-success" v-if="list.status == 'Y'">
                          진행중
                        </td>
                        <td
                          class="table-warning"
                          v-else-if="list.status == 'S'"
                        >
                          대기중
                        </td>
                        <td class="table-danger" v-else-if="list.status == 'N'">
                          종료
                        </td>
                        <td class="table-dark" v-else>?</td>
                        <td class="table-light">{{ list.couponPrice }}</td>
                      </tr>
                    </tbody>
                  </table>
                </strong>
              </button>
            </h2>
            <div
              v-bind:id="'flush-collapse' + index"
              class="accordion-collapse collapse"
              v-bind:aria-labelledby="'flush-heading' + index"
              data-bs-parent="#accordionFlushExample"
            >
              <div class="accordion-body">
                <!-- 쿠폰 -->
                <table
                  class="table table-bordered align-self-center table-striped"
                >
                  <thead>
                    <tr class="bg-secondary">
                      <th scope="col fw-bold">쿠폰발행일자</th>
                      <th scope="col fw-bold">쿠폰사용종료일</th>
                      <th scope="col fw-bold">이름</th>
                      <th scope="col fw-bold">번호</th>

                      <th scope="col fw-bold">할인금액</th>
                      <th scope="col fw-bold">할인율</th>

                      <!-- 클릭시 자세하게 볼 수 있도록 -->
                    </tr>
                  </thead>
                  <!-- 테이블 내용 @click="selectcoupon(list)"-->
                  <tbody>
                    <tr v-if="totalCount === 1">
                      <td class="table-danger" colspan="6">
                        등록된 쿠폰가 없습니다.
                      </td>
                    </tr>
                    <tr class="eachcouponRow">
                      <td
                        class="table-light text-wrap fw-light"
                        style="width: 10rem"
                      >
                        {{ list.publishedDate }}
                      </td>
                      <td
                        class="table-light text-wrap fw-light"
                        style="width: 10rem"
                      >
                        {{ list.couponEnd }}
                      </td>
                      <td class="table-light" style="width: 8rem">
                        {{ list.couponName }}
                      </td>
                      <td class="table-light" style="width: 12rem">
                        {{ list.couponNumber }}
                      </td>

                      <td class="table-light">{{ list.couponPrice }}</td>
                      <td class="table-light">{{ list.discountRate + "%" }}</td>
                    </tr>
                  </tbody>
                </table>
              </div>
            </div>
          </div>
        </div>
        <!-- 리스트 끝 -->
        
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
import JWT from "@/api/jwt";
import Detail from "@/components/admin/couponDetail.vue";

export default {
  data() {
    return {
      couponList: [],
      currentPage: 1, // 현재 페이지
      listPerPage: 10, // 한번에 보여줄 리스트숫자
      totalCount: 0, // 총 게시글 수
      showindex: 5, // 번호로 표시될 페이지 총 갯수
      statusOption: "ALL",
      selectedCoupon: "",
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
    console.log("관리자로 로그인하였습니다.");
    normal("정보를 불러옵니다. 잠시만 기다려주세요..", this);
    // 사용자 검증을 위해 토큰정보를 싣는다.
    const token = JWT.getToken();
    const headers = {
      "content-type": "application/json",
      accesstoken: token,
    };
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
        .post("/admin/adminEvent", data)
        .then((response) => {
          console.log(response);
          if (response.status === 200) {
            success("데이터 로딩 완료!", this);
            this.couponList = response.data;
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
        .get("/admin/adminEventcount", {
          params: {
            option: this.statusOption,
          },
        })
        .then((response) => {
          console.log(response);
          this.totalCount = response.data;
          console.log("등록된 쿠폰의 총 갯수 : " + this.totalCount);
        })
        .catch((err) => {
          console.log(err);
        });
    },
    requestChangeCouponStatus(couponId, status) {
      //상태변경
      const data = {
        eventId: couponId,
        status: status,
      };
      http
        .post("/admin/updateEventStatus", data)
        .then((response) => {
          if (response.data === "ok") {
            console.log(response.data);

            success("쿠폰 상태변경 완료!", this);
            this.clearDetail();
          } else if (response.data === "no") {
            success("쿠폰 상태변경  실패!", this);
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
    selectcoupon(list) {
      this.selectedcoupon = list;
      console.log(this.selectedcoupon);
      var confirmflag = confirm("이벤트를 변경합니다.");

      if (confirmflag) {
        if (this.selectedcoupon.status === "Y") {
          console.log(this.selectedcoupon.eventId);
          if (confirm("진행중인 이벤트입니다\n 취소하시겠습니까?")) {
            this.requestChangeCouponStatus(this.selectedcoupon.eventId, "N");
          }
        } else if (this.selectedcoupon.status === "N") {
          alert("종료된 이벤트 입니다.");
        } else {
          console.log(this.selectedcoupon.eventId);
          this.requestChangeCouponStatus(this.selectedcoupon.eventId, "Y");
        }
      } else {
        console.log("취소");
      }

      console.log(this.selectedcoupon.status);
    },
    clearDetail() {
      this.couponDetail = false;
      this.requestPage(this.currentPage);
    },
    approveCoupon() {
      if (this.selectedcoupon.status === "Y") {
        error("이미 승인된 쿠폰입니다!", this);
        return;
      }
      success("쿠폰를 승인합니다!", this);

      this.requestChangeCouponStatus(this.selectedcoupon.couponId, "Y");
    },
    denyCoupon() {
      if (this.selectedcoupon.status === "N") {
        error("이미 취소된 쿠폰입니다!", this);
        return;
      }
      error("쿠폰를 등록취소합니다!", this);
      this.requestChangeCouponStatus(this.selectedcoupon.couponId, "N");
    },
  },
};
</script>

<style scoped>
* {
  align-content: center;
}

.faqButton {
  padding: 20px;
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
