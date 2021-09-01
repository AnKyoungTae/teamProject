<template>
<EditBoardList :list="list" :image="images" @returnEdit="changeEdit" v-if="editToggle == true" ></EditBoardList>
<div class="container" style="width: 1000px;" v-else>
  <br /><br />
  <!-- 신고버튼 -->
  <div class="boardDelButtom">
    <button type="button" class="btn btn-danger" style="margin-left:10px;" @click="reportArticle(articleId)">신고</button>
    <div v-if="MYPAGE == true">
      <button type="button" class="btn btn-primary" style="margin-right:20px;" @click="changeEdit">수정</button>
      <button
        id="btn"
        type="button"
        class="btn btn-danger"
        @click="listdelete"
      >
        삭제
      </button>
    </div>
  </div>
  <br />
  <!-- 타이틀 -->
  <table style="border: 2px solid gray;">
    <thead id="title" style="text-align: center">
      <tr style="border-bottom: 1px solid gainsboro;">
        <th class="boardTitle" colspan='3' style="text-align: left; padding: 20px;">
          <span>구분</span><span style="padding-left:50px; font-size: 25px;">{{ list.title }}</span>
        </th>
      </tr>
      <tr style="height:40px;">
        <th class="col-md-5" style="text-align: left; padding-left: 20px;">{{ list.nickname }}</th>
        <th class="col-md-2"><i class="far fa-clock"></i>&nbsp;&nbsp;{{ list.regDate }}</th>
        <th class="col-md-1"><i class="far fa-eye"></i>&nbsp;&nbsp;{{ list.hit }}</th>
      </tr>
    </thead>
  </table>
  <!-- 본문 -->
  <div id="textarea" class="input-group" style="text-align: left;">
    <div class="form-control" style="padding:30px;">
    <!-- 이미지 -->
      <div v-if="images != null" class="w-full h-full flex items-center">
        <div v-for="image in images" :key="image.articlefileId"  style="padding-bottom:30px;">
          <img :src="image.name" alt="image.orgName">
        </div>
      </div>
    <!-- 이미지 끝 -->
      {{ list.body }}
    </div>
  </div>
  <br />
  <!-- 목록이동 버튼 -->
  <router-link class="btn btn-primary" :to="{ path: this.board }">
    목록
  </router-link>
  <br /><br />
  <!-- 댓글 -->
  <div>
    <ul class="list-group" style="list-style: none">
      <li class="list-group-item commentList" v-for="(comment,index) in comments" :key="comment.articleId">

        <div v-if="comment.status != 'S'">
          <div class="commentListTitle">
            <div>
              <strong style="padding-right: 10px; font-size:18px;">{{ comment.nickname }}</strong>
              ({{ comment.regDate }})
            </div>
          <div>
          <span v-if="MYPAGE == true">
            <button type="button" class="btn btn-outline-dark" style="padding:1px; margin-right:10px; font-size:15px;" v-if="commentToggle == false" @click="changeComment(comment.commentId)">수정</button>
            <button type="button" class="btn btn-outline-dark" style="padding:1px; margin-right:10px; font-size:15px;" v-else @click="changeComment(comment.commentId)">완료</button>
            <button type="button" class="btn btn-outline-dark" style="padding:1px; margin-right:10px; font-size:15px;" @click="deleteComment(comment.commentId)">삭제</button>
          </span>
          <button type="button" class="btn btn-outline-dark" style="padding:1px; font-size:15px;" @click="reportComment(comment.commentId)">신고</button>
          </div>
          </div>
          <div style="text-align: left;" :class=index >
            {{ comment.body }}
          </div>
        </div>
        <div v-else>
          이 글이 신고되었습니다
        </div>
        
        
  
      </li>
      <li class="list-group-item commentInput" style="background-color:gainsboro;">
        <textarea class="form-control" v-model.trim="commentTitle" id="floatingTextarea2" style="resize: none; width:800px; height:80px;"></textarea>
        <button class="btn btn-light" style="width:100px; height:80px;" @click="commentProc1">댓글입력</button>
      </li>
    </ul>
  </div>
</div>
</template>

<script>
import * as authAPI from "@/api/article.js";
import EditBoardList from "@/components/service/EditBoardList.vue";
import { mapMutations } from 'vuex';

