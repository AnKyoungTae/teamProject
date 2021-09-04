<template>
  <a href="#" class="shopList">
    <div class="row storeHeader">
      <div class="col-10 storeTitle">
        <h5 class="mb-1">{{ shopInfo.name }}</h5>
      </div>
      <div class="col-2 storeKind">
        <small class="text-muted">{{ getStoreKind }}</small>
      </div>
    </div>
    <div class="row">
      <!-- 음식사진시작 -->
      <div class="col-12 slide">
        <div v-if="fileList != null" class="imgList">
          <div v-for="(file, index) in fileList" :key="file">
            <img class="imgFile" v-bind:id="'imgFile' + index" :src="file.name" />
          </div>
        </div>
      </div>
      <!-- 종료 -->
    </div>
    <div class="row address">
      <small> {{ shopInfo.address }}, {{ shopInfo.addressDetail }} </small>
    </div>
  </a>
</template>

<script>
import http from "@/api/http";

export default {
  components: {
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
            console.log(res.data);

            this.fileList.splice(5);
          }
        })
        .catch((err) => {
          console.log("사진불러오는데 에러" + err);
        });
    }
  },
  data() {
    return {
      fileList: [],
    };
  },
  methods: {
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
.shopList {
  width: 100%;
  display: grid;
  justify-content: space-around;
  color: black;
  text-decoration: none;
}
.storeTitle {
  text-align: left;
  padding: 5px 0 0;
}
.storeKind {
  padding: 5px 0 0;
}
.address {
  text-align: left;
  justify-self: start;
  padding: 0px;
}
.imgList {
  display: flex;
  flex-direction: row;
  justify-content: space-between;
}
.imgFile {
  width: 100px;
  height: 70px;
}
@media screen and (min-width: 1200px) {
  .imgList {
    display: none;
  }
  .storeHeader {
    width: 300px;
    height: 50px;
  }
}
@media screen and (max-width: 1200px) {
  .imgList {
    width: 700px;
  }
  .storeHeader {
    width: 100%;
  }
}
@media screen and (max-width: 1100px) {
  .imgList {
    width: 600px;
  }
}
@media screen and (max-width: 900px) {
  .imgList {
    width: 500px;
  }
  #imgFile4 {
    display: none;
  }
}
@media screen and (max-width: 800px) {
  .imgList {
    width: 400px;
  }
  #imgFile3 {
    display: none;
  }
}
@media screen and (max-width: 700px) {
  .imgList {
    width: 300px;
  }
  #imgFile2 {
    display: none;
  }
}
@media screen and (max-width: 600px) {
  .imgList {
    width: 200px;
  }
  #imgFile1 {
    display: none;
  }
}
</style>