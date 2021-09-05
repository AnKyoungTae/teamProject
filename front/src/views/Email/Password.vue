<template>
  <div class="main">
    <button type="button" class="btn btn-outline-secondary touch" @click="changeStatusId">아이디</button>
    <button type="button" class="btn btn-outline-secondary touch" @click="changeStatusPwd">비밀번호</button>
    <div v-if="status == 'id'">
      <h3 class="title">아이디 찾기</h3>
      <table class="tab">
        
        <tr>
          <td>
            <span>이름 </span>
          </td>
          <td>
            <input type="text" class="text" v-model="name">
          </td>
        </tr>

        <tr>
          <td>
            <span>이메일</span>
          </td>
          <td>
            <input type="text" class="text" v-model="email">
          </td>
        </tr>
        
      </table>

      <button @click="sendId" class="btn btn-outline-info send-button">보내기</button>
    </div>
    <div v-else>
      <h3 class="title">비밀번호 찾기</h3>
      <table class="tab">
        <tr >
          <td>
            <span>아이디</span>
          </td>
          <td>
            <input type="text" class="text" v-model="loginId">
          </td>
        </tr>

        <tr>
          <td>
            <span>이름 </span>
          </td>
          <td>
            <input type="text" class="text" v-model="name">
          </td>
        </tr>

        <tr>
          <td>
            <span>이메일</span>
          </td>
          <td>
            <input type="text" class="text" v-model="email">
          </td>
        </tr>
        
      </table>

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

      http.post("/profile/findpwd", {params})
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

      http.post("/profile/findid", {params})
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
.main{
  margin-top: 5vh;
}
.main .touch{
  margin: 0 3vw;
}
.main .title{
  margin: 5vh 0;
}
.main .tab{
  margin: 0 auto;
}
.main .text{
  width: 20vw;
  height: 3vh;
  margin: 1vh 0;
}
.send-button{
  margin-top: 5vh;
}
</style>