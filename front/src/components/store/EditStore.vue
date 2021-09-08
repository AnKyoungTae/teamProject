<template>
  <div class="container" style="width:700px;"> 
    <br />
    <h2 style="padding:20px;">수정 페이지</h2>
    <div class="editStore-body">
      <div class="editStore-list" style="border-top: 1px solid #7c7c7c;">
        <div class="editStore-title">가게이름</div>
        <input class="form-control editStore-input"
              v-model="name"
              type="text"
              placeholder="이름을 다시 설정해주세요"
        />
      </div>
      <div class="editStore-list">
        <div class="editStore-title">가게번호</div>
        <input class="form-control editStore-input"
              v-model="phone"
              type="text"
              placeholder="번호를 다시 설정해주세요"
        />
      </div>
      <div class="editStore-list">
        <div class="editStore-title">가게종류</div>
        <input class="form-control editStore-input"
              v-model="storeKind"
              type="text"
              placeholder="종류를 다시 설정해주세요"
        />
      </div>
      <div class="editStore-list">
        <div class="editStore-title">가게설명</div>
        <textarea
          class="form-control editStore-input"
          rows="3"
          v-model="body"
          placeholder="가게설명을 다시 설정해주세요"
        ></textarea>
      </div>
      <div class="editStore-list">
        <div class="editStore-title">
          가게주소
        </div>
        <div class="input-group editStore-input"
              style="padding:0px;">
          <input class="form-control"
                v-model="address"
                type="text"
                placeholder="주소를 다시 설정해주세요"
                readonly
          />
          <div
            style="width:100px;"
            class="btn btn-secondary"
            @click="SET_MODAL_MAP(true)"
            @close="SET_MODAL_MAP(false)"
          >
            주소찾기
          </div>
        </div>
      </div>
      <div class="editStore-list">
        <div class="editStore-title">상세주소</div>
        <input class="form-control editStore-input"
              v-model="addressDetail"
              type="text"
              placeholder="상세주소를 다시 설정해주세요"
        />
      </div>
      <!-- 사진 -->
      <div class="store-deal-information-container">
        <div class="store-deal-information-title" style="text-align: center; margin-top:20px;">사진 등록</div>
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
                v-for="(imageFile, index) in storeFiles"
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
                <img :src="storeFiles.name" />
              </div>
              <div class="imageFile-preview-wrapper-upload">
                <div class="image-box">
                  <label for="imageFile">추가 사진 등록</label>
                  <input
                    type="file"
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
      <!-- 끝 -->
      <Map v-if="mapModal"></Map>
    </div>
    <div style="margin:30px;">
      <button type="button" @Click="edit()" style="margin-right:25px;" class="btn btn-dark">수정</button>
      <button type="button" @Click="edit()" style="margin-left:25px;" class="btn btn-danger">취소</button>
    </div>
  </div>
</template>

<script>
import Map from "@/components/modal/Map.vue";
import { mapGetters, mapState, mapMutations } from "vuex";

export default {
  components: { Map },
  props: ["storeInfo", "storeFiles", "isMyStore"],
  data() {
    return {
      name: null,
      phone: null,
      address: null,
      addressDetail: null,
      storeKind: null,
      body: null,
      files: [],
      filesPreview: [],
    }
  },
  created() {
    console.log(this.storeInfo);
    console.log(this.storeFiles);
    this.name = this.storeInfo.name
    this.phone = this.storeInfo.phone
    this.address = this.storeInfo.address
    this.addressDetail = this.storeInfo.addressDetail
    this.storeKind = this.storeInfo.storeKind
    this.body = this.storeInfo.body
  },
  methods: {
    ...mapMutations(["SET_MODAL_MAP"]),
    edit() {
      this.$emit("trueEdit", true)
    },
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
  },
  computed: {
    ...mapState({
      mapModal: "mapModal",
      address: "selectedPlace",
      addressDetail: "selectedAddressDetail",
    }),
  },
}
</script>

<style scoped>
.editStore-body {
  text-align: left;
  font-size: 25px;
}
.editStore-list {
  display: flex;
  justify-content: space-between;
  padding: 20px 10px;
  border-bottom: 1px solid #7c7c7c;
}
.editStore-title {
  float: left;
}
.form-switch {
  font-size: 30px;
  margin: 20px;
  display: inline-block;
}
.editStore-input {
  width:400px;
}
tr {
  margin:10px;
}
/***********   이미지   ***********/
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
  font-size:15px;
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
</style>
