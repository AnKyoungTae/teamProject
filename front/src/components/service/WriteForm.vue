<template>
  <div class="container" style="width: 1000px">
    <br />{{ parantId }}<br />
    <!-- 타이틀 -->
    <div class="writeBoardBtn" v-if="parantId == null">
      <div style="font-size: 20px; text-align: left">구분</div>
      <div style="font-size: 20px; text-align: left">글 공개</div>
    </div>
    <div
      class="writeBoardBtn"
      style="margin-bottom: 20px"
      v-if="parantId == null"
    >
      <select style="width: 150px; height: 40px" v-model="boardId">
        <option value disabled selected>선택해주세요</option>
        <option>가게</option>
        <option>딜리버리 주문</option>
        <option>제품/품질/서비스</option>
        <option>기타</option>
      </select>
      <button
        style="height: 40px"
        class="btn btn-outline-secondary"
        type="button"
        aria-expanded="false"
        @click="privateStatus"
      >
        {{ statusMessage }}
      </button>
    </div>
    <div class="writeBoard">
      <div style="font-size: 20px; text-align: left">제목</div>
      <input
        style="margin-bottom: 20px"
        type="text"
        class="form-control"
        aria-label="Text input with dropdown button"
        placeholder="제목을 입력해 주세요."
        v-model="title"
      />
      <!-- 문의 본문작성 -->
      <div style="font-size: 20px; text-align: left">내용</div>
      <textarea
        style="height: 400px"
        class="form-control"
        aria-label="With textarea"
        v-model="body"
      ></textarea>
    </div>

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

    <!-- 제출/취소 버튼 -->
    <div class="col-md-12 text-center">
      <button id="btn" class="btn btn-success" @click="writeRequest">
        작성
      </button>
      <router-link class="btn btn-primary" :to="{ name: 'Q&A' }"
        >취소</router-link
      >
    </div>
  </div>
</template>

<script>
import * as authAPI from "@/api/article.js";
import { mapMutations } from "vuex";
export default {
  data() {
    return {
      statusMessage: "공개",
      title: "",
      body: "",
      status: "Y",
      files: [], //업로드용 파일
      //baordId
      boardId: "",
      //답글필요
      parantId: 0,
    };
  },
  created() {
    this.$store.commit("SET_serviceCenters", 2);
    this.parantId = this.$route.params.articleId;
  },
  methods: {
    ...mapMutations(["SET_MODAL_LOGIN"]),
    privateStatus() {
      if (this.statusMessage === "공개") {
        this.statusMessage = "비공개";
        this.status = "H";
      } else {
        this.statusMessage = "공개";
        this.status = "Y";
      }
    },
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
    fileDeleteButton(index) {
      //보낼 파일에서 데이터 삭제
      this.files.pop(index);
    },
    //보내기 통신
    async writeRequest() {
      let pushBoardId = 7;
      //답글이 아니다
      if (this.parantId == undefined) {
        if (this.boardId == "") {
          return alert("구분을 선택하세요.");
        } else if (this.boardId == "가게") {
          pushBoardId = 4;
        } else if (this.boardId == "딜리버리 주문") {
          pushBoardId = 5;
        } else if (this.boardId == "제품/품질/서비스") {
          pushBoardId = 6;
        } else if (this.boardId == "기타") {
          pushBoardId = 3;
        }

        if (pushBoardId == 0) {
          return alert(pushBoardId);
        }

        if (this.title === "" || this.title == null) {
          return alert("제목을 입력하세요");
        }
        if (this.body === "" || this.body == null) {
          return alert("내용을 입력하세요");
        }
      }

      let params = {
        title: this.title,
        body: this.body,
        status: this.status,
        boardId: pushBoardId, //Q&A 개시판 3이상, 7은 답글
        parantId: this.parantId,
      };
      let articleId = 0;

      //파일 올리기
      await authAPI
        .writeProc(params) //axios 이동
        .then((res) => {
          if (res.data > 0) {
            articleId = res.data;
          }
          if (res.data == "no") {
            return (
              this.SET_MODAL_LOGIN(true), alert("비회원은 권한이 없습니다.")
            );
          } else if (res.data == 0) {
            return alert("다시 로그인 해주세요");
          }
        })
        .catch((err) => {
          console.log(err);
        });
      //이미지 있는지 확인
      if (articleId > 0 && this.files.length > 0) {
        let form = new FormData();
        let image;

        for (let i = 0; i < this.files.length; i++) {
          image = this.files[i].file;

          //이미지 업로드
          form.append("image", image);
        }
        //파일 보내기
        authAPI
          .write12(form, articleId)
          .then((res) => {
            if (res.data == 100) {
              alert("글을 작성하였습니다");
              return this.$router.push({ name: "Q&A" });
            } else if (res.data == 300) {
              return alert("글 작성 중 오류가 발생습니다");
            } else if (res.data == 400) {
              return alert("글 작성을 실패했습니다");
            } else {
              return alert("글 오류");
            }
          })
          .catch((err) => {
            console.log(err);
            console.log("에러");
          });
      } else {
        //이지미 없을 때
        alert("글을 작성하였습니다");
        return this.$router.push({ name: "Q&A" });
      }
    },
  },
};
</script>

<style scoped>
.writeBoard {
  display: flex;
  flex-direction: column;
}
.writeBoardBtn {
  display: flex;
  justify-content: space-between;
}
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
/* https://loy124.tistory.com/203        */
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
