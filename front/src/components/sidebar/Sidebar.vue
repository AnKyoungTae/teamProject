<template>
  <div class="sidebar" :style="{ width: sidebarWidth }">
    <span v-if="collapsed" @click="this.$router.push({ path: '/' })">
      <h1 :class="collapsed ? '' : 'logo'">
        <div>와</div>
        <div>푸</div>
      </h1>
      <hr />
      <span @click="toggleSidebar">
        <i class="fas fa-user-circle"></i>
      </span>
    </span>

    <span v-else>
      <!-- 멤버 롤 마다 가는곳 다르게 -->
      <div v-if="!userRole || userRole == 'BUYER'">
        <img
          @click="this.$router.push({ path: '/' })"
          class="logo"
          src="../../assets/logo.png"
        />
      </div>
      <div v-else-if="userRole != null && userRole == 'SELLER'">
        <h2 @click="this.$router.push({ path: '/store' })" class="logo">
          판매자
        </h2>
      </div>
      <div v-else-if="userRole != null && userRole == 'ADMIN'">
        <h2 @click="this.$router.push({ path: '/manageMember' })" class="logo">
          관리자
        </h2>
      </div>
      <!-- 펼쳤을때 -->
    </span>
    <Profile></Profile>
    <Location icon="fas fa-compass" />
    <div v-if="userRole != null && userRole == 'ADMIN'">
      <!-- 관리자로 로그인 했을때 보이는 메뉴들 -->
      <SidebarLink to="/manageMember" icon="fas fa-users">회원관리</SidebarLink>
      <SidebarLink to="/manageStore" icon="fas fa-home">가게관리</SidebarLink>
      <SidebarLink to="/admincoupon" icon="fas fa-ticket-alt"
        >쿠폰관리</SidebarLink
      >
      <SidebarLink to="/eventadd" icon="fas fa-ticket-alt"
        >이벤트등록</SidebarLink
      >
    </div>
    <div v-else-if="userRole != null && userRole == 'SELLER'">
      <!-- 판매자로 로그인 했을때 보이는 메뉴들 -->
      <SidebarLink to="/store" icon="fas fa-store"> 내 가게</SidebarLink>
      <SidebarLink to="/storeOrder" icon="fas fa-sort-amount-down">
        주문표</SidebarLink
      >
      <SidebarLink to="/storeMenus" icon="fas fa-bars"> 가게메뉴 </SidebarLink>
      <SidebarLink to="/storeGraph" icon="fas fa-chart-pie">
        매출관리
      </SidebarLink>
      <SidebarLink to="/eventadd" icon="fas fa-ticket-alt">
        이벤트등록
      </SidebarLink>
    </div>
    <div v-else>
      <SidebarLink to="/akinator" icon="fas fa-robot">아키네이터</SidebarLink>
      <SidebarLink to="/cart" icon="fas fa-shopping-cart">장바구니</SidebarLink>

      <SidebarLink to="/shopCategory" icon="fas fa-utensils"
        >음식/가게</SidebarLink
      >
    </div>
    <SidebarLink
      to="/serviceCenter"
      icon="far fa-comments"
      @click="serviceCenter"
      >고객센터</SidebarLink
    >
    <SidebarLink to="/event" icon="fas fa-utensils">이벤트</SidebarLink>
    <hr />
    <div
      class="orderInfoSearch"
      :class="[collapsed ? 'hiddenOrderSearch' : '']"
      data-bs-toggle="tooltip"
      data-bs-placement="top"
      title="주문번호를 입력하시면 주문조회가 가능합니다"
      @click="toggleFindOrder"
      v-if="!userRole || userRole == 'BUYER'"
    >
      주문조회
    </div>
    <div
      class="findOrderWrapper"
      :class="[
        collapsed ? 'hiddenOrderSearch' : '',
        findOrder ? 'hiddenFindOrderTab' : '',
      ]"
      v-if="!userRole || userRole == 'BUYER'"
    >
      <span><b>주문번호</b>를 입력해주세요!</span>
      <hr />
      <div class="form-floating mb-3">
        <input
          type="text"
          class="form-control-sm"
          id="floatingInput"
          placeholder="주문번호"
          v-model="inputOrderNumber"
        />
        <div class="btn btn-warning btn-sm m-1" @click="checkOrderInfo">
          찾기
        </div>
      </div>
    </div>
    <span class="burger" @click="toggleSidebar">
      <BurgerButton />
    </span>
  </div>
