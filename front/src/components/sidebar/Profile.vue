<template>
  <div
    class="card"
    id="profile"
    :class="[collapsed ? 'isCollap' : '']"
    v-if="!collapsed"
  >
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <!-- 프로필 -->
    <div v-if="getNickname != 'unknown'">
      <!-- 로그인 상태일 때 -->
      <div style="text-align: center">
        <div class="user m-2">
          <img
            :src="profilePicUrl"
            title="마이페이지로 이동하기"
            class="profilePic"
            @click="toMyPage"
          />
        </div>
        <p class="nicknameP">
          {{ getNickname }}
        </p>
        <hr />
      </div>
      <div class="login">
        <router-link to="/myPage" class="col-12">
          <button type="button" class="btn btn-sm col-12 myPageBtn">
            마이페이지
          </button>
        </router-link>
        <div
          type="button"
          class="btn btn-sm d-block col-12 mt-1 logoutBtn"
          @click="logout"
        >
          로그아웃
        </div>
      </div>
    </div>
    <!-- 로그인 상태가 아닐 때 -->
    <div v-else>
      <div class="p-2">
        <span class="profileInfoA mb-1"> 보다 다양한 WAPOO를 즐기려면 </span>
        <div
          type="button"
          class="btn btn-sm d-inline col-6 m-1 loginBtn mt-2"
          @click="SET_MODAL_LOGIN(true)"
        >
          로그인
        </div>
      </div>
    </div>
  </div>
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
    this.loadUserPic();
  },
  computed: {
    ...mapGetters({
      getNickname: "auth/getUserNickname",
      getUserId: "auth/getUserId",
    }),
    profilePicUrl() {
      if (this.loadedProfilePic == null || this.loadedProfilePic == "") {
        return "https://mblogthumb-phinf.pstatic.net/20140606_111/sjinwon2_1402052862659ofnU1_PNG/130917_224626.png?type=w2";
      } else {
        return this.loadedProfilePic;
      }
    },
  },
  updated() {
    if (this.loadedProfilePic == null || this.loadedProfilePic == "") {
      this.loadUserPic();
    }
  },
  methods: {
    ...mapMutations(["SET_MODAL_LOGIN", "SET_MODAL_REGISTER"]),
    logout() {
      this.$store.dispatch("auth/logout");
      setTimeout(() => {
        success("성공적으로 로그아웃 하였습니다!", this);
        this.loadedProfilePic = null;
        this.$router.push({ path: "/" });
      }, 500);
    },
    checkOrderInfo() {
      this.$router.push({
        path: "/orderInfo",
        query: { orderId: this.inputOrderNumber },
      });
      this.inputOrderNumber = null;
    },
    toMyPage() {
      this.$router.push({ path: "/mypage" });
    },
    loadUserPic() {
      if (this.getUserId != 0 && this.getUserId != null) {
        http
          .get("/profile/getMemberProfilePicture", {
            params: {
              memberId: this.getUserId,
            },
          })
          .then((res) => {
            this.loadedProfilePic = res.data;
          });
      }
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

  margin-top: 1em;
  margin-bottom: 0.5em;
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
.profileInfoA {
  font-size: 14px;
  text-underline-position: under;
  text-decoration-line: underline;
}
.loginBtn {
  background: #ffda77;
  border: 1px solid #fd3a69;
  border-radius: 15px;
  color: #fd3a69;
}
.loginBtn:hover {
  color: white;
  background: orange;
}
.logoutBtn {
  background: #ffda77;
  color: #fd3a69;
  border: 1px solid #fd3a69;
  border-radius: 15px 15px 0px 15px;
}
.logoutBtn:hover {
  color: white;
  background: orange;
}
.myPageBtn {
  background: #ffda77;
  color: #fd3a69;
  border: 1px solid #fd3a69;
  border-radius: 15px 15px 0px 15px;
}
.myPageBtn:hover {
  color: white;
  background: orange;
}
.nicknameP {
  font-weight: bold;
}
</style>
