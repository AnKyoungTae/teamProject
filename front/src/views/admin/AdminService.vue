<template>
<center>
  <div id="profileMenu" class="m-4">
    <div class="row">
      <div
        class="col d-inline m-1 spread-underline"
        @click="changeToggle('공지사항')"
      >
        <a :class="[toggle === '공지사항' ? 'choicedMenu' : '']">공지사항 글쓰기</a>
      </div>

      <div
        class="col d-inline m-1 spread-underline"
        @click="changeToggle('FAQ')"
      >
        <a :class="[toggle === 'FAQ' ? 'choicedMenu' : '']"
          >FAQ 글쓰기</a
        >
      </div>

      <div
        class="col d-inline m-1 spread-underline"
        @click="changeToggle('Q&A')"
      >
        <a :class="[toggle === 'Q&A' ? 'choicedMenu' : '']">Q&A 답변</a>
      </div>

      <div
        class="col d-inline m-1 spread-underline"
        @click="changeToggle('신고')"
      >
        <a :class="[toggle === '신고' ? 'choicedMenu' : '']">신고</a>
      </div>
    </div>
  </div>
  <div>
    <Notice v-if="toggle == '공지사항'" ref="notice"></Notice>
    <FAQ v-else-if="toggle == 'FAQ'" ref="FAQ"></FAQ>
    <Question v-else-if="toggle == 'Q&A'" ref="QA"></Question>
    <Suspend  v-else-if="toggle == '신고'"></Suspend>
  </div>
</center>
  
</template>

<script>
import Notice from"./write/Notice.vue"
import FAQ from"./write/FAQ.vue"
import Question from"./write/Question.vue"
import Suspend from"./write/Suspend.vue"

export default {
  components:{ Notice, FAQ, Question, Suspend},
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
        }
      }
        
      )
      
    },
    
  }
  
}
</script>

<style>
#profileMenu {
  width: 80%;
}
#profileMenu .col {
  float: left;
  height: 60px;
  line-height: 40px;
  text-align: center;
  border: 2px solid #fd3a69;
  overflow: hidden;
  cursor: pointer;
  font-size: 1.5em;
  border-radius: 10px 10px 10px 0;
  background: #ffda77;
}
#profileMenu .col:hover {
  background: #91afba;
  color: #fff;
  cursor: pointer;
}
.spread-underline {
  color: #333;
  text-decoration: none;
  display: inline-block;
  padding: 10px 0;
  position: relative;
}
.spread-underline:after {
  background: none repeat scroll 0 0 transparent;
  bottom: 0;
  content: "";
  display: block;
  height: 4px;
  left: 50%;
  position: absolute;
  background: #ffefa3;
  transition: width 0.3s ease 0s, left 0.3s ease 0s;
  width: 0;
}
.spread-underline:hover:after {
  width: 100%;
  left: 0;
}
.choicedMenu {
  text-decoration: underline;
  text-underline-position: under;
  color: #fd3a69;
}
a {
  text-decoration: none;
  color: #404040;
}
</style>