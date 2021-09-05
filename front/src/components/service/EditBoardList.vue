<template>
  <div class="container" style="width: 1000px">
    <br />
    <br />
    <div>
      <button
        type="button"
        class="btn btn-primary"
        style="margin-right: 20px"
        @click="updateBoard"
      >
        수정
      </button>
      <button
        id="btn"
        type="button"
        class="btn btn-danger"
        @click="returnBoard"
      >
        뒤로가기
      </button>
    </div>
    <br />
    <!-- 타이틀 -->
    <table style="border: 2px solid gray">
      <thead id="title" style="text-align: center">
        <tr style="border-bottom: 1px solid gainsboro">
          <th
            class="boardTitle"
            colspan="3"
            style="text-align: left; padding: 20px"
          >
            <span>구분</span
            ><input
              type="text"
              style="padding-left: 50px; font-size: 25px"
              v-model.trim="title"
            />
          </th>
        </tr>
        <tr style="height: 40px">
          <th class="col-md-5" style="text-align: left; padding-left: 20px">
            {{ nickname }}
          </th>
          <th class="col-md-2">
            <i class="far fa-clock"></i>&nbsp;&nbsp;{{ regDate }}
          </th>
          <th class="col-md-1">
            <i class="far fa-eye"></i>&nbsp;&nbsp;{{ hit }}
          </th>
        </tr>
      </thead>
    </table>

    <!-- 첨부파일 -->
    <div class="room-deal-information-container">
      <div class="room-file-upload-wrapper">
        <div v-if="!files.length" class="room-file-upload-example-container">
          <div class="room-file-upload-example">
            <div class="room-file-notice-item room-file-upload-button">
              <div class="image-box">
                <label for="file">사진 등록</label>
                <input
                  type="file"
                  id="file"
                  ref="files"
                  @change="imageAddUpload"
                  multiple
                />
              </div>
            </div>
          </div>
        </div>
        <div v-else class="file-preview-content-container">
          <div class="file-preview-container">
            <div
              v-for="(file, index) in this.files"
              :key="index"
              class="file-preview-wrapper"
            >
              <div class="file-close-button" @click="fileDeleteButton(index)">
                x
              </div>
              <img :src="file.name" />
            </div>
            <div class="file-preview-wrapper-upload">
              <div class="image-box">
                <label for="file">추가 사진 등록</label>
                <input
                  type="file"
                  id="file"
                  ref="files"
                  @change="imageAddUpload"
                  multiple
                />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 본문 -->
    <div id="textarea" class="input-group" style="text-align: left">
      <div class="form-control" style="padding: 30px">
        <textarea v-model.trim="body" cols="30" rows="10"></textarea>
      </div>
    </div>
    <br />
    <!-- 목록이동 버튼  -->
    <router-link class="btn btn-primary" :to="{ path: '/home' }">
      목록
    </router-link>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";

export default {
  props: ["list", "image"],

  data() {
    return {
      writerId: null,
      articleId: null,
      title: null,
      body: null,
      nickname: null,
      regDate: null,
      hit: null,

      //이미지
      files: [],
      //삭제이밈지
      imageDelete: [],

      //게시판 종휴
      board: null,
    };
  },
  created() {
    this.writerId = this.list.writerId;
    this.articleId = this.list.articleId;
    this.title = this.list.title;
    this.body = this.list.body;
    this.nickname = this.list.nickname;
    this.regDate = this.list.regDate;
    this.hit = this.list.hit;

    for (let i = 0; i < this.image.length; i++) {
      this.files.push({ file: this.image[i].fileId, name: this.image[i].name });
    }
    //페이지 정보
    this.boardListPage();
  },
  methods: {
    boardListPage() {
      //게시물 불러오기
      this.board = this.$route.query.board; //게시판 종류  (1.공지사항, 2.Q&A, 3.FQA)
    },
    //return board
    returnBoard() {
      return this.$emit("returnEdit");
    },

    // 새 이미지 삽입
    imageAddUpload() {
      this.files.push(
        //이미지 업로드
        {
          //실제 파일
          file: this.$refs.files.files[0],
          //이미지 프리뷰
          name: URL.createObjectURL(this.$refs.files.files[0]),
          //삭제및 관리를 위한 number
        }
      );
    },
    //원본 파일 이미지 삭제
    fileDeleteButton(index) {
      if (this.files[index].file > 0) {
        this.imageDelete.push(this.files[index].file);
      }
      this.files.pop(index);
    },
    //수정하기
    async updateBoard() {
      let params = {
        //수정 필요한 데이터들
        articleId: this.articleId,
        title: this.title,
        body: this.body,
        writerId: this.writerId,
        imageDelete: this.imageDelete, //삭제 이미지
      };
      let articleId = 0;

      await authAPI
        .update(params)
        .then((res) => {
          console.log(res);
          if (res.data > 0) {
            articleId = res.data;
          }
          if (res.data == "no") {
            alert("비회원은 권한이 없습니다.");
            this.SET_MODAL_LOGIN(true);
          } else if (res.data == "ok") {
            return alert("수정 되었습니다.");
          } else if (res.data == 0) {
            return alert("다시 로그인 해주세요");
          }
        })
        .catch((err) => {
          console.log(err);
          console.log(params);
        });

      let form = new FormData();
      let img;
      let num = 0;

      for (let i = 0; i < this.files.length; i++) {
        img = this.files[i].file;

        //이미지 업로드 articleId = res.data
        if (!(img > 0)) {
          form.append("image", img);
          num++;
        }
      }

      if (num > 0) {
        //파일 보내기
        authAPI
          .write12(form, articleId)
          .then((res) => {
            if (res.data == 100) {
              alert("글을 작성하였습니다");
              return this.$emit("returnEdit");
            } else if (res.data == 300) {
              alert("글 작성 중 오류가 발생습니다");
            } else if (res.data == 400) {
              alert("글 작성을 실패했습니다");
            } else {
              alert("글 오류");
            }
          })
          .catch((err) => {
            console.log(err);
            console.log("에러");
          });
      } else {
        //이지미 없을 때

        alert("글을 작성하였습니다");
        return this.$emit("returnEdit");
        // this.$router.push({name: 'BoardList', query: { "board": this.board, "articleId": this.articleId}})
      }
    },
  },
};
</script>

