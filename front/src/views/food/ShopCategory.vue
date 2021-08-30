<template>
  <div class="food-list">
    <fooddiv></fooddiv>
  </div>
</template>

<script>
import fooddiv from "../../components/food/fooddiv.vue";

export default {
  components: {
    fooddiv,
  },
};
</script>

<style scoped>
.food-list {
  padding: 5px;
  max-width: 1020px;
  margin: 0 auto;
}
.food-div {
  color: black;
  text-decoration: none;
  padding: 15px; /* 패딩: 안쪽여백, 마진: 밖여백 */

  /* 글자가 깨졌으니 밑의 display 추가하자 */
  display: inline-block; /* 인라인모드/블럭모드 이해 필요  */

  width: 200px;
  height: 200px;

  margin: 5px;
  text-align: left;
}
.food-a {
  position: relative;
  padding: 0;
  margin: 5px 0;
  text-decoration: none;
}
.food-title {
  width: 100%;
  color: #333;
  font-size: 18px;
  font-weight: bold;
}
.food-p {
  text-align: left;
  margin-left: 8px;
}
.food-img {
  position: absolute;
}
.food-div {
  overflow: hidden;
  border: 2px solid lightblue;
}
</style>
