<template style="background-color: #fafafa">
  <div v-if="dataLoaded">
    <div class="storeBox" style="background-color: #fafafa">
      <div
        class="storeName"
        style="font-weight: 800; font-size: 30px; margin-top: 30px"
      >
        {{ shopInfo.storeInfo.name }}
      </div>
      <hr style="width: 100%" />
      <div class="storeText" style="width: 100%; height: 300px">
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
          <p style="color: gray; margin-bottom: 35px">
            {{ shopInfo.storeInfo.storeKind }}
          </p>
          <span style="font-weight: 600; font-size: 17px">업체정보</span>
          <hr style="margin-top: 5px; width: 95%" />
          <table>
            <tr>
              <td style="color: gray">전화번호</td>
              <td style="padding-left: 20px">{{ shopInfo.storeInfo.phone }}</td>
            </tr>
            <tr>
              <td style="color: gray; margin-top: 20px">주소</td>
              <td style="padding-left: 20px">
                {{ shopInfo.storeInfo.address }}
                {{ shopInfo.storeInfo.addressDetail }}
              </td>
            </tr>
            <tr>
              <td style="color: gray; margin-top: 20px">별점</td>
              <td style="padding-left: 20px"></td>
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
      <review v-else-if="currentComp === 'review'"></review>
      <div v-else>
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
    </div>

    <div style="border: 1px solid black"></div>
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
export default {
  components: { shopMenu, shopMap, review },
  mounted() {
    let storeId = this.$route.query.shopInfo;
    this.getStoreInfo(storeId);
    this.setComponent("shopMenu");
  },
  data() {
    return {
      shopInfo: null,
      dataLoaded: false,
      currentComp: "shopMenu",
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
        });
    },
    setComponent(comp) {
      this.currentComp = comp;
    },
  },
};
</script>

<style scoped>
input {
  background-color: gray;
}
.btn-group {
  background-color: white;
}
.btn-outline-primary {
  border-radius: 0px;
}
</style>
