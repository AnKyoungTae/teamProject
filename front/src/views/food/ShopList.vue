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
                <a @click="requestShopList('ALL')" class="foodnum-a"
                  >전체보기</a
                >
              </li>
              <li>
                <a @click="requestShopList('1인분')" class="foodnum-a"
                  >1인분 주문</a
                >
              </li>
              <li>
                <a @click="requestShopList('프랜차이즈')" class="foodnum-a"
                  >프랜차이즈</a
                >
              </li>
              <li>
                <a @click="requestShopList('치킨')" class="foodnum-a">치킨</a>
              </li>
              <li>
                <a @click="requestShopList('양식')" class="foodnum-a"
                  >피자/양식</a
                >
              </li>
              <li>
                <a @click="requestShopList('중식')" class="foodnum-a">중국집</a>
              </li>
              <li>
                <a @click="requestShopList('한식')" class="foodnum-a">한식</a>
              </li>
              <li>
                <a @click="requestShopList('일식')" class="foodnum-a"
                  >일식/돈가스</a
                >
              </li>
              <li>
                <a @click="requestShopList('족발')" class="foodnum-a"
                  >족발/보쌈</a
                >
              </li>
              <li>
                <a @click="requestShopList('야식')" class="foodnum-a">야식</a>
              </li>
              <li>
                <a @click="requestShopList('분식')" class="foodnum-a">분식</a>
              </li>
              <li>
                <a @click="requestShopList('카페')" class="foodnum-a"
                  >카페/디저트</a
                >
              </li>
            </ul>
          </nav>

          <div>
            <div class="row">
              <foodlist :shopList="getShopList"></foodlist>
            </div>
          </div>
        </div>
      </div>
    </div>
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
    };
  },
  computed: {
    ...mapGetters(["GET_LAT", "GET_LON"]),
    getShopList() {
      return this.shopList;
    },
  },
  mounted() {
    window.addEventListener("scroll", this.handleScroll);
    let keyword = this.$route.query.keyword;
    if (!keyword) {
      this.requestShopList("ALL");
    } else {
      this.requestShopList(keyword);
    }
  },
  methods: {
    requestShopList(option) {
      if (!this.GET_LAT || !this.GET_LON) {
        error("위치정보를 확인할수 없습니다. 다시 시도해주세요.", this);
        this.$router.push({ path: "/" });
        return;
      }
      if (!this.quantity) {
        error("잘못된 요청입니다", this);
        return;
      }
      if (option != null) {
        this.option = option;
        this.shopList = [];
        this.quantity = 10;
        this.loadFrom = 0;
      }

      const data = {
        lat: this.GET_LAT,
        lon: this.GET_LON,
        quantity: this.quantity,
        loadFrom: this.loadFrom,
        options: this.option,
      };
      http
        .post("/store/getStoreListByLocation", data)
        .then((res) => {
          if (res.status === 200) {
            if (res.data == []) {
              this.shopList.push("none");
            }
            let arr = this.shopList.concat(res.data);
            this.shopList = arr;
            this.loadFrom += this.quantity;
            this.dataLoaded = true;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    handleScroll(event) {
      // console.log(
      //   "innerHeight : " +
      //     window.innerHeight +
      //     "\nscrollY : " +
      //     window.scrollY +
      //     "\noffset : " +
      //     document.body.offsetHeight
      // );
      if (
        window.innerHeight + window.scrollY + 5 >=
        document.body.offsetHeight
      ) {
        if (this.dataLoaded == false) {
          return;
        }
        if (this.dataLoaded == true) {
          this.dataLoaded = false;
          this.requestShopList();
        }
      }
    },
  },
  components: {
    foodlist,
  },
  unmounted() {
    console.log("파괴됨");
    window.removeEventListener("scroll", this.handleScroll);
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
  padding-top: 10px;
  font-style: bold;
  font-size: 15px;
  top: 4px;
}

.foodnum-ul li {
  display: inline;
  border-left: 1px solid #999; /* 메뉴의 왼쪽에 "|" 표시 */
  padding: 0 10px; /* 각 메뉴 간격 */
}
.foodnum-ul li:first-child {
  border-left: none;
} /* 메뉴 분류중 제일 왼쪽의 "|"는 삭제  */

.row div div a {
  margin-top: 10px;
}
</style>
