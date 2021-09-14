<template>
  <div class="wrapper">
    <div class="container" v-if="detail == true">
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
        </div>
      </div>


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
                          li.regDate[0] + li.regDate[1] + li.regDate[2] + li.regDate[3] + "년 " + 
                          li.regDate[5] + li.regDate[6] + "월 " +
                          li.regDate[8] + li.regDate[9] + "일 " +
                          li.regDate[11] + li.regDate[12] + "시 " +
                          li.regDate[14] + li.regDate[15] + "분 " +
                          li.regDate[17] + li.regDate[18] + "초"
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
      </div>

    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import SuspendDetail from "@/views/admin/write/SuspendDetail.vue"

export default {
  components: { SuspendDetail },
  data() {
    return {
      kind: "article",
      list: [],
      detail: true,
      selectedSuspend: "",
    };
  },
  methods: {
    articleProc() {
      http
        .post("/article/suspendArticle", this.kind)
        .then((res) => {
          console.log(res.data);
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
            if(status === 'Y'){
              
              alert("신고가 취소되엇습니다")
            }else{
            
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
    suspendDetailList(list) {
      this.selectedSuspend = list;
      this.detail = false;

    },
    clearDetail() {
      this.detail = true;
      this.selectedSuspend = "";
    },
  },
  mounted() {
    this.articleProc();
  },
};
</script>

<style scoped>
th,
td {

  border: 1px solid #91afba;
  font-size:20px;
}
tr {
  height:50px;

}
.wrapper {
  display: flex;
  flex-direction: column;
}
.container {
  width: 90%;
  padding: 40px;
}

.text {

  display:block;
  overflow: hidden; 
  text-overflow: ellipsis;
  white-space: nowrap;

  padding-left:10px;
  padding-right:5px;

}
</style>
