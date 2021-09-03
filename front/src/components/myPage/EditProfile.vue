<template>
<div class="manage-div">
    <div class="manage-div2">
      <h3 class="manage-text">내정보 수정</h3>
      <div class="profile-manage">
        <div class="manage-box">
          <a class="profile-img">
            <strong class="login-tit">이름</strong>
            <span class="login-txt">{{ name }}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">아이디</strong>
            <span class="login-txt">{{ loginId }}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">닉네임</strong>
            <span class="login-txt"><input
            v-model="nickname"
            type="text"
            placeholder="한글,영문,숫자만 2~10자리"
            maxlength="15"
          />
         
          <div v-if="this.nickname === ''" class="msg">닉네임을 입력하세요.</div></span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">이메일</strong>
            <span class="login-txt"><input
            v-model="email"
            type="email"
            placeholder="이메일 적어주세요"
            maxlength="20"
          />
          
          <div v-if="this.email === '' " class="msg"></div></span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">핸드폰번호</strong>
            <span class="login-txt"><input
            v-model="tel"
            type="text"
            placeholder="-없이 적어주세요"
            maxlength="11"
     
          />
        
          <div v-if="this.tel ===  '' " class="msg">핸드폰번호를 입력하세요</div></span>
          </a>
          <hr style="padding: 0px;"/>

          <div>
            비밀번호 변경하기
            <br>
            ON<input type="radio" @change="changePwd" name="pwd">
            OFF<input type="radio" @change="changeNotPwd" name="pwd" checked>
            <br>
          </div>
          <div class="row" v-if="pwdToggle == true">
            <a class="profile-img">
              <strong class="login-tit">비밀번호</strong>
              <span class="login-txt"><input
              v-model="pwd"
              type="password"
              placeholder="숫자,대문자,소문자 8~16자리"
              maxlength="16"
            /></span>
            </a>
            <hr style="padding: 0px;"/>
          </div>

          <div class="row" v-if="pwdToggle == true">
            <a class="profile-img">
              <strong class="login-tit">비밀번호 확인</strong>
              <span class="login-txt"><input
              v-model="pwdCheck"
              type="password"
              placeholder="위 비밀번호랑 똑같이 적으세요"
              maxlength="16"
            /></span>
            </a>
            <hr style="padding: 0px;"/>
          </div>
        </div>
      </div>
      <button type="button" @Click="editProfile" class="btn btn-secondary">
        <span style="font-size: 20px">수정완료</span>
      </button>
    </div>
  </div>

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
.profile-manage {
  margin-top: 10px;
  border-top: 1px solid #7c7c7c;
}
.manage-box {
  margin-top: 40px;
  text-align: left;
}
.profile-comm {
  margin-top: 4px;
  color: gray;
}
.profile-img {
  margin-top: 10px;
}
.profile-imgdiv {
  float: left;
  width: 80px;
  height: 80px;
  padding: auto 0;
  position: relative;
}
.profile-imgdiv img {
  display: block; 
  width: 80px; 
  height: 80px; 
  border-radius: 50%;
}
.img-text {
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
  float: right;
  color: #5066aa;
  font-size: 20px;
}
.m-2 {
  cursor: pointer;
}
.nicknamebox {
  float: left;
  line-height: 80px;
}
.filebox {
  margin-left: 30%;
  height: 80px;
  text-align: right;
}

.msg {
  color: red;
  font-size: 15px;
}
</style>