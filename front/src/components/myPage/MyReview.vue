<template>
  <div style="width: 100%">
    <div class="manage-div">
      <div class="manage-div2">
        <h3 class="manage-text">
          내가 작성한 리뷰 수: {{ reviewLists.length }}
        </h3>
        <div class="profile-manage">
          <div class="manage-box">
            <div v-for="(reviewList, index) in reviewLists" :key="index">
              <div v-if="reviewList.status != 'n'">
                <p class="profile-comm">
                  {{ reviewList.nickname }}님이 등록한 리뷰입니다.
                </p>
                <div style="width: 100%">
                  <div class="review3-text">
                    <span class="review3-span1"
                      >{{ reviewList.name }}</span>
                  </div>
                  <div class="review3-div">
                    <a class="review3-a m-1" @click="removeReview(reviewList.reviewId)">삭제</a>
                  </div>
                </div>
                <div class="review3-star" style="width: 400px">
                  <div class="star-1">
                    <i
                      class="fas fa-star"
                      v-for="star of reviewList.score"
                      :key="star"
                    ></i
                    >&nbsp;&nbsp;
                    <span style="color: burlywood"
                      >{{ reviewList.score }} /5.0</span>
                    <span class="review3-span2"
                      >{{ reviewList.regDate[0] }}.{{ reviewList.regDate[1]
                      }}{{ reviewList.regDate[2] }}</span>
                  </div>
                </div>
                <div>
                  <img
                    class="review3-img m-2"
                    :src="reviewList.foodPhoto"
                    style="width: 180px; height: 180px; margin-left: 10px"
                    v-if="reviewList.foodPhoto"
                  />
                </div>
                <div class="review3-view">
                  <!-- 내용 -->
                  <p v-if="reviewList.body" style="font-size: 18px">
                    {{ reviewList.body }}
                  </p>
                  <p v-else>삭제된 리뷰입니다.</p>
                </div>
                <!-- 사장님 댓글 -->
                <div
                  v-if="reviewList.reply"
                  style="
                    border: 1px solid lightgray;
                    background-color: lightgray;
                    margin-bottom: 16px;
                    padding: 10px;
                  "
                >
                  <span>&nbsp;&#10551;&nbsp; 사장님댓글</span>
                  <p style="margin: 0px">{{reviewList.reply}}</p>
                </div>
                <hr style="margin-left: -16px; margin-top: 40px" />
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import { error, success, normal } from "@/api/notification";

import { mapGetters } from "vuex";

export default {
  data() {
    return {
      memberId: null,
      reviewLists: [],
      reviewStatus: true,
    };
  },
  methods: {
    orderDel() {
      alert("삭제되었습니다!");
    },
    reviewList() {
      http
        .get("/profile/getMyReview", {
          params: {
            memberId: this.memberId,
          },
        })
        .then((res) => {
          console.log(res);
          console.log(res.data);
          this.reviewLists = res.data;
        })
        .catch((err) => {
          console.log(err);
        });
    },
    removeReview(reviewId) {
      if (this.getUserId != this.memberId) {
        alert("권한이 없습니다!");
        return;
      }
      let sure = confirm("정말 삭제하시겠습니까?");
      if (!sure) {
        return;
      }
      const data = {
        reviewId,
        status: "N",
      };
      http.post("/profile/ReviewStatus", data)
      .then((res) => {
        console.log(res);
        if (res.status === 200) {
          success("리뷰를 삭제했습니다!", this);
          location.reload();
        }
      });
    },
  },
  mounted() {
    this.memberId = this.getUserId;
    this.reviewList();
  },
  computed: {
    ...mapGetters({
      getUserId: "auth/getUserId",
    }),
  },
};
</script>

<style scoped>
.review3 {
  width: 100%;
  text-align: left;
  padding-left: 20px;
  padding-right: 20px;
}
.review3-text {
  float: left;
  width: 80%;
}
.review3-span1 {
  font-size: 20px;
  font-weight: bold;
}
.review3-span2 {
  color: lightgray;
  margin-left: 10px;
}
.review3-a {
  text-decoration: none;
  color: gray;
  font-weight: bold;
}
.review3-a:hover {
  cursor: pointer;
  color: tomato;
}

.review3-img {
  width: 100%;
  text-align: center;
}

#img {
  width: 100%;
}

.review3-menu {
  color: burlywood;
  font-size: 14px;
}

.fa-star {
  color: burlywood;
}

.review3-div {
  text-align: right;
  padding-right: 20px;
  float: right;
  width: 20%;
}

.manage-div {
  max-width: 560px;
  margin: 50px auto 0;
  display: block;
}
.manage-div2 {
  display: block;
}
.manage-text {
  font-weight: normal;
  font-size: 20px;
}
.profile-manage {
  margin-top: 10px;
  border-top: 1px solid #7c7c7c;
}
.manage-box {
  margin-top: 16px;
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
  color: #5066aa;
  font-size: 20px;
}
.m-2 {
  cursor: pointer;
}
.nicknamebox {
  float: left;
  line-height: 80px;
}
.filebox {
  margin-left: 30%;
  height: 80px;
  text-align: right;
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
.filebox label {
  display: inline-block;
  padding: 0.5em 0.75em;
  color: white;
  font-size: inherit;
  line-height: normal;
  vertical-align: middle;
  background-color: #2f855a;
  cursor: pointer;
  border: 1px solid #ebebeb;
  border-bottom-color: #e2e2e2;
  border-radius: 0.25em;
}
.filebox input[type="file"] {
  /* 파일 필드 숨기기 */
  position: absolute;
  width: 1px;
  height: 1px;
  padding: 0;
  margin: -1px;
  overflow: hidden;
  clip: rect(0, 0, 0, 0);
  border: 0;
}

.profile-scroll {
  overflow-x: hidden;
  overflow-y: auto;
  height: 600px;
}
.reviewCount {
  height: 50px;
  border-bottom: 1px solid black;
  display: flex;
  flex-direction: row;
  justify-content: center;
  align-items: center;
}
.myReview {
  height: 220px;
  border-top: 1px solid black;
  border-bottom: 1px solid black;
  border-right: 1px solid black;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}
.myReview-img {
  width: 250px;
  height: 200px;
}
.myReview-text {
  border-top: 1px solid black;
  border-bottom: 1px solid black;
  padding: 20px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}
.myReview-Contents {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}
.fa-star {
  color: rgb(196, 196, 36);
}
.Review-link:link,
.Review-link:visited,
.Review-link:hover {
  text-decoration: none;
  color: black;
}
.myReview-lining {
  text-align: start;
  white-space: normal;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}
</style>
