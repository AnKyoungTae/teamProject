<template>
  <div style="margin-top:50px;">
    <h1 class="nearShopTitle" style="margin-right: 50px;">
      <p>매출관리</p>
    </h1>
  </div>
  <div style="margin: 3vh">
    <table style="margin-left: 1px">
      <tr>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
      </tr>
      <tr class="storeChart" style="border-top: 2px solid gray">
        <!-- 선택사항 -->
        <td colspan="4">
          <div class="dropdown">
            <div
              style="width: 80%"
              class="btn btn-outline-secondary dropdown-toggle"
              href="#"
              role="button"
              id="dropdownMenuLink"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <strong>{{ dropDown }}</strong>
            </div>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuLink">
              <li
                class="dropdown-item"
                @click="changeDropdownButton('음식판매량', 'week')"
              >
                음식판매량
              </li>
              <li
                class="dropdown-item"
                @click="changeDropdownButton('요일별판매량', 'week')"
              >
                요일별판매량
              </li>
              <li
                class="dropdown-item"
                @click="changeDropdownButton('최근판매량', 'week')"
              >
                최근판매량
              </li>
            </ul>
          </div>
        </td>

        <th colspan="2">전월매출</th>
        <td colspan="2">{{ graphPayment.beforeMonthPayment }}</td>
        <th colspan="2">당월매출</th>
        <td colspan="2">{{ graphPayment.payment }}</td>
      </tr>
      <tr class="storeChart">
        <th colspan="2">전월할인</th>
        <td colspan="2">{{ graphPayment.beforeDiscount }}</td>
        <th colspan="2">전일매출</th>
        <td colspan="2">
          {{ graphPayment.beforeDayPayment }}
        </td>
        <th colspan="2">당일매출</th>
        <td colspan="2">{{ graphPayment.dayPayment }}</td>
      </tr>
    </table>
    <!-- 종류별로 바낌-->
    <!-- 음식판매량-->
    <table v-if="dropDown == '음식판매량'">
      <tr class="storeChart">
        <td colspan="3">
          하루
          <input
            type="radio"
            name="date"
            value="day"
            @click="clickRadio('day')"
          />
        </td>
        <td colspan="3">
          일주일
          <input
            type="radio"
            name="date"
            value="week"
            @click="clickRadio('week')"
            checked
          />
        </td>
        <td colspan="3">
          한달
          <input
            type="radio"
            name="date"
            value="month"
            @click="clickRadio('month')"
          />
        </td>
        <td colspan="3">
          일년
          <input
            type="radio"
            name="date"
            value="year"
            @click="clickRadio('month')"
          />
        </td>
      </tr>

      <tr class="storeChart">
        <th colspan="2">전체 수량</th>
        <td colspan="2">{{ plusResentFoodQuantity }}</td>
        <th colspan="2">전체 매출</th>
        <td colspan="2">{{ plusResentFoodPayment }}</td>
        <th colspan="2">할인금액</th>
        <td colspan="2">{{ discount }}</td>
      </tr>

      <tr class="storeChart">
        <th colspan="12">음식 판매량 순위</th>
      </tr>
      <tr class="storeChart">
        <th colspan="1">번호</th>
        <th colspan="4">이름</th>
        <th colspan="3">수량</th>
        <th colspan="4">매출</th>
      </tr>
      <tr
        class="storeChart"
        v-for="graphFood in graphFoods"
        :key="graphFood.rowNum"
      >
        <td colspan="1">{{ graphFood.rowNum }}</td>
        <td colspan="4">{{ graphFood.name }}</td>
        <td colspan="3">{{ graphFood.quantity }}</td>
        <td colspan="4">{{ graphFood.price }}</td>
      </tr>
      <tr>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
      </tr>
    </table>
    <!-- 요일별판매량-->
    <table v-if="dropDown == '요일별판매량'">
      <tr class="storeChart">
        <td colspan="4">
          일주일
          <input
            type="radio"
            name="date"
            value="week"
            @click="clickRadio('week')"
            checked
          />
        </td>
        <td colspan="4">
          한달
          <input
            type="radio"
            name="date"
            value="month"
            @click="clickRadio('month')"
          />
        </td>
        <td colspan="4">
          일년
          <input
            type="radio"
            name="date"
            value="year"
            @click="clickRadio('year')"
          />
        </td>
      </tr>

      <tr class="storeChart">
        <th colspan="2">전체 주문양</th>
        <td colspan="2">{{ plusResentFoodQuantity }}</td>
        <th colspan="2">전체 매출</th>
        <td colspan="2">{{ plusResentFoodPayment }}</td>
        <th colspan="2">전체 할인량</th>
        <td colspan="2">{{ graphPayment.discount }}</td>
      </tr>

      <tr class="storeChart">
        <th colspan="12">요일별 판매량</th>
      </tr>
      <tr class="storeChart">
        <th colspan="2">요일</th>
        <th colspan="5">주문수</th>
        <th colspan="5">매출합계</th>
      </tr>

      <tr
        v-for="(graphDay, index) in graphDays"
        :key="index"
        class="storeChart"
      >
        <td colspan="2">{{ graphDay.week }}</td>
        <td colspan="5">{{ graphDay.totalOrder }}</td>
        <td colspan="5">
          {{ graphDay.payment }}
        </td>
      </tr>
      <tr>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
      </tr>
    </table>

    <!-- 최근판매량-->
    <table v-else-if="dropDown == '최근판매량'">
      <tr class="storeChart">
        <th colspan="2">음식</th>
        <td colspan="5">
          <div class="dropdown">
            <button
              style="width: 80%"
              class="btn btn-outline-secondary dropdown-toggle"
              type="button"
              id="dropdownMenuButton1"
              data-bs-toggle="dropdown"
              aria-expanded="false"
            >
              <strong>{{ ResentDropdownButton }}</strong>
            </button>
            <ul class="dropdown-menu" aria-labelledby="dropdownMenuButton1">
              <li
                v-for="(resentFood, index) in resentFoodList"
                :key="index"
                @click="changeResentDropdownButton(resentFood)"
              >
                {{ resentFood }}
              </li>
            </ul>
          </div>
        </td>
        <td colspan="5">
          <span
            ><strong>{{ ResentDropdownButton }}</strong> 시간별 판매량</span
          >
        </td>
      </tr>

      <!-- 시간 버튼-->
      <tr class="storeChart">
        <td colspan="4">
          하루
          <input
            type="radio"
            name="date"
            value="month"
            @click="clickRadio('day')"
          />
        </td>
        <td colspan="4">
          일주일
          <input
            type="radio"
            name="date"
            value="week"
            @click="clickRadio('week')"
            checked
          />
        </td>
        <td colspan="4">
          한달
          <input
            type="radio"
            name="date"
            value="year"
            @click="clickRadio('month')"
          />
        </td>
      </tr>

      <tr class="storeChart">
        <th colspan="2">전체 수량</th>
        <td colspan="2">{{ plusResentFoodQuantity }}</td>
        <th colspan="2">전체 매출</th>
        <td colspan="2">{{ plusResentFoodPayment }}</td>
        <th colspan="2">전체 할인량</th>
        <td colspan="2">{{ graphPayment.discount }}</td>
      </tr>

      <tr class="storeChart">
        <th colspan="4">시간</th>
        <th colspan="4">주문수</th>
        <th colspan="4">매출합계</th>
      </tr>

      <tr
        class="storeChart"
        v-for="graphResentFood in graphResentFoods"
        :key="graphResentFood.time"
      >
        <td colspan="4">{{ graphResentFood.time }}</td>
        <td colspan="4">{{ graphResentFood.quantity }}</td>
        <td colspan="4">{{ graphResentFood.payment }}</td>
      </tr>
      <tr>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
        <td style="width: 100px"></td>
      </tr>
    </table>
    
  
  </div>
