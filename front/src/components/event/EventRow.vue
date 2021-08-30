<template>
  <div style="width:400px; height: 100px; margin-left: 50px; border: 2px solid black;">
    <strong style="font-size:30px;">1000원</strong>
    <div style="text-align: left;">
      가게이름
    </div>
    <div style="text-align: left;">
     사용기간 :  {{ data.updateDate }}~{{ data.dueDate }}
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
</style>