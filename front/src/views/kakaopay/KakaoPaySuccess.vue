<template>
  <div class="popupbox">
    <h1>결제가 완료되었습니다.</h1>
  </div>
</template>

<script>
import axios from "axios";

export default {
  mounted() {
    axios
      .get("http://localhost:8083/order/kakaoPayInfo", {
        params: {
          pg_token: this.$route.query.pg_token,
        },
      })
      .then((res) => {
        console.log("받은데이터"); // 핸드폰번호받기
        console.log(res.data);
        alert("주문이 완료되었습니다!");
        // 부모페이지 이동, 로그인되어있지않으면 못가..
        opener.location.href =
          "http://localhost:8081/orderInfo?orderId=" + res.data;
        window.close();
      });
  },
};
</script>

<style scoped>
.popupbox {
  position: fixed;
  z-index: 100;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgb(255, 255, 255);
  display: table;
  transition: opacity 0.3s ease;
}
</style>
