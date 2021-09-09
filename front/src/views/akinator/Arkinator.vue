<template>
  <div class="row wrapper m-4">
    <div class="col-md-8 left">
      <!-- 채팅화면탭 -->
      <!-- 질문말풍선 -->
      <div v-if="!akinatorLoaded" style="position: absolute; left: 500px">
        <div class="spinner-border" role="status">
          <span class="visually-hidden">Loading...</span>
        </div>
      </div>

      <div class="chatWrapper m-1 p-2" id="log">
        <!-- 답변들 -->

        <div
          class="answer"
          v-for="(answer, index) of answers"
          :key="index"
          @click="rollback(answer)"
        >
          <div class="questionText">
            <span>
              {{ questionText(answer.question_id) }}
            </span>
          </div>
          <div class="answerText">
            <span>
              {{ answer.answerText }}
            </span>
          </div>
        </div>

        <div
          class="question"
          :class="{ collapsedQuetionWidth: collapsed }"
          v-if="this.stageAkinator.length != 0"
        >
          <span>
            <p style="margin-right: 10px">Akinator 의 질문 ></p>
            {{ question }}
          </span>
        </div>
      </div>
      <div class="alternativesWrapper m-1 p-1">
        <!-- 답변목록탭 -->
        <div v-if="!akinatorLoaded">
          <div class="spinner-border" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
        <div v-if="stageAkinator.length == 0 && akinatorList.length == 0">
          <span>더 이상 질문이 없습니다.</span>
        </div>
        <div
          class="alternative p-1 m-1 btn btn-outline-secondary"
          v-for="(alternative, index) of alternatives"
          :key="index"
          @click="choiceAkinator(alternative)"
        >
          <span>
            {{ alternative.answerText }}
          </span>
        </div>
      </div>
    </div>
    <div class="col-md-4 col-sm-12 right row-cols-1 p-2">
      <div class="filterWrapper p-2">
        <!-- 검색탭 -->
        <div class="input-group mb-2">
          <input
            type="text"
            class="form-control"
            placeholder="검색"
            aria-describedby="food-filter"
            v-model="foodFilter"
          />
          <span
            class="input-group-text btn btn-outline-primary p-3"
            id="food-filter"
            ><i class="fas fa-search"></i
          ></span>
        </div>
      </div>
      <div class="foodListWrapper p-2">
        <div v-if="!foodDataLoaded">
          <div class="spinner-border mt-4" role="status">
            <span class="visually-hidden">Loading...</span>
          </div>
        </div>
        <div
          v-else-if="foodDataLoaded && foodList.length === 0"
          class="foodListContainer emptyFood m-1"
        >
          <span v-if="filteredFoodList"> 검색 결과가 없습니다 </span>
          <span v-else> 음식정보가 없습니다 </span>
        </div>
        <!-- 음식목록탭 -->
        <div
          v-else
          class="foodListContainer m-1 clickable"
          v-for="(food, index) of filteredFoodList"
          :key="index"
          @click="selectFood(food)"
        >
          <div
            class="foodWrapper"
            :class="[selectedFood === food ? 'choicedFoodList' : '']"
            :title="food.description"
          >
            <img :src="food.fileUrl" class="foodImg" />
            <div class="foodInfo">
              <span class="foodName">{{ food.name }}</span>
              <span class="foodPrice">{{ food.price }} 원</span>
              <span class="foodDistance">{{ food.distance }} Km</span>
            </div>
          </div>
        </div>
      </div>
      <div class="choicesWrapper m-1">
        <!-- 버튼탭 -->
        <div
          class="btn btn-outline-success"
          @click="toFoodDetail"
          :style="
            selectedFood != null
              ? 'font-size: 22px; transition: 0.5s; font-weight: bolder'
              : ''
          "
        >
          선택
        </div>
        <div class="btn btn-outline-danger" @click="toHome">나가기</div>
      </div>
    </div>
  </div>
