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
          <splide  class="slide" :options="options">
            <splide-slide v-for="file in fileList" :key="file" style="border:1px solid red;">
              <img class="menuImg" :src="file.name" />
            </splide-slide>
          </splide>
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
    console.log("사진불러옴");
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
        speed      : 300,       // 밀리초 단위의 전환 속도입니다.
        perMove    : 1,         //슬라이더가 다음 또는 이전 페이지로 이동할 때 이동해야 하는 슬라이드 수를 결정합니다.
        trimSpace  : true,      //첫 번째 슬라이드 이전 또는 마지막 슬라이드 이후에 공백을 트리밍할지 여부입니다.
        pagination : false,     //페이지 매김(표시 점)을 추가할지 여부입니다.
        type       : 'loop',    //슬라이더 유형을 결정합니다.
        focus      : 'center',  //어떤 슬라이드에 초점을 맞춰야 하는지 결정합니다.
        destroy    : true,
        breakpoints: {
          1200: {
            destroy: false,
            width: 700,
            perPage: 5,
          },
          1100: {
            width: 600,
            perPage: 5,
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
.slide {
  border:1px solid black;
}
@media screen and (min-width: 1200px) {
  .storeHeader {
    width: 300px;
    height: 50px;
  }
  .menuImg {
    display: none;
  }
}
@media screen and (max-width: 1200px) {
  .storeHeader {
    width: 100%;
  }
  .splide {
    width: 700px;
  }
  .menuImg {
    width: 100px;
    height: 70px;
  }
}
</style>
