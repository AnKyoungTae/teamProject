<template>
  <div class="food-div" v-for="(food, index) in foodCategories" :key="index">
    <a class="food-a"
      ><div class="nav-link" @click="chooseCatrgory(food.keyword)">
        <div class="food-title">
          <p class="food-p">{{ food.foodname }}</p>
          <img
            class="food-img"
            :class="[
              index === 0 ? 'first' : '',
              index !== 0 ? 'first' + index : '',
            ]"
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
          src: require("../../assets/food-6564270_1280.png"),
          keyword: "ALL",
        },
        {
          foodname: "1인분 주문",
          src: require("../../assets/salmon.jpg"),
          keyword: "1인분",
        },
        {
          foodname: "프랜차이즈",
          src: require("../../assets/burger.jpg"),
          keyword: "프랜차이즈",
        },
        {
          foodname: "치킨",
          src: require("../../assets/chicken.jpg"),
          keyword: "치킨",
        },
        {
          foodname: "피자/양식",
          src: require("../../assets/pizza-3000285_1280.png"),
          keyword: "양식",
        },
        {
          foodname: "중국집",
          src: require("../../assets/JaJangMyeon.jpg"),
          keyword: "중식",
        },
        {
          foodname: "한식",
          src: require("../../assets/korean.jpg"),
          keyword: "한식",
        },
        {
          foodname: "일식/돈가스",
          src: require("../../assets/sushi.jpg"),
          keyword: "일식",
        },
        {
          foodname: "족발/보쌈",
          src: require("../../assets/pork.jpg"),
          keyword: "족발",
        },
        {
          foodname: "야식",
          src: require("../../assets/giblets.jpg"),
          keyword: "야식",
        },
        {
          foodname: "분식",
          src: require("../../assets/tteokbokki.jpg"),
          keyword: "분식",
        },
        {
          foodname: "카페/디저트",
          src: require("../../assets/bananas.jpg"),
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
  padding-top: 10px;

  display: inline-block;

  width: 200px;
  height: 200px;

  margin: 5px;
  text-align: left;

  background-color: #fafafa;
}
.food-a {
  position: relative;
  padding: 0;
  margin: 5px 0;
  text-decoration: none;
}
.food-a:hover {
  cursor: pointer;
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
  border: 2px solid lightgray;
}
.food-div:hover {
  border: 2px solid teal;
}
.first {
  width: 220px;
  left: 10px;
}
.first1 {
  width: 220px;
  left: -5px;
}
.first2 {
  width: 220px;
  left: -10px;
}
.first3 {
  width: 230px;
  left: -20px;
}
.first4 {
  width: 300px;
  left: -10px;
  top: 35px;
}
.first5 {
  width: 225px;
  left: -7px;
}
.first6 {
  width: 210px;
  left: -8px;
}
.first7 {
  width: 220px;
  left: -6px;
}
.first8 {
  width: 197px;
  left: -1px;
}
.first9 {
  width: 220px;
  left: -7px;
}
.first10 {
  width: 220px;
  left: -5px;
}
.first11 {
  width: 220px;
  left: -7px;
}
</style>
