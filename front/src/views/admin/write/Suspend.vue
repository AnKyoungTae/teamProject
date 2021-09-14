<template>
  <div class="wrapper">
    <div class="container" v-if="detail == true">
      <div>
        
        <div>
          원글<input
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
      <table
        v-if="kind == 'article'"
        style="margin: 0 auto; text-align: center; vertical-align: middle; table-layout: fixed; width:1340px;"
      >
        <tr style="background-color:gray;">
          <th style="width: 50px">#</th>
          <th style="width: 200px">날짜</th>
          <th style="width: 200px">제목</th>
          <th style="width: 400px">내용</th>
          <th style="width: 400px">신고</th>
          <th>관리</th>
        </tr>
        <tr v-for="(li, index) in list" :key="index">
          <td>{{ index + 1 }}</td>
          <td>
            {{ 
              li.regDate[5] + li.regDate[6] +
              "월 " +
              li.regDate[8] + li.regDate[9] +
              "일"  
            }}
          </td>
          <td><span class="text">{{ li.title }}</span></td>
          <td><span class="text">{{ li.body }}</span></td>
          <td><span class="text">{{ li.suspend }}</span></td>
          <td>
            <button
              type="button"
              class="btn btn-outline-primary"
              @click="suspendDetailList(list)"
            >
              상세정보
            </button>
          </td>
        </tr>
        <tr v-if="list.length == 0">
          <td colspan="6">신고된 글이 없습니다.</td>
        </tr>
      </table>

      <table
        v-else
        style="margin: 0 auto; text-align: center; vertical-align: middle; table-layout: fixed; width:1140px;"
      >
        <tr style="background-color:gray;">
          <th style="width: 50px">#</th>
          <th style="width: 200px">날짜</th>
          <th style="width: 400px">내용</th>
          <th style="width: 400px">신고 내용</th>
          <th>관리하기</th>
        </tr>
        <tr v-for="(li, index) in list" :key="index">
          <td>{{ index + 1 }}</td>
          <td>
            {{ 
              li.regDate[5] + li.regDate[6] +
              "월 " +
              li.regDate[8] + li.regDate[9] +
              "일"  
            }}
          </td>
          <td><span class="text">{{ li.body }}</span></td>
          <td><span class="text">{{ li.suspend }}</span></td>
          <td>
            <button
              type="button"
              class="btn btn-outline-primary"
              @click="suspendDetailList(list)"
            >
              상세정보
            </button>
          </td>
        </tr>
        <tr v-if="list.length == 0">
          <td colspan="6">신고된 글이 없습니다.</td>
        </tr>
      </table>
    </div>
    <div v-if="detail == false">
      <SuspendDetail :data="selectedSuspend"></SuspendDetail>
      {{ selectedSuspend }}
      <button
        style="font-size:20px;"
        type="button"
        class="btn btn-outline-danger"
        @click="changeSuspend(selectedSuspend.status, 'N')"
      >
        삭제
      </button>
      <button
        style="font-size:20px; margin:20px;"
        type="button"
        class="btn btn-outline-primary"
        @click="changeSuspend(selectedSuspend.status, 'Y')"
      >
        복귀
      </button>
      <button
        style="font-size:20px;"
        type="button"
        class="btn btn-outline-success"
        @click="clearDetail()"
      >
        목록
      </button>
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
            alert("삭제되었습니다.");
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
