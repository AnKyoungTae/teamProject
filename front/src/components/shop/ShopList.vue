<template>
  <a href="#" class="shopList">
    <div class="row storeHeader">
      <div class="col-10 storeTitle">
        <h5 class="mb-1">{{ shopInfo.name }}</h5>
      </div>
      <div class="col-2 storeKind">
        <small class="text-muted">{{ getStoreKind }}</small>
      </div>
    </div>
    <div class="row">
      <!-- 음식사진시작 -->
      <div class="col-12">
        <div class="slide">
          <splide :options="options">
            <splide-slide v-for="file in fileList" :key="file">
              <img class="menuImg" :src="file.name" />
            </splide-slide>
          </splide>
        </div>
      </div>
      <!-- 종료 -->
    </div>
    <div class="row address">
      <small> {{ shopInfo.address }}, {{ shopInfo.addressDetail }} </small>
    </div>
  </a>
</template>

<script>
import { Splide, SplideSlide } from "@splidejs/vue-splide";
import "@splidejs/splide/dist/css/themes/splide-default.min.css";
import http from "@/api/http";

export default {
  components: {
    Splide,
    SplideSlide,
  },

  props: ["shopInfo"],
  mounted() {
    console.log(this.shopInfo.storeId); // 사진을 불러오기위함
    if (this.shopInfo.storeId != null) {
      http
        .get("/store/getStoreFiles", {
          params: {
            storeId: this.shopInfo.storeId,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            this.fileList = res.data;
            console.log(res.data);
          }
        })
        .catch((err) => {
          console.log("사진불러오는데 에러" + err);
        });
    }
  },
  data() {
    return {
      fileList: [],
      options: {
        rewind: true, //첫 번째 슬라이드 이전 또는 마지막 슬라이드 이후에 슬라이더를 되감는지 여부.
        speed: 300, // 밀리초 단위의 전환 속도입니다.
        rewindSpeed: 300, //되감기 시 전환 속도(밀리초)입니다.
        focus: "center", //어떤 슬라이드에 초점을 맞춰야 하는지 결정합니다.
        type: "loop",
        breakpoints: {
          1600: {
            width: 700,
            perPage: 0,
          },
          1200: {
            width: 700,
            perPage: 5,
          },
          1100: {
            width: 500,
            perPage: 3,
          },
          900: {
            width: 400,
            perPage: 3,
          },
          700: {
            width: 300,
            perPage: 3,
          },
        },
      },
    };
  },
  methods: {},
  computed: {
    getStoreKind() {
      let arr = this.shopInfo.storeKind.split(",");
      return arr[0] + ", " + arr[1] + "...";
    },
  },
};
</script>

<style scoped>
.shopList {
  width: 100%;
  display: grid;
  justify-content: space-around;
  color: black;
  text-decoration: none;
}
.storeTitle {
  text-align: left;
  padding: 5px 0 0;
}
.storeKind {
  padding: 5px 0 0;
}
.address {
  text-align: left;
  justify-self: start;
  padding: 0px;
}
@media screen and (min-width: 1200px) {
  .slide {
    width: 300px;
    display: none;
  }
  .menuImg {
    width: 0px;
    height: 0px;
  }
  .storeHeader {
    width: 300px;
    height: 50px;
  }
}
@media screen and (max-width: 1200px) {
  .menuImg {
    width: 100px;
    height: 70px;
  }
}
</style>
