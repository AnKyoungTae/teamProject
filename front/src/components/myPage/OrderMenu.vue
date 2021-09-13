<template>
  <div style="margin-top: 50px">
    <h1 class="nearShopTitle">
      <p>주문내역</p>
    </h1>
  </div>
  <div style="width: 100%">
    <div v-if="orderLists.length == 0">주문내역이 존재하지 않습니다</div>
    <div class="manage-div">
      <div class="manage-div2">
        <div class="profile-manage">
          <div class="menu-box">
            <div
              style="cursor: pointer; width: 100%"
              v-for="(orderList, index) in orderLists"
              :key="index"
              @click="showOrderDetail(orderList.orderId)"
            >
              <div class="menu-div">
                <div class="menu-top">
                  <div class="top-left">
                    <span style="color: lightgray"
                      >{{ orderList.orderDate[0] }}.{{
                        orderList.orderDate[1]
                      }}.{{ orderList.orderDate[2] }}&nbsp;&nbsp;
                      {{ orderList.orderDate[3] }}시
                      {{ orderList.orderDate[4] }}분 · 배달중</span
                    >
                  </div>
                  <div class="top-right">
                    <span>주문번호:{{ orderList.orderId }}</span>
                  </div>
                </div>
                <div class="menu-bottom">
                  <div class="bottom-left">
                    <router-link to="/food">
                      <img class="shopImg" :src="orderList.image" />
                    </router-link>
                  </div>
                  <div class="bottom-right">
                    <div style="padding: 20px 0px; line-height: 100px">
                      <div style="float: left; padding-left: 30px">
                        <h4>{{ orderList.storeName }}</h4>
                        <div class="myMenu">
                          <h6>
                            {{ orderList.foodName }}x{{ orderList.quantity }}개
                          </h6>
                        </div>
                      </div>
                    </div>
                  </div>
                </div>
                <div style="text-align: right">
                  <button
                    v-if="orderList.status == 'N'"
                    type="button"
                    class="btn"
                    style="font-size: 13px"
                  >
                    주문취소완료
                  </button>

                  <button
                    v-else-if="orderList.status == 'C'"
                    type="button"
                    class="btn"
                    style="font-size: 13px"
                  >
                    주문취소중입니다
                  </button>
                  <button
                    v-else
                    type="button"
                    @Click="orderDel(orderList.orderId)"
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
      </div>
    </div>
  </div>
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
    orderDel(orderId) {
      http
        .post("/order/requestCancel", {
          orderId: orderId,
          status: "C",
        })
        .then((res) => {
          if (res.status === 200) {
            alert("주문 취소를 요청하였습니다!");
          } else {
            alert("알수없는 오류입니다. 관리자에게 문의하세요");
          }
        });
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
          this.orderLists = res.data.reverse();
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
  margin: 0 auto;
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

.nearShopTitle {
  font-weight: bolder;
  text-align: end;
  margin-bottom: 20px;
  color: #91afba;
  font-family: BMDOHYEON;
  user-select: none;
  display: flex;
  justify-content: end;
  width: 80%;
}
.nearShopTitle > p {
  width: 30%;
  border-bottom: 8px solid #ffda77;
  padding-bottom: 10px;
}

@font-face {
  font-family: "BMDOHYEON";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
</style>
