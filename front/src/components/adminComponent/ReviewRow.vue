<template>
  <div class="review3" v-if="available">
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div style="width: 100%">
      <div class="review3-text">
        <span class="review3-span1">{{ data.nickname }}</span>
        <span class="review3-span2">{{ data.regDate[0] }}.{{ data.regDate[1] }}.{{  data.regDate[2] }}</span>
      </div>
      <div class="review3-div">
        <a class="review3-a m-1" v-if="ableToEdit" @click="removeReview"
          >삭제</a
        >
        <a class="review3-a m-1" @click="reportReview">신고</a>
      </div>
    </div>
    <div class="review3-star" style="width: 200px">
      <div class="star-1">
        <i class="fas fa-star" v-for="star of data.score" :key="star"></i
        >&nbsp;&nbsp; <span style="color: burlywood;">{{ data.score }} /5.0</span>
      </div>
    </div>
    <div>
      <img
        class="review3-img m-2"
        :src="data.foodPhoto"
        style="
          width: 180px;
          height: 180px;
          margin-left: 10px;
        "
        v-if="data.foodPhoto"
      />
    </div>
    <div class="review3-menu">
      <!-- 시킨음식 -->
      <span>{{data}}</span>
    </div>
    <div class="review3-view">
      <!-- 내용 -->
      <p v-if="data.body" style="font-size: 18px;">
        {{ data.body }}
      </p>
      <p v-else>삭제된 리뷰입니다.</p>
    </div>
    <!-- 사장님 댓글 -->
    <div v-if="data.reply" style="border: 1px solid lightgray; background-color: lightgray; margin-bottom: 16px; padding: 10px;">
      <span>&nbsp;&#10551;&nbsp; 사장님댓글</span>&nbsp;&nbsp;<span style="font-size: 14px; color: gray;"> {{ data.updateDate[0] }}.{{ data.updateDate[1] }}.{{ data.updateDate[2] }}</span>
      <p style="margin: 0px;">{{ data.reply }}</p>
    </div>
    <hr style="margin-left: -16px;"/>
    <div v-if="replySuccess == true">
      <!-- 댓글달기 성공했을때 새로고침대신 가짜로띄워주기-->
      <span>사장님댓글</span>
      <p>
        {{ reply }}
      </p>
    </div>
    <!-- 사장일때, -->
    <div v-if="!data.reply && isOwner && replymode == false && reply == ''" style="margin-bottom: 16px;">
      <span class="m-2">사장님이시군요? 리뷰에 댓글을 달아주세요!</span>
      <div class="btn btn-outline-primary" @click="willReply">댓글달기</div>
    </div>
    <div v-if="replymode === true">
      <!-- 입력창 -->
      <textarea
        name="reply"
        id="reply"
        rows="5"
        style="resize: none; width: 100%"
        v-model="reply"
        placeholder="저희 가게를 이용해주셔서 감사합니다~^^ "
      ></textarea>
      <!-- 버튼 -->
      <div
        class="btn btn-outline-success m-2"
        @click="requestReply(data.reviewId)"
      >
        댓글달기
      </div>
      <div class="btn btn-outline-danger m-2" @click="cancelReply">
        취소하기
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import { normal, error, success } from "@/api/notification";
import { mapGetters } from "vuex";

export default {
  props: ["data", "isOwner"],
  data() {
    return {
      replymode: false,
      reply: "",
      replySuccess: false,
      available: true,
    };
  },
  computed: {
    ...mapGetters({
      getUserId: "auth/getUserId",
    }),
    ableToEdit() {
      return this.getUserId == this.data.writerid;
    },
  },
  methods: {
    willReply() {
      this.replymode = true;
    },
    cancelReply() {
      this.replymode = false;
    },
    requestReply(reviewId) {
      if (this.isOwner === false) {
        alert("사장님만 댓글을 달 수 있습니다.");
        return;
      }
      if (!this.data.reviewId) {
        error("게시글을 파악할 수 없습니다!", this);
        console.log(this.data);

        return;
      }
      if (!this.reply) {
        error("댓글을 입력해주세요!", this);
        return;
      }
      const data = {
        reviewId,
        reply: this.reply,
      };
      http.post("/review/addReply", data).then((res) => {
        if (res.status === 200 && res.data > 0) {
          success("댓글을 달았습니다!", this);
          this.replySuccess = true;
          this.replymode = false;
        }
      });
    },
    removeReview() {
      if (!this.ableToEdit) {
        alert("권한이 없습니다!");
        return;
      }
      let sure = confirm("정말 삭제하시겠습니까?");
      if (!sure) {
        return;
      }
      const data = {
        reviewId: this.data.reviewId,
        status: "N",
      };
      http.post("/review/modifyReviewStatus", data).then((res) => {
        console.log(res);
        if (res.status === 200) {
          success("리뷰를 삭제했습니다!", this);
          this.available = false;
        }
      });
    },
    reportReview() {
      const data = {
        reviewId: this.data.reviewId,
        status: "S",
      };
      http.post("/review/modifyReviewStatus", data).then((res) => {
        console.log(res);
        if (res.status === 200) {
          normal("리뷰를 신고했습니다!", this);
        }
      });
    },
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
</style>
