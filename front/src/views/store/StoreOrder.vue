<template>
  <div style="margin-top: 50px">
    <h1 class="nearShopTitle" style="margin-right: 50px">
      <p>주문관리</p>
    </h1>
  </div>
  <div style="width: 1200px; margin: 3vh auto">
    <!-- 고객이 주문을 취소할때-->
    <div v-if="cancelStoreOrder.length > 0">
      <span style="font-size: 20px; color: red"> 고객 주문 취소 요청</span>
      <table
        style="
          border-top: 1.5px solid black;
          border-bottom: 1.5px solid black;
          width: 100%;
          margin-bottom: 3vh;
        "
      >
        <tr style="border-bottom: 1.5px solid black">
          <th>주문번호</th>
          <th>주문메뉴</th>
          <th>주문일</th>
          <th>음식가격</th>

          <th>결제금액</th>
          <th>주소</th>
          <th>핸드폰번호</th>
          <th>요청사항</th>
          <th>주문관리</th>
          <th>총관리</th>
        </tr>
        <tr v-for="(cancel, index) in cancelStoreOrder" :key="index">
          <td v-if="cancelStoreOrderId(index)">{{ cancel.orderId }}</td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
          <td>{{ cancel.name }} {{ cancel.quantity }}개</td>
          <td>{{ cancel.orderDate }}</td>
          <td>{{ cancel.price * cancel.quantity }}</td>

          <td>{{ cancel.payment }}</td>
          <td>{{ cancel.address }}</td>
          <td>{{ cancel.phone }}</td>
          <td>{{ cancel.requests }}</td>
          <!--버튼클릭시 status 변경-->
          <td>
            <span
              class="badge bg-danger"
              @click="
                deleteOrder(
                  cancel.orderInfoId,
                  cancel.name,
                  cancel.quantity,
                  cancel.orderId
                )
              "
              >주문취소</span
            >
          </td>
          <!--total button -->
          <td v-if="cancelStoreOrderId(index)">
            <span
              class="badge bg-danger"
              @click="deleteAllOrder('C',cancel.orderId)"
              >전체주문취소</span
            >
          </td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
        </tr>
      </table>
    </div>
    <!-- radio 버튼 -->
    <div style="text-align: left">
      <span style="font-size: 20px; margin-left: 20px">
        <span v-if="status == 'S'">미승인</span>
        <span v-else-if="status == 'Y'">승인</span>
        <span v-else>취소</span>

        주문건수: {{ total }}</span
      >
      <div class="row align-items-center">
        <div class="col-7">
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio1"
              value="ALL"
              checked
              @click="setStatusOption('S')"
            />
            <label class="form-check-label" for="inlineRadio1">미승인</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio3"
              @click="setStatusOption('Y')"
              value="Y"
            />
            <label class="form-check-label" for="inlineRadio3">승인</label>
          </div>
          <div class="form-check form-check-inline">
            <input
              class="form-check-input"
              type="radio"
              name="inlineRadioOptions"
              id="inlineRadio4"
              @click="setStatusOption('N')"
              value="N"
            />
            <label class="form-check-label" for="inlineRadio4">취소</label>
          </div>
        </div>
      </div>
    </div>

    <!-- 주문 승인 대기중-->
    <div v-if="status == 'S'">
      <span style="font-size: 20px"> 주문 요청</span>
      <table
        style="
          border-top: 1.5px solid black;
          border-bottom: 1.5px solid black;
          width: 100%;
        "
      >
        <tr style="border-bottom: 1.5px solid black">
          <th>주문번호</th>
          <th>주문메뉴</th>
          <th>주문일</th>
          <th>음식가격</th>

          <th>결제금액</th>
          <th>주소</th>
          <th>핸드폰번호</th>
          <th>요청사항</th>
          <th>주문관리</th>
          <th>총관리</th>
        </tr>
        <tr v-for="(li, index) in list" :key="index">
          <td v-if="sameOrderId(index)">{{ li.orderId }}</td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
          <td>{{ li.name }} {{ li.quantity }}개</td>
          <td>{{ li.orderDate }}</td>
          <td>{{ li.price * li.quantity }}</td>

          <td>{{ li.payment }}</td>
          <td>{{ li.address }}</td>
          <td>{{ li.phone }}</td>
          <td>{{ li.requests }}</td>
          <!--버튼클릭시 status 변경-->
          <td>
            <span
              class="badge bg-danger"
              style="margin: 1vh 0"
              @click="
                deleteOrder(li.orderInfoId, li.name, li.quantity, li.orderId)
              "
              >주문취소</span
            >
          </td>
          <!--total button -->
          <td v-if="pluslOrder(index)">
            <span class="badge bg-success" @click="approveOrder(li.orderId)"
              >전체주문받기</span
            >
            <span class="badge bg-danger" @click="deleteAllOrder('L',li.orderId)"
              >전체주문취소</span
            >
          </td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
        </tr>
      </table>
    </div>

    <!-- 주문 승인 완료-->
    <div v-else-if="status == 'Y'">
      <span style="font-size: 20px"> 주문 승인</span>
      <table
        style="
          border-top: 1.5px solid black;
          border-bottom: 1.5px solid black;
          width: 100%;
        "
      >
        <tr style="border-bottom: 1.5px solid black">
          <th>주문번호</th>
          <th>주문메뉴</th>
          <th>주문일</th>
          <th>음식가격</th>

          <th>결제금액</th>
          <th>주소</th>
          <th>핸드폰번호</th>
          <th>요청사항</th>
          <th>주문관리</th>
        </tr>
        <tr v-for="(li, index) in list" :key="index">
          <td v-if="sameOrderId(index)">{{ li.orderId }}</td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
          <td>{{ li.name }} {{ li.quantity }}개</td>
          <td>{{ li.orderDate }}</td>
          <td>{{ li.price * li.quantity }}</td>

          <td>{{ li.payment }}</td>
          <td>{{ li.address }}</td>
          <td>{{ li.phone }}</td>
          <td>{{ li.requests }}</td>

          <!--total button -->
          <td v-if="pluslOrder(index)">
            <span class="badge bg-success">승인완료</span>
          </td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
        </tr>
      </table>
    </div>

    <!-- 주문 취소 완료-->
    <div v-else-if="status == 'N'">
      <span style="font-size: 20px"> 주문 취소</span>
      <table
        style="
          border-top: 1.5px solid black;
          border-bottom: 1.5px solid black;
          width: 100%;
        "
      >
        <tr style="border-bottom: 1.5px solid black">
          <th>주문번호</th>
          <th>주문메뉴</th>
          <th>주문일</th>
          <th>음식가격</th>

          <th>결제금액</th>
          <th>주소</th>
          <th>핸드폰번호</th>
          <th>요청사항</th>
          <th>주문관리</th>
        </tr>
        <tr v-for="(li, index) in list" :key="index">
          <td v-if="sameOrderId(index)">{{ li.orderId }}</td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
          <td>{{ li.name }} {{ li.quantity }}개</td>
          <td>{{ li.orderDate }}</td>
          <td>{{ li.price * li.quantity }}</td>

          <td>{{ li.payment }}</td>
          <td>{{ li.address }}</td>
          <td>{{ li.phone }}</td>
          <td>{{ li.requests }}</td>

          <!--total button -->
          <td v-if="pluslOrder(index)">
            <span class="badge bg-danger">취소 완료</span>
          </td>
          <td v-else>
            <i class="fas fa-long-arrow-alt-up"></i>
          </td>
        </tr>
      </table>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
  data() {
    return {
      list: 0,
      status: "S",
      total: 0,
      //취소 주문
      cancelStoreOrder: [],
      payCash: 0,
    };
  },

  methods: {
    sameOrderId(index) {
      //첫번째
      if (index == 0) {
        this.payCash =
          this.list[index].payment +
          this.list[index].discount -
          this.list[index].price * this.list[index].quantity;

        if (this.payCash >= 0) {
          this.list[index].payment =
            this.list[index].price * this.list[index].quantity;
        } else {
          this.list[index].payment = "결제 오류";
        }
        return true;
      } //같은 주문일때
      else if (this.list[index].orderId == this.list[index - 1].orderId) {
        this.payCash =
          this.list[index].payment +
          this.list[index].discount -
          this.list[index].price * this.list[index].quantity;

        if (this.payCash >= 0) {
          this.list[index].payment =
            this.list[index].price * this.list[index].quantity;
        } else {
          this.list[index].payment = "결제 오류";
        }
        return false;
      } //주문이 다를때
      else {
        this.payCash =
          this.list[index].payment -
          this.list[index].price * this.list[index].quantity;

        if (this.payCash >= 0) {
          this.list[index].payment =
            this.list[index].price * this.list[index].quantity;
        } else {
          this.list[index].payment = "결제 오류";
        }
        return true;
      }
    },
    pluslOrder(index) {
      if (index == 0) {
        return true;
      } else if (this.list[index].orderId == this.list[index - 1].orderId) {
        return false;
      } else {
        return true;
      }
    },
    //고객 취소용
    cancelStoreOrderId(index) {
      //첫번째
      if (index == 0) {
        this.payCash =
          this.cancelStoreOrder[index].payment +
          this.cancelStoreOrder[index].discount -
          this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;
        if (this.payCash >= 0) {
          this.cancelStoreOrder[index].payment =
            this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;
        } else {
          this.cancelStoreOrder[index].payment = "결제 오류";
        }
        return true;
      } //같은 주문일때
      else if (
        this.cancelStoreOrder[index].orderId ==
        this.cancelStoreOrder[index - 1].orderId
      ) {
        this.payCash -
          this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;

        if (this.payCash >= 0) {
          this.cancelStoreOrder[index].payment =
            this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;
        } else {
          this.cancelStoreOrder[index].payment = "결제 오류";
        }
        return false;
      } //주문이 다를때
      else {
        this.payCash =
          this.cancelStoreOrder[index].payment +
          this.cancelStoreOrder[index].discount -
          this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;

        if (this.payCash >= 0) {
          this.cancelStoreOrder[index].payment =
            this.cancelStoreOrder[index].price *
            this.cancelStoreOrder[index].quantity;
        } else {
          this.cancelStoreOrder[index].payment = "결제 오류";
        }
        return true;
      }
    },
    //주문 1개 삭제
    deleteOrder(orderInfoId, name, quantity, orderId) {
      if (
        confirm(
          "주문번호 " +
            orderId +
            "번의 \n" +
            name +
            "  " +
            quantity +
            "개 주문을 취소합니다"
        )
      ) {
        http.post("/order/deleteOrder", orderInfoId).then((res) => {
       
          if (res.data == "ok") {
            alert("취소되었습니다.");
            this.setStatusOption(this.status);
          } else if (res.data == "no") {
            alert("문제 발생했습니다.");
          }
        });
      }
    },
    //주문 1건 삭제 cancelStoreOrder
    deleteAllOrder(word, orderId) {
      if (confirm("주문번호" + orderId + "번의 주문을 취소합니다")) {
        let allOrderInfoId = []
        if(word == 'C'){
          for(let i = 0; i < this.cancelStoreOrder.length; i++){
          
            if(this.cancelStoreOrder[i].orderId == orderId){
              allOrderInfoId.push(this.cancelStoreOrder[i].orderInfoId)
            }
         }
        }else{
          for(let i = 0; i < this.list.length; i++){
          
            if(this.list[i].orderId == orderId){
              allOrderInfoId.push(this.list[i].orderInfoId)
            }
          }
        }
        
        
        http.post("/order/deleteAllOrder", allOrderInfoId).then((res) => {
 
          if (res.data == "ok") {
            alert("취소되었습니다.");
            this.setStatusOption(this.status);
          } else if (res.data == "no") {
            alert("문제 발생했습니다.");
          }
        });
      }
    },
    //전체 주문 승인
    approveOrder(orderId) {
      let allOrderInfoId = []

      for(let i = 0; i < this.list.length; i++){
          if(this.list[i].orderId == orderId){
            allOrderInfoId.push(this.list[i].orderInfoId)
          }
        }

      if (confirm("주문번호" + orderId + "번의 주문을 승인합니다")) {
        http.post("/order/approveOrder", allOrderInfoId).then((res) => {
       
          if (res.data == "ok") {
            alert("주문이 승인되었습니다.");
            this.setStatusOption(this.status);
          } else if (res.data == "no") {
            alert("문제 발생했습니다.");
          }
        });
      }
    },
    //데이터 통신
    async setStatusOption(satus) {
      this.status = satus;
      http.post("/order/storeOrder", { satus }).then((res) => {
        

        if (satus == "Y") {
          this.list = res.data.storeOrder.reverse();
        } else {
          this.list = res.data.storeOrder;
        }

        this.total = res.data.total;
        this.cancelStoreOrder = res.data.cancelStoreOrder;
      });
    },
  },
  mounted() {
    this.setStatusOption("S");
  },
};
</script>

<style scoped>
th {
  border-left: 1px solid gray;
  background-color: lightyellow;
}
tr {
  border-bottom: 1px solid gray;
}
td {
  border-left: 1px solid gray;
  padding: 3px 10px;
}
.badge {
  cursor: pointer;
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
</style>
