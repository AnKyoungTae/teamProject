<template>
<center>
  <div>
    비밀번호 변경하기
    <br>
    ON<input type="radio" @change="changePwd" name="pwd">
    OFF<input type="radio" @change="changeNotPwd" name="pwd" checked>
    <br>
  </div>
  <div class="container" style="width: 800px; margin-right: 10px">
    <div>
      <div class="row" style="border-top: 1px solid black">
        <div class="col-4 edit-1">이름 :</div>
        <div class="col-8 edit-2">{{name}}</div>
      </div>
      <div class="row">
        <div class="col-4 edit-1">닉네임 :</div>
        <div class="col-8 edit-2">
          <input
            v-model="nickname"
            type="text"
            placeholder="한글,영문,숫자만 2~10자리"
            maxlength="10"
          />
         
          <div v-if="this.nickname === ''" class="msg">닉네임을 입력하세요.</div>
        </div>
      </div>
      <div class="row">
        <div class="col-4 edit-1">아이디 </div>
        <div class="col-8 edit-2">{{loginId}}</div>
      </div>
      
      <div class="row">
        <div class="col-4 edit-1">핸드폰번호 :</div>
        <div class="col-8 edit-2">
          <input
            v-model="tel"
            type="text"
            placeholder="-없이 적어주세요"
            maxlength="11"
     
          />
        
          <div v-if="this.tel ===  '' " class="msg">번화번호를 입력하요</div>
        </div>
      </div>
      <div class="row">
        <div class="col-4 edit-1">이메일 :</div>
        <div class="col-8 edit-2" style="border-bottom: 1px solid black">
          <input
            v-model="email"
            type="email"
            placeholder="이메일 적어주셈"
            maxlength="20"
          />
          
          <div v-if="this.email === '' " class="msg"></div>
        </div>

      </div>

      <div class="row" v-if="pwdToggle == true">
        <div class="col-4 edit-1">비밀번호 :</div>
        <div class="col-8 edit-2">
          <input
            v-model="pwd"
            type="password"
            placeholder="숫자,대문자,소문자 8~16자리"
            maxlength="16"
          />
    
        </div>
      </div>
      <div class="row" v-if="pwdToggle == true">
        <div class="col-4 edit-1">비밀번호 확인 :</div>
        <div class="col-8 edit-2">
          <input
            v-model="pwdCheck"
            type="password"
            placeholder="위 비밀번호랑 똑같이 적으셈"
            maxlength="16"
          />
        
        </div>
      </div>
      <br>
      <div >
        <button type="button" @Click="editProfile" class="btn btn-secondary">
          <span style="font-size: 20px">수정완료</span>
        </button>
      </div>

    </div>
  </div>
</center>
</template>

<script>
import http from "@/api/http";
export default {
  props: ['message'],
  
  data() {
    return {
      pwdToggle: false,//비밀번호 토글

        name: null,
        nickname: null,
        loginId: null,
        pwd: null,
        pwdCheck: null,
        tel: null,
        email: null,
      
    };
  },
  created(){
    console.log(this.message)
    this.name = this.message.name
    this.nickname = this.message.nickname
    this.loginId = this.message.loginId
    this.tel = this.message.tel
    this.email = this.message.email
    
  },
  computed: {
    
  },
  methods: {
    changePwd() {
      this.pwdToggle = true
    },
    changeNotPwd() {
      this.pwdToggle = false
    },
    
    //닉네임 검사
    nicknameValid() {
      return /^([a-zA-Z0-9ㄱ-ㅎ|ㅏ-ㅣ|가-힣]).{1,10}$/.test(this.nickname)
    },
    
    //전화번호 검사
    telValid() {
      return /^\d{11}$/.test(this.tel)
    },
    //이메일 검사
    emailValid() {
      return /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i.test(this.email );
    },
  //수정하기
    editProfile() {
      const passwordCheckReg = /^(?=.*[a-zA-Z])((?=.*\d)|(?=.*\W)).{6,20}$/;
      let pwdCheck = passwordCheckReg.test(this.pwd);
      const telCheck = /^[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*@[0-9a-zA-Z]([-_.]?[0-9a-zA-Z])*.[a-zA-Z]{2,3}$/i;
      let telCh = telCheck.test(this.email);
      if(this.nickname === ""){
        return alert("닉네임을 입력하세요")
      }else if(this.tel === ""){
        return alert("전화번호를 입력하세요")
      }else if(this.email === ""){
        return alert("이메일을 입력하세요")
      }else if(this.nicknameValid === false){
        return alert("dsdasadsdas가능합니다.")
      }
      
      if(this.pwd != null){
        if(!pwdCheck){
           return alert("숫자 문자 특수문자를 사용하세요")
        }
        if(this.pwd != this.pwdCheck){
        return alert("비밀번호와 비빌번호 확인이 다름니다.")
        }
      }
      if(!this.nicknameValid){
        return alert("닉네이다")
      }else if(!telCh){
        return alert("알맞은 전화번호 입력")
      }
      
      
      
      let params = {
        nickname : this.nickname,
        password: this.pwd,
        tel: this.tel,
        email: this.email
      }
      http.post("/profile/insertProfile",{ params: params})
      .then(res => {
        console.log(res)
        if(res.data == 'ok'){
          alert("수정되었습니다!")
          location.href="/myPage"
          //this.$store.dispatch("auth/getInfo")
          return this.$emit('trueEdit', true)
        }else{
          alert("문제가 발생했습니다.")
        }
      })
    }
  }
};
</script>

<style>
.edit-1 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  border-style: solid;
  border-width: 0px 1px 1px 1px;
}
.edit-2 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-style: solid;
  border-width: 0px 1px 1px 0px;
}
.msg {
  color: red;
  font-size: 15px;
}
</style>