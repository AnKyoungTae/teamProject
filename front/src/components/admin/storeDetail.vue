<template>
<center v-if="dataLoaded == true">
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
  <div v-if="foodList != null">
    <div v-for="list in foodList" :key="list">
      <div class="card" style="width: 18rem;">
        <img :src="list.fileUrl" class="card-img-top" alt="..." />
        <div class="card-body">
          <p class="card-text">
              가격: {{list.price}},
              이름: {{list.name}},
              설명: {{list.description}}
              상태: {{list.status}}
          </p>
        </div>
      </div>
    </div>
  </div>
  {{foodList}}
  <!--
  {{storeFiles}}
  {{shopInfo}}
  {{storeInfo}}-->
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
      shopInfo: null,
      storeFiles: [],
      dataLoaded: false,
      authorized: false,
      storeId: null,
      foodList: [],
      storeInfo: null,
    }
  },
  created() {
    this.storeId = this.data.storeId
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
            storeId: storeId,
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
    getStoreInfo(storeId) {
      http
        .get("/store/getStoreInfos", {
          params: {
            storeId: storeId,
          },
        })
        .then((res) => {
          console.log(res.data);
          this.shopInfo = res.data;
          console.log("shopInfo 들어옴");
          this.storeInfo = this.shopInfo.storeInfo;
          this.foodList = this.shopInfo.foodList;
          console.log("shopInfo 나눔");
          console.log(this.shopInfo.fileList);
          this.getStoreFiles(this.storeId);
        });
    },
  },
  mounted() {
    this.getStoreInfo(this.storeId);
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
