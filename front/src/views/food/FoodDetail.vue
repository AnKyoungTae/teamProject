<template style="background-color: #fafafa">
  <div v-if="dataLoaded">
    <div class="storeBox" style="background-color: #fafafa">
      <div
        class="storeName"
        style="font-weight: 800; font-size: 30px; padding-top: 20px"
      >
        {{ shopInfo.storeInfo.name }}
      </div>
      <div
        class="storeText"
        style="width: 100%; height: 300px; margin-top: 15px"
      >
        <div
          class="storeImg"
          style="float: left; width: 45%; text-align: right"
        >
          <img
            :src="shopInfo.fileList[0].name"
            class="col-md-6 mt-2"
            id="image"
            style="width: 280px; height: 280px"
          />
        </div>
        <div
          class="storeInf"
          style="float: right; width: 50%; text-align: left"
        >
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
              <td class="td2" style="padding-left: 20px">{{ averageScore }}</td>
            </tr>
            <tr>
              <td class="td1" style="color: gray; margin-top: 20px">
                <i class="fas fa-credit-card"></i
                ><span class="i-text">결제</span>
              </td>
              <td class="td2" style="padding-left: 20px">KakaoPay</td>
            </tr>
          </table>
        </div>
      </div>

      <div
        class="btn-group"
        role="group"
        aria-label="Basic radio toggle button group"
        style="width: 100%; height: 45px; background-color: #fafafa"
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
        v-if="currentComp === 'shopMenu'"
      ></shopMenu>
      <shopMap
        :localx="shopInfo.storeInfo.localx"
        :localy="shopInfo.storeInfo.localy"
        :shopname="shopInfo.storeInfo.name"
        v-else-if="currentComp === 'shopMap'"
      ></shopMap>
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
import { mapGetters } from "vuex";

export default {
  components: { shopMenu, shopMap, review },
  mounted() {
    this.initStore();
    if (this.myStore.storeId == this.storeId) {
      this.ownStore = true;
    }
    console.log("몇인데 스토어이디? :" + this.storeId);
    this.getStoreInfo(this.storeId);
    this.setComponent("shopMenu");
    this.getAverageScore();
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
    };
  },
  methods: {
    initStore() {
      let storeIdt;
      if (this.$route.query.shopInfo == null) {
        storeIdt = this.myStore.storeId;
      } else {
        storeIdt = this.$route.query.shopInfo;
      }
      this.storeId = storeIdt;
    },
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
.storeBox {
  width: 80%;
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
  color: rgb(255, 194, 115);
  background-color: white;
  border-color: lightgray;
  font-weight: bold;
  border-bottom: 5px solid rgb(255, 205, 139);
  box-shadow: none;
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
</style>
