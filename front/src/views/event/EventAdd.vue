<template>
  <h2 class="event-h2" style="margin-top: 50px">
    이벤트 추가 페이지
    <h5>(관리자 & 판매자 전용)</h5>
  </h2>
  <form action="" name="EventAdd" v-on:submit.prevent="submitForm">
    <table>
      <tr>
        <td class="td1">제목:</td>
        <td class="td2">
          <input
            type="text"
            class="eventadd-input"
            name="title"
            v-model="title"
            id="title"
          />
        </td>
      </tr>
      <tr>
        <td class="td1">내용:</td>
        <td class="td2">
          <input
            type="text"
            class="eventadd-input"
            name="body"
            v-model="body"
            id="body"
          />
        </td>
      </tr>
      <tr>
        <td class="td1">만료일:</td>
        <td class="td2">
          <input
            type="text"
            class="eventadd-input"
            name="dueDate"
            v-model="dueDate"
            id="dueDate"
          />
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
  margin-left: 25%;
}
.td1 {
  padding-top: 20px;
  width: 100px;
}
.eventadd-input {
  margin-top: 20px;
  width: 250px;
  height: 40px;
}
.eventadd-button {
  margin-top: 40px;
}
</style>
