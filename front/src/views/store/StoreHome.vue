<template>
  <div>
    <!-- 제목 -->
    <h1 class="ml-4" v-if="userInfo">{{ userInfo[2] }} 님, 환영합니다!</h1>
    <!-- 노티 -->
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <!-- 스피너 -->
    <div v-if="!dataLoaded">
      <div class="spinner-border mt-4" role="status">
        <span class="visually-hidden">Loading...</span>
      </div>
    </div>
    <div v-else>
      <!-- 가게상세정보 -->
      <div v-if="correction == true">
        <store-detail
          :storeFiles="storeFiles"
          :storeInfo="storeInfo"
          :isMyStore="authorized"
          @trueEdit="edit"
          v-if="storeInfo.status != null"
        ></store-detail>
      </div>
      <div v-if="correction == false">
        <edit-Store
          :storeFiles="storeFiles"
          :storeInfo="storeInfo"
          :isMyStore="authorized"
          @trueEdit="edit"
          v-if="storeInfo.status != null"
        ></edit-Store>
      </div>
      <!-- 가게가 없을때 -->
      <div id="storeWrapper" class="container" v-if="storeInfo.status == null">
        <div class="row align-items-center">
          <div class="col align-self-center">
            <div id="choices">
              <div>
                <span>아직 가게를 등록하지 않으셨군요?</span><br />
                <router-link to="storeRegister">
                  <button class="btn btn-primary mt-2">가게 등록</button>
                </router-link>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { mapGetters, mapMutations } from "vuex";
import { error, success, normal } from "@/api/notification";
import http from "@/api/http";
import storeDetail from "@/components/store/StoreDetail.vue";
import EditStore from "@/components/store/EditStore.vue";

export default {
  components: {
    storeDetail,
    EditStore,
  },
  data() {
    return {
      userInfo: null,
      storeInfo: null,
      storeFiles: [],
      dataLoaded: false,
      authorized: false,
      correction: true,
    };
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
    edit(request) {
      this.correction = request;
    },
  },
  mounted() {
    normal("가게정보를 검색합니다...", this);
    if (this.getRole != "SELLER") {
      error("이 페이지에 접근할 수없습니다.", this);
      this.$store.dispatch("auth/logout");
      this.$router.push({ path: "/" });
      return;
    }
    http
      .post("/store/findStore")
      .then((response) => {
        if (response.status === 200) {
          this.storeInfo = response.data;
          if (this.storeInfo.status != null) {
            success("등록된 가게정보를 찾았습니다!", this);
            // 전역변수에 가게 등록로직
            this.setMyStore(this.storeInfo);
            this.getStoreFiles(this.storeInfo.storeId);
            this.authorized = true;
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
  },
};
</script>

<style scoped>
#storeWrapper {
  width: 40rem;
  height: 30rem;
  position: relative;
  top: 2rem;
  border: 2px dashed palegreen;
  border-radius: 10px;
  text-align: center;
}
#choices {
  position: absolute;
  left: 30%;
  top: 40%;
}
</style>
