<template>
  <div class="home">
    <div class="emptyBox">
      <!-- 타이핑섹션 -->
      <div class="text_box">
        <span class="text"></span><span class="blink">|</span>
      </div>
    </div>
    <div class="emptyBox">
      <!-- 바로가기섹션 -->
      <div class="p2">
        <div class="heightBox"></div>
        <div class="heightBox">
          <div class="shortCut" @click="showAkinator">아키네이터 시작하기</div>
          <div class="shortCut" @click="showNearStores">주변가게보기</div>
        </div>
        <div class="heightBox"></div>
      </div>
    </div>
    <div class="emptyBox">
      <!-- 가까운가게섹션 -->
      <div class="shopList">
        <div id="layoutSidenav_content">
          <main>
            <!--메인 이름, 지도, 가게 리스트 -->
            <div class="container-fluid px-4">
              <!--메인 이름 WA POO -->
              <h1 class="mt-4">주변가게보기</h1>
              <div class="row">
                <!--지도 -->
                <div class="col-xl-7">
                  <div class="card mb-4">
                    <kakao style="z-index: 0" :shopList="nearShopList"></kakao>
                  </div>
                </div>
                <!--가게 리스트 -->
                <div v-if="!showList">
                  <!-- 로딩중 -->
                  <div class="spinner-border" role="status">
                    <span class="visually-hidden">Loading...</span>
                  </div>
                </div>
                <div class="col-xl-5">
                  <div class="card mb-4">
                    <!-- 가게가 있을 때 -->
                    <div v-if="showList">
                      <div class="list-group" v-if="nearShopList">
                        <ShopList
                          v-for="shop in nearShopList"
                          :key="shop"
                          :shopInfo="shop"
                        >
                        </ShopList>
                      </div>
                      <!-- 가게가 없을 때 -->
                      <span v-else-if="!nearShopList">
                        주변에 가게가 없습니당!!</span
                      >
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <!-- 메뉴 리스트 시작-->
            <div class="card mb-4"></div>
          </main>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import Kakao from "@/components/map/Kakao.vue";
import ShopList from "@/components/shop/ShopList.vue";
import { mapGetters, mapMutations } from "vuex";
import http from "@/api/http";

export default {
  data() {
    return {
      nearShopList: [],
      showList: false,
      isFirst: true,
    };
  },
  components: {
    Kakao,
    ShopList,
  },
  computed: {
    ...mapGetters({ LAT: "GET_LAT", LON: "GET_LON", OBS: "GET_OBSERVED" }),
  },
  watch: {
    LAT(newVal, oldVal) {
      if (newVal != oldVal) {
        this.showStoreList();
      }
    },
    LON(newVal, oldVal) {
      if (newVal != oldVal) {
        this.showStoreList();
      }
    },
  },
  methods: {
    ...mapMutations(["SET_AKINATOR"]),
    showStoreList() {
      if (this.OBS === true) {
        this.getStoreListByLocation(this.LAT, this.LON);
      }
    },
    getStoreListByLocation(lat, lon) {
      http
        .post("/store/getStoreListByLocation", { lat, lon })
        .then((res) => {
          this.nearShopList = res.data;
          this.showList = true;
        })
        .catch((e) => {
          console.log(e);
        });
    },
    showShopDetail(storeId) {
      // window.location.href =
      //   "http://localhost:8081/shopDetail?shopInfo=" + storeId;
      console.log(storeId);
    },
    runTyping() {
      const content = "오늘 뭐 먹지? \n -WAPOO";
      const text = document.querySelector(".text");
      let i = 0;

      function typing() {
        let txt = content[i++];
        text.innerHTML += txt === "\n" ? "<br/>" : txt;
        if (i > content.length) {
          text.textContent = "";
          i = 0;
        }
      }
      setInterval(typing, 200);
    },
    showAkinator() {
      this.$router.push({ path: "akinator" });
    },
    showNearStores() {
      document.querySelector(".shopList").scrollIntoView();
    },
  },
  mounted() {
    this.showStoreList();
    this.runTyping();
  },
};
</script>

<style scoped>
.home {
  background-image: url("../assets/bibim.jpg");
  background-color: #355f6e;
  background-repeat: no-repeat;
  background-size: cover;
  background-attachment: fixed;
  min-width: 100%;
  display: flex;
  flex-direction: column;
  scroll-snap-type: y mandatory;
  height: 100%;
}
.emptyBox {
  height: 800px;
  scroll-snap-align: center;
}
.text_box {
  font-size: 2rem;
  text-align: center;
  padding-top: 300px;
  color: white;
  font-size: 3em;
  font-weight: bolder;
}

.blink {
  animation: blink 0.5s infinite;
  font-size: 2.5rem;
}

@keyframes blink {
  to {
    opacity: 0;
  }
}
.shopList {
  background: white;
  height: 100vh;
}
.p2 {
  display: flex;

  align-items: center;
  flex-direction: column;
}
.heightBox {
  height: 200px;
}
.shortCut {
  background: #ffda77;
  font-weight: bold;
  font-size: 1.4em;
  color: #fd3a69;
  width: 400px;
  margin: 30px;
  padding: 10px;
  border: 4px outset white;
  border-radius: 30px;
}
.shortCut:hover {
  cursor: pointer;
  border: 4px inset whitesmoke;
}
</style>
