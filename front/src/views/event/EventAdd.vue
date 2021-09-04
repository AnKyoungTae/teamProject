<template>
  <h2 class="event-h2" style="margin-top: 50px">
    이벤트 추가 페이지
    <h5 @click="test">(판매자 전용)</h5>
  </h2>
  <notifications
    group="notifyApp"
    position="bottom right"
    style="margin-right: 30vh"
  />
  <div style="width: 100%" class="m-4">
    <form action="" name="EventAdd">
      <table>
        <tr>
          <td class="td1">행사 제목</td>
          <td class="td2" colspan="10" style="text-align: left">
            <input
              type="text"
              class="eventadd-input"
              name="title"
              id="title"
              style="width: 70%"
              v-model="eventTitle"
            />
          </td>
        </tr>
        <tr>
          <td class="td1">행사 기간</td>
          <td class="td2" colspan="7" style="text-align: left">
            <span class="time-ex">시작일: </span
            ><input
              type="date"
              class="eventadd-input"
              name="eventUpdate"
              id="eventUpdate"
              v-model="updateDate"
              required
            /><span style="color: lightgray" class="m-2">ex)2000-01-01</span>

            <span class="time-ex">종료일: </span
            ><input
              type="date"
              class="eventadd-input"
              name="dueDate"
              id="dueDate"
              v-model="dueDate"
            />
          </td>
        </tr>
        <tr>
          <td class="td1">설명</td>
          <td class="td2" colspan="7">
            <textarea
              class="form-control"
              style="resize: none"
              rows="3"
              placeholder="이벤트 내용을 작성해주세요."
              id="eventBody"
              v-model="eventBody"
            ></textarea>
          </td>
        </tr>
        <tr>
          <td class="td1" rowspan="5">할인정보</td>
          <td class="coupon1">할인 이름</td>
          <td class="coupon1">할인 음식</td>
          <td class="coupon1">할인 종료일</td>
        </tr>
        <tr class="coupon3">
          <td class="coupon2">
            <input
              type="text"
              class=""
              name="couponName"
              v-model="couponName"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <select
              class="form-select form-select-sm"
              ref="foodSelector"
              id="foodSelector"
            >
              <option selected>메뉴를 선택하세요</option>
              <option v-for="food of foodList" :key="food" :value="food.foodId">
                {{ food.name }}
              </option>
            </select>
          </td>

          <td class="coupon2">
            <input
              style=""
              type="date"
              class=""
              name="couponEnd"
              v-model="couponEnd"
              id="couponEnd"
            />
          </td>
        </tr>
        <tr>
          <td class="coupon1">할인가격</td>
          <td class="coupon1">할인율</td>
          <td class="coupon1">총할인금액</td>
        </tr>
        <tr>
          <td class="coupon2">
            <input
              style="width: 80px"
              type="text"
              name="couponPrice"
              v-model="couponPrice"
              id="couponPrice"
            />
            백원
          </td>
          <td class="coupon2">
            <label for="customRange1" class="form-label"
              >{{ discountRate }} %</label
            >
            <input
              type="range"
              class="form-range"
              id="customRange1"
              v-model="discountRate"
            />
          </td>
          <td class="coupon2">
            <input
              style="width: 80px"
              type="text"
              id="totalDiscount"
              v-model="totalDiscount"
              disabled
            />
            원
          </td>
        </tr>
        <tr>
          <td colspan="2">
            <span>* 등록된 할인정보는 이벤트와 함께 추가됩니다.</span>
          </td>
        </tr>
      </table>
      <span class="text_0"
        >* 등록 후 수정은 불가능하니 등록 시 신중하게 해주십시오.</span
      ><br />
      <div class="btn btn-outline-success" @click="applyEvent">등록신청</div>
    </form>
  </div>
</template>

<script>
import { normal, error, success } from "@/api/notification";
import http from "@/api/http";

