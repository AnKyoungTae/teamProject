<template>
  <div
    @click="storePage(shopInfo.storeId)"
    class="storeList"
    title="클릭시 상세페이지로 이동합니다"
    style="border:1px solid gray;"
  >
    <div class="storeHeader">
      <div class="headerTitle">
        <h5 class="storeTitle">
          {{ shopInfo.name }}
        </h5>
        <small style="color: gray">
          {{ getStoreKind }}
        </small>
      </div>
      <div class="storeReview">
        <Reviewicon :averageScore="this.averageScore"></Reviewicon>
      </div>
    </div>
    <!-- 음식사진시작 -->
    <div class="slide">
      <div v-if="fileList != null" class="imgList">
        <div v-for="(file, index) in fileList" :key="file">
          <img class="imgFile" v-bind:id="'imgFile' + index" :src="file.name" />
        </div>
      </div>
    </div>
    <!-- 종료 -->
    <div class="storeFooter">
      <small class="address">
        {{ shopInfo.address }}, {{ shopInfo.addressDetail }}
      </small>
      <div style="width: 100px; text-align: right">
        {{ shopInfo.distance }}km
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import Reviewicon from "@/components/shop/Reviewicon.vue";

export default {
  components: {
    Reviewicon,
  },
  props: ["shopInfo"],
  mounted() {
    if (this.shopInfo.storeId != null) {
      http
        .get("/store/getStoreFiles", {
          params: {
            storeId: this.shopInfo.storeId,
          },
        })
        .then((res) => {
          if (res.status === 200) {
            this.fileList = res.data;
            this.fileList.splice(5);
          }
        })
        .catch((err) => {
          console.log("사진불러오는데 에러" + err);
        });

      http
        .get("/review/getAverageScore", {
          params: {
            storeId: this.shopInfo.storeId,
          },
        })
        .then((response) => {
          this.averageScore = response.data;
        })
        .catch((err) => {
          console.log(err);
        });
    }
  },
  data() {
    return {
      fileList: [],
      averageScore: null,
    };
  },
  methods: {
    storePage(storeId) {
      this.$router.push({
        path: "/shopDetail?n=" + storeId,
        query: { shopInfo: storeId },
      });
    },
  },
  computed: {
    getStoreKind() {
      let arr = this.shopInfo.storeKind.split(",");
      return arr[0] + ", " + arr[1] + "...";
    },
  },
};
</script>

<style scoped>
.storeList {
  color: black;
  text-decoration: none;
  padding: 0px 10px;
  cursor: pointer;
}
.headerTitle {
  width: 100%;
  display: flex;
  align-items: center;
}
.storeHeader,
.storeFooter {
  width: 100%;
  display: flex;
  justify-content: space-between;
}
.storeTitle {
  text-align: left;
  padding: 5px 0 0;
}
.storeReview {
  text-align: right;
  padding: 5px 0 0;
  width: 160px;
}
.address {
  text-align: left;
  justify-self: start;
  padding: 0px;
}
.storeTitle,
.address {
  display: inline-block;
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
  width: 200px;
}
.imgList {
  display: flex;
  flex-direction: row;
  align-items: flex-end;
}
.imgFile {
  width: 100px;
  height: 70px;
  margin-left: 10px;
  margin-right: 20px;
}
.storeList:hover {
  outline: 1px solid orange;
}
@media screen and (min-width: 1200px) {
  .imgList {
    display: none;
  }
}
@media screen and (max-width: 1200px) {
  .storeHeader {
    width: 100%;
  }
  .storeTitle,
  .address {
    width: 300px;
  }
}
@media screen and (max-width: 950px) {
  #imgFile4 {
    display: none;
  }
}
@media screen and (max-width: 770px) {
  #imgFile3 {
    display: none;
  }
}
@media screen and (max-width: 660px) {
  #imgFile2 {
    display: none;
  }
  .storeTitle,
  .address {
    width: 200px;
  }
}
@media screen and (max-width: 550px) {
  #imgFile1 {
    display: none;
  }
  .storeTitle,
  .address {
    width: 150px;
  }
}
</style>
