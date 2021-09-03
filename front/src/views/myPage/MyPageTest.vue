<template>
    <div class="wrap_head" style="border-bottom: 1px solid lightgray;">
        <nav class="head_span">
            <ul class="head_list">
                <li class="list_li list1">
                    <a class="list_a" @click="changeView('myProfile')">내정보</a>
                </li>
                <li class="list_li">
                    <a class="list_a" @click="changeView('myCoupon')">쿠폰</a>
                </li>
                <li class="list_li">
                    <a class="list_a" @click="changeView('myOrderHistory')">주문내역</a>
                </li>
                <li class="list_li">
                    <a class="list_a" @click="changeView('myReview')">리뷰관리</a>
                </li>
                <li class="list_li">
                    <a class="list_a" @click="changeView('myQuestion')">질문관리</a>
                </li>
            </ul>
        </nav>
    </div>
    <div class="wrap_body">
        <my-profile v-if="currentView == 'myProfile'" />
        <my-coupon v-if="currentView == 'myCoupon'" />
        <my-order-history v-if="currentView == 'myOrderHistory'" />
        <my-review v-if="currentView == 'myReview'" />
        <my-Question  v-if="currentView == 'myQuestion'" @updateView = "changeView" />
    </div>
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
      console.log("넘어옴");
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

<style scoped>
#profileMenu {
  margin: 50px;
  width: 1000px;
  height: 80px;
}
#profileMenu ul li {
  float: left;
  width: 200px;
  height: 80px;
  line-height: 80px;
  text-align: center;
  border: 1px solid black;
  overflow: hidden;
  cursor: pointer;
}
#profileMenu ul li a {
  display: block;
}
#profileMenu ul li a:hover {
  background: gray;
  color: #fff;
}
a {
  text-decoration: none;
  color: #404040;
}
ul {
  padding: 0px;
}
li {
  list-style: none;
}
.side-row {
  border-bottom: 1px solid black;
}
.item {
  padding: 20px;
}


.wrap_head {
    position: relative;
    width: 1200px;
    margin: 0 auto;
}
.wrap_body {
    width: 1200px;
    height: 100px;
}
.head_span {
    width: 600px;
    height: 58px;
    margin: 0 auto;
}
.head_list {
    display: inline-block;
    overflow: hidden;
    height: 58px;
}
.list1 {
    margin-left: 0;
}
.list_li {
    float: left;
    margin-left: 35px;
}
.list_a {
    display: block;
    height: 39px;
    padding: 19px 5px 0;
    letter-spacing: 0;
    cursor: pointer;
}
</style>
