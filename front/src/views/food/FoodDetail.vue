<template>
  <div v-if="dataLoaded" class="wrapper">
    <div class="storeBox">
      <div class="storeName">
        <span> {{ shopInfo.storeInfo.name }} </span>
      </div>
      <hr />
      <div
        class="row storeText"
        style="width: 100%; height: 300px; margin-top: 15px"
      >
        <div class="col-md-6">
          <splide
            :options="primaryOptions"
            ref="primary"
            style="margin-bottom: 1rem; z-index: 0"
          >
            <splide-slide v-for="file in shopInfo.fileList" :key="file">
              <img class="splideImg" :src="file.name" alt="??" />
            </splide-slide>
          </splide>
        </div>
        <div class="col-md-6 storeInf" style="text-align: left">
          <p style="color: gray; margin-bottom: 35px; text-align: center">
            {{ shopInfo.storeInfo.storeKind }}
          </p>
          <span style="font-weight: 600; font-size: 17px">업체정보</span>
          <hr style="margin-top: 5px; width: 90%" />
          <table>
            <tr>
              <td class="td1" style="color: gray">
                <i class="fas fa-phone"></i><span class="i-text">전화번호</span>
              </td>
              <td class="td2" style="padding-left: 20px">
                {{ shopInfo.storeInfo.phone }}
              </td>
            </tr>
            <tr>
              <td class="td1" style="color: gray; margin-top: 20px">
                <i class="fas fa-map-marked-alt"></i
                ><span class="i-text">주소</span>
              </td>
              <td class="td2" style="padding-left: 20px">
                {{ shopInfo.storeInfo.address }}
                {{ shopInfo.storeInfo.addressDetail }}
              </td>
            </tr>
            <tr>
              <td class="td1" style="color: gray; margin-top: 20px">
                <i class="fas fa-thumbs-up"></i
                ><span class="i-text">리뷰점수</span>
              </td>
              <td class="td2" style="padding-left: 20px">
                <Reviewicon :averageScore="this.averageScore"></Reviewicon>
              </td>
            </tr>
            <tr>
              <td class="td1" style="color: gray; margin-top: 20px">
                <i class="fas fa-credit-card"></i
                ><span class="i-text">결제</span>
              </td>
              <td class="td2" style="padding-left: 20px">KakaoPay</td>
            </tr>
            <tr>
              <td class="td1" style="color: gray; margin-top: 20px">
                <i class="fas fa-store"></i>
                <span class="i-text">가게설명</span>
              </td>
              <td class="td2" style="padding-left: 20px">
                {{ shopInfo.storeInfo.body }}
              </td>
            </tr>
          </table>
        </div>
      </div>
      <hr />

      <div
        class="btn-group"
        role="group"
        aria-label="Basic radio toggle button group"
        style="
          width: 100%;
          height: 45px;
          background-color: #fafafa;
          margin-bottom: 25px;
        "
      >
        <input
          type="radio"
          class="btn-check"
          name="btnradio"
          id="btnradio1"
          autocomplete="off"
          @click="setComponent('shopMenu')"
          checked
        />
        <label class="btn btn-outline-primary" for="btnradio1">메뉴보기</label>
        <!-- StoreMenu.vue -->

        <input
          type="radio"
          class="btn-check"
          name="btnradio"
          id="btnradio2"
          autocomplete="off"
          @click="setComponent('shopMap')"
        />
        <label class="btn btn-outline-primary" for="btnradio2">지도보기</label>

        <input
          type="radio"
          class="btn-check"
          name="btnradio"
          id="btnradio3"
          autocomplete="off"
          @click="setComponent('review')"
        />
        <label class="btn btn-outline-primary" for="btnradio3">후기</label>
      </div>
      <shopMenu
        :foodList="shopInfo.foodList"
        :searchId="this.$route.query.foodIdsearch"
        v-if="currentComp === 'shopMenu'"
      ></shopMenu>
      <div v-else-if="currentComp === 'shopMap'" class="mapContainer">
        <shopMap
          :localx="shopInfo.storeInfo.localx"
          :localy="shopInfo.storeInfo.localy"
          :shopname="shopInfo.storeInfo.name"
        ></shopMap>
      </div>
      <review v-else-if="currentComp === 'review'" :storeId="storeId"></review>
      <div v-else>
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>
  </div>
  <div v-else-if="!dataLoaded">
    <div class="spinner-border" role="status">
      <span class="visually-hidden">Loading...</span>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import shopMenu from "@/components/adminComponent/StoreMenu.vue";
