<template>
    <div style="width: 1200px;">
        <div style="font-size: 40px;">주문관리</div>

        <!-- 고객이 주문을 취소할때-->
        <div v-if="cancelStoreOrder.length > 0">
          <span style="font-size: 20px; color: red;"> 고객 주문 취소 요청</span>
          <table style="border-top: 1.5px solid black; border-bottom: 1.5px solid black; width: 100%; margin-bottom: 3vh;">
              <tr style="border-bottom: 1.5px solid black;">
                  <th>주문번호</th>
                  <th>주문메뉴</th>
                  <th>주문일</th>
                  <th>음식가격</th>
                  <th>할인가격</th>
                  <th>총 결제금액</th>
                  <th>주소</th>
                  <th>핸드폰번호</th>
                  <th>요청사항</th>
                  <th>주문관리</th>
              </tr>
              <tr v-for="cancel,index in cancelStoreOrder" :key="index" >
                  <td v-if="sameOrderId(index)">{{cancel.orderId}}</td>
                  <td v-else>
                    <i class="fas fa-long-arrow-alt-up"></i>
                  </td>
                  <td>{{cancel.name}} {{cancel.quantity}}</td>
                  <td>{{cancel.orderDate}}</td>
                  <td>{{cancel.price}}</td>
                  <td>{{cancel.discount}}</td>
                  <td>{{cancel.payment}}</td>
                  <td>{{cancel.address}}</td>
                  <td>{{cancel.phone}}</td>
                  <td>{{cancel.requests}}</td>
                  <!--버튼클릭시 status 변경-->
                  <td>
                      <span class="badge bg-danger" @click="deleteOrder(cancel.orderInfoId, cancel.name, cancel.quantity, cancel.orderId)">주문취소</span>
                  </td>
                  <!--total button -->
              </tr>
          </table>
        </div>
        <!-- radio 버튼 -->
        <div style="text-align: left;">
            <span style="font-size: 20px; margin-left: 20px;">
              <span v-if="status == 'S'">미승인</span>
              <span v-else-if="status == 'Y'">승인</span>
              <span v-else>취소</span>
              
               주문건수: {{total}}</span>
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
          <span style="font-size: 20px;"> 주문 요청</span>
          <table style="border-top: 1.5px solid black; border-bottom: 1.5px solid black; width: 100%;">
              <tr style="border-bottom: 1.5px solid black;">
                  <th>주문번호</th>
                  <th>주문메뉴</th>
                  <th>주문일</th>
                  <th>음식가격</th>
                  <th>할인가격</th>
                  <th>총 결제금액</th>
                  <th>주소</th>
                  <th>핸드폰번호</th>
                  <th>요청사항</th>
                  <th>주문관리</th>
                  <th>총관리</th>
              </tr>
              <tr v-for="li,index in list" :key="index" >
                  <td v-if="sameOrderId(index)">{{li.orderId}}</td>
                  <td v-else>
                    <i class="fas fa-long-arrow-alt-up"></i>
                  </td>
                  <td>{{li.name}} {{li.quantity}}</td>
                  <td>{{li.orderDate}}</td>
                  <td>{{li.price}}</td>
                  <td>{{li.discount}}</td>
                  <td>{{li.payment}}</td>
                  <td>{{li.address}}</td>
                  <td>{{li.phone}}</td>
                  <td>{{li.requests}}</td>
                  <!--버튼클릭시 status 변경-->
                  <td>
                      <span class="badge bg-danger" style="margin: 1vh 0;" @click="deleteOrder(li.orderInfoId, li.name, li.quantity, li.orderId)">주문취소</span>
                  </td>
                  <!--total button -->
                  <td v-if="pluslOrder(index)">
                    <span class="badge bg-success" @click="approveOrder(li.orderId)">전체주문받기</span>
                    <span class="badge bg-danger" @click="deleteAllOrder(li.orderId)">전체주문취소</span>
                  </td>
                  <td v-else>
                    <i class="fas fa-long-arrow-alt-up"></i>
                  </td>
              </tr>
          </table>
        </div>

        <!-- 주문 승인 완료-->
        <div v-else-if="status == 'Y'">
          <span style="font-size: 20px;"> 주문 승인</span>
          <table style="border-top: 1.5px solid black; border-bottom: 1.5px solid black; width: 100%;">
              <tr style="border-bottom: 1.5px solid black;">
                  <th>주문번호</th>
                  <th>주문메뉴</th>
                  <th>주문일</th>
                  <th>음식가격</th>
                  <th>할인가격</th>
                  <th>총 결제금액</th>
                  <th>주소</th>
                  <th>핸드폰번호</th>
                  <th>요청사항</th>
                  <th>주문관리</th>
              </tr>
              <tr v-for="li,index in list" :key="index" >
                  <td v-if="sameOrderId(index)">{{li.orderId}}</td>
                  <td v-else>
                    <i class="fas fa-long-arrow-alt-up"></i>
                  </td>
                  <td>{{li.name}} {{li.quantity}}</td>
                  <td>{{li.orderDate}}</td>
                  <td>{{li.price}}</td>
                  <td>{{li.discount}}</td>
                  <td>{{li.payment}}</td>
                  <td>{{li.address}}</td>
                  <td>{{li.phone}}</td>
                  <td>{{li.requests}}</td>
                  
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
          <span style="font-size: 20px;"> 주문 취소</span>
          <table style="border-top: 1.5px solid black; border-bottom: 1.5px solid black; width: 100%;">
              <tr style="border-bottom: 1.5px solid black;">
                  <th>주문번호</th>
                  <th>주문메뉴</th>
                  <th>주문일</th>
                  <th>음식가격</th>
                  <th>할인가격</th>
                  <th>총 결제금액</th>
                  <th>주소</th>
                  <th>핸드폰번호</th>
                  <th>요청사항</th>
                  <th>주문관리</th>
              </tr>
              <tr v-for="li,index in list" :key="index" >
                  <td v-if="sameOrderId(index)">{{li.orderId}}</td>
                  <td v-else>
                    <i class="fas fa-long-arrow-alt-up"></i>
                  </td>
                  <td>{{li.name}} {{li.quantity}}</td>
                  <td>{{li.orderDate}}</td>
                  <td>{{li.price}}</td>
                  <td>{{li.discount}}</td>
                  <td>{{li.payment}}</td>
                  <td>{{li.address}}</td>
                  <td>{{li.phone}}</td>
                  <td>{{li.requests}}</td>
                  
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
      status: 'S',
      total: 0,
      //취소 주문
      cancelStoreOrder: []

    }
  },
  
  
  methods: {
    sameOrderId(index) {
      if(index == 0){
        return true
      }
      if(this.list[index].orderId == this.list[index - 1].orderId){
        return false
      }else{
        return true
        
      }
    },
    pluslOrder(index){
      if(index == 0){
        return true
      }else if(this.list[index].orderId == this.list[index - 1].orderId){
        return false
      }else{
        
        return true
      }
      
    },
    //주문 1개 삭제
    deleteOrder(orderInfoId, name, quantity, orderId) {
      if(confirm("주문번호 " + orderId+ "번의 \n"+ name +"  "+ quantity + "개 주문을 취소합니다")){
          http.post("/order/deleteOrder", orderInfoId)
          .then(res => {
            console.log(res.data)
            if(res.data == "ok"){
              alert("취소되었습니다.")
              this.setStatusOption(this.status)
            }else if(res.data == "no"){
              alert("문제 발생했습니다.")
            }
          })
      }
    },
    //주문 1건 삭제
    deleteAllOrder(orderId){
      if(confirm("주문번호" + orderId+ "번의 주문을 취소합니다")){
          http.post("/order/deleteAllOrder", orderId)
          .then(res => {
            console.log(res.data)
            if(res.data == "ok"){
              alert("취소되었습니다.")
              this.setStatusOption(this.status)
            }else if(res.data == "no"){
              alert("문제 발생했습니다.")
            }
            
          })
      }
    },
    //전체 주문 승인
    approveOrder(orderId){
      if(confirm("주문번호" + orderId+ "번의 주문을 승인합니다")){
          http.post("/order/approveOrder", orderId)
          .then(res => {
            console.log(res.data)
            if(res.data == "ok"){
              alert("주문이 승인되었습니다.")
              this.setStatusOption(this.status)
            }else if(res.data == "no"){
              alert("문제 발생했습니다.")
            }
            
          })
      }
    },
    //데이터 통신 
    async setStatusOption(satus) {
      this.status = satus
      http.post("/order/storeOrder", {satus})
      .then(res => {
        
        console.log(this.status)
        this.list = res.data.storeOrder
        this.total = res.data.total
        this.cancelStoreOrder = res.data.cancelStoreOrder
      })
     
    },
  },
  mounted() {
    this.setStatusOption("S")
  }

}
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
</style>