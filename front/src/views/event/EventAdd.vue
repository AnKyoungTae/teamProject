<template>
  <h2 class="event-h2" style="margin-top: 50px">
    이벤트 추가 페이지
    <h5>(판매자 전용)</h5>
  </h2>
  <div style="width: 100%;">
    <form action="" name="EventAdd" v-on:submit.prevent="submitForm">
      <table>
        <tr>
          <td class="td1">이벤트 타이틀</td>
          <td class="td2" colspan="7" style="text-align: left;">
            <input
              type="text"
              class="eventadd-input"
              name="title"
              v-model="title"
              id="title"
              style="width: 70%;"
            />
          </td>
        </tr>
        <tr>
          <td class="td1">이벤트 기간</td>
          <td class="td2" colspan="7" style="text-align: left;">
            <span class="time-ex">시작일: </span><input
              type='date'
              class="eventadd-input"
              name="start"
              v-model="start"
              id="start"
            /><span style="color:lightgray;">ex)2000-01-01</span>
            <br/>
            <span class="time-ex">종료일: </span><input
              type='date'
              class="eventadd-input"
              name="dueDate"
              v-model="dueDate"
              id="dueDate"
            />
          </td>
        </tr>
        <tr>
          <td class="td1">이벤트설명</td>
          <td class="td2" colspan="7">
            <textarea
              class="form-control"
              style="resize: none"
              rows="3"
              placeholder="이벤트 내용을 작성해주세요."
              v-model="body"
            ></textarea>
          </td>
        </tr>
        <tr>
          <td class="td1" rowspan="4">할인정보</td>
          <td class="coupon1">할인명</td>
          <td class="coupon1">할인 음식</td>
          <td class="coupon1">할인넘버</td>
          <td class="coupon1">할인가격</td>
          <td class="coupon1">할인율</td>
          <td class="coupon1">할인 종료일</td>
          <td class="coupon1"></td>
        </tr>
        <tr style="border-bottom: 2px solid black;">
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
            <input
              type="text"
              class=""
              name="couponName"
              v-model="couponFood"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <input
              type="text"
              class=""
              name="couponName"
              v-model="couponNumber"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <input
              style="width: 100px;"
              type="text"
              class=""
              name="couponName"
              v-model="couponPrice"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <input
              style="width: 100px;"
              type="text"
              class=""
              name="couponName"
              v-model="couponDiscount"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <input
              style=""
              type="date"
              class=""
              name="couponName"
              v-model="couponPublishedDate"
              id="couponName"
            />
          </td>
          <td class="coupon2">
            <button>등록</button>
          </td>
        </tr>
        <tr style="border-top: 2px solid black; border-bottom: 2px solid black;">
          <td colspan="2">
            할인명
          </td>
          <td>
            할인넘버
          </td>
          <td>
            할인가격
          </td>
          <td>
            할인 종료일
          </td>
          <td>
            <input
              type="submit"
              class="couponDelete"
              value="삭제"
              @click="couponDelete()"
            />
          </td>
        </tr>
        <tr style="border-bottom: 1px solid rgb(199, 198, 198);">
          <td colspan="2" class="coupon">
            <span>ex)9월 할인</span>
          </td>
          <td class="coupon">
            <span>ex)4FEX15D</span>
          </td>
          <td class="coupon">
            <span>ex)10000원</span>
          </td>
          <td class="coupon">
            <span>ex)2021-09-30</span>
          </td>
          <td class="coupon">
            <button>삭제</button>
          </td>
        </tr>
      </table>
      <input
        type="submit"
        class="eventadd-button"
        value="추가"
        @click="onClick"
      />
    </form>
  </div>
</template>

<script>
import { normal, error, success } from "@/api/notification";
import axios from "axios";
import JWT from "@/api/jwt";

export default {
  data: function () {
    return {
      title: this.title,
      body: this.body,
      dueDate: this.dueDate,
    };
  },
  methods: {
    submitForm: function () {
      console.log("확인");
      let access_token = JWT.getToken();
      console.log(this.title, this.body, this.dueDate, access_token);

      var data = {
        title: this.title,
        body: this.body,
        dueDate: this.dueDate,
        access_token,
      };

      axios
        .post("http://localhost:8083/event/eventinsert", data)
        .then((data) => {
          console.log(data);

          success("데이터 로딩 완료!", this);
        })

        .catch((error) => {
          console.log(error);
        });
    },
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
  background-color: #FAFAFA;
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
  padding: 20px;
}
.coupon1 {
  border-top: 2px solid black; 
  border-bottom: 1px solid rgb(199, 198, 198); 
  padding: 10px;
}
.coupon2 {
  border-bottom: 1px solid rgb(199, 198, 198); 
  padding: 10px;
}
</style>
