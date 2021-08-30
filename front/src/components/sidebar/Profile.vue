<template>
  <transition name="fade">
    <div class="card" id="profile" v-if="!collapsed">
      <notifications
        group="notifyApp"
        position="bottom right"
        style="margin-right: 30vh"
      />
      <!-- 프로필 -->
      <div v-if="getNickname != 'unknown'">
        <div style="text-align: center">
          <div class="user m-2">
            <img
              src="https://mblogthumb-phinf.pstatic.net/20140606_111/sjinwon2_1402052862659ofnU1_PNG/130917_224626.png?type=w2"
              class="profilePic"
            />
          </div>
          {{ getNickname }}
          <hr />
        </div>
        <div class="login">
          <router-link to="/myPage" class="col-12">
            <button type="button" class="btn btn-outline-success col-12">
              마이페이지
            </button>
          </router-link>
          <div
            type="button"
            class="btn btn-outline-danger d-block col-12 mt-1"
            @click="this.$store.dispatch('auth/logout')"
          >
            로그아웃
          </div>
        </div>
      </div>
      <div v-else>
        <div class="p-2">보다 다양한 WAPOO를 즐기려면 로그인하세요!</div>
        <hr />
        <div
          type="button"
          class="btn btn-success d-inline col-12"
          @click="SET_MODAL_LOGIN(true)"
        >
          로그인
        </div>
      </div>
    </div>
  </transition>
  <LoginModal v-if="this.$store.state.loginModal == true" />
  <RegisterModal v-if="this.$store.state.registerModal == true" />
</template>

<script>
import { collapsed } from "./state";
import { mapGetters, mapMutations } from "vuex";
import LoginModal from "@/components/modal/Login.vue";
import RegisterModal from "@/components/modal/Register.vue";
import { success } from "@/api/notification";

export default {
  props: {},
  setup() {
    return { collapsed };
  },
  computed: {
    ...mapGetters({
      getNickname: "auth/getUserNickname",
    }),
  },
  methods: {
    ...mapMutations(["SET_MODAL_LOGIN", "SET_MODAL_REGISTER"]),
    logout() {
      this.$store.dispatch("auth/logout");
      setTimeout(() => {
        this.$router.push({ path: "/" });
      }, 500);
      success("성공적으로 로그아웃 하였습니다!", this);
    },
  },
  components: {
    LoginModal,
    RegisterModal,
  },
};
</script>

<style scoped>
.card {
  color: black;
}
#profile {
  display: flex;

  position: relative;
  font-weight: 400;
  user-select: none;

  margin: 1em 0;
  padding: 0.4em;
  border-radius: 0.25em;
  width: 200px;

  background-color: white;
  text-decoration: none;
}
.login {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
}
.noLogin {
  display: flex;
  flex-direction: column;
  justify-content: space-around;
  align-items: center;
  height: 150px;
}
.fade-enter-active,
.fade-leave-active {
  transition: 0.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}
.profilePic {
  width: 100%;
  height: 155px;
  border-radius: 50%;
  padding: 5px;
  margin-bottom: 10px;
}
.user {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
</style>
