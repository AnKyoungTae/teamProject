<template>
  <div class="outLineWrapper">
    <div class="wrapper">
      <div class="btn">
        <div v-if="isEditMode">
          <span class="editBtns" @click="modifyImages">수정완료</span>
          <span class="editBtns" @click="rollback">취소하기</span>
        </div>
        <div v-else>
          <span class="editBtns" @click="toggleEdit">수정하기</span>
        </div>
      </div>
      <hr />
      <div class="nameContainer">
        <span class="nameWrap">
          <p v-if="dataLoaded">{{ storeInfo.name }}</p>
        </span>
      </div>

      <div class="shopImagesContainer">
        <div class="previewContainer">
          <div class="previewBottomTap"></div>
          <div class="previewWrapper">
            <div class="previewChangeWapper">
              <span @click="showPrevImage" class="arrow left"></span>
            </div>
            <div class="previewWindow">
              <img :src="selectedImage.path" />
            </div>
            <div class="previewChangeWapper">
              <span @click="showNextImage" class="arrow right"></span>
            </div>
          </div>
          <div class="previewBottomTap">
            <span
              @click="delFile"
              v-if="isEditMode"
              class="btn btn-outline-danger m-1 p-2"
              >삭제</span
            >
          </div>
        </div>
        <!-- 수정하기모드일때 -->
        <div class="imageListContainer" v-if="isEditMode">
          <div class="addImageListWrapper">
            <input type="file" id="file" @input="addFile" />
            <label for="file">추가하기</label>
          </div>
          <!-- 존재하는 파일 -->
          <div
            class="imageListWrapper"
            v-for="(file, index) in FileList"
            :key="index"
            @click="selectImage(index)"
            :class="selectedIndex == index ? 'selectedImage' : ''"
          >
            <div class="imageWrapper">
              <img :src="file.path" />
            </div>
          </div>
        </div>
      </div>
      <div class="Info_Map">
        <div style="width: 100%">
          <div class="nameContainer nameContainer1">
            <span class="nameWrap">
              <p>가게 정보</p>
            </span>
          </div>

          <div class="shopInfoContainer">
            <div class="infoWrap">
              <div class="infoRow">
                <div class="left">상호명</div>
                <div class="right">{{ storeInfo.name }}</div>
              </div>
              <div class="infoRow">
                <div class="left">가게 종류</div>
                <div class="right">{{ storeInfo.storeKind }}</div>
              </div>
              <div class="infoRow">
                <div class="left">등록일</div>
                <div class="right">{{ dateFormat(storeInfo.regDate) }}</div>
              </div>
              <div class="infoRow">
                <div class="left">마지막 정보 수정일</div>
                <div class="right">{{ dateFormat(storeInfo.updateDate) }}</div>
              </div>
              <div class="infoRow">
                <div class="left">현재 영업상태</div>
                <div class="right">{{ statusFormat(storeInfo.status) }}</div>
              </div>
              <div class="infoRow">
                <div class="left">가게 연락처</div>
                <div class="right">{{ storeInfo.phone }}</div>
              </div>
              <div class="infoRow">
                <div class="left">가게 주소</div>
                <div class="right">
                  {{ storeInfo.address }}, {{ storeInfo.addressDetail }}
                </div>
              </div>
              <div class="infoRow">
                <div class="left">가게 설명</div>
                <div class="right" v-if="!isEditMode">{{ description }}</div>
                <div class="right" v-else>
                  <textarea
                    class="form-control"
                    v-model="description"
                  ></textarea>
                </div>
              </div>
            </div>
          </div>
          <div class="infoRow">
            <div class="left">가게 주소</div>
            <div class="right">
              {{ storeInfo.address }} <br />
              {{ storeInfo.addressDetail }}
            </div>
          </div>
          <div class="shopMapContainer">
            <div id="staticMap"></div>
            <div class="addressContainer">
              <span>
                {{ storeInfo.address }}, {{ storeInfo.addressDetail }}
              </span>
            </div>
          </div>
        </div>
      </div>
      <div>
        <span class="nameWrap">
          <p>리뷰관리</p>
        </span>
        <div>
          <div class="review2">
            <div style="float: left; width: 70%">
              <!-- 글의 수 카운팅 -->
              <p class="review2-p">
                리뷰
                <span style="font-weight: 600"><!--{{ totalCount }}-->0</span>개
                · 사장님 댓글
                <span style="font-weight: 600"
                  ><!--{{ totalCountHasReply }}--></span
                >개
              </p>
            </div>
          </div>
          <div class="row">
            <!-- 맨 윗줄 카테고리영역 -->
            <div
              class="col btn btn-light spread-underline"
              @click="setStatusOption('recent')"
            >
              <span :class="[this.showOption === 'recent' ? 'choiced' : '']">
                최신순보기
              </span>
            </div>
            <div
              class="col btn btn-light spread-underline"
              @click="setStatusOption('comment')"
            >
              <span :class="[this.showOption === 'score' ? 'choiced' : '']">
                사장님댓글 없음
              </span>
            </div>
          </div>
          <hr style="margin-top: 0px" />
          <!-- 리뷰로우 -->
          <div v-if="!pageLoaded">
            <!-- 페이지 로딩되지않았을 때 -->
            <div class="spinner-border" role="status">
              <span class="visually-hidden">Loading...</span>
            </div>
          </div>
          <div v-else-if="Array.isArray(reviewList) && reviewList.length > 0">
            <div v-for="(review, index) in reviewList" :key="index" class="m-2">
              <review-row :data="review" :isOwner="isOwner"></review-row>
            </div>
            <nav aria-label="Page navigation">
              <ul class="pagination justify-content-center">
                <li class="page-item" :class="{ disabled: !hasPreviousPage }">
                  <a
                    class="page-link"
                    aria-disabled="true"
                    @click="requestPage(firstPageOfthisIndex - 1)"
                    :class="{ 'available-link': hasPreviousPage }"
                    >이전목록</a
                  >
                  <!-- 현재 단락의 가장 첫번째 페이지 -1을 요청해야함. -->
                </li>
                <!-- 페이지순번 -->
                <div v-for="(index, i) in maxIndex" :key="i">
                  <div
                    v-if="index + (currentIndex - 1) * maxIndex <= totalPages"
                  >
                    <li
                      class="page-item"
                      v-if="
                        (this.currentIndex - 1) * this.showindex + index !=
                        currentPage
                      "
                    >
                      <a
                        class="page-link available-link"
                        @click="
                          requestPage(index + (currentIndex - 1) * maxIndex)
                        "
                        >{{ index + (currentIndex - 1) * maxIndex }}</a
                      >
                    </li>
                    <li class="page-item active" v-else>
                      <span class="page-link">{{
                        index + (currentIndex - 1) * maxIndex
                      }}</span>
                    </li>
                  </div>
                </div>

                <li class="page-item" :class="{ disabled: !hasNextPage }">
                  <a
                    class="page-link"
                    @click="requestPage(lastPageOfthisIndex + 1)"
                    :class="{ 'available-link': hasNextPage }"
                    >다음목록</a
                  >
                </li>
              </ul>
            </nav>
          </div>
          <div v-else>등록된 리뷰가 없습니다.</div>
          <hr />
        </div>
      </div>
      <div class="nameContainer">
        <span class="nameWrap">
          <p>리뷰</p>
        </span>
      </div>
      <div v-if="Array.isArray(reviewList) && reviewList.length > 0">
        <div v-for="(review, index) in reviewList" :key="index" class="m-2">
          <review-row :data="review" isOwner="true"></review-row>
        </div>
      </div>
      <div v-else>등록된 리뷰가 없습니다</div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import ReviewRow from "@/components/adminComponent/ReviewRow.vue";