<style>
.main-container {
  width: 1200px;
  height: 400px;
  margin: 0 auto;
}

.room-deal-information-container {
  margin-top: 50px;
  color: #222222;
  border: 1px solid #dddddd;
}

.room-deal-information-title {
  text-align: center;
  font-size: 18px;
  line-height: 60px;
  border-bottom: 1px solid #dddddd;
}

.room-deal-information-content-wrapper {
  min-height: 50px;
  display: flex;
}

.room-deal-informtaion-content-title {
  font-size: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  background-color: #f9f9f9;
}

.room-deal-information-content {
  width: 100%;
  font-size: 14px;
}

.room-deal-option-selector {
  display: flex;
  align-items: center;
  padding: 15px;
}

.room-deal-option-item {
  width: 100px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #cccccc;
  border-radius: 5px;
  cursor: pointer;
}

.room-deal-option-item:last-child {
  margin-left: 10px;
}

.room-deal-option-notice {
  margin-left: auto;
  font-size: 14px;
  color: #888888;
}

.room-deal-option-item-deposit {
  margin-left: 10px;
}

.room-deal-information-wrapper {
  display: flex;
  flex-direction: column;
}

.room-deal-information-option {
  padding: 10px;
  display: flex;
  align-items: center;
}

.room-deal-information-option:last-child {
  border-bottom: 1px solid #dddddd;
}

.room-deal-information-item-type {
  font-size: 13px;
  color: #fff;
  background-color: #61b6e5;
  min-width: 50px;
  height: 26px;
  display: flex;
  justify-content: center;
  align-items: center;
  border-radius: 3px;
}

.room-deal-information-item-wrapper {
  display: flex;
  align-items: center;
  margin-left: 10px;
  height: 46px;
  width: 100%;
}

.room-deal-information-item-wrapper > input {
  border: 1px solid #dddddd;
  width: 140px;
  height: 100%;
  padding: 0 15px;
  font-size: 15px;
}

.room-deal-inforamtion-won {
  margin: 0 10px;
}

.room-deal-information-example {
  color: #888888;
}

.room-deal-information-option:not(:first-child) {
  margin-top: 10px;
}

.room-deal-inforamtion-divide {
  font-size: 22px;
  margin: 0 8px;
  color: #222222;
  font-weight: 100;
}

.room-deal-close-button-wrapper {
  margin-left: auto;
  cursor: pointer;
}

.room-deal-close-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  background-color: #666666;
  color: rgb(220, 220, 220);
}

.room-deal-cliked {
  background-color: rgb(235, 235, 235);
  color: rgb(170, 170, 170);
}

.room-file-upload-example {
  height: 100%;
}

.room-write-content-container {
  border-top: 1px solid #dddddd;
  min-height: 260px;
}

.room-picture-notice {
  margin: 20px;
  padding: 20px 40px;
  border: 1px solid #dddddd;
}

.file-preview-content-container {
  height: 100%;
}

.room-file-upload-wrapper {
  margin: 20px;
  border: 1px solid #dddddd;
  background-color: #f4f4f4;
  min-height: 350px;
  font-size: 15px;
  color: #888888;
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
}

.room-file-upload-example-container {
  display: flex;
  align-items: center;
  justify-content: center;
  /* height: 100%;
  width: 100%; */
}

.room-file-image-example-wrapper {
  text-align: center;
}

.room-file-notice-item {
  margin-top: 5px;
  text-align: center;
}

.room-file-notice-item-red {
  color: #ef4351;
}

.image-box {
  margin-top: 30px;
  padding-bottom: 20px;
  text-align: center;
}

.image-box input[type="file"] {
  position: absolute;
  width: 0;
  height: 0;
  padding: 0;
  overflow: hidden;
  border: 0;
}

.image-box label {
  display: inline-block;
  padding: 10px 20px;
  background-color: #232d4a;
  color: #fff;
  vertical-align: middle;
  font-size: 15px;
  cursor: pointer;
  border-radius: 5px;
}

.file-preview-wrapper {
  padding: 10px;
  position: relative;
}

.file-preview-wrapper > img {
  position: relative;
  width: 190px;
  height: 130px;
  z-index: 10;
}

.file-close-button {
  position: absolute;
  /* align-items: center; */
  line-height: 18px;
  z-index: 99;
  font-size: 18px;
  right: 5px;
  top: 10px;
  color: #fff;
  font-weight: bold;
  background-color: #666666;
  width: 20px;
  height: 20px;
  text-align: center;
  cursor: pointer;
}

.file-preview-container {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}

.file-preview-wrapper-upload {
  margin: 10px;
  padding-top: 20px;
  background-color: #888888;
  width: 190px;
  height: 130px;
}

.room-write-button-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #222222;
}

.room-write-button-wrapper > div {
  width: 160px;
  height: 50px;
  border: 1px solid #dddddd;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  cursor: pointer;
}

.room-write-button {
  margin-left: 15px;
  color: #fff;
  background-color: #1564f9;
}

.room-write-button:hover {
  opacity: 0.8;
}
</style>