</template>

<script>
import { collapsed } from "@/components/sidebar/state";
import http from "@/api/http";
import { mapGetters } from "vuex";

export default {
  methods: {
    requestFoodList() {
      if (this.answers.length === 0) {
        alert("질문에 대한 답변을 선택해 주세요!");
        return;
      }
      this.foodDataLoaded = false;
      // 필터링
      let filtered = (meta, answer) => {
        return meta.filter((item) => {
          return (
            item.question_id == answer.question_id &&
            item.answerValue == answer.answerValue
          );
        });
      };
      let list = [];
      // 각 배열로 골라진 값을 배치한다
      for (let i = 0; i < this.answers.length; i++) {
        list.push(
          filtered(this.akinatorMeta, this.answers[i]).map(
            (item) => item.food_id
          )
        );
      }
      // 리스트마다 있는 중복을 추출한다(두번이상 선택된, 보여줄 음식들)

      let filteredIdList;
      for (let i = 0; i < list.length; i++) {
        if (i == 0) {
          filteredIdList = list[0];
        } else {
          filteredIdList = filteredIdList.filter((it) => list[i].includes(it));
        }
      }
      // 값이 없을경우..
      if (filteredIdList.length === 0) {
        this.foodList = [];
        this.foodDataLoaded = true;
        return;
      }

      // 필터된 foodId리스트 요청. 거리순으로 나열
      const data = {
        filteredIdList: filteredIdList,
        LAT: this.LAT,
        LON: this.LON,
      };
      http.post("/akinator/getFoods", data).then((res) => {
        if (res.status === 200) {
          this.foodList = res.data;
          this.foodDataLoaded = true;
        }
      });
    },
    // hidden(index) {
    //   if (this.isHidden[index] == false) {
    //     this.isHidden[index] = true;
    //   } else {
    //     this.isHidden[index] = false;
    //   }
    // },
    choiceAkinator(alternative) {
      // 선택했을 때,
      this.answers.push(alternative);
      // 음식정보 요청한다.
      this.requestFoodList();
      // Stage 를 비우고 다시 Stage를 채운다
      this.executedAkinators.push(this.stageAkinator);
      this.setStage();
    },
    requestAkinators() {
      // 아키네이터를 불러온다.
      http.get("/akinator/getAkinators").then((res) => {
        if (res.status === 200) {
          this.akinatorList = res.data;
          this.shuffle(this.akinatorList);
          http.get("/akinator/getAkinatorMeta").then((res) => {
            // 아키네이터 빅데이터요청수집
            if (res.status === 200) {
              this.akinatorMeta = res.data;
              this.akinatorLoaded = true;
              this.foodDataLoaded = true;
              this.setStage();
              // 교수님요청, 첫질문일경우 아무값이나 넣기.
              if (this.isFirst == true) {
                let firstRandChoice =
                  this.alternatives[
                    Math.floor(Math.random() * this.alternatives.length)
                  ];
                this.choiceAkinator(firstRandChoice);

                this.isFirst = false;
              }
            }
          });
        }
      });
    },
    rollback(alternative) {
      // 만약 아키네이터에 하나밖에 없을경우..
      if (this.answers.length === 1) {
        alert("적어도 한개의 아키네이터 정보는 필요합니다!");
        return;
      }

      // 선택된 답장에 맞는 Akinator 돌려주기 from executed
      // answerId 가 겹치는게 있는 arkinator 를 akinatorList에 넣는다.
      const retrieve = this.executedAkinators.find((akinator) => {
        if (
          akinator.answers.find((answer) => {
            if (answer.answerId === alternative.answerId) {
              return true;
            }
          })
        ) {
          return true;
        }
      });
      this.akinatorList.push(retrieve);
      // answerId 가 겹치는게 없는 akinator 만 필터, executed 재정의.
      const rolledExecuted = this.executedAkinators.filter((akinator) => {
        if (
          akinator.answers.find((answer) => {
            if (answer.answerId === alternative.answerId) {
              return true;
            }
          })
        ) {
          return false;
        }
        return true;
      });
      this.executedAkinators = rolledExecuted;
      // answers 에서 클릭된 alternative 제거
      const rolledAnswers = this.answers.filter((answer) => {
        if (answer.answerId === alternative.answerId) {
          return false;
        }
        return true;
      });
      this.answers = rolledAnswers;
      this.requestFoodList();
      //
      if (this.stageAkinator.length == 0) {
        this.setStage();
      }
    },
    setStage() {
      // 남아있는게 없을 때,
      if (this.akinatorList.length == 0) {
        this.stageAkinator = [];
        return;
      }
      // 마지막을 꺼내서 Stage에 넣는다.
      this.stageAkinator = this.akinatorList.pop();
    },
    shuffle(arr) {
      // 배열을 섞는 메서드
      for (let i = arr.length - 1; i > 0; i--) {
        let j = Math.floor(Math.random() * (i + 1));
        [arr[i], arr[j]] = [arr[j], arr[i]];
      }
    },
    setLocation(latitude, longitude) {
      this.$store.commit("SET_LAT", latitude);
      this.$store.commit("SET_LON", longitude);
      this.$store.commit("SET_OBSERVE", true);
    },
    toHome() {
      // 나가기 클릭
      this.$router.push({ path: "/" });
    },
    toFoodDetail() {
      // 선택
      // target = http://localhost:8081/shopDetail?shopInfo=?&foodIdsearch=?
      this.$router.push({
        path: "/shopDetail",
        query: {
          shopInfo: this.selectedFood.storeId,
          foodIdsearch: this.selectedFood.foodId,
        },
      });
    },
    selectFood(food) {
      if (this.selectedFood == food) {
        this.selectedFood = null;
        return;
      }
      // 음식 클릭 했을 때,
      this.selectedFood = food;
    },
    questionText(question_id) {
      let poo = this.executedAkinators.find((akinator) => {
        return akinator.questionId == question_id ? true : false;
      });
      return poo.query;
    },
  },
  computed: {
    ...mapGetters({
      local: "GET_LOCAL",
      LAT: "GET_LAT",
      LON: "GET_LON",
    }),
    alternatives() {
      // 현재 Stage 에서 선택지
      return this.stageAkinator.answers;
    },
    question() {
      // 현재 Stage 에서 질문
      return this.stageAkinator.query;
    },
    filteredFoodList() {
      // 검색창이 활성화되면..
      if (this.foodFilter != "") {
        let arr;
        arr = this.foodList.filter((food) => {
          if (
            food.name.includes(this.foodFilter) ||
            food.description.includes(this.foodFilter)
          ) {
            return true;
          }
          return false;
        });
        return arr;
      } else {
        return this.foodList;
      }
    },
  },
  data() {
    return {
      answers: [], // 지금까지 한 답변들
      foodList: [], // 답변들로 나온 결과 음식들
      akinatorList: [], // 진행되지 않은 아키네이터
      executedAkinators: [], // 진행된 아키네이터
      stageAkinator: [], // 진행중인 아키네이터
      foodDataLoaded: false,
      akinatorLoaded: false,
      akinatorMeta: [], // 아키네이터 빅데이터
      foodFilter: "", // 검색창
      selectedFood: null, // 선택한 음식정보
      isFirst: true,
    };
  },
  mounted() {
    if (!this.local) {
      navigator.geolocation.getCurrentPosition(
        (position) => {
          this.setLocation(position.coords.latitude, position.coords.longitude);
        },
        (error) => {
          console.log("위치정보를 갱신할 수 없습니다" + error);
        }
      );
    }
    this.requestAkinators();
    document.querySelector(".wrapper").scrollIntoView();
  },
  setup() {
    return { collapsed };
  },
};
</script>