import shopMap from "@/components/adminComponent/StoreMap.vue";
import review from "@/components/adminComponent/Review.vue";
import Reviewicon from "@/components/shop/Reviewicon.vue";
import { Splide, SplideSlide } from "@splidejs/vue-splide";
import "@splidejs/splide/dist/css/themes/splide-default.min.css";

import { mapGetters } from "vuex";

export default {
  components: {
    Splide,
    SplideSlide,
    shopMenu,
    shopMap,
    review,
    Reviewicon,
  },
  mounted() {
    window.scrollTo(0, 0);
    this.storeId = this.$route.query.shopInfo;
    this.getStoreInfo(this.storeId);
    this.setComponent("shopMenu");
    this.getAverageScore();

    // 슬라이드 sync가 안됨
    //this.$refs.primary.sync( this.$refs.secondary.splide );
  },
  computed: {
    ...mapGetters({ myStore: "auth/getMyStore" }),
  },
  data() {
    return {
      shopInfo: null,
      dataLoaded: false,
      currentComp: "shopMenu",
      averageScore: null,
      ownStore: false,
      storeId: null,
      //슬라이드
      primaryOptions: {
        type: "loop",
        width: 500,
        height: 300,
        pagination: false,
      },
      /*
      secondaryOptions: {
        type        : 'slide',
        rewind      : true,
        width       : 300,
        gap         : '1rem',
        pagination  : false,
        fixedWidth  : 110,
        fixedHeight : 70,
        cover       : true,
        focus       : 'center',
        isNavigation: true,
      },
      */
    };
  },
  methods: {
    getStoreInfo(storeId) {
      http
        .get("/store/getStoreInfos", {
          params: {
            storeId: storeId,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.shopInfo = res.data;
          this.dataLoaded = true;
          console.log(this.shopInfo.fileList);
        });
    },
    setComponent(comp) {
      this.currentComp = comp;
    },
    getAverageScore() {
      http
        .get("/review/getAverageScore", {
          params: {
            storeId: this.storeId,
          },
        })
        .then((response) => {
          this.averageScore = response.data;
          console.log("점수 평균값 : " + this.averageScore);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
.wrapper {
  display: flex;
  flex-wrap: wrap;
  align-content: center;
  align-items: center;
  justify-content: center;
}
@font-face {
  font-family: "BMHANNAPro";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_seven@1.0/BMHANNAPro.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
.storeName {
  font-family: BMHANNAPro;
  font-weight: bolder;
  font-size: 3em;
  margin: 25px;
}
.storeName > span {
  border-bottom: 12px solid #ffda77;
  padding: 0 0 0.2em 0;
}
.storeText {
  display: flex;
  justify-content: center;
  margin-bottom: 1em;
}
.slide {
}
.storeBox {
  width: 80%;
  border-right: 1px solid #91afba;
  border-left: 1px solid #91afba;
  padding: 10px;
}
input {
  background-color: gray;
}
.btn-group {
  background-color: white;
}
.btn-outline-primary {
  border-radius: 0px;
}
/* (메뉴, 지도보기, 후기)버튼 클릭했을 때 */
.btn-check:active + .btn-outline-primary,
.btn-check:checked + .btn-outline-primary,
.btn-outline-primary.active,
.btn-outline-primary.dropdown-toggle.show,
.btn-outline-primary:active {
  color: #ffefa3;
  background-color: white;
  border-color: lightgray;
  font-weight: bold;
  border-bottom: 5px solid #ffefa3;
  box-shadow: none;
  z-index: 0;
}
.btn-outline-primary {
  border-color: lightgray;
  color: black;
}
/* 마우스커서가 올라갔을 때 */
.btn-outline-primary:hover {
  color: black;
  background-color: white;
  border-color: lightgray;
  border: 2px solid lightgray;
}
.td1 {
  font-size: 14px;
  padding: 3px;
}
.td2 {
  font-size: 14px;
}
.btn {
  padding: 10px;
}
.i-text {
  margin-left: 10px;
}
.splideImg {
  width: 100%;
  height: 300px;
}
</style>
