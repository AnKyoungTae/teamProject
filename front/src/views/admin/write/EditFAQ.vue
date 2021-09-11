<template>
  <div class="notice col-12 h-50" >
      <!-- 리스트 시작 -->
      <div class="accordion accordion-flush" id="accordionFlushExample">
        <div
          class="accordion-item"
          style="border-top: 2px solid gray"
         
        >제목:
          <input type="text" width="100" v-model="title"/>
          
        </div>

        <div class="">
          <textarea cols="100" rows="10" v-model="body"></textarea>
          
        </div>

      </div>
      <!-- 리스트 끝 -->
      <button type="button" class="btn btn-dark" @click="updateFAQ">수정</button>
      <button
       
        type="button"
        class="btn btn-danger"
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