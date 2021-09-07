<template>
  <div id="wrapper" class="mt-2 mb-2 p-2 container-fluid">
    <notifications
      group="notifyApp"
      position="bottom right"
      style="margin-right: 30vh"
    />
    <div class="row">
      <div class="col-sm-4">
        <img :src="foodInfo.filename" class="image d-block" />
      </div>
      <div class="col-sm-8">
        <div v-if="!isEditMode">
          <span class="foodname">{{ foodInfo.foodname }} </span>
          <span class="badge bg-success m-1" v-if="foodInfo.status == 1"
            >판매중</span
          >
          <span class="badge bg-danger m-1" v-if="foodInfo.status == 2"
            >품절</span
          >
          <span class="badge bg-secondary m-1" v-if="foodInfo.status == 3"
            >판매중단</span
          >
          <span
            class="badge bg-warning m-1"
            v-if="isNew"
            title="3일 이내로 등록된 음식입니다"
            >New</span
          >
        </div>
        <div v-else-if="isEditMode">
          <input
            class="form-control form-control-sm"
            type="text"
            aria-label=".form-control-sm example"
            v-model="bounded.foodname"
          />
        </div>
        <!-- 핫한음식 배지달기 -->
        <p class="price" style="font-size: 20px">{{ foodInfo.price }} 원</p>
        <div class="d-block">
          <div class="multiline-ellipsis" v-if="!isEditMode">
            {{ foodInfo.description }}
          </div>
          <div v-if="isEditMode">
            <div class="mb-3">
              <label for="foodDescInput" class="form-label"
                >수정할 내용을 입력하세요</label
              >
              <textarea
                class="form-control"
                id="foodDescInput"
                rows="3"
                v-model="bounded.description"
              ></textarea>
            </div>
          </div>
        </div>
        <!--  가격빼고 수정, 등록취소버튼? -->
        <div class="buttonWrapper mt-3 row">
          <div class="col-3"></div>
          <select
            name="foodStatus"
            id="foodStatus"
            class="form-select form-select-sm d-inline col"
            v-if="isEditMode"
            v-model="bounded.status"
          >
            <option value="1" selected>판매중</option>
            <option value="2">품절</option>
            <option value="3">판매중지</option>
          </select>
          <div class="col-1"></div>
          <div
            class="btn btn-primary d-inline m-1 col"
            @click="check"
            v-if="!isEditMode"
          >
            수정
          </div>
          <div
            class="btn btn-success d-inline m-1 col"
            @click="confirmEdit"
            v-else-if="isEditMode"
          >
            확인
          </div>
          <div
            class="btn btn-danger d-inline m-1 col"
            @click="check"
            v-if="isEditMode"
          >
            삭제
          </div>
          <div class="col-1"></div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import http from "@/api/http";
import { error, success, normal } from "@/api/notification";
export default {
  data() {
    return {
      editMode: false,
      bounded: "",
      uploadFile: null,
    };
  },
  mounted() {
    this.bounded = this.foodInfo;
  },
  props: ["foodInfo"],
  computed: {
    isNew() {
      return new Date().getDate() - this.foodInfo.regDate[2] < 3 ? true : false;
    },
    isEditMode() {
      return this.editMode;
    },
  },
  methods: {
    check() {
      let willEdit = confirm("메뉴를 수정하시겠습니까?");
      console.log(this.bounded);
      if (willEdit === true) {
        this.editMode = true;
      }
    },
    confirmEdit() {
      let doneEdit = confirm("이대로 진행할까요?");
      if (doneEdit === true) {
        if (!this.bounded.foodname) {
          error("음식 이름은 공백이 될 수 없습니다!", this);
          return;
        }

        if (!this.bounded.description) {
          error("음식 설명은 공백이 될 수 없습니다!", this);
          return;
        }
        normal("음식정보를 수정합니다.", this);

        http
          .post("http://localhost:8083/store/updateFoodInfo", {
            foodId: this.bounded.foodId,
            foodname: this.bounded.foodname,
            description: this.bounded.description,
            status: this.bounded.status,
          })
          .then((res) => {
            if (res.status === 200) {
              console.log("음식 수정이 완료되었습니다.");
              success("음식정보가 수정되었습니다.", this);
              //foodInfo = res.data;
            }
          })
          .catch((e) => {
            error("오류가 발생했습니다. 다시 시도해주세요", this);
            console.log(e);
          });
      } else {
        this.bounded = this.foodInfo;
      }
      this.editMode = false;
    },
  },
};
</script>

<style scoped>
#wrapper {
  border-radius: 10px;
  border: 2px solid grey;
}
.image {
  width: 180px;
  height: 180px;
  border-radius: 10px;
}
.foodname {
  font-size: 25px;
  font-weight: bold;
}
.price {
  font-size: 14px;
  font-weight: lighter;
}
.multiline-ellipsis {
  overflow: hidden;

  text-overflow: ellipsis;

  display: -webkit-box;

  -webkit-line-clamp: 3; /* 라인수 */

  -webkit-box-orient: vertical;

  word-wrap: break-word;
}
</style>
