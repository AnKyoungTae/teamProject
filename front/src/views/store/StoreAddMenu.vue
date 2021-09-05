<template>
  <div>
    <h1 class="mt-4">
      <span v-if="!foodName">여기서 음식을 추가하세요!</span>
      <span v-else>{{ foodName }}</span>
    </h1>
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div id="container" class="container">
      <div class="row">
        <div class="col-md-5 align-self-center" v-if="uploadImageFile">
          <img :src="uploadImageFile" />
          <div class="btn btn-danger mt-2" @click="clearImg">사진삭제</div>
        </div>
        <div
          id="imageContainer"
          class="col-md-5 align-self-center"
          v-if="!uploadImageFile"
        >
          <!-- 이미지 -->
          <div>
            <div class="p-5" id="txtForImg">
              음식을 대표할 사진을 추가해주세요!
            </div>
            <div class="filebox">
              <label for="ex_file">음식사진추가</label>
              <input type="file" id="ex_file" @input="onFileSelected" />
            </div>
          </div>
        </div>
        <div class="col-md-1"></div>
        <!-- 음식데이터 -->
        <div class="col-md-5 mt-2">
          <div class="mb-3">
            <label for="foodName" class="form-label">음식이름</label>
            <input
              type="text"
              class="form-control text-center"
              id="foodName"
              placeholder="예) 와푸 삼겹살볶음"
              @input="set_food_name"
              ref="image"
            />
            <label for="foodPrice" class="form-label mt-2">가격</label>
            <div class="input-group">
              <input
                type="text"
                class="form-control form-control-sm text-center"
                :class="{ 'is-invalid': !priceValid }"
                id="foodPrice"
                placeholder="예) 10000"
                v-model="price"
                @blur="chk_food_price"
              /><span class="input-group-text">원</span>
            </div>
          </div>
        </div>
      </div>
      <!-- 설명 -->
      <div class="mb-3 mt-4">
        <label for="food_description" class="form-label"
          >음식에 대한 설명을 작성해주세요!</label
        >
        <textarea
          class="form-control"
          id="food_description"
          v-model="foodDesc"
          placeholder="예) 우리나라를 대표하는 아이콘이라고 해도 무방한 김치!
	김치는 과거 겨울철에 채소를 먹기 힘들었던 우리 조상들이
	겨울철 양식으로 만들기 시작한 저장 음식이 지금까지 이어진 음식인데요~
	무 배추, 오이, 열무 등 채소를 소금에 절여 맛깔나게 버무린 김치는
	오늘날 식탁에서 빠지지 않는 대표 발효 음식입니다!
					"
          rows="7"
        ></textarea>
        <div class="spinner-border mt-4" role="status" v-if="akinators == null">
          <span class="visually-hidden">Loading...</span>
        </div>
        <!-- 아키네이터 -->
        <div id="akinators" class="mt-4" v-if="akinators != null">
          <!-- 아키네이터 질문양식에 대한 사각형 설명! -->
          <div id="txtForAki">
            <h4>아키네이터?</h4>
            <span id="warnForAki"
              >사용자와 판매자 모두를 위해 놀랍도록 친절한 아키네이터형식의
              질문을 제공합니다. 아래 질문에 <strong>성실하게</strong>답변을
              한다면, 예비 구매자가 사장님의 음식을 더욱 쉽게 찾을 수 있게
              도와줍니다. 저희가 제공하는 일련의 알고리즘을 따라서 손님이 원하는
              음식을 찾게 성실히 답변해주세요!</span
            ><br />
          </div>
          <div
            class="mt-4 questions"
            v-for="(akinator, index) in akinators"
            :key="index"
          >
            <strong class="questions">{{ akinator.query }}</strong
            ><br />
            <div
              class="form-check form-check-inline"
              v-for="answer in akinator.answers"
              :key="answer.answerId"
            >
              <input
                class="form-check-input"
                type="radio"
                :name="answer.question_id"
                :id="answer.answerId"
                :value="answer.answerValue"
                @click="set_food_info(answer.question_id, answer.answerValue)"
              />
              <label class="form-check-label" :for="answer.answerId">{{
                answer.answerText
              }}</label>
            </div>
          </div>
        </div>
      </div>
      <div class="row">
        <div class="col-md-1"></div>
        <div class="col-md-5">
          <div class="btn btn-success" @click="sendForm">등록하기</div>
        </div>
        <div class="col-md-5">
          <div class="btn btn-danger" @click="resetForm">다시입력</div>
        </div>
        <div class="col-md-1"></div>
      </div>
    </div>
  </div>
</template>

<script>
import { error, success, normal } from "@/api/notification";
import http from "@/api/http";
import { mapGetters } from "vuex";

