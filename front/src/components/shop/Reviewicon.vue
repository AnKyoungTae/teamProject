<template>
    <small>
        <div v-if="averageScore != null">
            <span class="star" v-if="averageScore >= 4.8">
                <!-- 5점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 4.3">
                <!-- 4.5점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star-half-alt"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 3.8">
                <!-- 4점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 3.3">
                <!-- 3.5점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star-half-alt"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 2.8">
                <!-- 3점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 2.3">
                <!-- 2.5점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="fas fa-star-half-alt"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 1.8">
                <!-- 2점 -->
                <i class="fas fa-star"></i><i class="fas fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 1.3">
                <!-- 1.5점 -->
                <i class="fas fa-star"></i><i class="fas fa-star-half-alt"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 0.8">
                <!-- 1점 -->
                <i class="fas fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 0.3">
                <!-- 0.5점 -->
                <i class="fas fa-star-half-alt"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span class="star" v-else-if="averageScore >= 0">
                <!-- 0점 -->
                <i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i><i class="far fa-star"></i>
            </span>
            <span>
                {{ averageScore }}점
            </span>
        </div>
        <div v-else>
            평점이 없습니다.
        </div>
    </small>
</template>

<script>

export default {
  props: {
    averageScore:  Number,
  }
};
</script>
<style scoped>
.star {
    color:#ffd400;
}
</style>