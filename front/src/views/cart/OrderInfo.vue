<template>
  <div class="container" style="width: 1000px">
    <div class="m-4">
      <h1>주문상세정보페이지</h1>
      <p>주문번호 : {{ orderId }}</p>
      <hr />
      <hr />
      <div v-if="loaded == false">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>
      <div class="orderInfoFlex" v-else>
        <!-- 로딩완료 -->
        <div
          style="
            font-size: 25px;
            text-align: left;
            border-bottom: 2px solid black;
          "
        >
          주문상품
        </div>
        <div
          class="orderList"
          style="font-size: 20px; border-bottom: 2px solid black"
        >
          <div style="width: 600px">상품명</div>
          <div style="width: 200px">수량</div>
          <div style="width: 200px">금액</div>
        </div>
        <div
          class="orderList"
          v-for="foodList in foodList"
          :key="foodList"
          style="height: 130px; border-bottom: 2px solid black"
        >
          <div style="font-size: 20px; width: 600px; text-align: left">
            <img
              :src="foodList.fileUrl"
              style="width: 100px; height: 100px; margin-right: 30px"
            />
            {{ foodList.name }}
          </div>
          <div style="font-size: 20px; width: 200px">
            {{ foodList.quantity }}
          </div>
          <div style="font-size: 20px; width: 200px">
            {{ foodList.price }}
          </div>
        </div>
        <div
          class="orderList"
          style="font-size: 20px; height: 60px; border-bottom: 2px solid black"
        >
          <div style="width: 800px; text-align: left">총 주문 금액</div>
          <div style="width: 200px">
            {{ order.totalPrice }}
          </div>
        </div>
        <div
          class="orderList"
          style="font-size: 20px; height: 60px; border-bottom: 2px solid black"
        >
          <div style="width: 800px; text-align: left">할인금액</div>
          <div style="width: 200px">
            {{ order.discount }}
          </div>
        </div>
        <div
          class="orderList"
          style="
            font-size: 20px;
            height: 60px;
            border-bottom: 1px solid gainsboro;
          "
        >
          <div style="width: 800px; text-align: left">총 결제금액</div>
          <div style="width: 200px">
            <b>
              {{ order.payment }}
            </b>
          </div>
        </div>
        <div
          class="orderList"
          style="
            font-size: 20px;
            height: 60px;
            border-bottom: 2px solid black;
            margin-bottom: 50px;
          "
        >
          <div style="width: 800px; text-align: left">결제방법</div>
          <div style="width: 200px">카카오페이</div>
        </div>
        <div
          style="
            font-size: 25px;
            text-align: left;
            border-bottom: 2px solid black;
          "
        >
          배송지 정보
        </div>
        <div
          class="orderList"
          style="
            font-size: 20px;
            height: 60px;
            border-bottom: 1px solid gainsboro;
          "
        >
          <div style="width: 200px">주소</div>
          <div style="height: 60px; border-left: 1px solid gainsboro"></div>
          <div style="width: 800px">
            {{ order.address }}
          </div>
        </div>
        <div
          class="orderList"
          style="font-size: 20px; height: 60px; border-bottom: 2px solid black"
        >
          <div style="width: 200px">전화번호</div>
          <div style="height: 60px; border-left: 1px solid gainsboro"></div>
          <div style="width: 800px">
            {{ order.phone }}
          </div>
        </div>
        <button
          type="button"
          class="btn btn-outline-danger"
          style="margin: 50px"
        >
          주문취소
        </button>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
export default {
  data() {
    return {
      orderId: null,
      loaded: false,
      order: null,
      foodList: null,
    };
  },
  mounted() {
    this.orderId = this.$route.query.orderId;
    // 오더 아이디가 없거나 0일때는?
    if (this.orderId == null || this.orderId == 0) {
      this.$router.push({ path: "/" });
      alert("잘못된 요청입니다");
    }
    console.log(this.orderId);
    http
      .get("/order/getOrderedFoodList", {
        params: {
          orderId: this.orderId,
        },
      })
      .then((res) => {
        this.order = res.data.order;
        this.foodList = res.data.foodList;
        this.loaded = true;
      });
  },
};
</script>

<style scoped>
.orderInfo {
  display: flex;
  flex-direction: column;
}
.orderList {
  display: flex;
  flex-direction: row;
  align-items: center;
}
</style>
