<template>
  <div class="container">
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div>
      <div class="container-fluid px-4">
        <div class="row">
          <nav>
            <ul class="foodnum-ul" style="position: sticky">
              <li>
                <a @click="setOptAndrequest('ALL')" class="foodnum-a"
                  >전체보기</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('1인분')" class="foodnum-a"
                  >1인분 주문</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('프랜차이즈')" class="foodnum-a"
                  >프랜차이즈</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('치킨')" class="foodnum-a">치킨</a>
              </li>
              <li>
                <a @click="setOptAndrequest('양식')" class="foodnum-a"
                  >피자/양식</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('중식')" class="foodnum-a"
                  >중국집</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('한식')" class="foodnum-a">한식</a>
              </li>
              <li>
                <a @click="setOptAndrequest('일식')" class="foodnum-a"
                  >일식/돈가스</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('족발')" class="foodnum-a"
                  >족발/보쌈</a
                >
              </li>
              <li>
                <a @click="setOptAndrequest('야식')" class="foodnum-a">야식</a>
              </li>
              <li>
                <a @click="setOptAndrequest('분식')" class="foodnum-a">분식</a>
              </li>
              <li>
                <a @click="setOptAndrequest('카페')" class="foodnum-a"
                  >카페/디저트</a
                >
              </li>
            </ul>
          </nav>

          <div>
            <div class="row" id="listContainer">
              <foodlist :shopList="getShopList"></foodlist>
              <div ref="scrollObserver" v-if="!noMoreShop">
                <div class="spinner-grow m-2" role="status">
                  <span class="visually-hidden">Loading...</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
    {{ loadFrom }}
    {{ quantity }}
    {{ noMoreShop }}
  </div>
</template>

<script>
import foodlist from "../../components/food/foodlist.vue";
import { mapGetters } from "vuex";
import { error, success } from "@/api/notification";
import http from "@/api/http";

export default {
  data() {
    return {
      shopList: [], // 불러온 가게리스트
      quantity: 10, // 몇개나 불러올건지?
      option: "ALL", // 무엇을 불러올것인지?
      loadFrom: 0,
      dataLoaded: false,
      noMoreShop: false,
      io: null,
    };
  },
  computed: {
    ...mapGetters(["GET_LAT", "GET_LON"]),
    getShopList() {
      return this.shopList;
    },
  },
  mounted() {
    let keyword = this.$route.query.keyword;

    if (!keyword) {
      this.options = "ALL";
    } else {
      this.options = keyword;
    }
    this.initIntersectionObserver();
  },
  methods: {
    async requestShopList() {
      this.dataLoaded = false;
      if (!this.GET_LAT || !this.GET_LON) {
        error("위치정보를 확인할수 없습니다. 다시 시도해주세요.", this);
        this.$router.push({ path: "/" });
        return;
      }
      if (!this.quantity) {
        error("잘못된 요청입니다", this);
        return;
      }

      if (this.noMoreShop == true) {
        return;
      }

      const data = {
        lat: this.GET_LAT,
        lon: this.GET_LON,
        quantity: this.quantity,
        loadFrom: this.loadFrom,
        options: this.option,
      };
      await http
        .post("/store/getStoreListByLocation", data)
        .then((res) => {
          if (res.status == 200) {
            if (res.data == []) {
              this.noMoreShop = true;
            }
            let arr = this.shopList.concat(res.data);
            this.shopList = arr;
            this.loadFrom += this.quantity;
            this.dataLoaded = true;
            if (res.data.length < this.quantity) {
              this.noMoreShop = true;
              this.io = null;
            }
          }
        })
        .catch((err) => {
          console.log("에러남 " + err);
          this.io = null;
          this.noMoreShop = true;
          success("더 이상 불러올 가게 정보가 없습니다", this);
        });
    },
    initIntersectionObserver() {
      this.io = new IntersectionObserver(
        async ([entry], observer) => {
          console.log("<인터섹팅>" + entry);
          if (entry.isIntersecting) {
            console.log("!관찰됨!");
            observer.unobserve(entry.target);
            await this.requestShopList();
            console.log("--기다림의 끝--");
            observer.observe(entry.target);
          }
        },
        {
          rootMargin: "50px",
          threshold: 0.5,
        }
      );
      console.log("관찰시작");
      this.io.observe(this.$refs.scrollObserver);
    },
    setOptAndrequest(option) {
      this.io = null;
      if (option != null) {
        this.dataLoaded = false;
        this.option = option;
        this.shopList = [];
        this.quantity = 10;
        this.loadFrom = 0;
        this.noMoreShop = false;
      }
      let func = this.initIntersectionObserver;
      // setTimeout(() => {
      //   func();
      // }, 500);
      this.$nextTick(function () {
        func();
      });
    },
  },
  components: {
    foodlist,
  },
};
</script>

<style scoped>
/* 기본 설정*/
.foodnum-a {
  text-decoration: none;
  color: #000000;
  padding: 4px;
}
.foodnum-a:hover {
  cursor: pointer;
  background-color: lightblue;
}

/* nav tag */

.foodnum-ul {
  font-style: bold;
  font-size: 15px;
  top: 4px;
  width: 1000px;
  padding: 0px;
  padding-top: 16px;
}

.foodnum-ul li {
  display: inline;
  border-left: 1px solid #999; /* 메뉴의 왼쪽에 "|" 표시 */
  padding: 0 10px; /* 각 메뉴 간격 */
}
.foodnum-ul li:first-child {
  border-left: none;
} /* 메뉴 분류중 제일 왼쪽의 "|"는 삭제  */

.moreShop {
  width: 120px;
  height: 40px;
  position: fixed;
  bottom: 10%;
  right: 10%;
}
</style>