</template>

<script>
import http from "@/api/http";


export default {

  data() {
    return {
      graphFoods: [],
      graphDays: [],
      graphResentFoods: [],
    
      //전달금액
      graphPayment: [],
      dropDown: "음식판매량",
      resentFoodList: [],
      //최근 음식별 판매 시간 드랍 다운시 버튼
      ResentDropdownButton: "",
      resentFoodTime: "",

      //날짜
      date:''
    };
  },
  computed: {
    discount() {
      let count = 0;

      if (this.dropDown == "음식판매량") {
        for (let i = 0; i < this.graphFoods.length; i++) {
          count += this.graphFoods[i].discount;
        }
      } else if (this.dropDown == "요일별판매량") {
        for (let i = 0; i < this.graphDays.length; i++) {
          count += this.graphDays[i].discount;
        }
      } else if (this.dropDown == "최근판매량") {
        for (let i = 0; i < this.graphResentFoods.length; i++) {
          count += this.graphResentFoods[i].discount;
        }
      }

      return count;
    },
    //전체 시간을 합한 수량
    plusResentFoodQuantity() {
      let quantity = 0;

      if (this.dropDown == "음식판매량") {
        for (let i = 0; i < this.graphFoods.length; i++) {
          quantity += this.graphFoods[i].quantity;
        }
      } else if (this.dropDown == "요일별판매량") {
        for (let i = 0; i < this.graphDays.length; i++) {
          quantity += this.graphDays[i].totalOrder;
        }
      } else if (this.dropDown == "최근판매량") {
        for (let i = 0; i < this.graphResentFoods.length; i++) {
          quantity += this.graphResentFoods[i].quantity;
        }
      }

      return quantity;
    },
    plusResentFoodPayment() {
      let payment = 0;

      if (this.dropDown == "음식판매량") {
        for (let i = 0; i < this.graphFoods.length; i++) {
          payment += this.graphFoods[i].price;
        }
      } else if (this.dropDown == "요일별판매량") {
        for (let i = 0; i < this.graphDays.length; i++) {
          payment += this.graphDays[i].payment;
        }
      } else if (this.dropDown == "최근판매량") {
        for (let i = 0; i < this.graphResentFoods.length; i++) {
          payment += this.graphResentFoods[i].payment;
        }
      }

      return payment;
    },
    plusResentFooddiscount() {
      let discount = 0;
    if(this.dropDown == "음식판매량"){
      for(let i = 0; i < this.graphFoods.length; i++){
      discount +=  this.graphPayment[i].discount
    }
    }else if(this.dropDown == "요일별판매량"){
      for(let i = 0; i < this.graphDays.length; i++){
      discount +=  this.graphPayment[i].discount
    }
    }else if(this.dropDown == "최근판매량"){
      for(let i = 0; i < this.graphResentFoods.length; i++){
      discount +=  this.graphPayment[i].discount
    }
    }
      return discount;
    },
  },
  methods: {
    clickRadio(date) {
      this.date = date
      if (this.dropDown == "음식판매량") {
        this.getFoodSaleAmount(date);
      } else if (this.dropDown == "요일별판매량") {
        this.getDayAmount(date);
      } else if (this.dropDown == "최근판매량") {
        this.getResentFood(date, this.ResentDropdownButton);
      }
    },

    //드랍 다운시 버튼
    changeDropdownButton(word, date) {
      if (word == "음식판매량") {
        this.getFoodSaleAmount(date);
      } else if (word == "요일별판매량") {
        this.getDayAmount(date);
      } else if (word == "최근판매량") {
        this.getResentFood(date, "");
      }
      this.dropDown = word;
    },
    //최근 음식별 판매 시간 드랍 다운시 버튼
    changeResentDropdownButton(word) {
      this.getResentFood(this.resentFoodTime, word);
    },

    //음식판매량 통신
    getFoodSaleAmount(date) {
      http
        .post("/order/getFoodSaleAmount", date)
        .then((res) => {
          this.graphFoods = res.data;
    
        })
        .catch((err) => {
          console.log(err);
        });
    }, //요일별 매출 통신
    getDayAmount(date) {
      http
        .post("/order/getDayAmount", date)
        .then((res) => {
          this.graphDays = res.data;
      
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //최근 음식별 판매 시간
    getResentFood(date, foodName) {
      http
        .post("/order/getResentFood", { date: date, name: foodName })
        .then((res) => {
          this.resentFoodList = res.data.list;
          this.ResentDropdownButton = res.data.name;
          this.graphResentFoods = res.data.graphResntFood;
          this.resentFoodTime = date;


        })
        .catch((err) => {
          console.log(err);
        });
    },
    //전월매출 등등
    getPayment() {
      http.post("/order/getPayment").then((res) => {
        this.graphPayment = res.data;
      });
    },
  },

  mounted() {
    this.changeDropdownButton("음식판매량", "week");
    this.getPayment();
  },
  updated(){
    
  }
};
</script>

<style>
.storeChart th,
.storeChart td {
  border-style: solid;
  border-width: 0px 2px 2px;
  height: 50px;
}
.storeChart th {
  background-color: gainsboro;
}
.storeChart tr {
  width: 1200px;
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