</template>

<script>
import { collapsed, toggleSidebar, sidebarWidth } from "./state";
import SidebarLink from "./SidebarLink.vue";
import Profile from "./Profile.vue";
import Location from "./Location.vue";
import BurgerButton from "./Burger.vue";
import { mapGetters, mapMutations } from "vuex";

export default {
  props: {},
  components: { SidebarLink, BurgerButton, Profile, Location },
  setup() {
    return { collapsed, toggleSidebar, sidebarWidth };
  },
  computed: {
    ...mapGetters({ userRole: "auth/getUserRole" }),
  },
  methods: {
    serviceCenter() {
      //serviceCenter 토글 true
      this.$store.commit("SET_serviceCenters", 1); //serviceCenter 안 버튼 상태
      this.$store.commit("SET_serviceCenterToggle", false); //sidebar에서 serviceCenter 클릭시
    },
    toggleFindOrder() {
      this.findOrder = !this.findOrder;
    },
    checkOrderInfo() {
      if (!this.inputOrderNumber) {
        alert("주문번호를 입력해 주세요");
        this.toggleFindOrder();
        return;
      }
      this.$router.push({
        path: "/orderInfo",
        query: { orderId: this.inputOrderNumber },
      });
      this.inputOrderNumber = null;
      this.toggleFindOrder();
    },
  },
  data() {
    return {
      findOrder: true,
      inputOrderNumber: null,
    };
  },
};
</script>

<style>
@import url("https://fonts.googleapis.com/css2?family=Lobster+Two:ital,wght@1,700&display=swap");
/* 기본 테마 정의 - 참고: https://blog.thereis.xyz/136 */
:root {
  --sidebar-bg-color: #355f6e;
  --sidebar-item-hover: #91afba;
  --sidebar-item-active: #ffda77;
}
</style>

<style scoped>
.sidebar {
  color: white;
  background-color: var(--sidebar-bg-color);

  float: left;
  position: fixed;
  z-index: 1;
  top: 0;
  left: 0;
  bottom: 0;
  padding: 0.5em;

  transition: 0.3s ease;

  display: flex;
  flex-direction: column;
  box-shadow: 4px 4px 4px 4px rgba(190, 190, 190, 0.6);
}

.burger {
  position: absolute;
  bottom: 0;
  right: 0.2em;
  padding: 0.75em;

  color: rgb(255, 255, 255);
}

.logo {
  cursor: pointer;
  user-select: none;
  width: 200px;
  background: #355f6e;
}
.orderInfoSearch {
  background: #c3a28e;
  position: fixed;
  display: inline-block;
  bottom: 2%;
  left: 2%;
  color: #755b4a;
  padding: 0px 10px;
  border: 1px solid gray;
  box-shadow: 0px 1px 1px 1px (0, 0, 0, 0.3);
  border-radius: 8px;
  font-size: 16px;
  font-weight: bolder;
  user-select: none;
  transition: 0.4s;
  opacity: 0.6;
}
.orderInfoSearch:hover {
  cursor: pointer;
  background: #755b4a;
  color: #ffe4d4;
  border: 1px solid #ffe4d4;
  box-shadow: 0px 2px 2px 1px (0, 0, 0, 0.7);
  opacity: 1;
}

.findOrderWrapper {
  background: #355f6e;
  position: fixed;
  left: 4px;
  bottom: 8%;
  width: 210px;
  height: 120px;
  border-radius: 10px;
  color: #fd3a69;
  padding: 10px;
  border: 1px solid gray;
  box-shadow: 0px 2px 2px 1px (0, 0, 0, 0.7);
  transition: 0.4s;
  user-select: none;
}
.findOrderWrapper > span {
  background: #ffda77;
  padding: 5px;
  border-radius: 10px;
}
.hiddenOrderSearch {
  transition: width 1s ease;
  opacity: 0;
  visibility: hidden;
  width: 0px;
  overflow: hidden;
}
.hiddenFindOrderTab {
  opacity: 0;
  transition: height 0.4s ease;
  height: 0px;
}
</style>
