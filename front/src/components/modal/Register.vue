<template>
  <Modal>
    <template v-slot:header>
      <div class="register-header">
        <span v-if="selectRegister === 'BUYER'">회원가입(일반 회원)</span>
        <span v-else>회원가입(판매자)</span>
      </div>
      <div class="close-button" @click="backTologin">
        <img src="@/assets/close.png" />
      </div>
    </template>

    <hr />
    <template v-slot:body>
      <notifications
        group="notifyApp"
        position="right right"
        style="margin-right: 30vh"
      />

      <div class="register-information-container">
        <div class="register-information-wrapper">
          <div class="register-title">로그인아이디</div>
          <div class="register-input-wrapper">
            <input
              v-model="loginId"
              class="register-input"
              placeholder="아이디를 입력해주세요"
              @blur="checkLoginId"
              :class="{ invalid: !availableLoginId }"
            />
          </div>
        </div>
        <div class="register-information-wrapper">
          <div class="register-title">비밀번호</div>
          <div class="register-input-wrapper">
            <input
              v-model="password"
              class="register-input"
              type="password"
              placeholder="비밀번호를 입력해주세요"
            />
          </div>
        </div>
        <div class="register-information-wrapper">
          <div class="register-title">비밀번호확인</div>
          <div class="register-input-wrapper">
            <input
              v-model="passwordCheck"
              class="register-input"
              type="password"
              placeholder="비밀번호를 다시 입력해주세요"
            />
          </div>
        </div>

        <div class="register-information-wrapper">
          <div class="register-title">이름</div>
          <div class="register-input-wrapper">
            <input
              v-model="name"
              class="register-input"
              placeholder="이름 입력해주세요"
            />
          </div>
        </div>

        <notifications group="validation" position="bottom center" />

        <div class="register-information-wrapper">
          <div class="register-title">닉네임</div>
          <div class="register-input-wrapper">
            <input
              v-model="nickname"
              class="register-input"
              placeholder="닉네임을 입력해주세요"
              @blur="checkNickname"
              :class="{ invalid: !availableNickname }"
            />
          </div>
        </div>
        <div class="register-information-wrapper">
          <div class="register-title">휴대폰 번호</div>
          <div class="register-input-wrapper">
            <input
              type="text"
              v-model="phone"
              class="register-input register-input-phone"
              maxlength="11"
              placeholder="휴대폰 번호를 입력해주세요"
              @blur="checkPhone"
              :class="{ invalid: !availablePhone }"
            />
          </div>
        </div>

        <div class="register-information-wrapper">
          <div class="register-title">이메일(아이디)</div>
          <div class="register-input-wrapper">
            <input
              v-model="email"
              class="register-input register-email"
              @blur="checkEmail"
              :class="{ invalid: !availableEmail }"
            />
            <span class="space">@</span>
            <input
              v-if="selectEtc"
              class="register-email-domain-input"
              placeholder="이메일"
            />
            <select
              v-model="selectedDomain"
              class="register-input register-email-domain"
            >
              <option value disabled selected>선택해주세요</option>
              <option>{{ emailDomain.naver }}</option>
              <option>{{ emailDomain.google }}</option>
              <option>{{ emailDomain.daum }}</option>
              <option>{{ emailDomain.hanmail }}</option>
              <option>{{ emailDomain.etc }}</option>
            </select>
          </div>
        </div>
      </div>
    </template>

    <template v-slot:footer>
      <div
        v-if="selectRegister === 'BUYER'"
        @click="registerUser"
        class="register-button-buyer"
      >
        회원가입(일반 회원)
      </div>
      <div v-else @click="registerUser" class="register-button-seller">
        회원가입(판매자)
      </div>
    </template>
  </Modal>
</template>

<script>
import { mapMutations, mapState } from "vuex";

import { error } from "@/api/notification";
import Modal from "@/components/modal/Modal_Register";
import http from "@/api/http";

