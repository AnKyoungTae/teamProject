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
              <span @click="showNextImage" class="arrow left"></span>
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

      <div class="nameContainer">
        <span class="nameWrap">
          <p>가게 정보</p>
        </span>
      </div>

      <div class="shopInfoContainer">
        <div class="infoWrap">
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
            <div class="left">가게 설명</div>
            <div class="right" v-if="!isEditMode">{{ description }}</div>
            <div class="right" v-else>
              <textarea class="form-control" v-model="description"></textarea>
            </div>
          </div>
        </div>
      </div>

      <div class="nameContainer">
        <span class="nameWrap">
          <p>지도</p>
        </span>
      </div>
      <div class="shopMapContainer">
        <div class="addressContainer">
          <span> {{ storeInfo.address }}, {{ storeInfo.addressDetail }} </span>
        </div>
        <div id="staticMap"></div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
  props: ["storeInfo", "storeFiles", "isMyStore"],
  components: {},
  data() {
    return {
      dataLoaded: false, // 데이터로딩
      selectedImage: "",
      selectedIndex: 0, // 데이터 로드되면 selectImage 실행
      FileList: [],
      needDelFileIdList: [],
      isEditMode: false,
      description: "",
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
  width: 100%;
  margin: 5px;
  display: flex;
  justify-content: center;
  overflow: hidden;
  flex-flow: row;
  height: 600px;
}
.previewContainer {
  width: 500px;
  margin: 20px;
  height: 500px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.previewWrapper {
  background: tan;
  width: 500px;
  height: 400px;
  display: flex;
}
.previewWindow {
  background: wheat;
  width: 400px;
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
  position: absolute;
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
  margin: 20px;
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
  margin: 50px 0 100px 0;
  display: flex;
  justify-content: center;
}
.infoWrap {
  width: 80%;
}
.infoRow {
  background: rgb(215, 237, 245);
  min-height: 50px;
  display: flex;
  justify-content: center;
  align-content: flex-start;
  align-items: flex-start;
}
.infoRow > .left {
  margin: 20px;
  width: 100%;
  text-align: end;
  padding: 0 20px 0 20px;
  font-family: BMHANNAPro;
  font-weight: bolder;
  font-size: 2em;
  margin: 20px;
  border-bottom: 4px solid orange;
}
.infoRow > .right {
  margin: 20px;
  width: 100%;
  padding: 10px;
  font-family: BMHANNAPro;
  font-weight: 400;
  font-size: 1.2em;
}
.shopMapContainer {
  width: 100%;
  height: 800px;
  margin: 5px;
  display: flex;
  justify-content: center;
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
  width: 80%;
  height: 80%;
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
</style>
