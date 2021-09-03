<template>
  <Modal>
    <template v-slot:header>
      <notifications
        group="notifyApp"
        position="bottom right"
        style="margin-right: 30vh"
      />
      <div class="header-top-wrapper">
        <div class="login-selector">
          <div
            @click="loginSelect"
            class="login-selector-user"
            :class="{ selectedLogin: loginType }"
          >
            Wapoo 회원 로그인
          </div>
        </div>
      </div>
      <div @click="SET_MODAL_REGISTER(false)" class="close-button">
        <img src="@/assets/close.png" />
      </div>
    </template>

    <template v-slot:body>
      <div class="login-input-wrapper">
        <input v-model="userId" class="login-id" placeholder="아이디" />
        <input
          v-model="userPassword"
          type="password"
          class="login-password"
          placeholder="비밀번호"
          @keyup.enter="doLogin"
        />
      </div>
      <div class="login-check-wrapper">
        <label class="login-check checkbox">
          <input @click="setLoginSave" v-model="loginSave" type="checkbox" />
          <span>아이디 저장</span>
        </label>
        <button class="find-password-button">비밀번호 찾기</button>
      </div>
      <div class="login-button-wrapper">
        <div>
          <button v-if="loginType" @click="doLogin" class="login-button">
            로그인
          </button>
          <button v-else @click="doLogin" class="login-button">로그인</button>
        </div>

        <div class="register-wrapper">
          아직 회원이 아니세요?
          <!-- OAUTH연동로직 -->
          <button @click="registerClick('BUYER')" class="register-button">
            회원가입하기
          </button>
        </div>
      </div></template
    >

    <template v-slot:footer
      ><div class="move-seller">
        가게를 등록하고싶으세요?
        <div>
          <button @click="registerClick('SELLER')" class="move-seller-button">
            판매자 회원가입
          </button>
        </div>
      </div>
    </template>
  </Modal>
</template>

<script>
import Modal from "@/components/modal/Modal_login";
import { error, success } from "@/api/notification";
import { mapState, mapGetters, mapActions, mapMutations } from "vuex";
export default {
  components: {
    Modal,
  },
  data() {
    return {
      loginSave: false,
      loginType: true,
      userId: "",
      userPassword: "",
      userInfo: null,
    };
  },
  mounted() {
    this.userId = localStorage.getItem("loginId");
    localStorage.getItem("loginId") === null
      ? (this.loginSave = false)
      : (this.loginSave = true);
  },
  computed: {
    ...mapGetters({
      userRole: "auth/getUserRole",
    }),
    ...mapState(["isAuthenticated"]),
  },
  methods: {
    ...mapMutations(["SET_MODAL_REGISTER", "SET_LOGIN", "SET_SELECT_REGISTER"]),
    ...mapActions({
      login: "auth/login",
    }),
    loginSelect() {
      this.loginType = true;
    },
    sellerlLoginSelect() {
      this.loginType = false;
    },
    doLogin() {
      if (this.userId === "") {
        error("아이디를 입력해주세요", this);
        return;
      } else if (this.userPassword === "") {
        error("비밀번호를 입력해주세요", this);
        return;
      }
      const response = this.login({
        userId: this.userId,
        password: this.userPassword,
      });
      // 받은 정보의 처리
      response
        .then((res) => {
          this.SET_MODAL_REGISTER(false);
          if (this.loginSave == true) {
            localStorage.setItem("loginId", this.userId);
          }
          setTimeout(() => {
            if (this.userRole != null) {
              this.$router.push({ path: "/" });

              if (this.userRole === "SELLER") {
                this.$router.push({ path: "/store" });
              } else if (this.userRole === "ADMIN") {
                this.$router.push({ path: "/admin" });
              }
            }

            // 로그인에 성공은 했으나 역할이 이상할떄 예외처리 해줘야함.
            return;
          }, 2000);
        })
        .catch((err) => {
          console.log("에러?" + err);
          error("서버에 연결할 수 없습니다", this);
        });
    },
    loginseller() {
      this.SET_MODAL_REGISTER(true);
    },
    registerClick(user) {
      //vuex내에 있는 MODAL(팝업창 열어주기)
      this.SET_SELECT_REGISTER(user);
      this.SET_MODAL_REGISTER(true);
      //유저 회원가입인지 판매자 회원가입인지 vuex에 저장
    },
    setLoginSave() {
      this.loginSave = !this.loginSave;
    },
  },
};
</script>
<style scoped>
* {
  color: black;
  z-index: 999;
}
.header-top-wrapper {
  display: flex;
  align-items: center;
  margin-bottom: 10px;
}
.login-selector-user:hover {
  cursor: pointer;
}
.login-selector-seller:hover {
  cursor: pointer;
}

.login-selector-seller {
  margin-left: 20px;
}
.close-button {
  /* margin-left: auto; */
  cursor: pointer;
}
.close-button > img {
  width: 20px;
  height: auto;
}
.login-selector {
  width: 100%;
  display: flex;
  margin-top: 10px;
  margin-bottom: 10px;
  /* margin-left: 60px; */
  justify-content: center;
  padding-left: 20px;
}

.login-selector > div {
  font-size: 14px;
}

.selectedLogin {
  font-weight: bold;
  padding-bottom: 2px;
  border-bottom: 2px solid #1a5ae8;
}
.login-input-wrapper {
  margin: 0 auto;
}
.login-title {
  text-align: center;
  margin-bottom: 20px;
}
.login-id {
  padding: 0 15px;
  width: 100%;
  height: 46px;
}
.login-password {
  margin-top: 8px;
  padding: 0 15px;
  width: 100%;
  height: 46px;
}

.login-check-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin: 18px 0;
  border: 0;
}

.login-check-wrapper > * {
  color: #888888;
  font-size: 14px;
}
.find-password-button {
  cursor: pointer;
  border: 0;
  color: #444444;
}

.login-button {
  cursor: pointer;
  width: 100%;
  height: 60px;
  background-color: #1a5ae8;
  border: 0;
  color: #fff;
}

.register-wrapper {
  text-align: center;
  color: #888888;
  padding: 24px 0 5px 0;
  font-size: 14px;
}

.register-button {
  cursor: pointer;
  margin-left: 6px;
  color: #1564f9;
  border: 0;
}

.move-seller {
  color: #888888;
  text-align: center;
  margin-right: 20%;
  padding-bottom: 10px;
}
.move-seller-button {
  cursor: pointer;
  margin-top: 10px;
  padding: 10px 20px;
  border: 1px solid #888888;
  /* border: 0; */
}

/* 체크박스 스타일링 */
.checkbox {
  display: inline-flex;
  cursor: pointer;
  position: relative;
}

.checkbox > span {
  /* color: #34495e; */
  padding: 0.5rem 0.25rem;
}

.checkbox > input {
  height: 20px;
  width: 20px;
  -webkit-appearance: none;
  -moz-appearance: none;
  -o-appearance: none;
  appearance: none;
  border: 1px solid #34495e;
  border-radius: 4px;
  outline: none;
  transition-duration: 0.3s;
  background-color: #fff;
  cursor: pointer;
}

.checkbox > input:checked {
  border: 1px solid #1564f9;
  background-color: #1564f9;
}

.checkbox > input:checked + span::before {
  content: "\2713";
  font-size: 15px;
  display: block;
  text-align: center;
  color: #fff;
  position: absolute;
  left: 0.35rem;
  top: 0.4rem;
}
.login-check {
  display: flex;
  align-items: center;
}
.checkbox > input:active {
  border: 2px solid #34495e;
}
</style>