export default {
  components: {
    Modal,
  },
  data() {
    return {
      // loginId, pwd, name, phone, email, nickname
      loginId: "",
      password: "",
      passwordCheck: "",
      name: "",
      nickname: "",
      selectedDomain: "",
      email: "",
      phone: "",
      emailDomain: {
        naver: "naver.com",
        google: "gmail.com",
        daum: "daum.net",
        hanmail: "hanmail.com",
        etc: "직접입력",
      },
      availableLoginId: true,
      availableEmail: true,
      availableNickname: true,
      availablePhone: true,
    };
  },
  computed: {
    ...mapState(["selectRegister"]),
    selectEtc() {
      return this.selectedDomain === "직접입력" ? true : false;
    },
  },
  //Observer패턴(감시), 숫자가 입력되지 않도록 한다
  watch: {
    phone() {
      return (this.phone = this.phone.replace(/[^0-9]/g, ""));
    },
  },
  methods: {
    ...mapMutations(["SET_MODAL_REGISTER", "SET_MODAL_LOGIN"]),
    backTologin() {
      this.SET_MODAL_REGISTER(false);
      this.SET_MODAL_LOGIN(true);
    },
    registerUser() {
      //최소 1개의 숫자혹은 특수문자를 포함해야 함

      const passwordCheckReg = /^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{6,20}$/;
      let passwordValidation = passwordCheckReg.test(this.password);
      if (this.loginId === "" || this.selectedDomain === "") {
        error("아이디가 공백입니다. 다시 입력해주세요", this);

        return;
      } else if (this.password !== this.passwordCheck) {
        error("비밀번호가 일치하지 않습니다. 다시 입력해주세요", this);
        //비밀번호 초기화
        this.password = this.passwordCheck = "";
        return;
      } else if (this.password === "" || this.passwordCheck === "") {
        error("비밀번호를 입력해주세요", this);
        //비밀번호 초기화
        this.password = this.passwordCheck = "";
        return;
      } else if (this.name === "") {
        error("이름을 입력해주세요", this);
        return;
        //휴대폰번호 비어있을때
      } else if (this.age === 0) {
        error("나이를 입력해주세요", this);
        return;
      } else if (this.nickname === "") {
        error("닉네임을 입력해주세요", this);
        return;
      } else if (this.phone === "") {
        error("휴대폰 번호를 모두 입력해주세요", this);
        return;
      } else if (passwordValidation === false) {
        error(
          "비밀번호는 최소 1개의 숫자 및 문자 그리고 특수문자를 포함해야합니다.",
          this
        );
        return;
      }
      this.registerRequest();
    },
    registerRequest() {
      const emailAddress = this.email + "@" + this.selectedDomain;
      let userData = {
        loginId: this.loginId,
        password: this.password,
        name: this.name,
        phone: this.phone,
        email: emailAddress,
        nickname: this.nickname,
        role: this.$store.state.selectRegister,
      };
      //등록 로직
      console.log(userData);
      if (
        !this.availableLoginId ||
        !this.availableEmail ||
        !this.availableNickname ||
        !this.availablePhone
      ) {
        this.$notify({
          group: "validation",
          type: "error",
          duration: 1000,
          title: "중복오류",
          text: "중복된 정보가 있습니다. 다시 확인해주세요.",
        });
        return;
      }
      // 중복이 모두 제거된상태에서 진행
      http
        .post("/api/member/register", userData)
        .then((response) => {
          if (response.status == 200) {
            // 회원가입이 성공적으로 진행되었을 경우,
            console.log(response.data);
            if (response.data == true) {
              alert("회원가입이 완료되었습니다. 로그인 하세요");
            }
          }
        })
        .catch((error) => {
          console.log(error);
        });
      setTimeout(this.SET_MODAL_REGISTER(false), 2000);
    },
    // 아이디 중복체크
    async checkLoginId() {
      let param = this.loginId;
      console.log("보내는 아이디 -> " + param);
      let url = "/api/validation/loginId?value=" + param;
      const response = await http.get(url);
      if (response.data == false) {
        this.$notify({
          group: "validation",
          type: "error",
          duration: 1000,
          title: "중복오류",
          text: "중복된 아이디입니다.",
        });
        this.availableLoginId = false;
      } else {
        this.availableLoginId = true;
      }
    },
    // 이메일 중복체크
    async checkEmail() {
      let param = this.email + "@" + this.selectedDomain;
      console.log("보내는 이메일 -> " + param);
      let url = "/api/validation/email?value=" + param;
      const response = await http.get(url);
      if (response.data == false) {
        this.$notify({
          group: "validation",
          type: "error",
          duration: 1000,
          title: "중복오류",
          text: "중복된 이메일입니다.",
        });
        this.availableEmail = false;
      } else {
        this.availableEmail = true;
      }
    },
    // 닉네임 중복체크
    async checkNickname() {
      let param = this.nickname;
      console.log("보내는 닉네임 -> " + param);
      let url = "/api/validation/nickname?value=" + param;
      const response = await http.get(url);
      if (response.data == false) {
        this.$notify({
          group: "validation",
          type: "error",
          duration: 1000,
          title: "중복오류",
          text: "중복된 닉네임입니다.",
        });
        this.availableNickname = false;
      } else {
        this.availableNickname = true;
      }
    },
    // 폰 중복체크
    async checkPhone() {
      let param = this.phone;
      console.log("보내는 닉네임 -> " + param);
      let url = "/api/validation/phone?value=" + param;
      const response = await http.get(url);
      if (response.data == false) {
        this.$notify({
          group: "validation",
          type: "error",
          duration: 1000,
          title: "중복오류",
          text: "사용할 수 없는 번호입니다.",
        });
        this.availablePhone = false;
      } else {
        this.availablePhone = true;
      }
    },
  },
};
</script>

