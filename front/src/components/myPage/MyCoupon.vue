<template>
  <div style="margin-top: 50px">
    <h1 class="nearShopTitle">
      <p>쿠폰함</p>
    </h1>
  </div>
  <div class="manage-div">
    <div class="manage-div2">
      <div class="profile-manage">
        <div class="row" style="overflow: auto" v-if="couponToggle == true">
          <!--쿠폰시작 -->
          <div
            class="col coupon"
            v-for="coupon in coupons"
            :key="coupon.couponId"
          >
            <div class="card coupon-card">
              <div class="coupon-main">
                <div class="content">
                  <div class="row">
                    <span style="color: burlywood"
                      ><span
                        style="
                          font-size: 35px;
                          font-weight: 600;
                          font-family: Black Han Sans, sans-serif;
                        "
                        >{{ coupon.total_discountPrice }}</span
                      ><span style="font-size: 20px"> 원 할인권</span></span
                    >
                    <span class="d-inline m-1" style="font-size: 18px"
                      >{{ coupon.couponName }}
                      <span
                        class="badge bg-success m-1"
                        v-if="coupon.couponUsed == 'N' && coupon.status == 'Y'"
                        >사용가능</span
                      >
                      <span class="badge bg-danger m-1" v-else
                        >사용불가</span
                      ></span
                    >
                  </div>

                  <p>쿠폰번호 : {{ coupon.couponNumber }}</p>
                  <p>
                    사용기간 ~ {{ coupon.couponEnd[0] }}.{{
                      coupon.couponEnd[1]
                    }}.{{ coupon.couponEnd[2] }} 까지
                  </p>
                </div>
                <div class="vertical"></div>
                <div class="copy-button">
                  <button
                    type="button"
                    class="copybtn btn btn-primary position-relative"
                    :class="[
                      coupon.couponUsed == 'N' ? '' : 'disabled',
                      coupon.status == 'Y' ? '' : 'disabled',
                    ]"
                    @click="moveToStoreInfo(coupon)"
                  >
                    사용
                    <span
                      class="
                        position-absolute
                        top-0
                        start-100
                        translate-middle
                        badge
                        rounded-pill
                        bg-success
                      "
                      v-if="getLeftDay(coupon.couponEnd) >= 0"
                    >
                      {{ dayLeft(coupon.couponEnd) }}
                    </span>
                    <span
                      class="
                        position-absolute
                        top-0
                        start-100
                        translate-middle
                        badge
                        rounded-pill
                        bg-danger
                      "
                      v-else
                    >
                      {{ dayLeft(coupon.couponEnd) }}
                    </span>
                  </button>
                </div>
              </div>
            </div>
          </div>
          <!--쿠폰종료 -->
        </div>

        <div class="col coupon" style="overflow: auto; height: 660px" v-else>
          쿠폰이 없습니다.
        </div>
      </div>
    </div>
  </div>
</template>
<script>
import http from "@/api/http";
import { end } from "@popperjs/core";

export default {
  data() {
    return {
      couponToggle: false,
      coupons: [],
    };
  },
  methods: {
    getCoupon() {
      http
        .post("/profile/getCoupon")
        .then((res) => {
          if (res.data.length > 0) {
            this.couponToggle = true;
            this.coupons = res.data;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    dayLeft(time) {
      let btDay = this.getLeftDay(time);
      if (btDay < 0) {
        return "X";
      }
      return btDay + " 일 남음";
    },
    moveToStoreInfo(coupon) {
      if (coupon.couponUsed != "N") {
        alert("이미 사용한 쿠폰입니다.");
        return;
      }
      if (coupon.status == "N") {
        alert("만료된 쿠폰입니다.");
        return;
      }
      let btDay = this.getLeftDay(coupon.couponEnd);
      if (btDay < 0) {
        alert("만료된 쿠폰입니다.");
        return;
      }
      this.$router.push({
        path: "/shopDetail",
        query: { shopInfo: coupon.store_id },
      });
    },
    getLeftDay(time) {
      let now = new Date();
      let year = now.getFullYear();
      let month = now.getMonth();
      let day = now.getDate();
      let today = new Date(year, month, day);
      let endDay = new Date(time[0], time[1] - 1, time[2]);
      let btMs = endDay.getTime() - today.getTime();
      let btDay = btMs / (1000 * 60 * 60 * 24);
      return btDay;
    },
  },
  mounted() {
    this.getCoupon();
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap");
.manage-div {
  max-width: 560px;
  margin: 0 auto;
  display: block;
}
.manage-div2 {
  display: block;
}
.manage-text {
  font-weight: normal;
  font-size: 30px;
}

.coupon {
  display: flex;
  flex-direction: row;
  justify-content: center;
  padding: 20px;
  height: 200px;
}
.coupon-card {
  width: 380px;
  height: 160px;
  border-radius: 12px;
  box-shadow: 0 4px 6px 0 rgba(0, 0, 0, 0.1);
  background-color: #fff;
  padding: 10px 10px;
  float: left;
  margin: 10px 10px;
  background-color: #fafafa;
}
.coupon-main {
  height: 100%;
}

.coupon-main,
.copy-button {
  display: flex;
  justify-content: space-between;
  padding: 0 10px;
  align-items: center;
}

.vertical {
  border-left: 2px Dashed darkgray;
  height: 160px;
  position: absolute;
  left: 65%;
}

.content {
  width: 65%;
}

.content h1 {
  font-size: 35px;
  margin-left: -20px;
  color: #ffffff;
}

.content h1 span {
  font-size: 18px;
}
.content h2 {
  font-size: 18px;
  margin-left: -20px;
  color: #ffffff;
  text-transform: uppercase;
}

.content p {
  font-size: 12px;
  color: lightslategray;
  margin-left: -20px;
  margin-bottom: 0px;
}

.copy-button {
  width: 30%;
}

.copy-button button {
  padding: 5px 20px;
  background-color: rgb(105, 150, 233);
  color: rgb(255, 255, 255);
  border: 1px solid transparent;
}
.disabled {
  background-color: red;
}
.title {
  font-size: 23px;
  font-weight: bold;
  text-align: left;
  margin-left: 90px;
  height: 50px;
  padding-top: 30px;
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
  width: 80%;
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
