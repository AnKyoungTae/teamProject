<template>
  <div style="margin: 3vh;">
    <table style="border: 1px solid red; width: 50vw;">
      <tr style="border: 1px solid red;">
        <!-- 선택사항 -->
        <th colspan="2" style="border-right: 1px solid red;">
          <div class="dropdown">
            <div  class="btn btn-secondary dropdown-toggle" href="#" role="button" id="dropdownMenuLink" data-bs-toggle="dropdown" aria-expanded="false">
              {{dropDown}}
            </div>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
              <li class="dropdown-item" @click="changeDropdownButton('음식판매량', 'week')">음식판매량</li>
              <li class="dropdown-item" @click="changeDropdownButton('요일별판매량', 'week')">요일별판매량</li>
              <li class="dropdown-item" @click="changeDropdownButton('최근판매량', 'week')">최근판매량</li>
            </ul>
          </div>
        </th>
        
        <th>전월매출</th>
        <th style="border-right: 1px solid red;">{{graphPayment.beforeMonthPayment}}</th>
        <th>당월매출</th>
        <th>{{graphPayment.payment}}</th>
      </tr>
      <tr style="border: 1px solid red;">
        <th>평균할인금액</th>
        <td style="border-right: 1px solid red;">{{graphPayment.discount}}</td>
        <th>전일매출</th>
        <th style="border-right: 1px solid red;">{{graphPayment.beforeDayPayment}}</th>
        <th>당일매출</th>
        <th>{{graphPayment.dayPayment}}</th>
      </tr>
    </table>
    <!-- 종류별로 바낌-->
    <!-- 음식판매량-->
    <table style="width: 50vw;" v-if="dropDown == '음식판매량'">
      <tr style="border: 1px solid red;">
        <td>
          하루<input type="radio" name="date"  value="day" @click="clickRadio('day')" />
        </td>
        <td>
          일주일<input type="radio" name="date" value="week" @click="clickRadio('week')" checked/>
        </td>
        <td>
          한달<input type="radio" name="date" value="month" @click="clickRadio('month')"/>
        </td>
        <td>
          일년<input type="radio" name="date" value="year" @click="clickRadio('month')"/>
        </td>
      </tr>
      <tr style="border: 1px solid red;" >
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

    <!-- 요일별판매량-->
    <table style="width: 50vw;" v-else-if="dropDown == '요일별판매량'">
      <tr style="border: 1px solid red;">
        <td>
          일주일<input type="radio" name="date" value="week" @click="clickRadio('week')" checked/>
        </td>
        <td>
          한달<input type="radio" name="date" value="month" @click="clickRadio('month')"/>
        </td>
        <td>
          일년<input type="radio" name="date" value="year" @click="clickRadio('year')"/>
        </td>
      </tr>
      <tr style="border: 1px solid red; "  >
        <th colspan="3" style="border: 1px solid red;">요일별 판매량</th>
      </tr>
      <tr style="border: 1px solid red;">
        <th style="border-right: 1px solid red;">요일</th>
        <th style="border-right: 1px solid red;">주문수</th>
        <th>매출합계</th>
      </tr>
      
      <tr v-for=" graphDay,index in graphDays" :key="index" style="border: 1px solid red;">
        <td style="border-right: 1px solid red;">{{graphDay.week}}</td>
        <td style="border-right: 1px solid red;">{{graphDay.totalOrder}}</td>
        <td style="border-right: 1px solid red;">{{graphDay.payment}}</td>
      </tr>
    </table>

    <!-- 최근판매량-->
    <table style="width: 50vw;" v-else-if="dropDown == '최근판매량'">
      <tr style="border: 1px solid red;">
        <td colspan="2">
          <div class="dropdown">
            <button class="btn btn-secondary dropdown-toggle" type="button" id="dropdownMenuButton1" data-bs-toggle="dropdown" aria-expanded="false">
              {{ResentDropdownButton}}
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
              <li v-for="resentFood,index in resentFoodList" :key="index" @click="changeResentDropdownButton(resentFood)">
                {{resentFood}}
              </li>
              
            </ul>
          </div>
        </td>
        <td colspan="2">
          <span>{{ResentDropdownButton}} 시간별 판매량</span>
        </td>
        
      </tr>
      <!-- 시간 버튼-->
      <tr style="border: 1px solid red; ">
        <td>
          하루<input type="radio" name="date" value="month" @click="clickRadio('day')"/>
        </td>
        <td colspan="2">
          일주일<input type="radio" name="date" value="week" @click="clickRadio('week')" checked/>
        </td>
        <td>
          한달<input type="radio" name="date" value="year" @click="clickRadio('month')"/>
        </td>
      </tr>

      <tr style="border: 1px solid red; ">
        <th style="border: 1px solid red;">전체 수량</th>
        <td>{{plusResentFoodQuantity}}</td>
        <th  style="border: 1px solid red;">전체 매출</th>
        <td>{{plusResentFoodPayment}}</td>
      </tr>
      <tr style="border: 1px solid red;">
        <th style="border-right: 1px solid red;">시간</th>
        <th style="border-right: 1px solid red;">이름</th>
        <th style="border-right: 1px solid red;">주문수</th>
        <th>매출합계</th>
      </tr>
      
      <tr  v-for="graphResentFood in graphResentFoods" :key="graphResentFood.time" style="border: 1px solid red; ">
        <td style="border-right: 1px solid red;">{{graphResentFood.time}}</td>
        <td style="border-right: 1px solid red;">{{graphResentFood.name}}</td>
        <td style="border-right: 1px solid red;">{{graphResentFood.quantity}}</td>
        <td>{{graphResentFood.payment}}</td>
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
    graphDays: [],
    graphResentFoods:[],
    //전달금액
    graphPayment: [],
    dropDown: "음식판매량",
    resentFoodList:[],
    //최근 음식별 판매 시간 드랍 다운시 버튼
    ResentDropdownButton: '',
    resentFoodTime: ''
   
  }
},
computed: {
  //전체 시간을 합한 수량
  plusResentFoodQuantity(){
    let quantity = 0
    for(let i = 0; i < this.graphResentFoods.length; i++){
      quantity +=  this.graphResentFoods[i].quantity
    }
    return quantity;
  },
  plusResentFoodPayment(){
    let payment = 0
    for(let i = 0; i < this.graphResentFoods.length; i++){
      payment +=  this.graphResentFoods[i].payment
    }
    return payment;
  }
},
methods: {
  clickRadio(date) {
    if(this.dropDown == "음식판매량"){
      this.getFoodSaleAmount(date)
    }else if(this.dropDown == "요일별판매량"){
      this.getDayAmount(date)
    }else if(this.dropDown == "최근판매량"){
      this.getResentFood(date, this.ResentDropdownButton)
    }
  },

  //드랍 다운시 버튼
  changeDropdownButton(word, date){
    if(word == "음식판매량"){
      this.getFoodSaleAmount(date)
    }else if(word == "요일별판매량"){
      this.getDayAmount(date)
    }else if(word == "최근판매량"){
      this.getResentFood(date, "")
    }
    this.dropDown =word
  },
  //최근 음식별 판매 시간 드랍 다운시 버튼
  changeResentDropdownButton(word){
    this.getResentFood(this.resentFoodTime, word)
  },

  //음식판매량 통신
  getFoodSaleAmount(date) {
    http.post("/order/getFoodSaleAmount", date)
    .then(res => {
      this.graphFoods = res.data
    })
    .catch(err=> {
      console.log(err)
    })
  },//요일별 매출 통신
  getDayAmount(date){
    http.post("/order/getDayAmount", date)
    .then(res => {
      console.log(res.data)
      this.graphDays = res.data
    })
    .catch(err=> {
      console.log(err)
    })
  },
  //최근 음식별 판매 시간
  getResentFood(date, foodName) {
    http.post("/order/getResentFood", {date: date, name: foodName})
    .then(res => {
      console.log(res)
      this.resentFoodList = res.data.list
      this.ResentDropdownButton = res.data.name
      this.graphResentFoods = res.data.graphResntFood
      this.resentFoodTime = date
      
    })
    .catch(err=> {
      console.log(err)
    })
  },
  //전월매출 등등
  getPayment() {
    http.post("/order/getPayment")
    .then(res => {
      console.log(res.data)
      this.graphPayment = res.data
    })
  }
},

mounted() {
  this.changeDropdownButton("음식판매량", "week")
  this.getPayment()
}
}
</script>

<style>

</style>