<style scoped>
* {
  color: black;
}

.close-button {
  padding-bottom: 30px;
}

.close-button > img {
  cursor: pointer;
  width: 20px;
  height: auto;
}

.register-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  border: none;
  background: transparent;
  text-align: left;
  margin-bottom: 20px;
  padding-bottom: 5px;
  font-size: 22px;
}
.register-information-container {
  border-top: 1px solid rgba(210, 210, 210);
  padding-top: 5px;
}

.register-information-wrapper {
  display: flex;
  margin-top: 20px;
  align-items: center;
}
.register-title {
  display: flex;
  align-items: center;
  width: 120px;
  height: 46px;
  font-size: 14px;
  /* line-height: 46px; */
  color: #222222;
}
.space {
  margin: 0 7px;
  font-size: 14px;
  color: #888888;
}

.space-age {
  width: 80px;
  margin: 0 7px;
  font-size: 14px;
  color: #888888;
}

.register-input-wrapper {
  margin-left: 10px;
  display: flex;
  align-items: center;
  width: 100%;
}
.register-input {
  width: 100%;
  height: 46px;
  padding: 4px 8px;
  text-align: center;
}

.register-email {
  width: 45%;
}

select {
  width: 200px;
  padding: 0.8em 0.5em;
  border: 1px solid #999;
  font-family: inherit;
  background: url("../../assets/arrow.jpeg") no-repeat 95% 50%;
  background-size: 25px;
  border-radius: 0px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

select::-ms-expand {
  display: none;
}

.register-email-domain-input {
  width: 60%;
  height: 46px;
  margin-right: 15px;
  padding: 4px 8px;
}

.register-input-phone {
  text-align: center;
}

.register-button-buyer {
  width: 100%;
  height: 70px;
  background-color: #ffda77;
  color: #fd3a69;
  font-size: 17px;
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border: 1px solid #fd3a69;
  border-radius: 10px;
}
.register-button-seller {
  width: 100%;
  height: 70px;
  background-color: #ffda77;
  color: black;
  font-size: 17px;
  margin: 0;
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  border: 1px solid #fd3a69;
  border-radius: 10px;
}

.invalid {
  color: red;
}
</style>
