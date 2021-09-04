<template>
  <div style="width: 100%">
    <div class="manage-div">
      <div class="manage-div2">
        <h3 class="manage-text">
          주문내역
        </h3>
        <div class="profile-manage">
          <div class="menu-box">
    <div
      style=" cursor: pointer; width: 100%;"
      v-for="(orderList, index) in orderLists"
      :key="index"
      @click="showOrderDetail(orderList.orderId)"
    >
    <div class="menu-div">
      <div class="menu-top">
        <div class="top-left">
          <span style="color: lightgray;">{{ orderList.orderDate }} · 배달중</span>
        </div>
        <div class="top-right">
          <span>주문번호:{{orderList.order_id}}</span>  
        </div>        
      </div>
      <div class="menu-bottom">
        <div class="bottom-left">
          <router-link to="/food">
          <img
            class="shopImg"
            :src="orderList.image"
            :alt="orderList.orgName"
          />
        </router-link>
        </div>
        <div class="bottom-right">
          <div style="padding: 20px 0px; line-height: 100px;">
            <div style="float: left; padding-left: 30px;">
              <h4>{{ orderList.storeName }}</h4>
              <div class="myMenu">
                <h6>{{ orderList.foodName }} {{ orderList.quantity }}개</h6>
              </div>  
            </div>
        </div>
        </div>
        </div>
        <div style="text-align: right;">
          <button
                type="button"
                @Click="orderDel()"
                class="btn btn-outline-danger"
                style="font-size: 13px"
              >
                주문취소요청
              </button> 
          </div>
        </div>
      </div>
    </div>
  </div>
  </div></div></div>
</template>

<script>
import http from "@/api/http";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      memberId: null,
      orderLists: [],
      orderListLoaded: false,
    };
  },
  methods: {
    orderDel() {
      alert("삭제되었습니다!");
    },
    //주문 상세 페이지
    showOrderDetail(orderId) {
      this.$router.push({ name: "OrderInfo", query: { orderId: orderId } });
    },
    orderList() {
      http
        .get("/profile/getOrder", {
          params: {
            memberId: this.memberId,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.orderLists = res.data;
          this.orderListLoaded = true;
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  mounted() {
    this.memberId = this.getUserId;
    this.orderList();
  },
  computed: {
    ...mapGetters({
      getUserId: "auth/getUserId",
    }),
  },
};
</script>

<style scoped>
.manage-div {
  max-width: 560px;
  margin: 50px auto 0;
  display: block;
}
.manage-div2 {
  display: block;
}
.menu-box {
  margin-top: 16px;
  text-align: left;
}
.menu-div {
  border-bottom: 1px solid lightgray;
  margin-top: 10px;
  padding: 8px 20px;
}
.menu-top {
  display: block;
  margin-bottom: 30px;
}
.top-left {
  float: left;
  width: 50%;
  text-align: left;
}
.top-right {
  float: right;
  width: 50%;
  text-align: right;
}
.bottom-left {
  float: left;
  width: 20%;
}
.bottom-right {
  float: right;
  width: 80%;
  text-align: left;
}
.shopImg {
  width: 100px;
  height: 100px;
  border-radius: 25px;
}
</style>
