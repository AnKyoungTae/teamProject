<template>
  <div style="width: 100%; margin-top: 50px">
    <h1 class="nearShopTitle" style="margin-right: 50px">
      <p>주문상세정보페이지</p>
    </h1>
  </div>
  <div class="container" style="width: 1000px">
    <div class="m-4">
      <p>주문번호 : {{ orderId }}</p>
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
          <div
            style="
              font-size: 20px;
              width: 600px;
              text-align: left;
              margin-left: 20px;
            "
          >
            <img
              :src="foodList.fileUrl"
              style="width: 100px; height: 100px; margin-right: 30px"
            />
            <span v-if="foodList.orderStatus == 'S'">
              <!-- 주문이 막 들어왔을 때 -->
              {{ foodList.name }}
              (<span class="prepareOrderTag">준비중</span>)
            </span>
            <span v-else-if="foodList.orderStatus == 'Y'">
              <!-- 주문이 승인났을때 -->
              {{ foodList.name }}
            </span>
            <span v-else-if="foodList.orderStatus == 'C'">
              {{ foodList.name }}
              <span class="requestCancleTag">취소요청중</span>
            </span>
            <span v-else>
              <!-- 주문이 취소되었을때 -->

              <span class="cancledItemName">{{ foodList.name }}</span>
              <span class="cancledItemTag"> 취소된 상품</span>
            </span>
          </div>
          <div style="font-size: 20px; width: 200px">
            {{ foodList.quantity }}
          </div>
          <div style="font-size: 20px; width: 200px">
            <span v-if="foodList.orderStatus == 'S'">
              {{ foodList.price }}
            </span>
            <span v-else-if="foodList.orderStatus == 'Y'">
              {{ foodList.price }}
            </span>
            <span v-else-if="foodList.orderStatus == 'C'">
              {{ foodList.price }}
              <span class="requestCancleTag">취소요청중</span>
            </span>
            <span v-else>
              <span class="cancledItemName">{{ foodList.price }}</span>
              <span class="cancledItemTag"> 0</span>
            </span>
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

        <div style="margin: 50px" v-if="this.orderStatus != ''">
          {{ this.orderStatus }}
        </div>
        <button
          v-else
          type="button"
          class="btn btn-outline-danger"
          style="margin: 50px"
          @click="cancelOrder"
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
      orderStatus: "",
    };
  },
  mounted() {
    this.orderId = this.$route.query.orderId;
    // 오더 아이디가 없거나 0일때는?
    if (this.orderId == null || this.orderId == 0) {
      this.$router.push({ path: "/" });
      alert("잘못된 요청입니다");
    }
    this.getOrderedFoodList();
    //
  },
  methods: {
    getOrderedFoodList() {
      http
        .get("/order/getOrderedFoodList", {
          params: {
            orderId: this.orderId,
          },
        })
        .then((res) => {
          console.log(res.data);
          if (res.data.foodList.length == 0 || res.data.order == null) {
            this.$router.push({ path: "/" });
            alert("잘못된 요청입니다");
            return;
          }
          this.order = res.data.order;
          this.foodList = res.data.foodList;
          this.loaded = true;
          this.getOrderStatus();
        })
        .catch((err) => {
          console.log(err);
        });
    },
    cancelOrder() {
      const orderId = parseInt(this.orderId);
      http
        .post("/order/requestCancel", {
          orderId: orderId,
          status: "C",
        })
        .then((res) => {
          if (res.status === 200) {
            this.getOrderedFoodList();
            alert("주문 취소를 요청하였습니다!");
          } else {
            alert("알수없는 오류입니다. 관리자에게 문의하세요");
          }
        });
    },
    getOrderStatus() {
      let hasC = this.foodList.find((food) => {
        if (food.orderStatus == "C") {
          return true;
        }
      });
      if (hasC) {
        this.orderStatus = "취소중인 주문입니다.";
      }
      let hasN = this.foodList.find((food) => {
        if (food.orderStatus == "N") {
          return true;
        }
      });
      if (hasN) {
        this.orderStatus = "부분 취소된 주문입니다.";
      }
    },
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
.nearShopTitle {
  font-weight: bolder;
  text-align: end;
  margin-bottom: 20px;
  color: #91afba;
  font-family: BMDOHYEON;
  user-select: none;
  display: flex;
  justify-content: end;
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
.cancledItemName {
  text-decoration-line: line-through;
  color: grey;
  white-space: nowrap;
}
.cancledItemTag {
  color: red;
  font-size: 15px;
}
.requestCancleTag {
  color: gray;
  font-size: 15px;
}
.prepareOrderTag {
  color: aqua;
}
</style>
