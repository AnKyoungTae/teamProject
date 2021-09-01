<template>
  <center>
    <div id="profileMenu" class="m-4">
      <div class="row">
        <div
          class="col d-inline m-1 spread-underline"
          @click="changeView('myProfile')"
        >
          <a :class="[view === 'myProfile' ? 'choicedMenu' : '']">내정보</a>
        </div>

        <div
          class="col d-inline m-1 spread-underline"
          @click="changeView('myCoupon')"
        >
          <a :class="[view === 'myCoupon' ? 'choicedMenu' : '']">쿠폰</a>
        </div>

        <div
          class="col d-inline m-1 spread-underline"
          @click="changeView('myOrderHistory')"
        >
          <a :class="[view === 'myOrderHistory' ? 'choicedMenu' : '']"
            >주문내역</a
          >
        </div>

        <div
          class="col d-inline m-1 spread-underline"
          @click="changeView('myReview')"
        >
          <a :class="[view === 'myReview' ? 'choicedMenu' : '']">리뷰관리</a>
        </div>

        <div
          class="col d-inline m-1 spread-underline"
          @click="changeView('myQuestion')"
        >
          <a :class="[view === 'myQuestion' ? 'choicedMenu' : '']">질문내역</a>
        </div>
      </div>
    </div>
    <hr />
    <my-profile v-if="currentView == 'myProfile'" />
    <my-coupon v-if="currentView == 'myCoupon'" />
    <my-order-history v-if="currentView == 'myOrderHistory'" />
    <my-review v-if="currentView == 'myReview'" />
    <my-Question v-if="currentView == 'myQuestion'" @updateView="changeView" />
  </center>
</template>
<script>
import myProfile from "@/components/myPage/MyProfile.vue";
import myCoupon from "@/components/myPage/MyCoupon.vue";
import myOrderHistory from "@/components/myPage/OrderMenu.vue";
import myReview from "@/components/myPage/MyReview.vue";
import myQuestion from "@/components/myPage/Question.vue";

export default {
  components: { myProfile, myCoupon, myOrderHistory, myReview, myQuestion },
  data() {
    return {
      view: "myProfile",
    };
  },
  computed: {
    currentView: function () {
      console.log(this.view);
      return this.view;
    },
  },
  methods: {
    changeView(request) {
      this.view = request;
    },
  },
};
</script>

<style>
#profileMenu {
  width: 100%;
}
#profileMenu .col {
  float: left;
  height: 60px;
  line-height: 40px;
  text-align: center;
  border: 1px solid black;
  overflow: hidden;
  cursor: pointer;
}
#profileMenu .col:hover {
  background: turquoise;
  color: #fff;
  cursor: pointer;
}
a {
  text-decoration: none;
  color: #404040;
}
.side-row {
  border-bottom: 1px solid black;
}
.item {
  padding: 20px;
}
.choicedMenu {
  text-decoration: underline;
  text-underline-position: under;
  color: teal;
}
.spread-underline {
  color: #333;
  text-decoration: none;
  display: inline-block;
  padding: 10px 0;
  position: relative;
}
.spread-underline:after {
  background: none repeat scroll 0 0 transparent;
  bottom: 0;
  content: "";
  display: block;
  height: 4px;
  left: 50%;
  position: absolute;
  background: #ffb000;
  transition: width 0.3s ease 0s, left 0.3s ease 0s;
  width: 0;
}
.spread-underline:hover:after {
  width: 100%;
  left: 0;
}
</style>