<style scoped>
.wrapper {
  height: 100vh;
  /* 전체 아키네이터의 최소크기설정 */
  min-width: 300px;
}
.left {
  /* 가운데 중심으로 왼쪽탭들 */
  border: 1px solid grey;
  display: inline-flex;
  flex-flow: row wrap;
  height: 100%;
  min-width: 480px;
  user-select: none;
}
.right {
  /* 가운데 중심으로 오른쪽 탭들 */
  border: 1px solid grey;
  /* 오른쪽 탭의 최소크기 설정 */
  min-width: 100px;
  /* 스크롤에 화면 안밀리게 */
  height: 100%;
  user-select: none;
}

/* 채팅화면 설정 */
@keyframes fadeInUp {
  from {
    opacity: 0;
    transform: translateY(10px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}
#log > div {
  position: relative;
  margin-top: 10px;
  /* 애니메이션속성 */
  animation: fadeInUp 0.5s ease forwards;
}

#log > div.deleted {
  visibility: hidden;
}

.question {
  position: relative;
  width: 100%;
  min-width: 320px;
  padding: 0.2em 1em;
  -webkit-border-radius: 10px;
  -moz-border-radius: 10px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  align-content: center;
}
.question > span:after {
  content: "";
  position: absolute;
  left: 0;
  top: 100%;
  width: 0;
  height: 0;
  border: 1em solid transparent;
  border-right-color: #fd3a69;
  border-left: 0;
  border-bottom: 0;
  margin-top: -1.5em;
  margin-left: 0em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
}

