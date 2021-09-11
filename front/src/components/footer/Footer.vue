<template>
  <div class="emptyspace"></div>
  <div class="footer" :style="{ 'margin-left': sidebarWidth }">
    <div class="wapoo-wrap">
      <div class="wapoo-logo" style="width:25%">
        <div style="margin-top: 30px;">
          <img
            @click="this.$router.push({ path: '/' })"
            class="logo"
            src="../../assets/logo_copy.png"
          />
        </div>
        <p class="logo-p">
          뭘 먹어야할지 고민일 때, <br />배달 주문 플렛폼 서비스 와푸
        </p>
      </div>
      <div class="wapoo-info1" style="width:75%;">
        <p style="padding-top: 30px; text-align: left">
          <strong>(주)인천일보아카데미</strong>
        </p>
        <div class="info1-span">
          <span>인천 미추홀구 매소홀로488번길 6-32 태승빌딩 5층</span> | 대표이사 : 조성호<br />
        </div>
        <br />
        <div>
          <span style="color: #91afba;">
            와푸는 통신판매중개자이며 통신판매의 당사자가 아닙니다. 따라서 상품/ 거래정보 및 거래와 관련하여 와푸에 등록된 판매자의 고의 또는 과실로 소비자에게 발생하는 손해에 대해 와푸는 책임을 지지 않습니다. 상품 및 거래에 관하여 보다 정확한 정보는 해당 판매자에게 직접 확인하여 주시기 바랍니다.
          </span>
        </div>
      </div>
      <!-- <div class="wapoo-info">
        <p style="padding-top: 30px">
          <strong>CONNECT</strong>
        </p>
        <span>GitHub</span><br />
        <span>Vue.js</span><br />
        <span>Kakao Map</span><br />
        <span>axios</span>
      </div> -->
    </div>
  </div>
</template>

<script>
import { sidebarWidth } from "@/components/sidebar/state";
export default {
  setup() {
    return { sidebarWidth };
  },
};
</script>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap");
.footer-div {
  padding-bottom: 20px;
  color: #91afba;
  overflow: auto;
  width: 100%;
  position: absolute;
  background-color: #355f6e;
  bottom: 0%;
  transition: 0.2s;
}
.wapoo-wrap {
  position: relative;
  overflow: auto;
  margin: 10px auto 0 auto;
  padding-left: 100px;
  padding-right: 100px;
  background-color: #355f6e;
}
.wapoo-info1 {
  float: left;
  padding-left: 30px;
  text-align: left;
}
.wapoo-info1 p {
  font-size: 24px;
  color: #ffda77;
}
.info1-span {
  color: #91afba;
}
.wapoo-logo {
  top: 0;
  left: 20px;
  float: left;
  text-align: left;
  width: 250px;
}
.logo {
  width: 150px;
}
.wapoo-logo span {
  height: 29px;
  text-decoration: none;
  vertical-align: middle;
  font-size: 50px;
  font-weight: 600;
  color: #fd3a69;
  /* font-family: "Black Han Sans", sans-serif; */
}
.logo-p {
  font-size: 20px;
  color: #91afba;
}
.wapoo-info {
  display: block;
  color: #91afba;
  line-height: 1.6;
  padding-left: 100px;
  text-align: right;
  padding-right: 30px;
}
.wapoo-info strong {
  display: inline-block;
  color: #ffda77;
  font-size: 20px;
}
.wapoo-info span {
  font-size: 20px;
}
span {
  cursor: pointer;
}
.emptyspace {
  height: 200px;
}
.footer {
  padding-bottom: 20px;
  color: #91afba;
  overflow: hidden;
  width: 100%;
  background-color: #355f6e;
  bottom: 0%;
  transition: 0.2s;
}
</style>
