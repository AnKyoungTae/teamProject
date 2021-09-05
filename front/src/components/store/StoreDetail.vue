<template>
  <div>
    <h2>storeInfo</h2>
    {{ storeInfo }}
    <h2>storeFiles</h2>
    {{ storeFiles }}
    <h2>isMyStore</h2>
    {{ isMyStore }}
  </div>
</template>

<script>
export default {
  props: ["storeInfo", "storeFiles", "isMyStore"],
  // isMyStore == true 일때 자신의 가게임.
  components: {},
};
</script>

<style scoped></style>
