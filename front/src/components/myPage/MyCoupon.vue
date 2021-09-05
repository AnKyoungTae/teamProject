<template>
<div class="manage-div">
    <div class="manage-div2">
      <h3 class="manage-text">쿠폰함</h3>
      <div class="profile-manage">
        <div
          class="row"
          style="overflow: auto;"
          v-if="couponToggle == true"
        >
        <!--쿠폰시작 -->
        <div class="col coupon" v-for="coupon in coupons" :key="coupon.couponId">
          <div class="card coupon-card">
            <div class="coupon-main">
              <div class="content">
                <div class="row">
                  <span style="color: burlywood;"><span style="font-size: 35px; font-weight: 600; font-family: Black Han Sans, sans-serif;">{{ coupon.total_discountPrice }}</span><span style="font-size: 20px;"> 원 할인권</span></span>
                  <span class="d-inline m-1" style="font-size: 18px; "
                    >{{ coupon.couponName }}
                    <span class="badge bg-success m-1" v-if="coupon.status == 'Y'"
                      >사용가능</span
                    >
                    <span
                      class="badge bg-danger m-1"
                      v-else-if="coupon.status == 'N'"
                      >사용불가</span
                    ></span
                    >
                  </div>

                  <p>쿠폰번호 : {{ coupon.couponNumber }}</p>
                  <p>사용기간 ~ {{ coupon.couponEnd[0] }}.{{ coupon.couponEnd[1] }}.{{ coupon.couponEnd[2] }} 까지</p>
                </div>
                <div class="vertical"></div>
                <div class="copy-button">
                  <button
                    type="button"
                    class="copybtn btn btn-primary position-relative"
                    :class="[coupon.status == 'Y' ? '' : 'disabled']"
                    @click="moveToStoreInfo(coupon.store_id)"
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
                        bg-danger
                      "
                    >
                      {{ dayLeft(coupon.couponEnd) }}
                      <span class="visually-hidden">unread messages</span>
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
          console.log(res.data.length);
          if (res.data.length > 0) {
            this.couponToggle = true;
            this.coupons = res.data;
            console.log(this.coupons);
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    dayLeft(time) {
      let now = new Date();
      var day = ("0" + now.getDate()).slice(-2);
      let leftday = day - time[2];
      if (leftday < 0) {
        return "X";
      }
      return leftday + " 일";
    },
    moveToStoreInfo(storeId) {
      this.$router.push({ path: "/shopDetail", query: { shopInfo: storeId } });
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
  margin: 50px auto 0;
  display: block;
}
.manage-div2 {
  display: block;
}
.manage-text {
  font-weight: normal;
  font-size: 20px;
}
.profile-manage {
  margin-top: 10px;
  border-top: 1px solid #7c7c7c;
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
  background-color: #FAFAFA;
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
  background-color: darkgray;
  color: rgb(255, 255, 255);
  border: 1px solid transparent;
}
.title {
  font-size: 23px;
  font-weight: bold;
  text-align: left;
  margin-left: 90px;
  height: 50px;
  padding-top: 30px;
}
</style>
