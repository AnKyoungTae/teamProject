<template>
  <div id="storeRegister" class="m-4">
    <h1>가게 등록 페이지</h1>
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div class="store-write-notification-wra">
      <ul class="store-write-wrapper">
        <li>알림사항</li>
      </ul>
      <!-- 이름 -->
      <div class="input-group-sm mb-3">
        <span class="input-group-text" id="store_name"
          >가게 이름을 입력해주세요</span
        >
        <input
          type="text"
          class="form-control"
          placeholder="예) 또봉이 치킨"
          aria-label="storename"
          aria-describedby="store_name"
          v-model="store_name"
        />
      </div>
      <!-- 번호 -->
      <div class="input-group-sm mb-3">
        <span class="input-group-text" id="store_contact"
          >가게 연락처를 입력해주세요.</span
        >
        <input
          type="text"
          class="form-control"
          placeholder="예) 032-123-1234"
          aria-label="storecontact"
          aria-describedby="store_contact"
          v-model="store_contact"
        />
      </div>
      <!-- 가게종류 -->
      <div class="input-group-sm mb-3">
        <span class="input-group-text" id="store_type"
          >가게 종류에대해 설명해주세요</span
        >
        <input
          type="text"
          class="form-control"
          placeholder="예) 한식, 중식 전문점."
          aria-label="storeType"
          aria-describedby="store_type"
          v_model="store_type"
          v-bind:value="store_type"
          @input="type_typing"
        />
      </div>
      <!-- 가게에대한 설명 -->
      <div class="mb-3">
        <label for="store_desc" class="form-label"
          >가게에 대해 설명해주세요</label
        >
        <textarea
          class="form-control"
          id="store_desc"
          rows="3"
          v-model="store_desc"
        ></textarea>
      </div>
      <!--지도 -->
      <label for="address" class="form-label mt-4"
        ><b>주소</b>를 입력해주세요.</label
      >
      <input
        class="form-control"
        type="text"
        id="address"
        v-model="address.place_name"
        aria-label="readonly input example"
        readonly
        style="text-align: center"
      />
      <label for="addressDetail" class="form-label mt-2"
        ><b>상세주소</b>를 입력해주세요.</label
      >
      <input
        class="form-control"
        type="text"
        id="addressDetail"
        v-model="addressDetail"
        style="text-align: center"
        @input="type_addressDetail"
      />
      <div
        class="btn btn-primary mt-4"
        @click="SET_MODAL_MAP(true)"
        @close="SET_MODAL_MAP(false)"
      >
        주소찾기
      </div>

      <!-- 사진 -->
    </div>
    <!-- <div class="mb-3">
      <label for="formFileMultiple" class="form-label" @click="upload"
        >Multiple files input example</label
      >
      <input class="form-control" type="imageFile" id="formFileMultiple" multiple />
    </div> -->
    <div class="store-deal-information-container">
      <div class="store-deal-information-title">사진 등록</div>
      <div class="store-picture-notice">
        <ul class="store-write-wrapper">
          <li>
            사진은 가로로 찍은 사진을 권장합니다. (가로 사이즈 최소 800px)
          </li>
          <li>사진 용량은 사진 한 장당 10MB 까지 등록이 가능합니다.</li>
          <li>
            사진은 최소 3장 이상 등록해야하며, 최대 15장 까지 권장합니다. 그
            이상 등록할 경우 업로드 시간이 다소 지연될 수 있습니다.
          </li>
        </ul>
      </div>
      <div class="store-imageFile-upload-wrapper">
        <div
          v-if="!files.length"
          class="store-imageFile-upload-example-container"
        >
          <!--  -->
          <div class="store-imageFile-upload-example">
            <div class="store-imageFile-image-example-wrapper">이미지</div>
            <div class="store-imageFile-notice-item">
              실사진 최소 1장 이상 등록하셔야 하며, 가로사진을 권장합니다.
            </div>
            <div
              class="
                store-imageFile-notice-item store-imageFile-notice-item-red
              "
            >
              사진 올릴떄 주의사항
            </div>
            <div
              class="store-imageFile-notice-item store-imageFile-upload-button"
            >
              <div class="image-box">
                <!-- <div class="image-profile">
                <img :src="profileImage" />
                </div>-->
                <label for="imageFile">일반 사진 등록</label>
                <input
                  type="file"
                  id="imageFile"
                  ref="files"
                  @change="imageUpload"
                  multiple
                />
              </div>
            </div>
          </div>
        </div>
        <div v-else class="imageFile-preview-content-container">
          <!-- v-else -->
          <div class="imageFile-preview-container">
            <div
              v-for="(imageFile, index) in files"
              :key="index"
              class="imageFile-preview-wrapper"
            >
              <div
                class="imageFile-close-button"
                @click="fileDeleteButton"
                :name="imageFile.number"
              >
                x
              </div>
              <img :src="imageFile.preview" />
            </div>
            <div class="imageFile-preview-wrapper-upload">
              <div class="image-box">
                <label for="imageFile">추가 사진 등록</label>
                <input
                  type="imageFile"
                  id="imageFile"
                  ref="files"
                  @change="imageAddUpload"
                  multiple
                />
              </div>
              <!-- <div class="imageFile-close-button" @click="fileDeleteButton" :name="imageFile.number">x</div> -->
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="store-write-button-wrapper">
      <div class="store-write-button" @click="storeWrite">등록</div>
    </div>
    <Map v-if="mapModal"></Map>
  </div>
