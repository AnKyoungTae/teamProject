<template>
  <transition name="fade">
    <div
      class="card"
      id="profile"
      v-if="!collapsed"
      :class="[collapsed ? 'isCollap' : '']"
    >
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
              :src="profilePicUrl"
              :title="profilePicUrl"
              class="profilePic"
              @click="test"
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
        <a
          class="searchOrder"
          data-bs-toggle="collapse"
          data-bs-target="#collapseExample"
          aria-expanded="false"
          aria-controls="collapseExample"
          >주문조회..</a
        >
        <div class="collapse mt-2" id="collapseExample">
          <div class="card card-body">
            <p class="card-text">주문번호를 입력하세요</p>
            <div class="input-group mb-1">
              <input
                type="text"
                class="form-control"
                placeholder="주문번호"
                v-model="inputOrderNumber"
                aria-label="Recipient's username"
                aria-describedby="button-addon2"
              />
              <button
                class="btn btn-outline-secondary"
                type="button"
                id="button-addon2"
                @click="checkOrderInfo"
              >
                조회
              </button>
            </div>
          </div>
        </div>
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
import http from "@/api/http";

export default {
  props: {},
  setup() {
    return { collapsed };
  },
  data() {
    return {
      loadedProfilePic: null,
      inputOrderNumber: null,
    };
  },
  mounted() {
    console.log("불러온 userId" + this.getUserId);
    if (this.getUserId != 0 && this.getUserId != null) {
      http
        .get("/profile/getMemberProfilePicture", {
          params: {
            memberId: this.getUserId,
          },
        })
        .then((res) => {
          this.loadedProfilePic = res.data;
          console.log("불러온 프로필 파일 URL" + res.data);
        });
    }
  },
  computed: {
    ...mapGetters({
      getNickname: "auth/getUserNickname",
      getUserId: "auth/getUserId",
    }),
    profilePicUrl() {
      if (this.loadedProfilePic != null) {
        return this.loadedProfilePic;
      } else {
        return "https://mblogthumb-phinf.pstatic.net/20140606_111/sjinwon2_1402052862659ofnU1_PNG/130917_224626.png?type=w2";
      }
    },
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
    checkOrderInfo() {
      this.$router.push({
        path: "/orderInfo",
        query: { orderId: this.inputOrderNumber },
      });
      this.inputOrderNumber = null;
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
.isCollap {
  width: 0px;
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
  transition: all ease 0.2s 0s;
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
.searchOrder {
  color: grey;
}
.searchOrder:hover {
  color: blue;
  cursor: pointer;
  text-decoration-line: underline;
  text-underline-position: under;
}
</style>