.question > span > p {
  color: springgreen;
}
.question > span {
  display: flex;
  font-size: 1.2em;
  font-weight: bolder;
  background: #fd3a69;
  border: #7f7f7f solid 1px;
  padding: 0.4em 1em 0em 1em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  color: white;
}
.chatWrapper {
  /* 채팅화면탭 */
  background: rgb(166, 213, 252);
  width: 100%;
  height: 70%;
  display: flex;
  /* 정렬방식 */
  flex-flow: column;
  flex-wrap: nowrap;
  align-items: flex-end;
  justify-content: end;
  overflow: hidden;
  border: 1px solid #91afba;
  box-shadow: 0px 2px 2px 2px rgba(0, 0, 0, 0.4);
  background: #355f6e;
}
.answer {
  position: relative;
  border-radius: 0.4em;
  display: flex;
  width: 100%;
  justify-content: space-between;
  flex-flow: column;
  padding: 0em 1em;
}
.collapsedQuetionWidth {
  left: 135px;
}
.answer > .answerText:after {
  content: "";
  position: absolute;
  right: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 1em solid transparent;
  border-left-color: #ffda77;
  border-right: 0;
  border-bottom: 0;
  margin-top: 1em;
  margin-right: 0em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
}
.answer > .questionText:after {
  content: "";
  position: absolute;
  left: 0;
  top: 50%;
  width: 0;
  height: 0;
  border: 1em solid transparent;
  border-right-color: #fd3a69;
  border-left: 0;
  border-bottom: 0;
  margin-top: -1.5em;
  margin-left: 0em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
}
.answer > .questionText {
  display: flex;
  flex-wrap: wrap;
  opacity: 0.8;
}
.answer > .answerText {
  display: flex;
  flex-wrap: wrap;
  flex-direction: row-reverse;
  opacity: 0.8;
}
.answer:hover .answerText,
.answer:hover .questionText {
  cursor: pointer;
  opacity: 1;
  font-size: 1.1em;
  text-decoration-line: line-through;
}
.questionText > span {
  background: #fd3a69;
  padding: 0.5em 1em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
  color: white;
}
.answerText > span {
  background: #ffda77;
  padding: 0.5em 1em;
  box-shadow: 0px 1px 0px 0px rgba(0, 0, 0, 0.1);
  border-radius: 5px;
}

