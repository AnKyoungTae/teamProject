<template>
  <h2 class="text-center">게시물작성</h2>
  <br /><br /><br /><br />
  <!-- 타이틀 -->
  <div class="input-group">
    <button
      class="btn btn-outline-secondary"
      type="button"
      aria-expanded="false"
      @click="privateStatus"
    >
      {{ statusMessage }}
    </button>

    <input
      type="text"
      class="form-control"
      aria-label="Text input with dropdown button"
      placeholder="제목을 입력해 주세요."
      v-model="title"
    />
  </div>

  <!-- 문의 본문작성 -->
  <div id="textarea" class="input-group">
    <span class="input-group-text">상세내용</span>
    <textarea
      class="form-control"
      aria-label="With textarea"
      v-model="body"
    ></textarea>
  </div>

  <!-- 첨부파일 -->
  <div class="input-group">
    <input
      type="imageFile"
      class="form-control"
      id="inputGroupFile04"
      aria-describedby="inputGroupFileAddon04"
      aria-label="Upload"
    />
    <button
      class="btn btn-outline-secondary"
      type="button"
      id="inputGroupFileAddon04"
    >
      파일추가
    </button>
  </div>

  <!-- 제출/취소 버튼 -->
  <div class="col-md-12 text-center">
    <button
      id="btn"
      type="button"
      class="btn btn-success"
      @click.prevent="writeRequest"
    >
      작성
    </button>
    <router-link class="btn btn-primary" :to="{ name: 'Q&A' }"
      >취소</router-link
    >
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";

export default {
  data() {
    return {
      statusMessage: "공개",
      title: null,
      body: null,
      status: "a",
    };
  },
  methods: {
    privateStatus() {
      if (this.statusMessage === "공개") {
        this.statusMessage = "비공개";
      } else {
        this.statusMessage = "공개";
      }
    },
    writeRequest() {
      if (this.statusMessage == "공개") {
        this.status = "a";
      } else {
        this.status = "b";
      }

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
        boardId: "3", //Q&A 개시판 2
      };
      console.log(params);

      authAPI
        .writeProc(params) //axios 이동
        .then((res) => {
          alert("등록되었습니다.");
          location.href = "/qna"; //페이지 이동

          console.log(res);
        })
        .catch((err) => {
          console.log(err);
        });
    },
  },
};
</script>

<style scoped>
#textTitle {
  height: 30px;
  width: 1000px;
  margin: 5px;
}

#textarea {
  height: 250px;
  width: 100%;
  margin: 5px;
  resize: none;
}
#btn {
  margin: 30px;
}
</style>
