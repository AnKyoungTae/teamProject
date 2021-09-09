<template>
<center v-if="data">
  <table class="storeDetailList">
    <tr>
      <th>가게 아이디</th>
      <td>{{data.storeId}}</td>
    </tr>
    <tr>
      <th>가게 이름</th>
      <td>{{data.storename}}</td>
    </tr>
    <tr>
      <th>판매자</th>
      <td>{{data.membername}}</td>
    </tr>
    <tr>
      <th>가게 상태</th>
      <td>{{data.status}}</td>
    </tr>
    <tr>
      <th>가게 등록일</th>
      <td>{{parsingDate(data.regDate)}}</td>
    </tr>
    <tr>
      <th>가게 주소</th>
      <td>{{data.address}}</td>
    </tr>
  </table>
  {{storeInfo}}
  {{storeFiles}}
</center>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { error, success, normal } from "@/api/notification";
import http from "@/api/http";

export default {
  props: ["data"],
  data() {
    return {
      storeInfo: null,
      storeFiles: [],
      dataLoaded: false,
      authorized: false,
    }
  },
  computed: {
    ...mapGetters({
      getRole: "auth/getUserRole",
      getToken: "auth/getAccessToken",
    }),
  },
  methods: {
    ...mapMutations({
      setMyStore: "auth/SET_MY_STORE",
    }),
    parsingDate(date) {
      // 배열로 들어온다고 가정한다.
      return date[0] +" 년 "+ date[1] + " 월 " + date[2] + " 일 "
    },
    getStoreFiles(storeId) {
      http
        .get("/store/getStoreFiles", {
          params: {
            storeId: this.data.storeId,
          },
        })
        .then((response) => {
          if (response.status === 200) {
            this.storeFiles = response.data;
            success("가게사진들을 불러왔습니다!", this);
            this.dataLoaded = true;
            return;
          }
        })
        .catch((err) => {
          console.log(err);
          error("오류가 발생했습니다. 다시 시도해주세요", this);
          // this.$store.dispatch("auth/logout");
          // this.$router.push({ path: "/" });
        });
    },
    edit(request) {
      this.correction = request;
    },
  },
  mounted() {
    normal("가게정보를 검색합니다...", this);
    http
      .post("/store/findStore")
      .then((response) => {
        if (response.status === 200) {
          console.log("들어옴");
          console.log(response.data);
          this.storeInfo = response.data;
          if (this.storeInfo.status != null) {
            console.log("값이있네");
            success("등록된 가게정보를 찾았습니다!", this);
            // 전역변수에 가게 등록로직
            this.setMyStore(this.storeInfo);
            this.getStoreFiles(this.storeInfo.storeId);
            this.authorized = true;
            console.log("완성");
            return;
          }
          error("등록된 가게가 없습니다!", this);
          this.dataLoaded = true;
        }
      })
      .catch((err) => {
        console.log(err);
        error("오류가 발생했습니다. 다시 시도해주세요", this);
        this.$store.dispatch("auth/logout");
        this.$router.push({ path: "/" });
      });
  }
};
</script>

<style>
.storeDetailList {
  width:600px;
  font-size:25px;
  vertical-align: top;
  text-align: left;
  align-content: center;
}
.storeDetailList tr th {
  width: 120px;
}
.storeDetailList tr td {
  width: 480px;
  word-break:break-all;
  padding-left:50px;
}
.storeDetailList tr th,
.storeDetailList tr td {
  padding-bottom:15px;
}
</style>