.alternativesWrapper {
  /* 답변목록탭 */
  background: #91afba;
  border: 1px solid gray;
  width: 100%;
  height: 25%;
  /* 정렬방식 */
  justify-content: flex-start;
  align-items: flex-start;
  flex-direction: column;
  flex-wrap: wrap;
  overflow: auto;
  box-shadow: 0px 2px 2px 2px rgba(0, 0, 0, 0.4);
}
.alternative {
  font-size: 18px;
  background: #ffda77;
  display: inline-block;
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
}
.alternative:hover {
  background: orange;
}
.alternative > span {
  padding: 2px 10px;
  display: inline-block;
}
.input-group {
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
}
.filterWrapper {
  /* 검색탭 */

  width: 100%;
  height: 10%;
  display: flex;
  /* 정렬방식 */
  align-items: center;
}
.foodListWrapper {
  /* 음식목록탭 */
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
  border: 1px solid grey;
  width: 100%;
  height: 80%;
  display: flex;
  /* 정렬방식 */
  flex-direction: column;
  overflow-y: scroll;
  overflow-x: hidden;
}
.choicesWrapper {
  /* 버튼탭 */
  width: 100%;
  height: 10%;
  display: flex;
  /* 정렬방식 */
  justify-content: space-evenly;
  align-items: center;
}
.choicesWrapper > div {
  width: 40%;
}
.noMoreAkinator {
  display: flex;
  height: 5%;
  align-items: center;
}

.foodListContainer {
  display: table-column;
  height: 28%;
  border-radius: 8px;
  border: 1px solid gray;
  padding: 8px;
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.4);
}
.emptyFood {
  /* 음식목록이 비어있을 때 가운데정렬 */
  height: 100%;
  padding-top: 50%;
}
.emptyFood > span {
  font-size: 20px;
  color: tomato;
}
.foodWrapper {
  box-shadow: 0px 1px 1px 1px rgba(0, 0, 0, 0.1);
  width: 100%;
  height: 100%;
  display: flex;
}
.foodWrapper:hover {
  background: #91afba;
}
.foodImg {
  width: 50%;
  margin: 10px;
  border-radius: 5px;
}
.foodDistance {
  font-size: 12px;
  text-align: right;
  margin-right: 5%;
  padding-top: 15%;
  margin: 0px;
}
.foodInfo {
  width: 40%;
  height: 100%;
  padding-top: 15px;
  display: flex;
  flex-flow: column;
  justify-content: center;
}
.foodName {
  margin-top: 20px;
  font-weight: bold;
}
.foodPrice {
  font-weight: lighter;
  color: rgb(77, 77, 77);
  font-size: 0.8rem;
  font-size: 14px;
}
/* 스크롤바 */
.foodListWrapper::-webkit-scrollbar {
  width: 1rem;
}
.foodListWrapper::-webkit-scrollbar-thumb {
  background-color: #a9c4ff;
  border-radius: 10px;
}
.foodListWrapper::-webkit-scrollbar-thumb:hover {
  background-color: #5c79f8;
}
.foodListWrapper::-webkit-scrollbar-track {
  background-color: rgb(221, 221, 221);
  border-radius: 10px;
}
.clickable:hover {
  cursor: pointer;
}
/* 선택되었을 때 */
.choicedFoodList {
  border: 4px solid #fd3a69;
  border-radius: 6px;
  transition: 0.2s;
}
.alternative:hover {
  -webkit-transform: scale(1.05);
  -moz-transform: scale(1.05);
  -ms-transform: scale(1.05);
  -o-transform: scale(1.05);
  transform: scale(1.05);
}

.alternative span {
  -webkit-transition: all 0.1s linear;
  transition: all 0.1s linear;
  display: inline-block;
}

.btn-outline-primary {
  color: #2c656b;
  border-color: #5a8c94;
}
.btn-outline-primary:hover {
  background-color: #5a8c94;
  color: #fff;
}
</style>
