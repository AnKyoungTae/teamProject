<template>
  <div class="eventWapper m-1 p-4">
    <div class="data-title">
      {{ data.title }}
    </div>
    <hr />
    <!-- 쿠폰이 로드되었을 경우 -->
    <div v-if="!couponLoaded">만료된 이벤트입니다.</div>
    <div v-else-if="couponLoaded">
      <!-- 이벤트 정보 -->

      <div>
        <span class="data-date"
          >{{ data.updateDate[0] }}년 {{ data.updateDate[1] }}월
          {{ data.updateDate[2] }}일 ~ {{ data.dueDate[0] }}년
          {{ data.dueDate[1] }}월 {{ data.dueDate[2] }}일까지</span
        >
      </div>
      <div>
        <span class="data-span">WHAT POO 회원을 대상으로 {{ data.body }}</span>
      </div>
      <div>
        <hr />
        <div v-if="eventavailable == true"></div>
        <a>
          <div class="event-btn" :class="{ disabled: !eventavailable }">
            <div class="btn-div">
              <span class="btn-span1">{{ couponInfo.name }} 할인쿠폰</span
              ><br />
              <span class="btn-span2">{{ couponInfo.couponPrice }}원 할인</span>
            </div>
            <div style="float: right">
              <img :src="couponInfo.fileUrl" style="height: 100px" />
            </div>
          </div>
        </a>
        <br />
        <div>
          <a>
            <div class="order-div">
              <span class="order-span mb-4" @click="applyEvent"
                >이벤트 참여 &nbsp;></span
              >
            </div>
          </a>
        </div>
        <hr style="clear: both" />
        <!-- 쿠폰정보 -->
        <div>
          쿠폰 유효기간 : {{ couponInfo.couponEnd[0] }}년
          {{ couponInfo.couponEnd[1] }}월 {{ couponInfo.couponEnd[2] }}일
          {{ couponInfo.couponEnd[3] }}시 까지 사용가능
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import { mapGetters } from "vuex";
export default {
  props: ["data"],
  data() {
    return {
      couponInfo: null,
      couponLoaded: false,
      eventavailable: false,
    };
  },
  methods: {
    applyEvent() {
      const memberId = this.getUserId;
      if (memberId > 0) {
        let applyCoupon = confirm("이벤트에 참여하시겠습니까?");
        if (applyCoupon == true) {
          // 이벤트참여, 쿠폰지급
          const requestData = {
            memberId,
            couponId: this.couponInfo.couponId,
          };
          http.post("/coupon/applyCoupon", requestData).then((res) => {
            console.log(res);
            if (res.status === 200) {
              alert("이벤트에 참여하여, 쿠폰이 지급되었습니다. 쿠폰사용은...");
            } else {
              alert("알수없는 서버오류! 다시 시도해주세요..");
            }
          });
        } else {
          return;
        }
      } else {
        alert("로그인된 회원만 참여 가능합니다");
      }
    },
  },
  computed: {
    ...mapGetters({ getUserId: "auth/getUserId" }),
    totalDiscountPrice() {
      let discountPrice = this.couponInfo.couponPrice;
      let discountRate = this.couponInfo.discountRate;
      return discountPrice * (discountRate / 100);
    },
  },
  mounted() {
    if (this.data.status == "Y") {
      // 이벤트가 활성화 되어있을 경우, 쿠폰 데이터를 불러온다.

      http
        .get("/coupon/getCouponInfo", {
          params: {
            eventId: this.data.eventId,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            console.log(res.data);
            if (res.data !== "") {
              this.couponInfo = res.data;
              this.couponLoaded = true;
              if (this.couponInfo || this.couponInfo !== "") {
                this.eventavailable = true;
              }
            }
          }
        });
    } else {
      return;
    }
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap");
.eventWapper {
  border: 1px solid gray;
}
.gray {
  background-color: gray;
}
.data-title {
  font-family: "Black Han Sans", sans-serif;
  font-size: 60px;
}
.data-date {
  font-size: 20px;
}
.data-span {
  font-size: 20px;
}
.event-btn {
  border: 1px solid rgb(255, 194, 115);
  padding: 10px;
  background-color: rgb(255, 194, 115);
  border-radius: 15px;
  width: 360px;
  height: 120px;
  margin: auto;
  margin-top: 30px;
  margin-bottom: 15px;
  cursor: pointer;
}
.btn-div {
  float: left;
  padding-top: 10px;
}
.btn-span1 {
  font-size: 20px;
}
.btn-span2 {
  font-size: 30px;
  font-family: "Black Han Sans", sans-serif;
}

.order-div {
  border: 1px solid black;
  background-color: black;
  border-radius: 15px;
  width: 200px;
  height: 60px;
  line-height: 60px;
  float: right;
  cursor: pointer;
}
.order-span {
  color: white;
  font-family: "Black Han Sans", sans-serif;
  font-size: 25px;
  line-height: middle;
}
.order-div:hover {
  background-color: darkred;
  transition: 0.2s;
}
</style>
