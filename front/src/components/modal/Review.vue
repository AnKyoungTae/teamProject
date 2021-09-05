<template>
  <Modal>
    <template v-slot:cancel>
      <div
        style="width: 100%; text-align: right; padding: 10px"
        @click="SET_MODAL_REVIEW(false)"
      >
        <i class="far fa-window-close fa-2x"></i>
      </div>
    </template>
    <template v-slot:header>
      <!-- 사진 미리보기 -->
      <span></span>
    </template>
    <template v-slot:body>
      <!-- alt 에 이미지 없을때 비상용 이미지 추가? -->
      <div>
        <a v-for="(star, index) in stars" :key="star" @click="scoreStar(index)">
          <i class="fas fa-star" v-if="star === true"></i>
          <i class="far fa-star" v-if="star === false"></i>
        </a>
      </div>
      <span>{{ totalScore }} /5 점</span>
      <div>
        <textarea
          class="form-control m-2"
          style="resize: none"
          rows="3"
          placeholder="음식에 대한 솔직한 리뷰를 남겨주세요"
          v-model="content"
        ></textarea>
      </div>

      <img v-if="inputfile == true"
        class="popupImageItem"
        :src="uploadImageFile"
        style="width: 100px; height: 100px; background-color: grey"
      />
      <div class="file">
        <label for="file" style="margin-top: 10px;">첨부파일</label>
        <input
          type="file"
          id="file"
          name='' multiple
          @change="imageAddUpload($event)"
          ref="reviewImage"
        />
      </div>
      <div v-if="inputfile == false" style="height: 100px;"></div>
    </template>
    <template v-slot:footer>
      <div class="btn btn-success" @click="uploadReview">리뷰 등록하기</div>
    </template>
  </Modal>
</template>

<script>
import Modal from "./Modal_review.vue";
import { mapMutations, mapGetters } from "vuex";
import http from "@/api/http";
import { error } from "@/api/notification";

export default {
  props: ["data", "storeId"],
  components: {
    Modal,
  },
  data() {
    return {
      counter: 1,
      uploadImageFile: null,
      stars: [true, true, false, false, false],
      content: null,
      image: "",
      inputfile: false,
    };
  },
  mounted() {},
  computed: {
    ...mapGetters({ checkCart: "checkCart", getUserId: "auth/getUserId" }),
    totalScore() {
      return this.stars.filter((star) => {
        return star === true;
      }).length;
    },
  },
  methods: {
    ...mapMutations({
      SET_MODAL_REVIEW: "SET_MODAL_REVIEW",
      addCart: "addCart",
    }),
    scoreStar(index) {
      this.stars = [];
      for (let i = 0; i < 5; i++) {
        if (i < index + 1) {
          this.stars.push(true);
        } else {
          this.stars.push(false);
        }
      }
    },

    imageAddUpload(event) {
      var input = event.target;
      if (input.files && input.files[0]) {
        var reader = new FileReader();
        reader.onload = (e) => {
          this.uploadImageFile = e.target.result;
        };
        this.inputfile = true;
        reader.readAsDataURL(input.files[0]);
      }
    },
    uploadReview() {
      // 회원인지, validation check
      if (!this.getUserId) {
        alert("회원만 후기를 남길 수 있습니다");
        return;
      }
      if (!this.content) {
        error("내용을 입력해 주세요!", this);
        return;
      }
      // 구현
      let formdata = new FormData();
      formdata.append("score", this.totalScore);
      formdata.append("body", this.content);
      formdata.append("writerid", this.getUserId);
      formdata.append("store_id", this.storeId);

      this.image = this.$refs.reviewImage.files[0];
      console.log(this.image);
      formdata.append("imageFile", this.image);
      http.post("/review/writeReview", formdata).then((res) => {
        this.SET_MODAL_REVIEW(false);
        alert("리뷰를 남겼습니다!");
        window.location.href =
          "http://localhost:8081/shopDetail?shopInfo=" + this.storeId;
      });
    },
  },
};
</script>

<style scoped>
.form-control {
  width: 95%;
}
.review-camera {
  margin-top: 5px;
  width: 60px;
  line-height: 60px;
  border: 1px solid lightgray;
}
.fa-camera {
  width: 23px;
  height: 23px;
}




.file label {
  display: inline-block;
  width: 100px;
  height: 45px;
  background-color: #4a4a4a;
  color: #fff;
  cursor: pointer;
  line-height: 45px;
  border-radius: 5px;
  text-align: center;
}

.file input[type="file"] {
  position: absolute;
  width :1px;
  height: 1px;
  margin: -1px;
  clip: rect(0,0,0,0);
  overflow: hidden;
  padding: 0;
}
</style>
