<template>
  <div class="container" style="width: 1000px">
    <br /><span style="font-size:30px;">FAQ 작성</span><br />
    <!-- FAQ 제목 -->

    <div class="writeBoard">
      <div style="font-size: 20px; text-align: left">제목</div>
      <input
        style="margin-bottom: 20px"
        type="text"
        class="form-control"
        aria-label="Text input with dropdown button"
        placeholder="제목을 입력해 주세요."
        v-model="title"
      />
      <!-- FAQ 본문작성 -->
      <div style="font-size: 20px; text-align: left">내용</div>
      <textarea
        style="height: 400px"
        class="form-control"
        aria-label="With textarea"
        placeholder="내용을 입력해 주세요."
        v-model="body"
      ></textarea>
    </div>

    <!-- 제출/취소 버튼 -->
    <div class="col-md-12 text-center">
      <button 
        style="font-size:20px; margin:20px;" 
        class="btn btn-success" 
        @click="writeRequest"
      >
        작성
      </button>

      <button
        type="button"
        class="btn btn-danger"
        style="font-size:20px; margin:20px;"
        @click="returnBoard"
      >
        뒤로가기
      </button>
      
    </div>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";

export default {
  data(){
    return{
      title:null,
      body: null,
      status: "Y",
    }
  },
  methods:{
    //return board
    returnBoard() {
      return this.$emit('changeToggle', 'change')
    },
    writeRequest(){
      
      if (this.title === "" || this.title == null) {
        return alert("제목을 입력하세요");
      }
      if (this.body === "" || this.body == null) {
        return alert("내용을 입력하세요");
      }
      

      let params = {
        title: this.title,
        body: this.body,
        status: this.status,
        boardId: 1, //Q&A 개시판 3이상, 7은 답글
      
      };
     
      authAPI
        .writeProc(params) //axios 이동
        .then((res) => {
          if (res.data > 0) {
            alert("작성되었습니다.")
            return this.$emit('changeToggle', 'change')
          }
          if (res.data == "no") {
            return (
              this.SET_MODAL_LOGIN(true), alert("비회원은 권한이 없습니다.")
            );
          } else if (res.data == 0) {
            return alert("다시 로그인 해주세요");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    }
  }
}
</script>

<style>
</style>