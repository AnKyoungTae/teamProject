<template>
  <div class="notice col-12 h-50" >
    <br>
    <button type="button" class="btn btn-primary" @click="writeRequest">제출</button>
    <button type="button" class="btn btn-info" @click="returnBoard">뒤로가기</button>
    <br>
      <!-- 리스트 시작 -->
      <div class="accordion accordion-flush" id="accordionFlushExample">
        <div
          class="accordion-item"
          style="border-top: 2px solid gray"
         
        >
          <h2 class="accordion-header" v-bind:id="'flush-heading' ">
            <button
              class="accordion-button collapsed faqButton"
              type="button"
              data-bs-toggle="collapse"
              v-bind:data-bs-target="'#flush-collapse' "
              aria-expanded="false"
              v-bind:aria-controls="'flush-collapse' "
            >
              <span>제목:</span>
              <input 
                type="text" 
                style="font-size: 18px; width: 40vw;" 
                v-model="title"
                placeholder="내용을 입력하세요"
              />
            </button>
          </h2>
          <div
            v-bind:id="'flush-collapse' "
            class="accordion-collapse collapse"
            v-bind:aria-labelledby="'flush-heading' "
            data-bs-parent="#accordionFlushExample"
          >
            
            <textarea type="text" 
              class="accordion-body" 
              style="width: 40vw;" 
              v-model="body" 
              placeholder="내용을 입력하세요"/>
          </div>
        </div>
      </div>
      <!-- 리스트 끝 -->
      
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
.faqButton {
  padding: 20px;
}
</style>