export default {
  components: { EditBoardList },
  data() {
    return {
      articleId: null,
      //삭제
      modalToggle: false,
      //조회
      board: null,
      list: {},
      MYPAGE: null,//작성자 일때 true 토글
      images: '', //이미지 불러오기
      //댓글
      comments: null, //댓글 리스트
      //수정
      editToggle: false,

      imageDelete: [],  //삭제 이미지
      files: [], //업로드용 파일

      uploadImageIndex: 0, // 이미지 업로드를 위한 변수
      
      //댓글 보내기
      commentTitle: null,

      //댓글 수정
      commentEdit: null,
      commentToggle: false
    };
  },
  created() {
    this.boardListPage()
  },
  
  methods: {
    ...mapMutations(["SET_MODAL_LOGIN", "SET_serviceCenters"]),
    //화면 통신
    boardListPage() {
    //게시물 불러오기
      let query = this.$route.query.articleId; //articleId받기
      this.articleId = query
      this.board = this.$route.query.board; //게시판 종류  (1.공지사항, 2.Q&A, 3.FQA)
      if(this.board == "notice"){
        this.SET_serviceCenters(1)
      }else if(this.board == "qna"){
        this.SET_serviceCenters(2)
      }else if(this.board == "fqa"){
        this.SET_serviceCenters(3)
      }
    
      authAPI //통신코드
        .list(query)
        .then((res) => {
          console.log(res.data)
          this.list = res.data.article; //list에  DB데이터 박기
          this.MYPAGE = res.data.MYPAGE;
          this.comments = res.data.list;
          this.images = res.data.articleImageFile; //이미지
        });
    },
    //댓글 수정
   
    //댓글 달기 통신
    commentProc1() {
      if (this.commentTitle === "" || this.commentTitle == null) {
        return alert("글을 입력하세요");
      }
      let params = {
        title: this.commentTitle,
        parantId: this.list.articleId,
      };
      authAPI
        .commentProc(params)
        .then((res) => {
          console.log(res);
          if (res.data == 100) {
            alert("입력되었습니다.");
            this.commentTitle=''
            return  this.boardListPage()
              
          } else if (res.data == 200) {
            alert("비회원은 권한이 없습니다.");
            this.SET_MODAL_LOGIN(true);
          } else if (res.data == 250) {
            alert("작성자가 아닙니다.");
            location.href = "/" + this.board;
          } else if (res.data == 300) {
            alert("DB문제입니다");
            location.href = "/" + this.board;
          } else if (res.data == 400) {
            alert("글없습니다");
            location.href = "/" + this.board;
          } else {
            alert("서버에 문제가 있습니다.");
            location.href = "/" + this.board;
          }
        })
        .catcherr((err) => {
          console.log(err);
        });
    },
    //댓글 삭제
    deleteComment(commentId) {
      
      authAPI
      .deleteComment(this.list.articleId, commentId)
      .then(res => {
        console.log(res.data)
        if(res.data == 'ok'){
          this.boardListPage()
          alert("삭제되었습니다.")
          //location.href="/boardList?board="+ this.board + "&articleId=" + this.list.articleId

        }else if(res.data == 'no'){
          alert("문제있습니다.")
        }
      })
    },
    //수정 페이지 이동
    changeEdit() {
      if(this.editToggle == false){
        this.editToggle = true
      }else{
        this.editToggle = false
        this.boardListPage()
      }
     
    },
   
    //신고하기!!!!!!!!!!!!!!!!!!!!!!111
    reportArticle(articleId) {
      authAPI
      .reportArticle(articleId)
      .then(res=> {
        if(res.data == "ok"){
          return alert("신고되었습니다."), this.$router.push({path: this.board})
        }else if(res.data == "no"){
          return alert("문제있습니다.")
        }else{
          return alert("다시한번 시도하세요.")
        }
      })
    },
    reportComment(commentId) {
      authAPI
      .reportComment(commentId)
      .then(res=> {
        if(res.data == "ok"){
          return alert("신고되었습니다."), this.boardListPage()
        }else if(res.data == "no"){
          return alert("문제있습니다.")
        }else{
          return alert("다시한번 시도하세요.")
        } 
      })
    }
  },
};
</script>

<style scoped>
.boardTitle{
  display: flex;
  align-items: center;
}
.boardDelButtom {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.commentList {
  display: flex;
  flex-direction: column;
}
.commentListTitle {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.commentInput {
  display: flex;
  flex-direction: row;
  justify-content: center;
}
</style>