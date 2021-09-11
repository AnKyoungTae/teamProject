<template>

  <button type="button" class="btn btn-outline-primary" @click="changeToggle('공지사항')">공지사항 글쓰기</button>
  <button type="button" class="btn btn-outline-secondary" @click="changeToggle('FAQ')">FAQ 글쓰기</button>
  <button type="button" class="btn btn-outline-success" @click="changeToggle('Q&A')">Q&A 답변</button>
  <button type="button" class="btn btn-outline-danger" @click="changeToggle('신고')">신고</button>
  <div>
    <Notice v-if="toggle == '공지사항'" ref="notice"></Notice>
    <FAQ v-else-if="toggle == 'FAQ'" ref="FAQ"></FAQ>
    <Question v-else-if="toggle == 'Q&A'" ref="QA"></Question>
  </div>
  
</template>

<script>
import Notice from"./write/Notice.vue"
import FAQ from"./write/FAQ.vue"
import Question from"./write/Question.vue"

export default {
  components:{ Notice, FAQ, Question},
  data() {
    return {
      toggle: "공지사항"
    }
  },
  methods: {
    changeToggle(word) {
      this.toggle = word
      this.$nextTick(function(){
        if(word == "공지사항"){
          this.$refs.notice.changeToggle('change')
        }else if(word == "FAQ"){
          this.$refs.FAQ.changeToggle('change')
        }else if(word == "Q&A"){
          this.$refs.QA.changeToggle('change')
        }else if(word == "신고"){
          console.log("신고");
        }
      }
        
      )
      
    },
    
  }
  
}
</script>

<style>

</style>