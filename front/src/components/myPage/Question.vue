<template>
  <div style="margin-top: 50px;">
    <h1 class="nearShopTitle">
              <p>질문내역</p>
            </h1>
  </div>
  <div
    class="container"
    style="width: 800px; border-style: solid; border-width: 1px 1px 0px 1px"
    v-if="questionToggle == true"
  >
    <div class="row" v-for="question in questions" :key="question.articleId">
      <div class="col Q">
        <div class="Q-title">
          <h4>
            {{ question.title }}
            <span v-if="question.children > 0">
              [{{ question.children }}]
            </span>
          </h4>

          <div>
            <button
              class="btn btn-outline-primary"
              style="margin-right: 5px"
              @click="throwQNA(question.articleId)"
            >
              바로가기
            </button>
            <button
              type="button"
              @Click="QDel(question.writerId, question.articleId)"
              class="btn btn-outline-danger"
            >
              글 삭제
            </button>
          </div>
        </div>
        <div class="Q-text">
          {{ question.body }}
        </div>
      </div>
    </div>
  </div>

  <div
    style="width: 800px; border-style: solid; border-width: 1px 1px 0px 1px"
    v-else
  >
    글이 없습니다.
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";

export default {
  data() {
    return {
      questions: [],
      questionToggle: false,
    };
  },
  methods: {
    throwQNA(articleId) {
      this.$router.push({
        name: "BoardList",
        query: { board: "qna", articleId: articleId },
      });
    },
    QDel(writerId, articleId) {
      //삭제 코드
      let params = {
        //삭제에 필요한 데이터들
        writerId: writerId,
        articleId: articleId,
      };

      authAPI
        .listDelete(params)
        .then((res) => {
          if (res.data == 100) {
            this.questions = [];
            this.getMyArticle();
            alert("삭제되었습니다.");
            this.$emit("updateView", "myQuestion");
          } else if (res.data == 200) {
            alert("비회원은 권한이 없습니다.");
            this.SET_MODAL_LOGIN(true);
          } else if (res.data == 250) {
            alert("작성자가 아닙니다.");
          } else if (res.data == 300) {
            alert("DB문제입니다");
          } else if (res.data == 400) {
            alert("글없습니다");
            location.href = "/" + this.board;
          } else {
            alert("서버에 문제가 있습니다.");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    getMyArticle() {
      authAPI
        .pagingMyBoard(2)
        .then((res) => {
          if (res.status === 200) {
            this.questions = res.data;
            console.log(res.data);
            this.questionToggle = true;
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
  mounted() {
    this.getMyArticle();
  },
};
</script>

<style>
.Q {
  border-bottom: 2px solid grey;
}
.Q-title {
  border-bottom: 1px solid grey;
  padding: 10px;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
}
.Q-text {
  padding: 10px;
  text-align: start;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 3;
  -webkit-box-orient: vertical;
  overflow: hidden;
  height: 80px;
}

.nearShopTitle {
  font-weight: bolder;
  text-align: end;
  margin-bottom: 20px;
  color: #91afba;
  font-family: BMDOHYEON;
  user-select: none;
  display: flex;
  justify-content: end;
}
.nearShopTitle > p {
  width: 30%;
  border-bottom: 8px solid #ffda77;
  padding-bottom: 10px;
}

@font-face {
  font-family: "BMDOHYEON";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_one@1.0/BMDOHYEON.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
</style>
