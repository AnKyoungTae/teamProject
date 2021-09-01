<template>
  <div>
    <div class="wrapper m-4 container-fluid p-2">
      <!-- 노티피케이션 -->
      <notifications
        group="notifyApp"
        position="bottom right"
        style="margin-right: 30vh"
      />
      <div class="cart">
        <h1 class="m-2">장바구니</h1>
        <hr />
        <div class="cartOrder">
          <div class="orderText">
            <p>배달정보</p>
          </div>
          <div class="orderAddress row p-4">
            <table>
              <tr>
                <td style="width: 25%; text-align: center">
                  <span>주소</span>
                </td>
                <td style="padding-left: 10px">
                  <!-- 주소가 맞지 않을 수 있으니 수정할수있도록 할것 -->
                  <span>
                    <div>
                      {{ finalAddress }}
                    </div>
                    <!-- 모달 취소를 했을 때, placeSelector를 false로 만들어야함 -->
                    <span
                      class="badge bg-info text-dark m-1"
                      style="cursor: pointer"
                      @click="openMap"
                      @close="closeMap"
                      >주소찾기</span
                    ></span
                  >
                  <input
                    type="text"
                    placeholder="(필수) 상세주소 입력"
                    style="width: 100%"
                    v-model="addressDetail"
                    @input="type_addressDetail"
                  />
                </td>
              </tr>
              <tr>
                <td style="width: 25%; text-align: center">
                  <span class="mt-2">전화번호</span>
                </td>
                <td style="padding-left: 10px">
                  <input
                    class="mt-2"
                    type="text"
                    placeholder="예) 010-2274-4895"
                    style="width: 100%"
                    v-model="phone"
                  />
                </td>
              </tr>
              <tr>
                <td style="width: 25%; text-align: center">
                  <span class="mt-2">요청사항</span>
                </td>
                <td style="padding-left: 10px">
                  <textarea
                    class="mt-2"
                    type="text"
                    placeholder="경비실에 맡겨주세요"
                    style="width: 100%"
                    v-model="orderRequest"
                  />
                </td>
              </tr>
            </table>
          </div>
        </div>
        <div class="orderText">
          <p>주문내역</p>
        </div>
        <!-- 로딩이 안됐을땐, -->
        <div v-if="!onloaded">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
        <div v-else>
          <!-- 장바구니에 담지 않았을 때 -->
          <div v-if="!foodList || foodList.length == 0">
            <div class="foodContainer d-block m-4 emptyContainer">
              <span> 장바구니가 비어있습니다! </span>
            </div>
          </div>
          <!-- 장바구니에 무엇인가를 담아뒀을 때 -->
          <div v-else-if="foodList.length > 0">
            <div v-for="(food, index) in foodList" :key="index">
              <div class="foodContainer d-block">
                <div class="row">
                  <img :src="food.fileUrl" class="foodImage col-4" />
                  <div class="text col-8">
                    <div class="row">
                      <div>
                        <h4 class="d-inline m-3">
                          {{ food.name }}
                        </h4>
                        <span
                          class="badge bg-success d-inline"
                          v-if="food.status == 1"
                          >판매중</span
                        >
                        <span
                          class="badge bg-secondary d-inline"
                          v-else-if="food.status == 2"
                          >품절</span
                        >
                        <span
                          class="badge bg-danger d-inline"
                          v-else-if="food.status == 3"
                          >판매중지</span
                        >
                      </div>
                    </div>
                    <div class="row">
                      <div>
                        <span>{{ food.price }} 원</span>
                      </div>
                      <nav aria-label="Page navigation example">
                        <ul class="pagination">
                          <li class="page-item">
                            <a
                              class="page-link"
                              @click="decreaseQuantity(food.foodId)"
                              >-</a
                            >
                          </li>
                          <li class="page-item">
                            <a class="page-link">{{
                              foodQuantity(food.foodId)
                            }}</a>
                          </li>

                          <li class="page-item">
                            <a
                              class="page-link"
                              @click="increaseQuantity(food.foodId)"
                              >+</a
                            >
                          </li>
                          <div
                            class="btn btn-primary"
                            @click="removeFood(food.foodId)"
                          >
                            삭제하기
                          </div>
                        </ul>
                      </nav>
                      <div>
                        총액 : {{ pricePerFood(food.foodId, food.price) }} 원
                      </div>
                    </div>
                  </div>
                </div>
              </div>
            </div>
          </div>
          <div class="orderText" style="margin-top: 30px">
            <p>할인 쿠폰</p>
          </div>
          <div class="couponWrapper d-block">
            <!-- 사용할 수 있는 쿠폰이 있으면 불러오기 -->
            <!-- 사용기한, 체크박스로 체크하면 사용 -->
            <div
              v-if="
                !couponLoaded || couponList.length == 0 || couponList[0] == ''
              "
            >
              쿠폰정보가 없습니다
            </div>
            <div v-else-if="couponLoaded">
              <div
                class="form-check"
                v-for="(item, index) in couponList"
                :key="index"
              >
                <input
                  class="form-check-input"
                  type="checkbox"
                  :value="item.id"
                  :id="item.id"
                  v-model="checkedCoupon"
                />
                <label class="form-check-label" :for="item.id">
                  {{ item.couponName }}: {{ item.name }} 할인 쿠폰,
                  {{ item.total_discountPrice }} 원 할인, 만료기간
                  {{ item.couponEnd[0] }} 년 {{ item.couponEnd[1] }} 월
                  {{ item.couponEnd[2] }} 일까지
                </label>
              </div>
            </div>
          </div>
          <!-- 결제수단 선택 -->
          <div class="cartOrder">
            <div class="orderText">
              <p>결제수단 선택</p>
            </div>
            <div class="orderPay mt-2">
              <div>
                <span style="font-weight: 550; font-size: 18px"
                  >카카오페이로 결제</span
                ><br /><span style="font-size: 14px; color: lightgray">
                  현재 카카오페이 결제밖에 지원되지 않습니다.</span
                ><br />
                <div class="payKakao">
                  <img
                    src="../../assets/payment_icon_yellow_large.png"
                    width="140"
                  />
                </div>
              </div>
            </div>
          </div>
          <div
            class="calculatorWrapper d-block"
            style="padding-top: 0px; margin-top: 100px"
          >
            <div class="orderText">
              <h4>주문내역</h4>
            </div>
            <div class="orderList mt-1">
              <div v-for="(food, index) in foodList" :key="index">
                <div class="col-8" style="float: left">
                  {{ food.name }} X {{ foodQuantity(food.foodId) }} 개
                </div>
                <div class="col-4" style="float: right">
                  {{ pricePerFood(food.foodId, food.price) }} 원
                </div>
              </div>
              <!-- 쿠폰할인 -->
              <hr />
              <div v-if="checkedCoupon.length > 0">
                <div class="col-8 mt-2" style="float: left">
                  <strong>쿠폰할인</strong>
                </div>
                <div v-for="(couponId, index) in checkedCoupon" :key="index">
                  <div class="col-4 mt-2" style="float: right">
                    <span>-{{ getCouponDiscountPrice(couponId) }} 원</span>
                  </div>
                </div>
              </div>
              <!-- 총액 -->
              <hr />
              <div class="col-8 mt-2" style="float: left">
                <strong>총 결제 금액</strong>
              </div>
              <div class="col-4 mt-2" style="float: right">
                <span>{{ totalPriceAppliedCoupons }}</span>
              </div>
            </div>
            <!-- orderList 에 대한 총액정리 쿠폰 적용 후. -->
          </div>
          <div class="commandOrderWrapper" v-if="foodList.length > 0">
            <div class="row">
              <div class="col commandOrder" @click="putOrder">주문하기</div>
              <div class="col commandCancel" @click="clearOrder">취소하기</div>
            </div>
          </div>
        </div>
      </div>
    </div>
    <kakaomap v-if="mapModal"></kakaomap>
  </div>