export default {
  props: ["storeInfo", "storeFiles", "isMyStore"],
  components: { ReviewRow },
  data() {
    return {
      dataLoaded: false, // 데이터로딩
      selectedImage: "",
      selectedIndex: 0, // 데이터 로드되면 selectImage 실행
      FileList: [],
      needDelFileIdList: [],
      isEditMode: false,
      description: "",
      reviewList: [],
    };
  },
  mounted() {
    if (window.kakao && window.kakao.maps) {
      //
    } else {
      const script = document.createElement("script");
      script.onload = () => kakao.maps.load(this.initMap);
      script.src =
        "http://dapi.kakao.com/v2/maps/sdk.js?autoload=false&appkey=0feaa40f6d94ad4428a4f4f8a6cab340&libraries=services,clusterer,drawing";
      document.head.appendChild(script);
    }
    this.$nextTick(() => {
      if (this.storeFiles != null) {
        this.initFileList(this.storeFiles);
        this.selectImage(0);
      }
      this.initMap();
      this.description = this.storeInfo.body;
      this.requestReviewList();
      this.dataLoaded = true;
    });
  },
  methods: {
    selectImage(index) {
      this.selectedIndex = index;
      this.selectedImage = this.FileList[index];
    },
    showNextImage() {
      if (this.FileList.length > this.selectedIndex + 1) {
        this.selectImage(this.selectedIndex + 1);
      } else {
        this.selectImage(0);
      }
    },
    showPrevImage() {
      this.selectedIndex == 0
        ? this.selectImage(this.FileList.length - 1)
        : this.selectImage(this.selectedIndex - 1);
    },
    addFile(e) {
      this.FileList = [
        ...this.FileList,
        {
          file: e.target.files[0],
          path: URL.createObjectURL(e.target.files[0]),
          num: this.FileList.length,
        },
      ];
    },
    delFile() {
      let pointer = this.FileList[this.selectedIndex];
      // 이미 있는 파일일때, 삭제예정배열에 아이디만 저장해놓고, 업로드에정인것인 fileList에서 제거.
      if (pointer.file.fileId) {
        // 이미 존재한다면, fileId 가있음.
        this.needDelFileIdList.push(pointer.file.fileId);
      }
      // 업로드 예정 취소라면,
      this.FileList.splice(this.selectedIndex, 1);
      this.selectedImage = "";
      this.selectImage(0);
    },
    initFileList(storeFiles) {
      for (let i = 0; i < storeFiles.length; i++) {
        this.FileList.push({
          file: storeFiles[i],
          path: storeFiles[i].name,
          num: i,
        });
      }
    },
    modifyImages() {
      if (!confirm("이대로 수정하시겠습니까?")) {
        return;
      }
      console.log("수정을 진행합니다.");
      const formData = new FormData();
      let addedFiles = this.FileList.filter((file) => {
        if (file.file.fileId) {
          return false;
        }
        return true;
      }).map((data) => {
        return data.file;
      });
      formData.append("description", this.description);
      formData.append("delFileIdList", this.needDelFileIdList);
      addedFiles.forEach((file) => {
        formData.append("fileList", file);
      });
      http
        .post("/store/modifyStore", formData, {
          headers: {
            "Content-Type": false,
            processData: false,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            alert("수정이 완료되었습니다.");
            // 다시 로그인시킨다.
            this.isEditMode = false;
          }
        });
    },
    initMap() {
      var marker = {
        position: new kakao.maps.LatLng(
          this.storeInfo.localy,
          this.storeInfo.localx
        ),
        text: this.storeInfo.name,
      };

      var staticMapContainer = document.getElementById("staticMap"), // 이미지 지도를 표시할 div
        staticMapOption = {
          center: new kakao.maps.LatLng(
            this.storeInfo.localy,
            this.storeInfo.localx
          ), // 이미지 지도의 중심좌표
          level: 3, // 이미지 지도의 확대 레벨
          marker: marker, // 이미지 지도에 표시할 마커
        };

      // 이미지 지도를 생성합니다
      var staticMap = new kakao.maps.StaticMap(
        staticMapContainer,
        staticMapOption
      );
    },
    toggleEdit() {
      console.log(this.isEditMode);
      this.isEditMode = !this.isEditMode;
    },
    dateFormat(date) {
      let arr = date.split(" ");
      let YMD = arr[0].split("-");
      if (YMD[1].length > 1) {
        YMD[1] = YMD[1].split("")[1];
      }
      return YMD[0] + " 년 " + YMD[1] + " 월 " + YMD[2] + " 일 ";
    },
    statusFormat(status) {
      return status == "Y" ? "정상 영업중" : "영업중이 아님";
    },
    rollback() {
      this.description = this.storeInfo.body;
      this.FileList = [];
      this.initFileList(this.storeFiles);
      this.selectImage(0);
      this.toggleEdit();
    },
    requestReviewList() {
      const storeId = parseInt(this.storeInfo.storeId);
      const data = {
        listPerPage: 100,
        currentPage: 1,
        storeId: storeId,
        showOption: "recent",
      };
      http.post("/review/getReviewList", data).then((res) => {
        if (res.status === 200) {
          this.reviewList = res.data;
        }
      });
    },
  },
  computed: {},
};
</script>

