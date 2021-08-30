<template>
  <div>
    <!-- 로딩중일때 -->
    <div v-if="!shopList">
      <div class="spinner-border" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
    <!-- 가게가 없을때 -->
    <div v-else-if="shopList.length == 0">
      <div>충분한정보가없습니다!</div>
    </div>
    <!-- 가게가 있을때 -->
    <div v-else-if="shopList != null || shopList.lenth > 1">
      <div
        class="col-xs-8 col-sm-6 m-2 shopdiv"
        v-for="(shop, index) in shopList"
        :key="index"
      >
        <div class="list-group-item" style="height: 100px">
          <div @click="showShopDetail(shop.store_id)">
            <div class=".col-md-3 .col-md-pull-9 f-left">
              <img class="menu-img" :src="shop.fileUrl" />
            </div>
            <div class=".col-md-9 .col-md-push-3 f-right">
              <div class="d-flex w-100 justify-content-between">
                <!--가게이름-->
                <h6 class="mb-1">{{ shop.name }}</h6>
                <!-- 가게거리, computed를 이용해서 대략적 시간계산할것 -->
                <small>{{ shop.distance }} km</small>
              </div>
              <!-- 가게 종류 -->
              <p class="mb-1 menu-p">{{ shop.storeKind }}</p>
              <p class="menu-p" style="margin-bottom: 5px">
                <!-- 주소 -->
                {{ shop.address }}, {{ shop.addressDetail }}
              </p>
              <!--주소-->
              <p class="menu-p">
                <!-- 전화번호 -->
                {{ shop.phone }}&nbsp;&nbsp;<span class="details">{{
                  shop.distance
                }}</span>
              </p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
export default {
  props: ["shopList"],
  methods: {
    showShopDetail(storeId) {
      this.$router.push({
        path: "/shopDetail?n=" + storeId,
        query: { shopInfo: storeId },
      });
    },
  },
};
</script>

<style scoped>
.col-xs-8 .list-group-item {
  border: 2px solid lightskyblue;
}
.menu-img {
  width: 80px;
  height: 80px;
  left: 14px;
}
.mb-1 {
  font-weight: bold;
}
.menu-p {
  text-align: left;
  font-size: 10px;
}
.f-left {
  float: left;
  width: 28%;
}
.f-right {
  float: right;
  width: 70%;
  height: 80px;
}

.row div div a {
  margin-top: 10px;
}
.list-group-item:hover {
  cursor: pointer;
  background-color: rgba(11, 153, 153, 0.3);
}
</style>
