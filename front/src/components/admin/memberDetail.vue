<template>
  <div>
    <h4>
      <span v-if="!data.role || data.role == 'BUYER'">사용자</span>
      <span v-if="!data.role || data.role == 'SELLER'">판매자</span>
      <span v-if="!data.role || data.role == 'ADMIN'">관리자</span>
      {{ data.nickname }}님의 회원정보
    </h4>
  </div>
  <div class="manage-div4">
    <div class="manage-div2">
      <div class="profile-manage">
        <div class="manage-box">
          <a class="profile-img">
            <strong class="login-tit profile-tit">프로필사진</strong>
            <span class="login-txt"
              ><a class="profile-img">
                <img
                  :src="data.profileUrl"
                  style="
                    display: block;
                    width: 80px;
                    height: 80px;
                    border-radius: 50%;
                  "
                /> </a
            ></span>
          </a>
        </div>
        <hr style="padding: 0px" />
        <div class="manage-box">
          <a class="profile-img">
            <a class="profile-img">
              <strong class="login-tit">회원 번호</strong>
              <span class="login-txt">{{ data.memberId }}</span>
            </a>
            <hr style="padding: 0px" />

            <strong class="login-tit">이름</strong>
            <span class="login-txt">{{ data.name }}</span>
          </a>
          <hr style="padding: 0px" />

          <a class="profile-img">
            <strong class="login-tit">닉네임</strong>
            <span class="login-txt">{{ data.nickname }}</span>
          </a>
          <hr style="padding: 0px" />

          <a class="profile-img">
            <strong class="login-tit">이메일</strong>
            <span class="login-txt">{{ data.email }}</span>
          </a>
          <hr style="padding: 0px" />

          <a class="profile-img">
            <strong class="login-tit">핸드폰번호</strong>
            <span class="login-txt">{{ data.phone }}</span>
          </a>
          <hr style="padding: 0px" />

          <a class="profile-img">
            <strong class="login-tit">역할</strong>
            <span class="login-txt">{{ data.role }}</span>
          </a>
          <hr style="padding: 0px" />

          <a class="profile-img">
            <strong class="login-tit">신고당한 횟수</strong>
            <!-- 수정요망 -->
            <span class="login-txt">{{ data.memberId }}</span>
          </a>
          <hr style="padding: 0px" />
        </div>
      </div>
    </div>
    <div class="manage-div3">
      <div class="category">
        <div
          class="menu"
          :class="selectedMenu == 'menu1' ? '' : 'deactivatedMenu'"
          @click="selectMenu('menu1')"
        >
          <span :class="selectedMenu == 'menu1' ? 'activatedMenu' : ''"
            >작성한 글</span
          >
        </div>
        <div
          class="menu"
          :class="selectedMenu == 'menu2' ? '' : 'deactivatedMenu'"
          @click="selectMenu('menu2')"
        >
          <span :class="selectedMenu == 'menu2' ? 'activatedMenu' : ''"
            >신고당한 글</span
          >
        </div>
        <div
          class="menu"
          :class="selectedMenu == 'menu3' ? '' : 'deactivatedMenu'"
          @click="selectMenu('menu3')"
        >
          <span :class="selectedMenu == 'menu3' ? 'activatedMenu' : ''"
            >작성한 후기</span
          >
        </div>
      </div>
      <!-- 내용 -->
      <div class="wrapper">
        <!-- 작성한 글 -->
        <div
          class="articleContainer"
          v-if="selectedMenu == 'menu1' && dataLoaded == true"
        >
          <div v-if="articleList.length == 0" class="noData">
            <span>데이터가 없습니다</span>
          </div>
          <div
            class="rowWrap"
            v-for="(article, index) in articleList"
            :key="index"
          >
            <!-- 데이터 로딩되었을 때 -->
            <div class="info1">
              <span class="articleStatus">
                <i class="fas fa-lock" v-if="article.status == 'H'"></i>
                <i class="fas fa-unlock" v-else-if="article.status == 'Y'"></i>
                <i
                  class="fas fa-bullhorn"
                  v-else-if="article.status == 'S'"
                ></i>
                <i class="fas fa-ban" v-else></i>
              </span>
              <span class="articleTitle">{{ article.title }}</span>
              <span class="articleRegDate">
                <p>
                  {{ parsingDate(article.regDate) }}
                </p></span
              >
            </div>
            <div class="info2">
              <span class="articleBody">{{ article.body }}</span>
            </div>
          </div>
        </div>
        <!-- 작성한 글 끝 -->

        <!-- 작성한 리뷰 -->
        <div
          class="articleContainer"
          v-else-if="selectedMenu == 'menu3' && dataLoaded == true"
        >
          <div v-if="reviewList.length == 0" class="noData">
            <span>데이터가 없습니다</span>
          </div>
          <div
            class="rowWrap"
            v-for="(review, index) in reviewList"
            :key="index"
          >
            <!-- 데이터 로딩되었을 때 -->
            <div class="info1">
              <span class="articleStatus">
                <i class="fas fa-lock" v-if="review.status == 'H'"></i>
                <i class="fas fa-unlock" v-else-if="review.status == 'Y'"></i>
                <i class="fas fa-bullhorn" v-else-if="review.status == 'S'"></i>
                <i class="fas fa-ban" v-else></i>
              </span>
              <span class="articleTitle"
                >{{ review.storeId }} : {{ review.name }}</span
              >
              <span class="articleRegDate">
                <p>
                  {{ parsingDate(review.regDate) }}
                </p></span
              >
            </div>
            <div class="info2">
              <span class="articleBody">{{ review.body }}</span>
            </div>
          </div>
        </div>
        <!-- 작성한 리뷰 -->

        <!-- 신고글 보기 -->
        <div
          class="articleContainer"
          v-else-if="selectedMenu == 'menu2' && dataLoaded == true"
        >
          <div v-if="filteredReportedArticles.length == 0" class="noData">
            <span>데이터가 없습니다</span>
          </div>
          <div
            class="rowWrap reportedWrap"
            v-for="(article, index) in filteredReportedArticles"
            :key="index"
          >
            <!-- 데이터 로딩되었을 때 -->
            <div class="info1 reportedInfo1">
              <span class="articleStatus">
                <i class="fas fa-lock" v-if="article.status == 'H'"></i>
                <i class="fas fa-unlock" v-else-if="article.status == 'Y'"></i>
                <i
                  class="fas fa-bullhorn"
                  v-else-if="article.status == 'S'"
                ></i>
                <i class="fas fa-ban" v-else></i>
              </span>
              <span class="articleTitle">{{ article.title }}</span>
              <span class="articleRegDate">{{
                parsingDate(article.regDate)
              }}</span>
            </div>
            <div class="info2 reportedInfo">
              <span class="articleBody">{{ article.body }}</span>
            </div>
            <div class="info2 reportedInfo">
              <span class="articleBody reportedReason">{{
                parsingReportedReason(article.suspend)
              }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";

export default {
  props: ["data"],
  data() {
    return {
      selectedMenu: "menu1",
      dataLoaded: false,
      articleList: [],
      reviewList: [],
      reserve: null,
    };
  },
  updated() {
    if (this.reserve !== this.data) {
      this.reserve = this.data;
      if (this.data && this.data != "") {
        this.requestArticleList();
        this.requestReviewList();
      }
    }
    if (this.data == "") {
      this.selectedMenu = "menu1";
    }
  },
  methods: {
    selectMenu(menu) {
      this.selectedMenu = menu;
    },
    requestArticleList() {
      this.dataLoaded = false;
      const memberId = this.data.memberId;
      http
        .get("/admin/getAllArticles", {
          params: {
            memberId,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            this.articleList = res.data;
            this.dataLoaded = true;
          }
        });
    },
    requestReviewList() {
      this.dataLoaded = false;
      const memberId = this.data.memberId;
      http
        .get("/admin/getAllReviews", {
          params: {
            memberId,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            this.reviewList = res.data;
            this.dataLoaded = true;
          }
        });
    },
    parsingDate(date) {
      return (
        date[0] +
        "년 " +
        date[1] +
        "월 " +
        date[2] +
        "일 " +
        date[3] +
        "시 " +
        date[4] +
        "분"
      );
    },

    parsingReportedReason(data) {
      let mark = "///";
      let result = data.slice(data.indexOf(mark) + mark.length);
      return result;
    },
  },
  computed: {
    filteredReportedArticles() {
      let filteredArr = this.articleList.filter((article) => {
        return article.status == "S" ? true : false;
      });
      return filteredArr;
    },
  },
};
</script>

<style scoped>
.manage-div4 {
  display: flex;
  justify-content: space-evenly;
}
.manage-div2 {
  display: block;
  width: 45%;
}
.manage-text {
  font-weight: normal;
  font-size: 30px;
}
.profile-manage {
  margin-top: 10px;
}
.manage-box {
  text-align: left;
}
.profile-comm {
  margin-top: 4px;
  color: gray;
}
.profile-img {
  margin-top: 10px;
}
.profile-imgdiv {
  float: left;
  width: 80px;
  height: 80px;
  padding: auto 0;
  position: relative;
}
.profile-imgdiv img {
  display: block;
  width: 80px;
  height: 80px;
  border-radius: 50%;
}
.img-text {
  overflow: hidden;
  display: block;
  font-size: 22px;
  height: 80px;
  line-height: middle;
  padding-left: 20px;
  padding-bottom: 10px;
}
.login-tit {
  font-size: 20px;
}
.login-txt {
  float: right;
  color: #5a8c94;
  font-size: 20px;
}
.m-2 {
  cursor: pointer;
}
.nicknamebox {
  float: left;
  line-height: 80px;
}

.profile-1 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
  border-style: solid;
  border-width: 0px 0px 1px 1px;
}
.profile-2 {
  font-weight: 400;
  padding: 20px;
  display: flex;
  flex-direction: column;
  align-items: center;
  border-style: solid;
  border-width: 0px 1px 1px;
}
.button {
  padding: 20px;
  margin: 20px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.profile-tit {
  height: 80px;
  line-height: 80px;
}
.accordion-body {
  overflow-y: scroll;
  height: 250px;
}
.manage-div3 {
  width: 45%;
  min-width: 450px;
  display: flex;
  flex-direction: column;
}
.category {
  display: flex;
  justify-content: flex-start;
}
.menu {
  padding: 10px 20px 10px 20px;
  margin: 0 4px 0 0px;
  border-radius: 2px 15px 0px 0px;
  background: #5a8c94;
  transition: 0.2s;
}
.menu > span {
  padding: 3px 9px 3px 9px;
  border-radius: 8px;
}
.menu:hover > span {
  color: azure;
  cursor: pointer;
}
.activatedMenu {
  background: orange;
}
.deactivatedMenu {
  background: #436274;
  border-bottom: 2px solid rgb(30, 29, 39);
}
.wrapper {
  background: #5a8c94;
  width: 100%;
  height: 500px;
  display: flex;
  justify-content: center;
  align-items: center;
  overflow-y: scroll;
  flex-flow: wrap;
  border: 4px solid #5a8c94;
}
.articleContainer {
  width: 95%;
  background: #5a8c94;
  display: flex;
  flex-direction: column;
  overflow: auto;
  justify-content: flex-start;
  justify-items: flex-start;
  align-content: flex-start;
  align-items: flex-start;
}
.rowWrap {
  width: 99%;
  height: 200px;
  margin: 2px;
  display: flex;
  flex-flow: wrap;
  background: wheat;
  border-radius: 10px;
}
.info1 {
  width: 100%;
  height: 25%;
  display: flex;
  flex-flow: nowrap;
  padding: 5px;
}
.info1 > span {
  background: white;
  padding: 2px 9px 2px 9px;
  border-radius: 8px;
  margin: 5px;
}
.articleStatus {
  width: 7%;
}
.articleTitle {
  width: 60%;
}
.articleRegDate {
  width: 28%;
  font-size: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
}
.info2 {
  width: 100%;
  height: 75%;
  display: flex;
}
.articleBody {
  background: white;
  padding: 2px 9px 2px 9px;
  border-radius: 8px;
  margin: 0px 5px 5px 5px;
  width: 100%;
  overflow: auto;
  text-align: start;
}
.noData {
  width: 100%;
}
.noData > span {
  background: white;
  padding: 4px 12px 4px 12px;
  border-radius: 8px;
}
.reportedReason {
  background: orange;
  color: red;
}
.reportedWrap {
  height: 400px;
}
.reportedInfo {
  height: 40%;
}
.reportedInfo1 {
  height: 14%;
}

.articleTitle {
  display: inline-block; 
  width: 200px; 
  white-space: nowrap; 
  overflow: hidden; 
  text-overflow: ellipsis;
}
</style>
