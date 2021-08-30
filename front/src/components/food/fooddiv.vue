<template>
  <div class="food-div" v-for="(food, index) in foodCategories" :key="index">
    <a class="food-a"
      ><div class="nav-link" @click="chooseCatrgory(food.keyword)">
        <div class="food-title">
          <p class="food-p">{{ food.foodname }}</p>
          <img
            class="food-img"
            :src="food.src"
            style="width: 300px; left: -10px; top: 35px"
          />
        </div>
        <!-- 이미지만 바꿔주면 됨 -->
      </div></a
    >
  </div>
</template>

<script>
export default {
  data() {
    return {
      foodCategories: [
        {
          foodname: "전체보기",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "ALL",
        },
        {
          foodname: "1인분 주문",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "1인분",
        },
        {
          foodname: "프랜차이즈",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "프랜차이즈",
        },
        {
          foodname: "치킨",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "치킨",
        },
        {
          foodname: "피자/양식",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "양식",
        },
        {
          foodname: "중국집",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "중식",
        },
        {
          foodname: "한식",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "한식",
        },
        {
          foodname: "일식/돈가스",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "일식",
        },
        {
          foodname: "족발/보쌈",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "족발",
        },
        {
          foodname: "야식",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "야식",
        },
        {
          foodname: "분식",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "분식",
        },
        {
          foodname: "카페/디저트",
          src: "../../assets/pizza-3000285_1280.png",
          keyword: "카페",
        },
      ],
    };
  },
  methods: {
    chooseCatrgory(keyword) {
      this.$router.push({ path: "/shopList", query: { keyword: keyword } });
    },
  },
};
</script>

<style scoped>
.food-div {
  color: black;
  text-decoration: none;
  padding: 15px;

  display: inline-block;

  width: 200px;
  height: 200px;

  margin: 5px;
  text-align: left;

  background-color: lightyellow;
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
.nav-link:hover {
  cursor: pointer;
}
</style>