</template>

<script>
import axios from "axios";
// import { error } from "@/api/notification";
// import { mapState } from "vuex";
import JWT from "@/api/jwt";
import Map from "@/components/modal/Map.vue";
import { error, success } from "@/api/notification";
import { mapGetters, mapState, mapMutations } from "vuex";
export default {
  components: { Map },
  data() {
    return {
      files: [],
      filesPreview: [],
      date: new Date(),
      store_name: "",
      store_contact: "",
      store_type: "",
      store_desc: "",
      localx: "",
      localy: "",
    };
  },
  watch: {},
  mounted() {},
  computed: {
    ...mapState({
      mapModal: "mapModal",
      address: "selectedPlace",
      addressDetail: "selectedAddressDetail",
    }),
  },
  methods: {
    ...mapMutations(["SET_MODAL_MAP"]),
    imageUpload() {
      console.log(this.$refs.files.files);
      // this.files = [...this.files, this.$refs.files.files];
      //하나의 배열로 넣기
      let num = -1;
      for (let i = 0; i < this.$refs.files.files.length; i++) {
        this.files = [
          ...this.files,
          {
            imageFile: this.$refs.files.files[i],

            preview: URL.createObjectURL(this.$refs.files.files[i]),

            number: i,
          },
        ];
        num = i;
      }
      this.uploadImageIndex = num + 1; //이미지 index의 마지막 값 + 1 저장
      console.log(this.files);
      // console.log(this.filesPreview);
    },

    imageAddUpload() {
      console.log(this.$refs.files.files);

      // this.files = [...this.files, this.$refs.files.files];
      //하나의 배열로 넣기c
      let num = -1;
      for (let i = 0; i < this.$refs.files.files.length; i++) {
        console.log(this.uploadImageIndex);
        this.files = [
          ...this.files,
          //이미지 업로드
          {
            //실제 파일
            imageFile: this.$refs.files.files[i],
            //이미지 프리뷰
            preview: URL.createObjectURL(this.$refs.files.files[i]),
            //삭제및 관리를 위한 number
            number: i + this.uploadImageIndex,
          },
        ];
        num = i;
      }
      this.uploadImageIndex = this.uploadImageIndex + num + 1;

      console.log(this.files);
      // console.log(this.filesPreview);
    },
    fileDeleteButton(e) {
      const name = e.target.getAttribute("name");
      this.files = this.files.filter((data) => data.number !== Number(name));
      // console.log(this.files);
    },
    async storeWrite() {
      let formData = new FormData();
      if (!this.store_name) {
        // 에러
        error("가게 이름을 입력해주세요!", this);
        return false;
      }
      formData.append("name", this.store_name);
      // 전화번호 valiation
      const phoneCheckReg = /^\d{2,3}-\d{3,4}-\d{4}$/;
      let phoneValidation = phoneCheckReg.test(this.store_contact);
      if (!this.store_contact) {
        error("가게 전화번호를 입력해주세요!", this);
        return false;
      } else if (phoneValidation === false) {
        error("가게번호는 숫자와 -(하이픈)만 입력 가능합니다.");
        return;
      }
      formData.append("phone", this.store_contact);

      if (!this.address.address_name) {
        error("주소찾기를 눌러 주소를 입력해주세요", this);
        return false;
      }
      console.log("주소완료");
      formData.append("address", this.address.address_name);
      if (!this.addressDetail) {
        error("상세주소를 입력해주세요!", this);
        return false;
      }
      console.log("상세주소완료");
      formData.append("addressDetail", this.addressDetail);
      formData.append("localx", this.address.x);
      formData.append("localy", this.address.y);
      if (!this.store_type) {
        error("가게 종류를 입력해주세요!", this);
        console.log(this.store_type);
        return false;
      }
      console.log("가게종류완료");
      formData.append("storeKind", this.store_type);
      if (!this.store_desc) {
        error("가게 상세정보를 입력해주세요!", this);
        return false;
      }
      console.log("가게상세정보");
      formData.append("body", this.store_desc);
      if (this.files.length < 1) {
        error("최소 1장의 사진을 업로드 하세요", this);
        return;
      }
      console.log("채소한장");
      // 파일 추가
      for (let i = 0; i < this.files.length; i++) {
        formData.append("fileList", this.files[i].imageFile);
      }
      const token = JWT.getToken();
      const headers = {
        "Content-type": "multipart/form-data",
        Authorization: token,
      };
      // console.log("헤더는 사랑을 싣고");
      // axios.defaults.headers.post = null;
      // console.log("보낼토큰입니다 : " + token);
      axios
        .post("http://localhost:8083/store/addstore", formData, {
          headers,
        })
        .then((response) => {
          if (response.status === 200) {
            success("가게 등록에 성공했습니다.", this);
            alert("가게 등록에 성공했습니다. 관리자의 승인을 기다립니다");
            this.$router.push({ path: "/" });
          }
        })
        .catch((error) => {
          if (error.response.status === 500) {
            alert("오류가 발생했습니다. 다시 로그인해 주세요!");
            this.$store.dispatch("auth/logout");
            this.$router.push({ path: "/" });
          }
        });
    },
    type_typing(e) {
      this.store_type = e.target.value;
    },
    type_addressDetail(e) {
      this.addressDetail = e.target.value;
    },
  },
};
</script>

