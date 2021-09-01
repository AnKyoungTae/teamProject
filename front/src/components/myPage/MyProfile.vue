<template>
  <div class="container" style="width: 800px">
    <div style="height: 660px">
      <div v-if="correction == true">
        <div v-if="profile != null">
          <div class="row" style="padding: 20px; border: 1px solid black">
            <div class="img" style="padding-bottom: 20px">
              <img
                :src='profile.profileUrl'
                style="width: 150px; height: 150px; border-radius: 50%"
              />
            </div>
            <div class="filebox">
              <label for="ex_file" >업로드</label>
              <input type="file" id="ex_file" ref="files" @change="changeImage"/>
              <span class="m-2" v-if="profileImage != null" @click="updateIamge">수정</span>
            </div>
          </div>
          <div class="row">
            <div class="col-4 profile-1">이름 :</div>
            <div class="col-8 profile-2">
              {{ profile.name }}
              <!-- 배열위치는 언제든 바뀔수있음. 그보다 정확한 변수명을 명시해줄것 -->
            </div>
          </div>
          <div class="row">
            <div class="col-4 profile-1">닉네임 :</div>
            <div class="col-8 profile-2">
              {{ profile.nickname }}
            </div>
          </div>
          <div class="row">
            <div class="col-4 profile-1">아이디 :</div>
            <div class="col-8 profile-2">
              {{ profile.loginId }}
            </div>
          </div>
          <div class="row">
            <div class="col-4 profile-1">핸드폰번호 :</div>
            <div class="col-8 profile-2">
              {{ profile.tel }}
              <!-- tel 로 받아놓게했는데 DB에선 phone 으로 했음. 헷갈릴수있음 -->
            </div>
          </div>
          <div class="row">
            <div class="col-4 profile-1">이메일 :</div>
            <div class="col-8 profile-2">
              {{ profile.email }}
            </div>
          </div>
        </div>

      </div>

      <div v-if="correction == false">
        <EditProfile @trueEdit="edit" :message="profile"  ></EditProfile>
      </div>

      <div v-if="profile == null">
        <div>정말 탈퇴 하시겠습니까?</div>4
        <br>
        <div>탈퇴하려면 비밀번호가 필요합니다.</div>
        <input class="m-5 " type="password" v-model="deletePassword" placeholder="비밀번호를 입력하세요" />
      
      </div>
    
    </div>

    

    <div class="button" v-if="profileDelete == false">
      <div v-if="correction == true">
        <button type="button"  @Click="edit(false)" class="btn btn-secondary">
          <span style="font-size: 20px">수정하기</span>
        </button>
      </div>
      
      <button v-if="correction == true" type="button" @Click="secession()" class="btn btn-secondary" style="margin-left: 50px;">
        <span style="font-size: 20px">탈퇴하기</span>
      </button>
    </div>
    <!-- 탈퇴 버튼-->
    <div v-else>
      <button  type="button" @click="reallyDelete" class="btn btn-secondary" style="margin-left: 50px;">
        <span style="font-size: 20px">탈퇴하기</span>
      </button>

      <button type="button" @click="secession()" class="btn btn-secondary">
          <span style="font-size: 20px">뒤로가기</span>
        </button>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import { error, success, normal } from "@/api/notification";
import EditProfile from "@/components/myPage/EditProfile.vue";
export default {
  components: { EditProfile },
  data() {
    return {
      correction : null,
      profile: '',       //프로필 null이면 삭제화면
      profileDelete: false,//삭제 토글
      deletePassword: "", //삭제시 필요한 비밀번호
      profileImage:null

    };
  },
  methods: {
    //수정버튼
    edit(request) {
      this.correction = request;
  
      if(this.correction == false)
      {
        console.log("프로필 수정을 완료했습니다.");
      }
      else
      {
        this.profile = ''
        this.upload()
        console.log("프로필 수정창으로 이동합니다.");
      }
    },
    // 탈퇴로직 작성
    secession() {
      if(this.profileDelete == false){
        this.profile = null
        this.profileDelete = true
      }else{
        this.upload()
        this.profileDelete = false
      }
      
    },
    //삭제하기
    reallyDelete(){
      if( this.deletePassword == ""){
        return alert("비밀번호를 입력하세요")
      }

      http.post("/profile/deleteprofile",  this.deletePassword)
      .then(res => {
        console.log(res)
        if(res.data == "ok"){
          alert("삭제되었습니다.")
          this.$router.push({name: 'Home'})
          
          //위치
        }else if(res.data == "no"){
          alert("문제있습니다.")
        }else{
          alert("서버문제입니다.")
        }
      })
    },

    //이미지 change
    changeImage() {
      console.log(this.$refs)
      console.log(this.$refs.files)
       console.log(this.$refs.files.files[0])
      this.profileImage = {   //실제 파일
              file: this.$refs.files.files[0],
      }
      //이미지 프리뷰
      this.profile.profileUrl = URL.createObjectURL(this.$refs.files.files[0])
    },
    //이미지 DB수정
    updateIamge() {
      let image = this.profileImage.file

      var form = new FormData()
      form.append("image", image)

      http
      .post("/profile/insertImage", form, {headers: {'Content-Type': 'multipart/form-data'},
       
      })
      .then(res => {
        console.log(res)
        if(res.data == 'ok'){
          return alert("변경되었습니다."), location.href="/myPage"
        }else if(res.data == 'no'){
          return alert("변경안됨")
        }else{
           return alert("문제")
        }
      })
    },
    //프로필 가져오기
    upload() {
      http
        .get("/profile/myProfile")
        .then((response) => {
          console.log(response.data)
          if (response.status === 200) {
            
            this.profile = response.data;
            if (this.profile != null) {
              if (this.profile.status == "Y")
              {
                success("프로필을 불러왔습니다.", this);
                this.correction = true;
                return;
              }
              else
              {
                error("정지된 아이디입니다.", this);
                this.$store.dispatch("auth/logout");
              }
            }
            // 로직상 이쪽에 올리가 없음. 만약 오게된다면 통신장애.
            error("통신중 오류가 발생했습니다.", this);
          }
          error("정보가 존재하지 않습니다.", this);
          // 이러면 잘못된 정보로 요청을 한것이고, 로그아웃을 시켜야함.
          // 로그아웃이되면, 토큰정보와 로그인정보가 사라짐.
          // jwt의 단점인 토큰말소의 어려움을 해소할수 있는 방법중 하나.
          this.$store.dispatch("auth/logout");
        })
        .catch((err) => {
          // 에러 내용을 보려면 err.response
          // err 와 error 는 다른것이기에 이렇게 작성한것은 잘했음.
          console.log(err.response.status);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
          // 만약 500 오류가 나면 어떻게 처리할것인가?
          this.$store.dispatch("auth/logout");
        });
    }
  },
  mounted() {
    this.upload()
  }
};
</script>

<style>
.profile-1 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  border-style: solid;
  border-width: 0px 0px 1px 1px;
}
.profile-2 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-style: solid;
  border-width: 0px 1px 1px;
}
.button {
  padding: 20px;
  margin: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.filebox label {
  display: inline-block;
  padding: 0.5em 0.75em;
  color: white;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #2f855a;
  cursor: pointer;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: 0.25em;
}
.filebox input[type="file"] {
  /* 파일 필드 숨기기 */
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
</style>
