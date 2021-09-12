<template>
  <div>
    <div style="padding:30px;">
      <h2>게시판</h2>
      <div>
        본글<input type="radio" name="suspend" value="article" checked @click="changeKind('article')"/>
        댓글<input type="radio" name="suspend" value="comment" @click="changeKind('comment')"/>
      </div>
    </div>
    <table v-if="kind == 'article'" style="margin: 0 auto; text-align: center; vertical-align: middle;">
      <tr>
        <th style="width:50px;">#</th>
        <th style="width:120px;">날짜</th>
        <th style="width:200px;">제목</th>
        <th style="width:400px;">내용</th>
        <th style="width:400px;">신고</th>
        <th colspan="2">관리하기</th>
     
      </tr>
      <tr v-for="li,index in list" :key="index">
        <td>{{index + 1}}</td>
        <td>{{li.regDate}}</td>
        <td>{{li.title}}</td>
        <td>{{li.body}}</td>
        <td>{{li.suspend}}</td>
        <td>
          <button type="button" class="btn btn-outline-danger" @click="changeSuspend(li.articleId , 'N')">삭제</button>
        </td>
        <td>
          <button type="button" class="btn btn-outline-primary"  @click="changeSuspend(li.articleId, 'Y')">복귀</button>
        </td>
      </tr>
    </table>

    <table v-else style="margin: 0 auto; text-align: center; vertical-align: middle;">
      <tr>
        <th style="width:30px;">#</th>
        <th style="width:120px;">날짜</th>
        <th style="width:400px;">내용</th>
        <th style="width:400px;">신고</th>
        <th colspan="2">관리하기</th>
     
      </tr>
      <tr v-for="li,index in list" :key="index">
        <td>{{index + 1}}</td>
        <td>{{li.regDate}}</td>
        <td>{{li.body}}</td>
        <td>{{li.suspend}}</td>
        <td>
          <button type="button" class="btn btn-outline-danger" @click="changeSuspend(li.commentId , 'N')">삭제</button>
        </td>
        <td>
          <button type="button" class="btn btn-outline-primary"  @click="changeSuspend(li.commentId, 'Y')">복귀</button>
        </td>
      </tr>
    </table>
  </div>

</template>

<script>
import http from "@/api/http";

export default {
  data(){
    return {
      kind: "article",
      list:[]
    }
  },
  methods: {
    articleProc(){
      http.post("/article/suspendArticle", this.kind)
      .then(res=>{
        console.log(res.data);
        this.list = res.data

        for(let i = 0;i < this.list.length; i++){
          
          
          if(this.list[i].suspend != null){
            console.log(i+ '번 성공');
            let suspend = this.list[i].suspend.split("///")
            this.list[i].suspend = ''

            console.log(suspend);
            for(let j = 1;j < suspend.length;j++){
              if(j+1 == suspend.length) {
                this.list[i].suspend += j + "번째 신고내용: " + suspend[j]
              }
              else {
                this.list[i].suspend += j + "번째 신고내용: " + suspend[j] + ",\n"
              }
            }
          }
          
        }

      })
      .catch(err=>{
        console.log(err);
      })
    },
    //라디오 버튼
    changeKind(word){
      this.kind = word

      this.articleProc()
    },
    changeSuspend(tableId, status){
      http.post("/article/changeSuspend",{
        param:{
          tableId: tableId,
          status: status,
          kind: this.kind
        }
      })
      .then(res=>{
        if(res.data == "ok"){
          alert("되었습니다.")
          this.articleProc()
        }else{
          alert("문제가 발생했습니다.")
        }
      })
      .catch(err=>{
        console.log(err);
      })
    }
  },
  mounted(){
    this.articleProc()
  }
}
</script>

<style>
th, td {
  border:1px solid black;
}
</style>