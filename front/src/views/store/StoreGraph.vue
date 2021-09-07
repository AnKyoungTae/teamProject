<template>
<button @click="getDayAmount">dddddddddd</button>
  <div style="margin: 3vh;">
    <table style="border: 1px solid red;">
      <tr style="border: 1px solid red;">
        <!-- 선택사항 -->
        <th colspan="2" style="border-right: 1px solid red;">
          <div class="dropdown">
            <div  class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
              {{dropDown}}
            </div>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
              <li class="dropdown-item" @click="changeDropdownButton('음식판매량')">음식판매량</li>
              <li class="dropdown-item" @click="changeDropdownButton('요일별판매량')">요일별판매량</li>
              <li class="dropdown-item" @click="changeDropdownButton('최근판매량')">최근판매량</li>
            </ul>
          </div>
        </th>
        
        <th>전월매출</th>
        <th style="border-right: 1px solid red;">전월매출 금액</th>
        <th>당월매출</th>
        <td>당월매출 금액</td>
      </tr>
      <tr style="border: 1px solid red;">
        <th>평균할인금액</th>
        <td style="border-right: 1px solid red;">평균할인 금액</td>
        <th>전일매출</th>
        <th style="border-right: 1px solid red;">전일매출 금액</th>
        <th>당일매출</th>
        <th>당일매출 금액</th>
      </tr>
      <!-- 종류별로 바낌-->
      <tr style="border: 1px solid red;">
        <th colspan="6">음식 판매량 순위</th>
      </tr>
      <tr style="border: 1px solid red;">
        <th style="border-right: 1px solid red;">번호</th>
        <th colspan="2" style="border-right: 1px solid red;">이름</th>
        <th style="border-right: 1px solid red;">수량</th>
        <th colspan="2">매출</th>
      </tr>
      <tr v-for="graphFood in graphFoods" :key="graphFood.rowNum" style="border: 1px solid red;">
        <td style="border-right: 1px solid red;">{{graphFood.rowNum}}</td>
        <td colspan="2" style="border-right: 1px solid red;">{{graphFood.name}}</td>
        <td style="border-right: 1px solid red;">{{graphFood.quantity}}</td>
        <td colspan="2">{{graphFood.price}}</td>
      </tr>
    </table>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
data () {
  return{
    graphFoods: [],
    dropDown: "음식판매량"
  }
},
methods: {
  changeDropdownButton(word){
    this.dropDown =word
    if(word == "음식판매량"){
      this.getFoodSaleAmount()
    }else if(word == "요일별판매량"){
      alert(word)
    }else if(word == "최근판매량"){
      alert(word)
    }
  },

  //음식판매량 통신
  getFoodSaleAmount() {
    http.post("/order/getFoodSaleAmount")
    .then(res => {
      this.graphFoods = res.data
    })
  },//요일별 매출 통신
  getDayAmount(){
    http.post("/order/getDayAmount")
    .then(res => {
      this.graphFoods = res.data
    })
  },
},

mounted() {
  this.changeDropdownButton("음식판매량")
}
}
</script>

<style>

</style>