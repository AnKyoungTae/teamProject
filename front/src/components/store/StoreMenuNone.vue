<template>
  <div id="wrapper">
    <router-link to="/addMenu"
      ><div style="height: 35%"></div>
      <div class="btn btn-primary">음식 추가하기</div></router-link
    >
  </div>
</template>

<script>
export default {};
</script>

<style scoped>
#wrapper {
  height: 180px;
  border-radius: 10px;
  border: 2px dashed paleturquoise;
  vertical-align: middle;
  display: table-cell;
}
.btn-primary {
  background-color: #00a178;
}
.btn-primary:hover {
  background-color: #75ab7f;
}
</style>