<style scoped>
* {
  box-sizing: border-box;
}
.store-write-notification-wrapper {
  margin-top: 30px;
  border: 1px solid #dddddd;
  padding: 20px 40px;
  box-shadow: rgba(188, 196, 219, 0.77) 5px 5px 13px 0px;
}
.store-write-wrapper > li {
  color: #222222;
  list-style: none;
  font-size: 15px;
  position: relative;
  font-weight: 100;
}
.store-write-wrapper > li:not(:first-child) {
  margin-top: 8px;
}

.store-write-wrapper > li::before {
  content: "";
  width: 3px;
  height: 3px;
  background-color: rgb(34, 34, 34);
  position: absolute;
  top: 50%;
  left: -10px;
  transform: translateY(-50%);
  border-radius: 50%;
}
.store-write-type-container {
  border: 1px solid #bbbbbb;
  box-shadow: rgba(188, 196, 219, 0.77) 5px 5px 13px 0px;
  color: #222222;
  margin-top: 50px;
}

.store-write-information-wrapper {
  margin-top: 20px;
}

.store-write-type-header {
  text-align: center;
  border-bottom: 1px solid #cccccc;
  box-shadow: rgba(0, 0, 0, 0.05) 0px 1px 5px 0px;
  height: 60px;
  line-height: 60px;
  font-size: 18px;
}

.store-write-type-wrapper {
  height: 70px;
  display: flex;
  align-items: center;
}
.store-write-type-wrapper:not(:last-child) {
  border-bottom: 0.8px solid #dddddd;
  /* box-shadow: rgba(0, 0, 0, 0.05) 0px 1px 5px 0px; */
}
.store-write-type-title {
  width: 150px;
  height: 100%;
  font-size: 15px;
  line-height: 70px;
  text-align: center;
  background-color: rgb(249, 249, 249);
  font-weight: 400;
}

.store-write-type-selctor {
  display: flex;
  padding-left: 20px;
}
.store-write-type {
  text-align: center;
  min-width: 82px;
  height: 46px;
  font-size: 15px;
  line-height: 46px;
  /* border: 0.5px solid #dddddd; */
  border-radius: 5px;
  cursor: pointer;
}
.store-write-type:not(:first-child) {
  margin-left: 10px;
}

.store-write-type > input[type="radio"] {
  position: relative;
  user-select: none;
}

