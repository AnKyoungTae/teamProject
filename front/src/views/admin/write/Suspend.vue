<template>
  <div class="wrapper">
    <div class="container">
      <div>
        <div class="susRadio">
          본문<input
            type="radio"
            name="suspend"
            value="article"
            checked
            @click="changeKind('article')"
          />
          댓글<input
            type="radio"
            name="suspend"
            value="comment"
            @click="changeKind('comment')"
          />
          리뷰<input
            type="radio"
            name="suspend"
            value="comment"
            @click="reviewProc()"
          />
        </div>
      </div>
      <div v-if="list.length == 0">
        <strong style="font-size: 25px">신고된 글이 없습니다.</strong>
      </div>
      <div v-else-if="list.length != 0">
        <!-- 게시글 -->
        <table v-if="kind == 'article'" class="susTable">
          <tr>
            <th style="width: 100px">#</th>
            <th style="width: 300px">날짜</th>
            <th style="padding-right: 45px">제목</th>
          </tr>
          <tr>
            <td colspan="3">
              <!-- 리스트 시작 -->
              <div class="accordion accordion-flush" id="accordionFlushExample">
                <div
                  class="accordion-item"
                  v-for="(li, index) in list"
                  :key="index"
                >
                  <h2
                    class="accordion-header"
                    v-bind:id="'flush-heading' + index"
                  >
                    <button
                      class="accordion-button collapsed susTitle"
                      type="button"
                      data-bs-toggle="collapse"
                      v-bind:data-bs-target="'#flush-collapse' + index"
                      aria-expanded="false"
                      v-bind:aria-controls="'flush-collapse' + index"
                    >
                      <span class="susTitleNum">{{ index + 1 }}</span>
                      <span class="susTitleDay">
                        {{
                          li.regDate[0] +
                          li.regDate[1] +
                          li.regDate[2] +
                          li.regDate[3] +
                          "년 " +
                          li.regDate[5] +
                          li.regDate[6] +
                          "월 " +
                          li.regDate[8] +
                          li.regDate[9] +
                          "일 " +
                          li.regDate[11] +
                          li.regDate[12] +
                          "시 " +
                          li.regDate[14] +
                          li.regDate[15] +
                          "분 " +
                          li.regDate[17] +
                          li.regDate[18] +
                          "초"
                        }}
                      </span>
                      <span class="susTitleContents">{{ li.title }}</span>
                    </button>
                  </h2>
                  <div
                    v-bind:id="'flush-collapse' + index"
                    class="accordion-collapse collapse"
                    v-bind:aria-labelledby="'flush-heading' + index"
                    data-bs-parent="#accordionFlushExample"
                  >
                    <div class="accordion-body susBody">
                      <div class="susBodyList">
                        <strong>글 제목 : </strong>{{ li.title }}
                      </div>
                      <div class="susBodyList">
                        <strong>글 내용 : </strong>{{ li.body }}
                      </div>
                      <div class="susBodyList">
                        <strong>신고 내용 : </strong>{{ li.suspend }}
                      </div>
                      <div class="susBodyBtn">
                        <button
                          type="button"
                          class="btn btn-outline-danger"
                          @click="changeSuspend(li.articleId, 'N')"
                        >
                          삭제
                        </button>
                        <button
                          type="button"
                          class="btn btn-outline-primary"
                          @click="changeSuspend(li.articleId, 'Y')"
                        >
                          복귀
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 리스트 끝 -->
            </td>
          </tr>
        </table>
        <!-- 댓글 -->
        <table v-else-if="kind == 'comment'" class="susTable">
          <tr>
            <th style="width: 100px">#</th>
            <th style="width: 300px">날짜</th>
            <th style="padding-right: 45px">글 내용</th>
          </tr>
          <tr>
            <td colspan="3">
              <!-- 리스트 시작 -->
              <div class="accordion accordion-flush" id="accordionFlushExample">
                <div
                  class="accordion-item"
                  v-for="(li, index) in list"
                  :key="index"
                >
                  <h2
                    class="accordion-header"
                    v-bind:id="'flush-heading' + index"
                  >
                    <button
                      class="accordion-button collapsed susTitle"
                      type="button"
                      data-bs-toggle="collapse"
                      v-bind:data-bs-target="'#flush-collapse' + index"
                      aria-expanded="false"
                      v-bind:aria-controls="'flush-collapse' + index"
                    >
                      <span class="susTitleNum">{{ index + 1 }}</span>
                      <span class="susTitleDay">
                        {{
                          li.regDate[0] +
                          li.regDate[1] +
                          li.regDate[2] +
                          li.regDate[3] +
                          "년 " +
                          li.regDate[5] +
                          li.regDate[6] +
                          "월 " +
                          li.regDate[8] +
                          li.regDate[9] +
                          "일 " +
                          li.regDate[11] +
                          li.regDate[12] +
                          "시 " +
                          li.regDate[14] +
                          li.regDate[15] +
                          "분 " +
                          li.regDate[17] +
                          li.regDate[18] +
                          "초"
                        }}
                      </span>
                      <span class="susTitleContents">{{ li.body }}</span>
                    </button>
                  </h2>
                  <div
                    v-bind:id="'flush-collapse' + index"
                    class="accordion-collapse collapse"
                    v-bind:aria-labelledby="'flush-heading' + index"
                    data-bs-parent="#accordionFlushExample"
                  >
                    <div class="accordion-body susBody">
                      <div class="susBodyList">
                        <strong>글 내용 : </strong>{{ li.body }}
                      </div>
                      <div class="susBodyList">
                        <strong>신고 내용 : </strong>{{ li.suspend }}
                      </div>
                      <div class="susBodyBtn">
                        <button
                          type="button"
                          class="btn btn-outline-danger"
                          @click="changeSuspend(li.commentId, 'N')"
                        >
                          삭제
                        </button>
                        <button
                          type="button"
                          class="btn btn-outline-primary"
                          @click="changeSuspend(li.commentId, 'Y')"
                        >
                          복귀
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 리스트 끝 -->
            </td>
          </tr>
        </table>

        <!-- 리뷰 -->
        <table v-else class="susTable">
          <tr>
            <th style="width: 100px">#</th>
            <th style="width: 300px">날짜</th>
            <th style="padding-right:45px;">글 내용</th>
          </tr>
          <tr>
            <td colspan="3">
              <!-- 리스트 시작 -->
              <div class="accordion accordion-flush" id="accordionFlushExample">
                <div class="accordion-item"
                v-for="(li, index) in list" :key="index">
                  <h2 class="accordion-header" v-bind:id="'flush-heading' + index">
                    <button
                      class="accordion-button collapsed susTitle"
                      type="button"
                      data-bs-toggle="collapse"
                      v-bind:data-bs-target="'#flush-collapse' + index"
                      aria-expanded="false"
                      v-bind:aria-controls="'flush-collapse' + index"
                    >
                      <span class="susTitleNum">{{ index + 1 }}</span>
                      <span class="susTitleDay">
                        {{
                          li.regDate[0]  + "년 " + 
                          li.regDate[1] + "월 " +
                          li.regDate[2] + "일 " +
                          li.regDate[3] + "시 " 
                         
                        }}
                      </span>
                      <span class="susTitleContents">{{ li.body }}</span>
                    </button>
                  </h2>
                  <div
                    v-bind:id="'flush-collapse' + index"
                    class="accordion-collapse collapse"
                    v-bind:aria-labelledby="'flush-heading' + index"
                    data-bs-parent="#accordionFlushExample"
                  >
                    <div class="accordion-body susBody">
                      <div class="susBodyList">
                        <strong>가게 댓글 : </strong>{{ li.reply }}
                      </div>
                      <div class="susBodyList">
                        <strong>신고 내용 : </strong>{{ li.suspend }}
                      </div>
                      <div class="susBodyBtn">
                        <button
                          type="button"
                          class="btn btn-outline-danger"
                          @click="changeReviewSuspend(li.reviewId, 'N')"
                        >
                          삭제
                        </button>
                        <button
                          type="button"
                          class="btn btn-outline-primary"
                          @click="changeReviewSuspend(li.reviewId, 'Y')"
                        >
                          복귀
                        </button>
                      </div>
                    </div>
                  </div>
                </div>
              </div>
              <!-- 리스트 끝 -->
            </td>
          </tr>
        </table>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
  data() {
    return {
      kind: "article",
      list: [],
    };
  },
  methods: {
    //review
    reviewProc(){
        this.changeKind('review')
       http
        .post("/review/suspendReview")
        .then(res=>{
          console.log(res.data);
          this.list = res.data;
        })

    },
    changeReviewSuspend(reviewId, status){
      http
        .post("/review/changeReviewSuspend", {
          param: {
            reviewId: reviewId,
            status: status,
          }
        })
        .then(res=>{
            if(res.data == "ok"){
              if(status == 'Y'){
                alert("신고가 취소되었습니다")
              }else{
                alert("삭제되었습니다")
              }
            }else{
              alert("문제가 발생했습니다")
            }
            this.reviewProc()
        })
    },

    //article, comment
    articleProc() {
      http
        .post("/article/suspendArticle", this.kind)
        .then((res) => {
          
          this.list = res.data;

          for (let i = 0; i < this.list.length; i++) {
            if (this.list[i].suspend != null) {
              console.log(i + "번 성공");
              let suspend = this.list[i].suspend.split("///");
              this.list[i].suspend = "";

              console.log(suspend);
              for (let j = 1; j < suspend.length; j++) {
                if (j + 1 == suspend.length) {
                  this.list[i].suspend += j + "번째 신고내용: " + suspend[j];
                } else {
                  this.list[i].suspend +=
                    j + "번째 신고내용: " + suspend[j] + ",\n";
                }
              }
            }
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //라디오 버튼
    changeKind(word) {
      this.kind = word;

      this.articleProc();
    },
    changeSuspend(tableId, status) {
      http
        .post("/article/changeSuspend", {
          param: {
            tableId: tableId,
            status: status,
            kind: this.kind,
          },
        })
        .then((res) => {
          if (res.data == "ok") {
            if (status === "Y") {
              alert("신고가 취소되엇습니다");
            } else {
              alert("삭제되었습니다.");
            }

            this.articleProc();
          } else {
            alert("문제가 발생했습니다.");
          }
        })
        .catch((err) => {
          console.log(err);
        });
    },
    //riview 승인 제거
    changeReview(){

    }
  },
  mounted() {
    this.articleProc();
  },
};
</script>

<style scoped>
th,
td {
  border-style: solid;
  border-width: 1px 1px 0px 1px;
}
.wrapper {
  display: flex;
  flex-direction: column;
}
.container {
  width: 90%;
  padding: 40px;
}
.susRadio {
  font-size: 20px;
  padding-bottom: 10px;
}
.susTable {
  margin: 0 auto;
  text-align: center;
  vertical-align: middle;
  width: 800px;
}
.susTitle {
  padding: 0px;
  padding-right: 20px;
  text-align: center;
  font-size: 20px;
  border-style: solid;
  border-width: 0px 0px 1px 0px;
  border-color: gray;
}
.susTitleNum {
  width: 99px;
  padding: 20px 0px;
  margin: 0px;
  border-right: 1px solid gray;
}
.susTitleDay {
  width: 300px;
  padding: 20px 0px;
  border-right: 1px solid gray;
}
.susTitleContents {
  width: 700px;
  padding: 0px 20px;
  display: block;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
.susBody {
  text-align: left;
  border-bottom: 1px solid gray;
}
.susBodyList {
  width: 100%;
  padding: 5px;
}
.susBodyList strong {
  font-size: 23px;
}
.susBodyBtn {
  width: 100%;
  text-align: center;
}
.susBodyBtn button {
  margin: 20px 50px;
}
</style>