</template>

<script>
import { mapState, mapGetters, mapMutations } from "vuex";
import http from "@/api/http";
import axios from "axios";
import kakaomap from "@/components/modal/Map.vue";
import { error, success } from "@/api/notification";

export default {
  components: { kakaomap },
  data() {
    return {
      foodList: [],
      onloaded: false,
      orderList: new Map(),
      orderRequest: "",
      addressDetail: "",
      phone: "",
      couponList: [],
      couponLoaded: false,
      checkedCoupon: [],
      placeSelector: false,
    };
  },
  computed: {
    ...mapState({
      mapModal: "mapModal",
      address: "selectedPlace",
    }),
    ...mapGetters({
      checkCart: "checkCart",
      GET_LOCAL: "GET_LOCAL",
      getUserId: "auth/getUserId",
    }),
    currentPlace() {
      if (this.GET_LOCAL == null || this.GET_LOCAL.address_name == null) {
        this.refreshLocation();
      }
      return this.GET_LOCAL;
    },
    totalPrice: function () {
      let result = 0;
      for (let food of this.foodList) {
        let foodPrice = food.price;
        let qantity = this.orderList.get(food.foodId);
        result += foodPrice * qantity;
      }
      return result;
    },
    totalPriceAppliedCoupons: function () {
      let result = 0;
      for (let food of this.foodList) {
        let foodPrice = food.price;
        let qantity = this.orderList.get(food.foodId);
        result += foodPrice * qantity;
      }
      for (let checkedFoodId of this.checkedCoupon) {
        result -= this.getCouponDiscountPrice(checkedFoodId);
      }
      return result;
    },
    finalAddress() {
      if (this.placeSelector == false && this.currentPlace) {
        return this.currentPlace.address_name;
      } else if (this.placeSelector == true && this.address.place_name) {
        return this.address.place_name;
      } else {
        return this.currentPlace.address_name;
      }
    },
  },
  mounted() {
    if (this.GET_LOCAL == null) {
      this.refreshLocation();
    }
    if (this.checkCart != null) {
      this.getFoodList(this.checkCart);
    } else {
      this.onloaded = true;
    }
  },
  methods: {
    ...mapMutations(["delCart", "clearCart", "SET_MODAL_MAP"]),
    getFoodList(foodIdSet) {
      // 페이지 초기화할때 카드에 저장된 음식 정보를 불러온다.
      const foodIdList = Array.from(foodIdSet);
      http
        .post("/order/getFoodList", {
          foodIdList,
        })
        .then((res) => {
          if (res.status === 200) {
            this.foodList = res.data;
            let orderList = new Map();
            let memberId = this.getUserId;
            for (let food of res.data) {
              orderList.set(food.foodId, food.quantity);
              // foodId와 memberId로 사용가능한 쿠폰 가져오기
              http
                .get("/coupon/getMemberCoupon", {
                  params: {
                    memberId: memberId,
                    foodId: food.foodId,
                  },
                })
                .then((res) => {
                  if (res.status === 200) {
                    console.log("쿠폰정보들을 가져옵니다.");
                    this.couponList.push(res.data);
                  }
                });
            }
            this.couponLoaded = true;
            this.orderList = orderList;
          }
        })
        .catch((e) => console.log(e));
      this.onloaded = true;
    },
    increaseQuantity(foodId) {
      let q = this.orderList.get(foodId);
      this.orderList.set(foodId, ++q);
    },
    decreaseQuantity(foodId) {
      let q = this.orderList.get(foodId);
      if (q == 0) {
        return;
      }
      this.orderList.set(foodId, --q);
    },
    test() {
      console.log(this.orderList);
    },
    foodQuantity(foodId) {
      return this.orderList.get(foodId);
    },
    pricePerFood(foodId, price) {
      return this.orderList.get(foodId) * price;
    },
    putOrder() {
      // 클릭했을 때 재 클릭을 방지해야함.
      // validation
      if (!this.finalAddress) {
        error("주소를 입력해주세요!", this);
        return;
      }
      if (this.addressDetail == "" || !this.addressDetail) {
        console.log(this.address);
        error("상세 주소를 입력해주세요", this);
        return;
      }
      const phoneCheckReg = /^\d{2,3}-\d{3,4}-\d{4}$/;
      let phoneValidation = phoneCheckReg.test(this.phone);
      if (!this.phone) {
        error("전화번호를 입력해주세요", this);
        return false;
      } else if (phoneValidation === false) {
        error("전화번호는 숫자와 -(하이픈)만 입력 가능합니다.");
        return;
      }

      // 계산 진행, 만약 회원이면 정보로 하고, 로그인 아니면 필요정보입력
      // 정보들은 백단에서 한번 더 재확인해야한다.
      const address = this.finalAddress + ", " + this.addressDetail;
      const phone = this.phone;
      const orderRequest = this.orderRequest;
      const totalPrice = this.totalPrice;
      // 쿠폰정보 쿠폰사용가능 정보 확인
      const couponIdList = this.checkedCoupon;
      // member Id 얻어오기
      const memberId = this.getUserId;
      // 데이터 정렬
      const orderData = {
        address,
        phone,
        orderRequest,
        totalPrice,
        couponIdList,
        memberId,
      };
      console.log("==== 생성되어 들어가는 memeberId = " + memberId);
      // DB에 오더 넣기.
      axios
        .create({ baseURL: "http://localhost:8083" })
        .post("/order/putOrder", orderData)
        .then((res) => {
          if (res.status === 200) {
            console.log("오더정보 삽입완료");
            let orderId = res.data;
            const orderMap = this.orderList;
            // 각 오더상품을 오더인포로 넣기. 수량이 0개면 뺀다.
            let orderList = Array.from(orderMap);
            let filteredOrderList = orderList.filter((arr) => {
              return arr[1] > 0;
            });
            console.log(filteredOrderList);
            const postOrderInfo = (foodInfo) =>
              axios
                .create({
                  baseURL: "http://localhost:8083",
                  headers: {
                    "Content-type": "application/json",
                  },
                })
                .post("/order/putOrderInfo", foodInfo);

            filteredOrderList
              .reduce((prevProm, list) => {
                list.push(orderId);
                return prevProm.then(() => postOrderInfo(JSON.stringify(list)));
              }, Promise.resolve())
              .then((res) => {
                if (res.status === 200) {
                  axios
                    .get("http://localhost:8083/order/kakaoPay")
                    .then((res) => {
                      console.log(res.data);
                      // 카카오페이 팝업 띄우기
                      window.open(
                        res.data,
                        "kakaoPopUp",
                        "toolbar=no,location=no,status=no,menubar=no,scrollbars=yes,resizable=no,width=540,height=700,left=100,top=100"
                      );
                    });
                } else {
                  // orderInfo 만들다가 실패함.
                }
              });
          } else {
            return console.log("다시시도해주새요");
          }
        });
    },
    clearOrder() {
      let willClear = confirm("정말 장바구니를 비우시겠습니까?");
      if (willClear === true) {
        this.clearCart();
        this.foodList = [];
        this.orderList.clear();
      }
    },
    removeFood(foodId) {
      let filteredFoodList = this.foodList.filter(
        (food) => food.foodId !== foodId
      );
      this.foodList = filteredFoodList;
      if (this.orderList.has(foodId)) {
        this.orderList.delete(foodId);
      }
      this.delCart(foodId);
    },
    getCouponDiscountPrice(input) {
      let couponId = +input;
      let poo = this.couponList.filter((item) => {
        return item.id == couponId;
      });
      return poo[0].total_discountPrice;
    },
    refreshLocation() {
      return navigator.geolocation.getCurrentPosition(
        (position) => {
          this.setLocation(position.coords.latitude, position.coords.longitude);
        },
        (error) => {
          console.log("위치정보를 갱신할 수 없습니다" + error);
        }
      );
    },
    setLocation(latitude, longitude) {
      this.$store.commit("SET_LAT", latitude);
      this.$store.commit("SET_LON", longitude);
      console.log("사용자 위치 추적: " + latitude + ", " + longitude);
      this.$store.commit("SET_OBSERVE", true);
    },
    openMap() {
      this.placeSelector = true;
      this.SET_MODAL_MAP(true);
    },
    closeMap() {
      this.placeSelector = false;
      this.SET_MODAL_MAP(false);
    },
    type_addressDetail(e) {
      this.addressDetail = e.target.value;
    },
  },
};
</script>