.store-write-type > input[type="radio"],
input[type="radio"]:checked {
  position: absolute;
  appearance: none;
  width: 0.9rem;
  height: 0.9rem;
  border-radius: 100%;
  margin-right: 0.1rem;
}
.store-write-type > p {
  min-width: 82px;
  height: 100%;
  border-radius: 5px;
  padding: 0 15px;
  border: 1px solid #dddddd;
}

.store-write-type > input[type="radio"]:checked + p {
  color: white;
  background-color: #1a5ae8;
  border: 1px solid #1a5ae8;
}

.store-location-container {
  color: #222222;
  margin-top: 30px;
  border: 1px solid #dddddd;
  box-shadow: rgba(188, 196, 219, 0.77) 5px 5px 13px 0px;
}

.store-location-title-wrapper {
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 60px;
  border-bottom: 1px solid #dddddd;
}
.store-location-title-wrapper > div {
  flex: 1;
}
.store-location-title {
  text-align: center;
  font-size: 18px;
}
.store-location-sub-title {
  text-align: right;
  font-size: 14px;
  color: #888888;
  transform: translateX(-7%);
}

.store-location-content-container {
  display: flex;
  height: 370px;
  box-shadow: rgba(188, 196, 219, 0.77) 5px 5px 13px 0px;
}
.store-location-content-title {
  font-size: 15px;
  width: 150px;
  text-align: center;
  line-height: 370px;
  background-color: #f9f9f9;
}
.store-location-content-wrapper {
  width: 560px;
  padding: 20px;
  display: flex;
  align-items: center;
}

.store-location-content-notification {
  font-size: 14px;
  color: #888888;
}
.store-location-input-wrapper {
  margin-top: 15px;
  display: flex;
  height: 46px;
}
.store-location-input {
  height: 100%;
  width: 430px;
  border: none;
  border: 1px solid #cccccc;
  border-radius: 4px;
  padding: 0 15px;
}
.store-location-search-button {
  height: 100%;
  cursor: pointer;
  margin-left: 10px;
  background-color: #232d4a;
  color: white;
  min-width: 90px;
  font-size: 14px;
  text-align: center;
  line-height: 46px;
  border-radius: 4px;
}
.store-location-search-type {
  display: inline-block;
  width: 60px;
}

.store-location-search-result {
  display: flex;
  justify-content: center;
  flex-direction: column;
  margin-top: 12px;
  width: 528px;
  height: 100px;
  border: 1px solid #cccccc;
  padding: 20px 15px;
  font-size: 15px;
  color: #666666;
}
.store-location-search-result > div {
  margin-top: 5px;
}

.store-location-result-detail-wrapper {
  margin-top: 10px;
  height: 46px;
  display: flex;
  align-items: center;
}

.store-location-result-detail {
  display: flex;
  height: 46px;
  align-items: center;
}

.store-location-result-detail:not(:first-child) {
  margin-left: 8px;
}
.store-location-result-detail-input {
  border: none;
  border: 1px solid #cccccc;
  width: 212px;
  height: 100%;
  padding: 0 15px;
}
.store-location-result-detail-sub {
  color: #888888;
  font-size: 14px;
  width: 48px;
  height: 100%;
  line-height: 46px;
  text-align: center;
  border: 1px solid #cccccc;
  border-left: none;
}

.store-location-dong-check-wrapper {
  padding-top: 30px;
  display: flex;
  align-items: center;
  font-size: 14px;
}

.checkbox {
  display: inline-flex;
  cursor: pointer;
  position: relative;
}

.checkbox > span {
  /* color: #34495e; */
  padding: 0.5rem 0.25rem;
  margin-left: 5px;
}

.checkbox > input {
  height: 20px;
  width: 20px;
  -webkit-appearance: none;
  -moz-appearance: none;
  -o-appearance: none;
  appearance: none;
  border: 1px solid #dddddd;
  border-radius: 4px;
  outline: none;
  transition-duration: 0.3s;
  background-color: #fff;
  cursor: pointer;
}

.checkbox > input[type="checkbox"]:checked {
  border: 1px solid #1564f9;
  background-color: #1564f9;
}

.checkbox > input[type="checkbox"]:checked + span::before {
  content: "\2713";
  font-size: 15px;
  display: block;
  text-align: center;
  color: #fff;
  position: absolute;
  left: 0.35rem;
  top: 0.4rem;
}
.login-check {
  display: flex;
  align-items: center;
}
.checkbox > input:active {
  border: 2px solid #34495e;
}
.store-location-map-wrapper {
  padding: 10px 10px 10px 0;
  /* width: 440px; */

  height: 100%;
}
#map {
  width: 380px;
  height: 100%;
}

