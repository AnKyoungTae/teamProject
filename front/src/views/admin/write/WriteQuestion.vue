<template>
  <div class="container" style="width: 1000px">
    <br /><br />
    <!-- 신고버튼 -->
    <div class="boardDelButtom">
      <div>
        <button
          id="btn"
          type="button"
          class="btn btn-danger"
          @click="deleteArticle"
        >
          삭제
        </button>
      </div>
      <div v-if="list.boardId != 7">
        <button
          type="button"
          class="btn btn-primary"
          style="margin-left: 20px"
          @click="changeReply"
        >
          목록
        </button>
      </div>
    </div>
    <br />
    <!-- 타이틀 -->
    <div style="border: 2px solid gray">
      <div
        class="boardTitle"
        style="
          border-bottom: 1px solid gainsboro;
          text-align: left;
          padding: 20px;
          font-weight: 700;
        "
      >
        <span v-if="list.boardId == 3">기타</span>
        <span v-else-if="list.boardId == 4">주문</span>
        <span v-else-if="list.boardId == 5">딜리버리 주문</span>
        <span v-else-if="list.boardId == 6">제품/품질/서비스</span>
        <span v-else-if="list.boardId == 7">답글</span>
        <span v-else>작성글</span>
        <span style="padding-left: 50px; font-size: 25px">{{
          list.title
        }}</span>
      </div>
      <div
        style="width: 100%; font-weight: 600; height: 40px; line-height: 40px"
      >
        <div
          style="float: left; width: 30%; text-align: left; padding-left: 20px"
        >
          <span>{{ list.nickname }}</span>
        </div>
        <div
          style="
            float: left;
            width: 55%;
            text-align: right;
            padding-right: 30px;
          "
        >
          <span
            ><i class="far fa-clock"></i>&nbsp;&nbsp;{{ list.regDate }}</span
          >
        </div>
        <span><i class="far fa-eye"></i>&nbsp;&nbsp;{{ list.hit }}</span>
      </div>
    </div>

    <!-- 본문 -->
    <div id="textarea" class="input-group" style="text-align: left">
      <div class="form-control" style="padding: 30px">
        <!-- 이미지 -->
        <div v-if="images != null" class="w-full h-full flex items-center">
          <div
            v-for="image in images"
            :key="image.articlefileId"
            style="padding-bottom: 30px"
          >
            <img
              :src="image.name"
              alt="image.orgName"
              style="max-width: 874px"
            />
          </div>
        </div>
        <!-- 이미지 끝 -->
        {{ list.body }}
      </div>
    </div>
    <br />
    <!-- 목록이동 버튼 -->

    <br /><br />
    <!-- 댓글 -->
    <div>
      <ul class="list-group" style="list-style: none">
        <li
          class="list-group-item commentList"
          v-for="(comment, index) in comments"
          :key="comment.articleId"
        >
          <div>
            <div class="commentListTitle">
              <div>
                <strong style="padding-right: 10px; font-size: 18px">{{
                  comment.nickname
                }}</strong>
                ({{ comment.regDate }})
              </div>
              <div>
                <span>
                  <button
                    type="button"
                    class="btn btn-outline-dark"
                    style="padding: 1px; margin-right: 10px; font-size: 15px"
                    @click="deleteComment(comment.commentId)"
                  >
                    삭제
                  </button>
                </span>
              </div>
            </div>
            <div style="text-align: left" :class="index">
              {{ comment.body }}
            </div>
          </div>
        </li>
        <li
          class="list-group-item commentInput"
          style="background-color: gainsboro"
        >
          <textarea
            class="form-control"
            v-model.trim="commentTitle"
            id="floatingTextarea2"
            style="resize: none; width: 800px; height: 80px"
          ></textarea>
          <button
            class="btn btn-light"
            style="width: 100px; height: 80px"
            @click="commentProc1"
          >
            댓글입력
          </button>
        </li>
      </ul>
    </div>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";
import http from "@/api/http";
import { mapMutations } from "vuex";

export default {
  props: ["articleId"],
  data() {
    return {
      //조회
      board: null,
      list: {},
      MYPAGE: null, //작성자 일때 true 토글
      images: "", //이미지 불러오기
      //댓글
      comments: null, //댓글 리스트
      //수정
      editToggle: false,
      imageDelete: [], //삭제 이미지
      files: [], //업로드용 파일
      uploadImageIndex: 0, // 이미지 업로드를 위한 변수

      //댓글 보내기
      commentTitle: null,
      //댓글 수정
      commentEdit: null,
      commentToggle: false,
    };
  },
  created() {
    this.boardListPage();
  },

  methods: {
    ...mapMutations(["SET_MODAL_LOGIN", "SET_serviceCenters"]),
    //화면 통신
    boardListPage() {
      //게시물 불러오기
      let query = this.articleId;

      authAPI //통신코드
        .list(query)
        .then((res) => {
          console.log(res);
          this.list = res.data.article; //list에  DB데이터 박기

          this.comments = res.data.list;
          this.images = res.data.articleImageFile; //이미지
        });
    },
    //목록
    changeReply() {
      return this.$emit("changeToggle", "change");
    },

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
          if (res.data == 100) {
            alert("입력되었습니다.");
            this.commentTitle = "";
            return this.boardListPage();
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
        .catch((err) => {
          console.log(err);
        });
    },
    //게시글 삭제
    deleteArticle() {
      let deleteComm = confirm("게시글을 삭제하시겠습니까?");

      if (deleteComm) {
        http.post("/article/deleteArticle", this.articleId).then((res) => {
          if (res.data == "ok") {
            alert("삭제되었습니다.");
            return this.$emit("changeToggle", "change");
          } else if (res.data == "no") {
            alert("문제있습니다.");
          }
        });
      }
    },
    //댓글 삭제
    deleteComment(commentId) {
      let deleteComm = confirm("댓글을 삭제하시겠습니까?");

      if (deleteComm) {
        authAPI.deleteComment(this.list.articleId, commentId).then((res) => {
          if (res.data == "ok") {
            this.boardListPage();
            alert("삭제되었습니다.");
          } else if (res.data == "no") {
            alert("문제있습니다.");
          }
        });
      }
    },
  },
};
</script>

<style scoped>
.boardTitle {
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
