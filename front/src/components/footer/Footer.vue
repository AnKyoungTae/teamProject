<template>
  <div class="emptyspace"></div>
  <div class="footer">
    <div class="wapoo-wrap">
      <div class="wapoo-logo">
        <div>
          <span @click="this.$router.push({ path: '/' })"> 와푸 </span>
          <img
            @click="this.$router.push({ path: '/' })"
            class="logo"
            src="../../assets/logo.png"
          />
        </div>
        <p class="logo-p">
          뭘 먹어야할지 고민일 때, <br />배달 주문 플렛폼 서비스 와푸
        </p>
      </div>
      <div class="wapoo-info1">
        <p style="padding-top: 30px; text-align: left">
          <strong>인천일보아카데미</strong>
        </p>
        <span>인천 미추홀구 매소홀로488번길 6-32 태승빌딩 5층</span><br />
      </div>
      <div class="wapoo-info">
        <p style="padding-top: 30px">
          <strong>CONNECT</strong>
        </p>
        <span>GitHub</span><br />
        <span>Vue.js</span><br />
        <span>Kakao Map</span><br />
        <span>axios</span>
      </div>
      <div class="wapoo-last">
        <p style="padding-top: 30px">
          <strong>WAPOO</strong>
        </p>
        <span @click="this.$router.push({ path: '/cart' })">장바구니</span
        ><br />
        <span @click="this.$router.push({ path: '/shopCategory' })"
          >음식/가게</span
        ><br />
        <span @click="this.$router.push({ path: '/serviceCenter' })"
          >고객센터</span
        ><br />
        <span @click="this.$router.push({ path: '/event' })">이벤트</span>
      </div>
    </div>
  </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap");
.footer {
  padding-bottom: 20px;
  color: #91afba;
  overflow: hidden;
  width: 100%;
  background-color: #355f6e;
  bottom: 0%;
}
.wapoo-wrap {
  position: relative;
  max-width: 1200px;
  margin: 10px auto 0 auto;
  padding-left: 100px;
  padding-right: 100px;
  background-color: #355f6e;
}
.wapoo-info1 {
  float: left;
  padding-left: 80px;
}
.wapoo-info1 p {
  font-size: 24px;
  color: #ffda77;
}
.wapoo-info1 span {
  font-size: 20px;
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
}
.wapoo-info {
  display: block;
  color: #91afba;
  line-height: 1.6;
  padding-left: 100px;
  text-align: right;
  float: left;
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
.wapoo-last {
}
.wapoo-last strong {
  color: #ffda77;
  font-size: 20px;
}
.wapoo-last span {
  font-size: 20px;
}
span {
  cursor: pointer;
}
.emptyspace {
  height: 200px;
}
</style>
