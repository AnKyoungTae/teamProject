<!-- 데이터 FoodDetail참고 (가게 id가져오기-->
<template>
  <div class="review-box">
    <div class="review-store">
      <p>가게이름</p>
      <span>별점</span>
    </div>
    <div>시킨 음식</div>
    <div>
      <div>
        <textarea
          class="form-control"
          rows="3"
          placeholder="음식에 대한 솔직한 리뷰를 남겨주세요"
        ></textarea>
      </div>
      <div>
        <div class="review-camera"><i class="fas fa-camera"></i></div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.review-box {
  border: 1px solid black;
}
.form-control {
  width: 60%;
}
.review-camera {
  width: 70px;
  line-height: 70px;
  border: 1px solid lightgray;
}
.fa-camera {
  width: 25px;
  height: 25px;
}
</style>
