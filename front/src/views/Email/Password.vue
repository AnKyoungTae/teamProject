<template>
  <div class="main">
    <div class="manage-div">
      <div class="manage-div2">
        <div class="title">
          <div class="passwordBtn spread-underline" @click="changeStatusId">
            <a :class="[status === 'id' ? 'choicedMenu' : '']">
              <h3>아이디 찾기</h3>
            </a>
          </div>
          <div class="passwordBtn spread-underline" @click="changeStatusPwd">
            <a :class="[status === 'pwd' ? 'choicedMenu' : '']">
              <h3>비밀번호 찾기</h3>
            </a>
          </div>
        </div>
        <div class="Login-manage" v-if="status == 'id'">
          <h3 class="Login-Exp">
            가입시 회원정보로 등록한 이름과 이메일을 입력해 주세요.
          </h3>
          <table class="manage-box">
            <tr>
              <th class="login-txt">이름 입력</th>
              <td class="login-tit">
                <input
                  type="text"
                  class="text"
                  style="width: 350px"
                  v-model="name"
                  placeholder="이름"
                />
              </td>
            </tr>
            <tr>
              <th class="login-txt">이메일 입력</th>
              <td class="login-tit">
                <input
                  type="text"
                  class="text"
                  style="width: 350px"
                  v-model="email"
                  placeholder="이메일"
                />
              </td>
            </tr>
          </table>
        </div>
        <div v-else class="Login-manage">
          <h3 class="Login-Exp">
            가입시 회원정보로 등록한 아이디, 이름과 이메일을 입력해 주세요.
          </h3>
          <table class="manage-box">
            <tr style="margin-top: 30px">
              <th class="login-txt">아이디 입력</th>
              <td class="login-tit">
                <input
                  type="text"
                  class="text"
                  style="width: 350px"
                  v-model="loginId"
                  placeholder="아이디"
                />
              </td>
            </tr>
            <tr>
              <th class="login-txt">이름 입력</th>
              <td class="login-tit">
                <input
                  type="text"
                  class="text"
                  style="width: 350px"
                  v-model="name"
                  placeholder="이름"
                />
              </td>
            </tr>
            <tr style="margin-top: 30px">
              <th class="login-txt">이메일 입력</th>
              <td class="login-tit">
                <input
                  type="text"
                  class="text"
                  style="width: 350px"
                  v-model="email"
                  placeholder="이메일"
                />
              </td>
            </tr>
          </table>
        </div>
      </div>
    </div>
    <div v-if="status == 'id'">
      <button @click="sendId" class="btn btn-warning send-button">
        이메일 인증
      </button>
    </div>
    <div v-else>
      <button @click="sendEmail" class="btn btn-warning send-button">
        이메일 인증
      </button>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
  data() {
    return {
      loginId: "",
      name: "",
      email: "",

      status: "id",
    };
  },
  methods: {
    changeStatusId() {
      this.loginId = "";
      this.name = "";
      this.email = "";
      return (this.status = "id");
    },
    changeStatusPwd() {
      this.loginId = "";
      this.name = "";
      this.email = "";
      return (this.status = "pwd");
    },
    //비밀번호
    sendEmail() {
      if (this.loginId == "") {
        return alert("아이디를 입력하세요.");
      } else if (this.name == "") {
        return alert("이름을 입력하세요.");
      } else if (this.email == "") {
        return alert("이메일을 입력하세요.");
      }

      let params = {
        loginId: this.loginId,
        name: this.name,
        email: this.email,
      };

      http.post("/profile/findpwd", { params }).then((res) => {
        if (res.data == "ok") {
          alert("이메일이 전송되었습니다.");
          location.href = "/";
        } else if (res.data == "id") {
          alert("아이디가 알맞지 않습니다.");
        } else if (res.data == "email") {
          alert("이름 또는 이메일이 알맞지 않습니다.");
        } else {
          alert("이메일 전송 실패");
        }
      });
    },
    //아이디
    sendId() {
      if (this.name == "") {
        return alert("이름을 입력하세요.");
      } else if (this.email == "") {
        return alert("이메일을 입력하세요.");
      }

      let params = {
        name: this.name,
        email: this.email,
      };

      http.post("/profile/findid", { params }).then((res) => {
        console.log(res.data);
        if (res.data == "ok") {
          alert("이메일이 전송되었습니다.");
          location.href = "/";
        } else if (res.data != "no") {
          alert("이름 또는 이메일이 알맞지 않습니다.");
        } else {
          alert("이메일 전송 실패");
        }
      });
    },
  },
};
</script>

<style scoped>
.manage-div {
  max-width: 700px;
  margin: 50px auto 0;
  display: block;
}
.manage-div2 {
  display: block;
}
.manage-text {
  font-weight: normal;
  font-size: 20px;
}
.Login-manage {
  margin-top: 10px;
  border-top: 1px solid #7c7c7c;
}
.manage-box {
  margin: 40px auto;
  text-align: center;
  width: 500px;
}
.Login-Exp {
  padding-top: 30px;
}
.login-tit {
  font-size: 25px;
}
.login-txt {
  padding-left: 20px;
  color: #5066aa;
  font-size: 25px;
}
.text {
  border: none;
  border-bottom: 1.5px solid lightgray;
  text-align: center;
}
.text:focus {
  outline: none;
}
.send-button {
  font-size: 25px;
  width: 150px;
}

.main {
  margin-top: 5vh;
}
.main .touch {
  margin: 0 3vw;
}
.title {
  margin: 5vh 0 15px 0;
  display: flex;
  justify-content: space-around;
}
.main .tab {
  margin: 0 auto;
}
.main .text {
  width: 20vw;
  margin: 1vh 0;
}
.passwordBtn {
  float: left;
  width: 250px;
  height: 60px;
  line-height: 40px;
  text-align: center;
  border: 2px solid #fd3a69;
  overflow: hidden;
  cursor: pointer;
  font-size: 1.5em;
  border-radius: 10px 10px 10px 0;
  background: #ffda77;
}
.passwordBtn:hover {
  background: #91afba;
  color: #fff;
  cursor: pointer;
}
.choicedMenu {
  text-decoration: underline;
  text-underline-position: under;
  color: #fd3a69;
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
  background: #ffefa3;
  transition: width 0.3s ease 0s, left 0.3s ease 0s;
  width: 0;
}
.spread-underline:hover:after {
  width: 100%;
  left: 0;
}
a {
  text-decoration: none;
  color: #404040;
}
</style>