<style scoped>
@font-face {
  font-family: "BMHANNAPro";
  src: url("https://cdn.jsdelivr.net/gh/projectnoonnu/noonfonts_seven@1.0/BMHANNAPro.woff")
    format("woff");
  font-weight: normal;
  font-style: normal;
}
.outLineWrapper {
  display: flex;
  width: 100%;
  justify-content: center;
  padding: 20px;
  min-width: 900px;
}
.wrapper {
  width: 80%;
  margin-top: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 10px;
}
.nameContainer {
  width: 100%;
  height: 100px;
  margin: 5px;
  display: flex;
  justify-content: end;
  align-items: center;
  margin: 20px;
}
.nameContainer1 {
  margin: 0px;
}
.nameWrap {
  padding: 0 20px 0 20px;
  font-family: BMHANNAPro;
  font-weight: bolder;
  font-size: 3em;
  margin: 25px;
}
.nameWrap > p {
  border-bottom: 12px solid #ffda77;
  padding: 0 0 0.2em 0;
}
.shopImagesContainer {
  width: 95%;
  margin: 5px;
  display: flex;
  flex-flow: row;
  flex-direction: row;
  flex-wrap: wrap;
  justify-content: center;
  align-content: center;
}
.previewContainer {
  width: 410px;
  height: 410px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.previewWrapper {
  background: tan;
  height: 350px;
  display: flex;
}
.previewWindow {
  background: wheat;
  width: 350px;
}
.previewWindow > img {
  display: block;
  width: 100%;
  height: 100%;
  padding: 10px;
  transition: 2s;
}
.previewChangeWapper {
  width: 50px;
  height: 400px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.arrow {
  width: 3vmin;
  height: 3vmin;
  background: transparent;
  border-top: 1vmin solid white;
  border-right: 1vmin solid white;
  box-shadow: 0 0 0 lightgray;
  transition: all 200ms ease;
}
.arrow.left {
  transform: translate3d(0, -50%, 0) rotate(-135deg);
}
.arrow.right {
  transform: translate3d(0, -50%, 0) rotate(45deg);
}
.arrow:hover {
  border-color: orange;
  box-shadow: 0.5vmin -0.5vmin 0 white;
}
.arrow:before {
  content: "";
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-40%, -60%) rotate(45deg);
  width: 200%;
  height: 200%;
}
.previewBottomTap {
  width: 100%;
  height: 10%;
}
.imageListContainer {
  width: 470px;
  margin-left: 60px;
  margin-top: 15px;
  height: 520px;
  display: flex;
  flex-flow: wrap;
  overflow-y: auto;
  overflow-x: hidden;
  justify-content: flex-start;
  align-items: flex-start;
  align-content: flex-start;
  background: lightgray;
}
.selectedImage {
  border: 2px solid red;
  transition: 0.5s;
}
.addImageListWrapper {
  background: tan;
  width: 150px;
  padding: 5px;
  height: 150px;
  margin: 2px;
  display: flex;
  justify-content: center;
  align-items: center;
}
.addImageListWrapper input[type="file"] {
  width: 0;
  height: 0;
  overflow: hidden;
}
.addImageListWrapper > label {
  padding: 40px;
}
.addImageListWrapper > label:hover {
  cursor: pointer;
}
.imageListWrapper {
  background: tan;
  width: 150px;
  padding: 5px;
  height: 150px;
  margin: 2px;
}
.imageWrapper {
  overflow: hidden;
}
.imageWrapper > img {
  width: 140px;
  height: 140px;
}
.shopInfoContainer {
  width: 100%;
  display: flex;
  justify-content: center;
}
.infoWrap {
  width: 100%;
}
.infoRow {
  background: rgb(215, 237, 245);
  min-height: 50px;
  display: flex;
  justify-content: center;
  align-content: flex-start;
  align-items: flex-start;
  flex-wrap: wrap;
}
.infoRow > .left {
  margin-bottom: 10px;
  width: 100%;
  text-align: start;
  padding: 0 20px 0 20px;
  font-family: BMHANNAPro;
  font-weight: bolder;
  font-size: 25px;
  border-bottom: 4px solid orange;
}
.infoRow > .right {
  width: 100%;
  font-family: BMHANNAPro;
  font-weight: 400;
  margin-bottom: 20px;
  font-size: 1.2em;
}
.shopMapContainer {
  width: 100%;
  height: 800px;
  margin-left: 5px;
  display: flex;
  align-items: center;
  flex-direction: column;
}
.addressContainer {
  margin: 20px;
  width: 100%;
  padding: 10px;
  font-family: BMHANNAPro;
  font-weight: 400;
  font-size: 1.2em;
}
#staticMap {
  width: 100%;
  height: 70%;
  text-align: center;
  border: 1px solid gray;
}
.editBtns {
  font-size: 1.5em;
  padding: 0.4em 1em 0.4em 1em;
  font-weight: bolder;
  background: #ffda77;
  border: 2px solid #fd3a69;
  color: black;
  border-radius: 10px 10px 10px 0px;
  margin: 20px;
  transition: 0.2s;
}
.editBtns:hover {
  background: orange;
  color: white;
}
.Info_Map {
  display: flex;
  width: 1400px;
  height: 750px;
}

.modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.5);
  display: table;
  transition: opacity 0.3s ease;
}
.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.review {
  width: 100%;
  border: 1px solid lightgray;
}
.review-left {
  font-size: 40px;
}

.review-2 {
  margin-left: 10px;
}

.review2 {
  width: 100%;
  text-align: left;
  background-color: lightgray;
  height: 50px;
  line-height: 40px;
  padding-left: 20px;
}
.review2-p {
  line-height: middle;
}

.review2-div {
  border: 2px solid rgb(170, 170, 170);
  background-color: #fafafa;
  text-align: center;
  border-radius: 25px;
  padding: -5px;
}
.available-link {
  cursor: pointer;
}
.choiced {
  text-decoration-line: underline;
  text-underline-position: under;
}
.spread-underline {
  color: #333;
  text-decoration: none;
  display: inline-block;
  padding: 10px 0;
  position: relative;
}
.spread-underline:after {
  background: none repeat scroll 0 0 transparent;
  bottom: 0;
  content: "";
  display: block;
  height: 4px;
  left: 50%;
  position: absolute;
  background: #ffb000;
  transition: width 0.3s ease 0s, left 0.3s ease 0s;
  width: 0;
}
.spread-underline:hover:after {
  width: 100%;
  left: 0;
}
.reviewWrite:hover {
  cursor: pointer;
  background-color: #ffa799;
}

.review2-p {
  margin: 0px;
  height: 60px;
  line-height: 50px;
}
</style>
