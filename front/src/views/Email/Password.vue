<template>
  <div class="main">
    <div v-if="status == 'id'">
      <div class="manage-div">
        <div class="manage-div2">
          <h3 class="title">아이디 찾기</h3>
          <div class="Login-manage">
            <div class="manage-box">
              <div>
                <strong><span class="login-txt">이름 입력</span></strong>
                <a class="Login-Abox">
                  <strong class="login-tit"><input type="text" class="text" v-model="name" placeholder="이름" ></strong>
                </a>
              </div>
              <div style="margin-top: 30px;">
                <strong><span class="login-txt">이메일 입력</span></strong>
                <a class="Login-Abox">
                  <strong class="login-tit"><input type="text" class="text" v-model="email" placeholder="이메일"></strong>
                </a>
              </div>
              <p style="margin-top: 8px;">
                <span style="cursor: pointer" @click="changeStatusId">아이디</span> &nbsp;|&nbsp; <span style="cursor: pointer" @click="changeStatusPwd">비밀번호</span>
              </p>
            </div>
          </div>
        </div>
      </div>

      <button @click="sendId" class="btn btn-outline-info send-button">보내기</button>
    </div>
    <div v-else>
      <div class="manage-div">
        <div class="manage-div2">
          <h3 class="title">비밀번호 찾기</h3>
          <div class="Login-manage">
            <div class="manage-box">
              <div>
                <strong><span class="login-txt">이름 입력</span></strong>
                <a class="Login-Abox">
                  <strong class="login-tit"><input type="text" class="text" v-model="loginId" placeholder="이름"></strong>
                </a>
              </div>
              <div style="margin-top: 30px;">
                <strong><span class="login-txt">아이디 입력</span></strong>
                <a class="Login-Abox">
                  <strong class="login-tit"><input type="text" class="text" v-model="name" placeholder="아이디"></strong>
                </a>
              </div>
              <div style="margin-top: 30px;">
                <strong><span class="login-txt">이메일 입력</span></strong>
                <a class="Login-Abox">
                  <strong class="login-tit"><input type="text" class="text" v-model="email" placeholder="이메일"></strong>
                </a>
              </div>
              <p style="margin-top: 8px;">
                <span style="cursor: pointer" @click="changeStatusId">아이디</span> &nbsp;|&nbsp; <span style="cursor: pointer" @click="changeStatusPwd">비밀번호</span>
              </p>
            </div>
          </div>
        </div>
      </div>

      <button @click="sendEmail" class="btn btn-outline-info send-button">보내기</button>
    </div>
    
  </div>
  
</template>

<script>
import http from "@/api/http";

export default {
  data() {
    return {
      loginId: '',
      name: '',
      email: '',


      status: "id"
    }
  },
  methods: {
    changeStatusId(){
      this.loginId = ""
      this.name = ""
      this.email=""
      return this.status = "id"
    },
    changeStatusPwd() {
      this.loginId = ""
      this.name = ""
      this.email=""
      return this.status = "pwd"
    },
    //비밀번호
    sendEmail() {
      if(this.loginId == ""){
        return alert("아이디를 입력하세요.")
      }else if(this.name == ""){
        return alert("이름을 입력하세요.")
      }else if(this.email == ""){
        return alert("이메일을 입력하세요.")
      }



      let params = {
        loginId: this.loginId,
        name: this.name,
        email: this.email
      }

      http.post("/Login/findpwd", {params})
      .then(res => {
        if(res.data == "ok"){
          alert("이메일이 전송되었습니다.")
          location.href="/"
        }else if(res.data == "id"){
          alert("아이디가 알맞지 않습니다.")
        }else if(res.data == "email"){
          alert("이름 또는 이메일이 알맞지 않습니다.")
        }else{
          alert("이메일 전송 실패")
        }
      })

    },
    //아이디
    sendId() {
      if(this.name == ""){
        return alert("이름을 입력하세요.")
      }else if(this.email == ""){
        return alert("이메일을 입력하세요.")
      }

      let params = {
        name: this.name,
        email: this.email
      }

      http.post("/Login/findid", {params})
      .then(res => {
        console.log(res.data)
        if(res.data == "ok"){
          alert("이메일이 전송되었습니다.")
          location.href="/"
        }else if(res.data != "no"){
          alert(  "이름 또는 이메일이 알맞지 않습니다.")
        }else{
          alert("이메일 전송 실패")
        }
      })


    },
    
  }
}
</script>

<style scoped>
.manage-div {
  max-width: 560px;
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
  width: 400px;
}
.Login-Abox {
  margin-top: 10px;
  text-decoration: none;
}
.Login-Aboxdiv {
  float: left;
  width: 80px;
  height: 80px;
  padding: auto 0;
  position: relative;
}
.Login-Aboxdiv Abox {
  display: block; 
  width: 80px; 
  height: 80px; 
  border-radius: 50%;
}
.Abox-text {
  overflow: hidden;
  display: block;
  font-size: 22px;
  height: 80px;
  line-height: middle;
  padding-left: 20px;
  padding-bottom: 10px;
}
.login-tit {
  font-size: 20px;
}
.login-txt {
  margin-right: 15px;
  color: #5066aa;
  font-size: 20px;
}
.text {
  border: none;
  border-bottom: 1.5px solid lightgray;
  width: 100%;
  text-align: center;
}
.send-button {
  width: 25%;
}

.main{
  margin-top: 5vh;
}
.main .touch{
  margin: 0 3vw;
}
.title{
  margin: 5vh 0 15px 0;
}
.main .tab{
  margin: 0 auto;
}
.main .text{
  width: 20vw;
  margin: 1vh 0;
}
</style>