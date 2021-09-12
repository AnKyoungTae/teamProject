<template>
<center v-if="dataLoaded == true">
  <div class="store-list">
    <table>
      <tr>
        <td colspan="2" align=center>
          <h2 class="store-title"><strong>{{storeInfo.name}}</strong></h2>
        </td>
      </tr>
      <tr>
        <th style="vertical-align: bottom;"><div class="store-th" style="vertical-align: bottom;">업체정보</div></th>
        <td style="vertical-align: bottom;"><div class="store-td" style="vertical-align: bottom; font-size:20px;">{{ storeInfo.storeKind }}</div></td>
      </tr>
      <tr>
        <td colspan="2">
          <div style="padding:0px; border-bottom: 1px solid gray;"></div>
        </td>
      </tr>
      <tr>
        <th style="padding-top:20px;"><div class="store-th">가게등록일</div></th>
        <td style="padding-top:20px;"><div class="store-td">{{parsingDate(data.regDate)}}</div></td>
      </tr>
      <tr>
        <th class="store-th" style="padding: 5px 0px;"></th>
      </tr>
      <tr>
        <th><div class="store-th">가게주소</div></th>
        <td><div class="store-td">{{storeInfo.address}}</div></td>
      </tr>
      <tr>
        <th class="store-th" style="padding: 5px 0px;"></th>
      </tr>
      <tr>
        <th><div class="store-th">상세주소</div></th>
        <td><div class="store-td">{{storeInfo.addressDetail}}</div></td>
      </tr>
      <tr>
        <th class="store-th" style="padding: 5px 0px;"></th>
      </tr>
      <tr>
        <th><div class="store-th">가게번호</div></th>
        <td><div class="store-td">{{ storeInfo.phone }}</div></td>
      </tr>
      <tr>
        <th class="store-th" style="padding: 5px 0px;"></th>
      </tr>
      <tr>
        <th><div class="store-th">가게설명</div></th>
        <td colspan="2"><div class="store-td" style="width:auto;">{{ storeInfo.body }}</div></td>
      </tr>
    </table>
  </div>
  <div style="margin:50px; border-top:1px solid black;"></div>
  <h2><strong>가게 사진</strong></h2>
  <div v-if="storeFiles != null">
    <div class="imgList">
      <div v-for="file in storeFiles" :key="file">
        <img class="imgFile" :src="file.name" />
      </div>
    </div>
  </div>
  <div v-else>
    <span style="font-size: 25px;">가게사진이 없습니다!</span>
  </div>
  <div style="margin:50px; border-top:1px solid black;"></div>
  <h2><strong>가게 메뉴</strong></h2>
  <div class="foodList" v-if="foodList != null">
    <div v-for="list in foodList" :key="list">
      <div class="card" style="width: 18rem; margin:20px;">
        <img :src="list.fileUrl" class="card-img-top" alt="..." />
        <div class="card-body">
          <p class="card-text">
            <table>
              <tr>
                <th style="text-align:center; font-size:25px;">{{list.name}}</th>
              </tr>
              <tr>
                <th align=right style="text-align:right;">{{list.price}}원</th>
              </tr>
              <tr>
                <td>{{list.description}}</td>
              </tr>
            </table>
          </p>
        </div>
      </div>
    </div>
  </div>
  <div v-if="foodList == null">
    <span style="font-size: 25px;">메뉴가 없습니다!</span>
  </div>
</center>
<div v-if="dataLoaded ==false">
  <div class="spinner-border" role="status">
    <span class="visually-hidden">Loading...</span>
  </div>
</div>
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
          console.log("shopInfo 받음");
          this.storeInfo = this.shopInfo.storeInfo;
          this.foodList = this.shopInfo.foodList;
          console.log("shopInfo 나눔");
          console.log(this.shopInfo.fileList);
          this.getStoreFiles(this.storeId);
        });
    },
  },
  mounted() {
    this.$nextTick(()=>
    this.getStoreInfo(this.storeId)
    )
  }
};
</script>

<style>
.store-th {
  width:300px;
  padding: 0px 50px;
  text-align: center;
}
.store-td {
  padding: 0px 50px;
  word-break:break-all;
  text-align: left;
}
.store-list table tr th, 
.store-list table tr td {
  vertical-align: top;
}
.store-list{
  width: 960px;
  font-size: 25px;
  align-content: center;
}
.store-title{
  margin: 20px;
}
.storeHeader {
  display: flex;
  justify-content: center;
  flex-wrap: wrap;
  padding: 10px;
}
.foodList {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.imgList {
  display: flex;
  flex-direction: row;
  flex-wrap: wrap;
}
.imgFile {
  width: 300px;
  height: 200px;
  margin: 14px;
}
</style>