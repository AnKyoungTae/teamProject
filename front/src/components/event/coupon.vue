<template>
  <div class="couponon-div">
    <couponon></couponon>
  </div>

  <div class="couponend-div">
    <couponend></couponend>
  </div>
</template>

<script>
import couponon from "./couponon.vue";
import couponend from "./couponend.vue";

export default {
  components: {
    couponon,
    couponend,
  },
};
</script>

<style scoped>
.couponon-div {
  width: 100%;
  height: 100%;
  clear: both;
  display: block;
}

.couponend-div {
  width: 840px;
  clear: both;
}
</style>