export default {
  data() {
    return {
      uploadImageFile: null,
      price: 0,
      priceValid: true,
      foodDesc: null,
      akinators: null,
      foodName: null,
      foodInfo: new Map(),
      uploadFile: null,
    };
  },
  mounted() {
    normal("음식등록을 위한 데이터를 불러옵니다", this);
    http
      .get("/store/getAkinatorList")
      .then((response) => {
        if (response.status === 200) {
          this.akinators = response.data;
          success("성공!", this);
        }
      })
      .catch((err) => {
        console.log(err);
        error("오류가 발생했습니다. 다시 시도해주세요", this);

        alert("관리자의 승인이후에 음식을 등록할 수 있습니다!");
        this.$store.dispatch("auth/logout");
        this.$router.push({ path: "/" });
      });
  },
  computed: {
    ...mapGetters({
      getToken: "auth/getAccessToken",
    }),
  },
  methods: {
    sendForm() {
      let formData = new FormData();

      if (!this.foodName) {
        error("음식 이름을 입력해주세요!", this);
        return;
      }
      formData.append("name", this.foodName);

      if (!this.price || !this.priceValid) {
        error("음식 가격을 입력해주세요!", this);
        return;
      }
      formData.append("price", this.price);

      if (!this.foodDesc) {
        error("음식 설명을 제대로 기입해주세요", this);
        return;
      }
      formData.append("description", this.foodDesc);

      if (this.foodInfo.size != this.akinators.length) {
        error("모든 아키네이터 항목에 답변을 달아주세요!", this);
        console.log(
          this.foodInfo.size +
            " 와 " +
            this.akinators.length +
            " 가 같지않습니다."
        );
        return;
      }

      if (!this.uploadFile) {
        error("대표사진을 선택해주세요!", this);
        return;
      }
      formData.append("file", this.uploadFile);

      normal("음식을 등록하는중입니다..", this);

      http
        .post("/store/addFood", formData)
        .then((response) => {
          if (response.status === 200) {
            this.requestAddAkinator(response.data);
          }
        })
        .catch((err) => {
          console.log(err);

          alert("관리자의 승인이후에 음식을 등록할 수 있습니다!");
          error("오류가 발생했습니다. 다시 시도해주세요", this);
          this.$store.dispatch("auth/logout");
          this.$router.push({ path: "/" });
        });
    },
    requestAddAkinator(foodId) {
      const map = this.foodInfo;
      // Map to object
      const modified = [...map];
      const reducerApplied = modified.reduce((accum, current) => {
        return {
          ...accum,
          [current[0]]: current[1],
        };
      }, {});
      const data = {
        akinator: reducerApplied,
        foodId: foodId,
      };
      http
        .post("/store/setAkinator", data)
        .then((response) => {
          console.log(response);
          success("음식을 등록했습니다!", this);
          this.$router.push({ path: "/storeMenus" });
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
          // 나중에 음식수정에서 아키네이터 다시 수정할수있게 해야지
          this.$store.dispatch("auth/logout");
          this.$router.push({ path: "/" });
        });
    },
    chk_food_price() {
      // validation
      let ChkifOnlyNumber = /[^0-9]/;
      if (ChkifOnlyNumber.test(this.price)) {
        this.priceValid = false;
        error("가격은 숫자만 입력가능합니다", this);
        return;
      }
      this.priceValid = true;
    },
    onFileSelected(event) {
      let input = event.target;
      this.uploadFile = input.files[0];
      if (input.files && input.files[0]) {
        let reader = new FileReader();
        reader.onload = (e) => {
          this.uploadImageFile = e.target.result;
        };
        reader.readAsDataURL(input.files[0]);
        success("대표사진이 추가되었습니다!", this);
      }
    },
    set_food_name(e) {
      this.foodName = e.target.value;
    },
    set_food_info(question_id, answerValue) {
      this.foodInfo.set(question_id, answerValue);
    },
    resetForm() {
      location.reload();
    },
    clearImg() {
      this.uploadImageFile = null;
    },
  },
};
</script>

<style scoped>
#container {
  background-color: aliceblue;
  margin: 2rem;
  width: 100%;
  height: 100%;
}
#imageContainer {
  border: 2px dashed palegreen;
  border-radius: 10px;
  height: 15rem;
}
img {
  height: 15rem;
  border-radius: 10px;
  margin-left: -12px;
  width: 15rem;
}
.questions {
  -ms-user-select: none;
  -moz-user-select: -moz-none;
  -khtml-user-select: none;
  -webkit-user-select: none;
  user-select: none;
}
.form-check-label {
  cursor: pointer;
}
.filebox label {
  display: inline-block;
  padding: 0.5em 0.75em;
  color: #fff;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #5cb85c;
  cursor: pointer;
  border: 1px solid #4cae4c;
  border-radius: 0.25em;
  -webkit-transition: background-color 0.2s;
  transition: background-color 0.2s;
}

.filebox label:hover {
  background-color: #6ed36e;
}

.filebox label:active {
  background-color: #367c36;
}

.filebox input[type="file"] {
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}
#cancleImg {
  width: 20px;
  height: 20px;
  background-color: red;
  z-index: 1;
}
#txtForImg {
  font-weight: lighter;
  font-size: 12px;
  color: gray;
}
#txtForAki {
  border: 1px solid salmon;
  border-radius: 10px;
  margin: 10px;
  padding: 20px;
}
#warnForAki {
  font-size: 14px;
  color: tomato;
}
</style>