export default {
  data: function () {
    return {
      eventTitle: null,
      updateDate: null,
      dueDate: null,
      eventBody: null,
      couponName: null,
      foodList: [],
      couponEnd: null,
      couponPrice: null,
      discountRate: 100,
    };
  },
  computed: {
    selectedFood() {
      if (!isNaN(this.$refs.selectedFood.value)) {
        return null;
      }
      return this.$refs.foodSelector.value;
    },
    totalDiscount() {
      if (isNaN(this.couponPrice)) {
        return "숫자만 입력하세요";
      } else {
        let result;
        if (this.couponPrice > 0) {
          result = (this.couponPrice * (100 - this.discountRate)) / 100;
        } else {
          result = ((this.couponPrice * (100 - this.discountRate)) / 100) * -1;
        }
        return parseInt(result * 100);
      }
    },
  },
  methods: {
    requestFoodList() {
      http.get("/event/getFoodList").then((res) => {
        this.foodList = res.data;
      });
    },
    setDateInputs() {
      var today = new Date();
      var dd = today.getDate();
      var mm = today.getMonth() + 1;
      var yyyy = today.getFullYear();
      if (dd < 10) {
        dd = "0" + dd;
      }
      if (mm < 10) {
        mm = "0" + mm;
      }

      today = yyyy + "-" + mm + "-" + dd;
      document.getElementById("eventUpdate").setAttribute("min", today);
      document.getElementById("dueDate").setAttribute("min", today);
      document.getElementById("couponEnd").setAttribute("min", today);
    },
    applyEvent() {
      // validation //

      // 제목
      if (!this.eventTitle) {
        error("행사 제목을 입력해주세요!", this);
        document.getElementById("title").focus();
        return;
      }
      let titleRegx = /^[가-힣a-zA-Z]+$/;
      if (!titleRegx.test(this.eventTitle)) {
        error("행사 제목을 올바르게 입력해주세요!", this);
        document.getElementById("title").focus();
        return;
      }

      // 기간
      if (!this.updateDate) {
        error("행사 시작일을 선택해주세요", this);
        document.getElementById("eventUpdate").focus();
        return;
      }
      if (!this.dueDate) {
        error("행사 종료일을 선택해주세요", this);
        document.getElementById("dueDate").focus();
        return;
      }
      if (!this.couponEnd) {
        error("할인 종료일을 선택해주세요", this);
        document.getElementById("couponEnd").focus();
        return;
      }

      // 설명
      if (!this.eventBody) {
        error("행사에 대한 설명을 작성해주세요", this);
        document.getElementById("eventBody").focus();
        return;
      }
      // // 쿠폰이름
      if (!this.couponName) {
        error("할인쿠폰 이름을 작성해주세요", this);
        document.getElementById("couponName").focus();
        return;
      }
      // // 쿠폰 음식
      if (!this.selectedFood) {
        error("할인 음식을 골라주세요", this);
        document.getElementById("foodSelector").focus();
        return;
      }
      // 할인가격
      if (!this.couponPrice) {
        error("할인가격을 100원 단위로 입력해주세요", this);
        document.getElementById("couponPrice").focus();
        return;
      }
      // 음식 최대가격을 총 할인금액이 넘지 않았는지
      // 음식 가격 구하기
      let foodPrice = this.foodList.filter((food) => {
        return food.foodId == this.$refs.foodSelector.value;
      })[0].price;
      if (this.couponPrice * 100 > foodPrice) {
        error("할인가격은 음식가격보다 높을 수 없습니다", this);
        document.getElementById("couponPrice").focus();
        this.couponPrice = null;
        return;
      }
      if (this.totalDiscount > foodPrice) {
        error("총 할인가격은 음식가격보다 높을 수 없습니다", this);
        document.getElementById("totalDiscount").focus();
        return;
      }

      const data = {
        title: this.eventTitle,
        body: this.eventBody,
        updateDate: this.updateDate,
        dueDate: this.dueDate,
        name: this.couponName,
        couponEnd: this.couponEnd,
        price: this.couponPrice * 100,
        discountRate: this.discountRate,
        foodId: this.selectedFood,
      };

      http.post("/event/addEvent", data).then((res) => {
        console.log(res.data);
      });
    },
  },
  mounted() {
    this.requestFoodList();
    this.setDateInputs();
  },
};
</script>

<style scoped>
table {
  border-top: 2px solid black;
  border-bottom: 2px solid black;
}
.td1 {
  background-color: lightgray;
  padding: 10px;
  border-bottom: 1px solid rgb(199, 198, 198);
}
.td2 {
  background-color: #fafafa;
  padding: 10px;
  border-bottom: 1px solid rgb(199, 198, 198);
}
.eventadd-input {
}
.eventadd-button {
  margin-top: 40px;
}
.time-ex {
  color: gray;
}
.coupon {
  padding: 0px 10px;
}
.coupon1 {
  border-top: 2px solid black;
  border-bottom: 1px solid rgb(199, 198, 198);
  padding: 10px;
  background-color: rgb(236, 236, 236);
}
.coupon2 {
  border-bottom: 1px solid rgb(199, 198, 198);
  padding: 10px;
}
.text_0 {
  color: rgb(194, 51, 51);
}
.coupon3 {
  background-color: rgb(236, 236, 236);
}
</style>
