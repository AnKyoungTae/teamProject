<template>
  <div>
    <h4><span v-if="!data.role || data.role == 'BUYER'">사용자</span>
    <span v-if="!data.role || data.role == 'SELLER'">판매자</span>
    <span v-if="!data.role || data.role == 'ADMIN'">관리자</span>
    {{data.nickname}}님의 회원정보</h4>
    <div style="text-align: left; font-size: 20px; ">
      <span style="padding-left: 52%;">신고 횟수 &nbsp;:&nbsp; <!--{{신고당한 수}}--></span>
    </div>
  </div>
  <div class="manage-div">
    <div class="manage-div2">
      <div class="profile-manage">
        <div class="manage-box">
          <a class="profile-img">
            <strong class="login-tit profile-tit">프로필사진</strong>
            <span class="login-txt"><a class="profile-img">
                  <img
                  :src='data.profileUrl'
                  style="display: block; 
                        width: 80px; 
                        height: 80px; 
                        border-radius: 50%;"
                  />
            </a></span>
          </a>
        </div>
          <hr style="padding: 0px;"/>
        <div class="manage-box">  
          <a class="profile-img">
            <strong class="login-tit">이름</strong>
            <span class="login-txt">{{data.name}}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">닉네임</strong>
            <span class="login-txt">{{data.nickname}}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">이메일</strong>
            <span class="login-txt">{{data.email}}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">핸드폰번호</strong>
            <span class="login-txt">{{data.phone}}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">역할</strong>
            <span class="login-txt">{{data.role}}</span>
          </a>
          <hr style="padding: 0px;"/>

          <a class="profile-img">
            <strong class="login-tit">회원 번호</strong>
            <span class="login-txt">{{data.memberId}}</span>
          </a>
          <hr style="padding: 0px;"/>
        </div>
        </div>
      </div>
      <div class="manage-div3">
        <div class="accordion accordion-flush" id="accordionFlushExample" style="margin-top: 20px;">
        <div
          class="accordion-item"
          style="border-top: 2px solid gray"
          v-for="(notice, index) in title"
          :key="notice.id"
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading' + index">
            <button
              class="accordion-button collapsed faqButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse' + index"
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse' + index"
            >
              <strong style="font-size: 18px">{{ notice.title }}</strong>
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse' + index"
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-heading' + index"
            data-bs-parent="#accordionFlushExample"
          >
            <div class="accordion-body" style="text-align: left;">
              <span style="color: gray; font-size: 14px;">고객센터 - QAN 제목</span>
              <div>
                <span>{{ notice.text }}</span>
              </div>
              <hr/>
            </div>
          </div>
        </div>
      </div>

        <!-- <div
          class="accordion-item"
          style="border-top: 2px solid gray"
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading2' + index">
            <button
              class="accordion-button collapsed titleButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse2' + index"
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse2' + index"
            >
              <strong style="font-size: 18px">신고당한 리뷰</strong>
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse2' + index"
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-headin2' + index"
            data-bs-parent="#accordionFlushExample"
          >
            <div class="accordion-body">
              신고당한 리뷰내용
            </div>
          </div>

          <div
          class="accordion-item"
          style="border-top: 2px solid gray"
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading3' + index">
            <button
              class="accordion-button collapsed titleButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse3' + index"
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse3' + index"
            >
              <strong style="font-size: 18px">신고당한 글</strong>
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse3' + index"
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-heading3' + index"
            data-bs-parent="#accordionFlushExample"
          >
            <div class="accordion-body">신고당한 글내용</div>
          </div>
        </div>

        <div
          class="accordion-item"
          style="border-top: 2px solid gray"
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading4' + index">
            <button
              class="accordion-button collapsed titleButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse4' + index"
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse4' + index"
            >
              <strong style="font-size: 18px">신고당한 리뷰</strong>
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse4' + index"
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-headin4' + index"
            data-bs-parent="#accordionFlushExample"
          >
            <div class="accordion-body">
              신고당한 리뷰내용
            </div>
          </div>
        </div> -->
        </div>
      </div>
</template>

<script>
export default {
  props: ["data"],
  data() {
    return {
      title: [
        {
          title: "작성 글",
          text: "작성내용",
        },
        {
          title: "작성리뷰",
          text: "작성 리뷰내용",
        },
        {
          title: "신고당한 글",
          text: "글내용",
        },
        {
          title: "신고당한 리뷰",
          text: "리뷰내용",
        },
      ],
    };
  },
};
</script>

<style scoped>
.manage-div {
  display: flex;
  justify-content: space-evenly;
}
.manage-div2 {
  display: block;
  width: 45%;
}
.manage-text {
  font-weight: normal;
  font-size: 30px;
}
.profile-manage {
  margin-top: 10px;
}
.manage-box {
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
  color: #5A8C94;
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
a {
  text-decoration: none;
  color: black;
}
.manage-div3 {
  width: 45%;
}
.profile-tit {
  height: 80px;
  line-height: 80px;
}
.accordion-body {
  overflow-y: scroll;
  height: 250px;
}
</style>
