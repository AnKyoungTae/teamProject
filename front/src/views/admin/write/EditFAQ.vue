<template>
  <div class="container" style="width: 1000px">
    <br /><span style="font-size:30px;">FAQ 수정</span><br />
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
    <!-- 끝 -->
    <button 
      type="button" 
      class="btn btn-dark" 
      style="font-size:20px; margin:20px;"
      @click="updateFAQ"
    >
      수정
    </button>
    <button
      type="button"
      class="btn btn-danger"
      style="font-size:20px; margin:20px;"
      @click="back"
    >
      뒤로가기
    </button>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";


export default {
  props:['notice'],
  data(){
    return {
      title: '',
      body: ''
    }
  },
  mounted(){
    this.title = this.notice.title
    this.body = this.notice.body
    console.log(this.notice);
  },

  methods: {
    back() {
      return this.$emit('changeToggle', 'change')
    },
    updateFAQ(){
      let params = {
        //수정 필요한 데이터들
        articleId: this.notice.articleId,
        title: this.title,
        body: this.body,
        writerId: 0,
        imageDelete: []
        
      };

      authAPI
        .update(params)
        .then((res) => {
          console.log(res);
          if (res.data > 0) {
            alert("수정되었습니다")
            return this.$emit('changeToggle', 'change')
          }
          if (res.data == "no") {
            alert("비회원은 권한이 없습니다.");
            this.SET_MODAL_LOGIN(true);
          } else if (res.data == "ok") {
            return alert("수정 되었습니다.");
          } else if (res.data == 0) {
            return alert("다시 로그인 해주세요");
          }
        })
        .catch((err) => {
          console.log(err);
          console.log(params);
        });
    }
  },
  
}
</script>

<style>
.faqButton {
  padding: 20px;
}
</style>