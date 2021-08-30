<template>
  <div class="sidebar" :style="{ width: sidebarWidth }">
    <span v-if="collapsed" @click="this.$router.push({ path: '/' })">
      <h1 class="logo">
        <div>와</div>
        <div>푸</div>
      </h1>
      <span @click="toggleSidebar">
        <i class="fas fa-user-circle"></i>
      </span>
    </span>

    <span v-else>
      <!-- 멤버 롤 마다 가는곳 다르게 -->
      <h2 @click="this.$router.push({ path: '/' })" class="logo">WAPOO</h2>
      <!-- 펼쳤을때 -->
      <Profile class="profile" />
    </span>
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
      <SidebarLink
        to="/serviceCenter"
        icon="far fa-comments"
        @click="serviceCenter"
        >고객센터</SidebarLink
      >
    </div>
    <div v-else-if="userRole != null && userRole == 'SELLER'">
      <!-- 판매자로 로그인 했을때 보이는 메뉴들 -->
      <SidebarLink to="/store" icon="fas fa-store"> 내 가게</SidebarLink>
      <SidebarLink
        to="/storeMenus"
        icon="fas fa-bars"
        v-if="userRole == 'SELLER'"
      >
        가게메뉴</SidebarLink
      >
    </div>
    <div v-else>
      <SidebarLink to="/#" icon="fas fa-robot">아키네이터</SidebarLink>
      <SidebarLink to="/" icon="fas fa-home">홈페이지</SidebarLink>
      <SidebarLink to="/cart" icon="fas fa-shopping-cart">장바구니</SidebarLink>
      <SidebarLink
        to="/serviceCenter"
        icon="far fa-comments"
        @click="serviceCenter"
        >고객센터</SidebarLink
      >
      <SidebarLink to="/shops" icon="fas fa-utensils">음식/가게</SidebarLink>
      <SidebarLink to="/event" icon="fas fa-utensils">이벤트</SidebarLink>
    </div>
    <SidebarLink to="/test" icon="fas fa-question">Test</SidebarLink>
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
    ...mapMutations([
      //serviceCenter 토글
      "SET_COUNT_NOTICE",
      "SET_COUNT_QUEAN",
      "SET_COUNT_FQA",
    ]),
    serviceCenter() {
      //serviceCenter 토글 true
      this.SET_COUNT_NOTICE(true);
      this.SET_COUNT_QUEAN(true);
      this.SET_COUNT_FQA(true);
      this.$store.commit("SET_serviceCenters", 0);
    },
  },
};
</script>

<style>
/* 기본 테마 정의 - 참고: https://blog.thereis.xyz/136 */
:root {
  --sidebar-bg-color: #2f855a;
  --sidebar-item-hover: #38a169;
  --sidebar-item-active: #276749;
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
}
.profile {
  transition: 0.5s ease;
}

.burger {
  position: absolute;
  bottom: 0;
  right: 0.2em;
  padding: 0.75em;

  color: gba(255, 255, 255);
}

.logo {
  cursor: pointer;
}
</style>