<style scoped>
.orderText {
  background-color: aliceblue;
  padding: 10px;
}
.orderText p {
  margin: 0px;
}
.orderAddress {
  padding: 10px;
  text-align: left;
}
.orderAddress span {
  font-weight: 550;
  align-items: center;
}

.Pay {
  height: 60px;
  border: 1px solid lightgray;
}
.payLeft {
  float: left;
  width: 49%;
}
.payRight {
  float: right;
  width: 49%;
}
.payKakao {
  margin-top: 10px;
  margin-bottom: 30px;
}

.wrapper {
  height: 100%;
  width: 100%;
}
.cart {
  border: 2px solid tomato;
}
.foodContainer {
  height: 9rem;
  overflow: hidden;
  border: 1px solid gainsboro;
  display: flex;
  align-items: center;
}
.calculatorWrapper {
  overflow: hidden;
  border: 1px solid gainsboro;
  display: flex;
  align-items: center;
  padding: 50px;
}
.emptyContainer {
  padding: 50px;
}
.couponWrapper {
  overflow: hidden;
  border: 1px solid gainsboro;
  display: flex;
  align-items: center;
  padding: 50px;
}
.foodContainer span {
  position: relative;
}
.foodImage {
  margin: 1rem;
  width: 8rem;
  height: 6.8rem;
}
.text {
  padding: 4px;
}
.desc {
  display: -webkit-box;
  font-weight: lighter;
  color: grey;
  font-size: 12px;
  padding: 5px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: normal;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  line-height: 1.2;
  height: 3.6rem;
}
.commandOrderWrapper {
  height: 8rem;
  padding: 2rem;
}
.commandOrder {
  height: 50px;
  cursor: pointer;
  border: 1px solid gainsboro;
}
.commandCancel {
  cursor: pointer;
  border: 1px solid gainsboro;
}
</style>
