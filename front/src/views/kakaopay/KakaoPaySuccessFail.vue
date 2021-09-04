<template>
  <div class="popupbox">
    <h1>결제가 취소되었습니다.</h1>
  </div>
</template>

<script>
export default {
  mounted() {
    alert("결제가 취소되었습니다!");
    opener.location.href = "http://localhost:8081/";
    window.close();
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