.store-location-map {
  border: 1px solid #dddddd;
  width: 440px;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  color: #888888;
  font-size: 15px;
}
.store-location-map > div {
  margin-top: 5px;
}

.store-location-img-wrapper {
  width: 50px;
  height: 50px;
  border: 1px solid #dddddd;
  border-radius: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.store-location-img-wrapper > img {
  width: 50%;
  height: 50%;
}

.store-deal-information-container {
  margin-top: 50px;
  color: #222222;
  border: 1px solid #dddddd;
  box-shadow: rgba(188, 196, 219, 0.77) 5px 5px 13px 0px;
}
.store-deal-information-title {
  text-align: center;
  font-size: 18px;
  line-height: 60px;
  border-bottom: 1px solid #dddddd;
}

.store-deal-information-content-wrapper {
  min-height: 50px;
  display: flex;
}

.store-deal-informtaion-content-title {
  font-size: 15px;
  display: flex;
  align-items: center;
  justify-content: center;
  width: 150px;
  background-color: #f9f9f9;
}

.store-deal-information-content {
  width: 100%;

  font-size: 14px;
}

.store-deal-option-selector {
  display: flex;
  align-items: center;

  padding: 15px;
}

.store-deal-option-item {
  width: 100px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border: 1px solid #cccccc;
  border-radius: 5px;
  cursor: pointer;
}

.store-deal-option-item:last-child {
  margin-left: 10px;
}

.store-deal-option-notice {
  margin-left: auto;
  font-size: 14px;
  color: #888888;
}

.store-deal-option-item-deposit {
  margin-left: 10px;
}

.store-deal-information-wrapper {
  display: flex;

  flex-direction: column;
}

.store-deal-information-option {
  padding: 10px;
  display: flex;
  align-items: center;
}

.store-deal-information-option:last-child {
  border-bottom: 1px solid #dddddd;
}

.store-deal-information-item-type {
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
.store-deal-information-item-wrapper {
  display: flex;
  align-items: center;
  margin-left: 10px;
  height: 46px;
  width: 100%;
}

.store-deal-information-item-wrapper > input {
  border: 1px solid #dddddd;
  width: 140px;
  height: 100%;
  padding: 0 15px;
  font-size: 15px;
}
.store-deal-inforamtion-won {
  margin: 0 10px;
}
.store-deal-information-example {
  color: #888888;
}

.store-deal-information-option:not(:first-child) {
  margin-top: 10px;
}

.store-deal-inforamtion-divide {
  font-size: 22px;
  margin: 0 8px;
  color: #222222;
  font-weight: 100;
}
.store-deal-close-button-wrapper {
  margin-left: auto;
  cursor: pointer;
}

.store-deal-close-button {
  display: flex;
  align-items: center;
  justify-content: center;
  width: 22px;
  height: 22px;
  background-color: #666666;
  color: rgb(220, 220, 220);
}
.store-deal-cliked {
  background-color: rgb(235, 235, 235);
  color: rgb(170, 170, 170);
}

.building-information-wrapper {
  display: flex;
  width: 100%;
}

.bulding-information-scale {
  display: flex;
  flex: 1;
  border-bottom: 1px solid #dddddd;
  height: 150px;
}

.bulding-information-title {
  width: 150px;
  font-size: 15px;
  color: #222222;
  background-color: #f9f9f9f9;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.bulding-information-calendar-selector {
  margin-left: 20px;
  display: flex;
  align-items: center;
  font-size: 14px;
}
.choose-date-picker {
  margin-right: 20px;
}
.bulding-information-calendar-wrapper {
  display: flex;
}

.bulding-information-calendar-title {
  width: 150px;
  height: 70px;
  background-color: #f9f9f9f9;
  font-size: 14px;
  color: #222222;
  display: flex;
  justify-content: center;
  align-items: center;
}

.bulding-information-option-wrapper {
  width: 430px;
  display: flex;
  flex-direction: column;
  font-size: 15px;
}
.bulding-information-option-wrapper > div {
  display: flex;
  align-items: center;
  padding: 0 20px;
  flex: 1;
}

.bulding-information-option1 {
  border-top: 1px solid #dddddd;
}
.bulding-inforamtion-floor-wrapper {
  display: flex;
  align-items: center;
  padding: 20px;
}
.bulding-information-floor-title {
  font-size: 15px;
}
.bulding-information-floor {
  display: flex;
  align-items: center;
}

.bulding-information-floor-select {
  margin-left: 10px;
  width: 168px;
  height: 46px;
}

select {
  width: 200px;
  padding: 0.8em 0.5em;
  border: 1px solid #999;
  font-family: inherit;
  background: url("../../assets/arrow.jpeg") no-repeat 95% 50%;
  background-size: 25px;
  border-radius: 0px;
  -webkit-appearance: none;
  -moz-appearance: none;
  appearance: none;
}

select::-ms-expand {
  display: none;
}
.bulding-information-input-wrapper {
  height: 46px;
  font-size: 15px;
}
.bulding-information-input-wrapper > input {
  width: 100px;
  height: 46px;
  margin: 0 7px;
  padding: 0 15px;
  font-size: 15px;
}

.store-option-type {
  text-align: center;
  min-width: 82px;
  height: 46px;
  font-size: 15px;
  line-height: 46px;
  /* border: 0.5px solid #dddddd; */
  border-radius: 5px;
  cursor: pointer;
}
.store-option-type:not(:first-child) {
  margin-left: 20px;
}

.store-option-type > input[type="checkbox"] {
  position: relative;
  user-select: none;
}

.store-option-type > input[type="checkbox"] {
  position: absolute;
  appearance: none;
  width: 0.9rem;
  height: 0.9rem;
  border-radius: 100%;
  margin-right: 0.1rem;
}
.store-option-type > input[type="checkbox"]:checked {
  position: absolute;
  appearance: none;
  width: 0.9rem;
  height: 0.9rem;
  border-radius: 100%;
  margin-right: 0.1rem;
}
.store-option-type > p {
  min-width: 82px;
  height: 100%;
  border-radius: 5px;
  padding: 0 15px;
  border: 1px solid #dddddd;
}

.store-option-type > input[type="checkbox"]:checked + p {
  color: white;
  background-color: #1a5ae8;
  border: 1px solid #1a5ae8;
}

.store-deal-option-wrapper {
  padding: 20px;
  display: flex;
}
.store-write-title-container {
  height: 70px;
}

.store-write-title-input-wrapper {
  height: 100%;
  padding: 12px 20px;
}
.store-write-title-input-wrapper > input {
  border: 1px solid #dddddd;
  width: 100%;
  height: 100%;
  padding: 0 15px;
  font-size: 15px;
  color: rgb(68, 68, 68);
}

.store-write-title-input-wrapper > textarea {
  border: 1px solid #dddddd;
  width: 100%;
  height: 100%;
  padding: 20px;
  color: rgb(68, 68, 68);
  font-size: 17px;
}
.store-imageFile-upload-example {
  height: 100%;
}
.store-write-content-container {
  border-top: 1px solid #dddddd;
  min-height: 260px;
}

.store-picture-notice {
  margin: 20px;
  padding: 20px 40px;
  border: 1px solid #dddddd;
}
.imageFile-preview-content-container {
  height: 100%;
}

.store-imageFile-upload-wrapper {
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

.store-imageFile-upload-example-container {
  display: flex;
  align-items: center;
  justify-content: center;
  height: 100%;
  /* height: 100%;
  width: 100%; */
}

.store-imageFile-image-example-wrapper {
  text-align: center;
}

.store-imageFile-notice-item {
  margin-top: 5px;
  text-align: center;
}
.store-imageFile-notice-item-red {
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

.imageFile-preview-wrapper {
  padding: 10px;
  position: relative;
}

.imageFile-preview-wrapper > img {
  position: relative;
  width: 190px;
  height: 130px;
  z-index: 10;
}

.imageFile-close-button {
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

.imageFile-preview-container {
  height: 100%;
  display: flex;
  flex-wrap: wrap;
}

.imageFile-preview-wrapper-upload {
  margin: 10px;
  padding-top: 20px;
  background-color: #888888;
  width: 190px;
  height: 130px;
}

.store-write-button-wrapper {
  margin-top: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
  color: #222222;
}
.store-write-button-wrapper > div {
  width: 160px;
  height: 50px;
  border: 1px solid #dddddd;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 15px;
  cursor: pointer;
}

.store-write-button {
  margin-left: 15px;
  color: #fff;
  background-color: #1564f9;
}

.store-write-button:hover {
  opacity: 0.8;
}
</style>
