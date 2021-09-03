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
        <div id="container">
          <div class="slide_wrap">
            <div class="slide_box">
              <div class="slide_list clearfix">
                <div v-for="file in fileList" :key="file" v-bind:class="'slide_content slide' + index">
                  <img class="menuImg" :src="file.name" />
                </div>
              </div>
            </div>
            <div class="slide_btn_box">
              <button type="button" class="slide_btn_prev"><i class="fas fa-chevron-left"></i></button>
              <button type="button" class="slide_btn_next"><i class="fas fa-chevron-right"></i></button>
            </div>
            <ul class="slide_pagination"></ul>
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
            this.renderSplide();
          }
        })
        .catch((err) => {
          console.log("사진불러오는데 에러" + err);
        });
    }

    const slideList = document.querySelector('.slide_list'); // Slide parent dom
    const slideContents = document.querySelectorAll('.slide_content'); // each slide dom
    const slideBtnNext = document.querySelector('.slide_btn_next'); // next button
    const slideBtnPrev = document.querySelector('.slide_btn_prev'); // prev button
    const slideLen = slideContents.length; // slide length
    const slideWidth = 400; // slide width
    const slideSpeed = 300; // slide speed
    const startNum = 0; // initial slide index (0 ~ 4)
    slideList.style.width = slideWidth * (slideLen + 2) + "px";
    // Copy first and last slide
    let firstChild = slideList.firstElementChild;
    let lastChild = slideList.lastElementChild;
    let clonedFirst = firstChild.cloneNode(true);
    let clonedLast = lastChild.cloneNode(true);
    // Add copied Slides
    slideList.appendChild(clonedFirst);
    slideList.insertBefore(clonedLast, slideList.firstElementChild);
    slideList.style.transform = "translate3d(-" + (slideWidth * (startNum + 1)) + "px, 0px, 0px)";
    let curIndex = startNum; // current slide index (except copied slide)
    let curSlide = slideContents[curIndex]; // current slide dom
    curSlide.classList.add('slide_active');
    /** Next Button Event */
    slideBtnNext.addEventListener('click', function() {
      if (curIndex <= slideLen - 1) {
        slideList.style.transition = slideSpeed + "ms";
        slideList.style.transform = "translate3d(-" + (slideWidth * (curIndex + 2)) + "px, 0px, 0px)";
      }
      if (curIndex === slideLen - 1) {
        setTimeout(function() {
          slideList.style.transition = "0ms";
          slideList.style.transform = "translate3d(-" + slideWidth + "px, 0px, 0px)";
        }, slideSpeed);
      curIndex = -1;
    }
    curSlide.classList.remove('slide_active');
    curSlide = slideContents[++curIndex];
    curSlide.classList.add('slide_active');
    });
    /** Prev Button Event */
    slideBtnPrev.addEventListener('click', function() {
      if (curIndex >= 0) {
        slideList.style.transition = slideSpeed + "ms";
        slideList.style.transform = "translate3d(-" + (slideWidth * curIndex) + "px, 0px, 0px)";
      }
      if (curIndex === 0) {
        setTimeout(function() {
          slideList.style.transition = "0ms";
          slideList.style.transform = "translate3d(-" + (slideWidth * slideLen) + "px, 0px, 0px)";
        }, slideSpeed);
      curIndex = slideLen;
      }
      curSlide.classList.remove('slide_active');
      curSlide = slideContents[--curIndex];
      curSlide.classList.add('slide_active');
    });

    // Add pagination dynamically
    let pageChild = '';
    for (var i = 0; i < slideLen; i++) {
    pageChild += '<li class="dot';
    pageChild += (i === startNum) ? ' dot_active' : '';
    pageChild += '" data-index="' + i + '"><a href="#"></a></li>';
    }
    pagination.innerHTML = pageChild;
    const pageDots = document.querySelectorAll('.dot'); // each dot from pagination

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
/* 슬라이드 css */
.slide_wrap { 
  position: relative; 
  width: 700px; 
  margin: auto;
  border: 1px solid yellow;
}
.slide_box { 
  width: 100%; 
  margin: auto; 
  overflow-x: hidden; 
  border: 1px solid red;
}
.slide_content { 
  display: table; 
  float: left; 
  width: 100px; 
  height: 70px; 
  border: 1px solid blue;
}
img {
  width: 100px;
  height: 70px;
}
/*
@media screen and (min-width: 1200px) {
  .slide {
    display: none;
  }
  .storeHeader {
    width: 300px;
    height: 50px;
  }
}
@media screen and (max-width: 1200px) {
  img {
    width: 100px;
    height: 70px;
  }
  .slide {
    width: 700px;
  }
  .storeHeader {
    width: 100%;
  }
}
@media screen and (max-width: 1100px) {
  .slide {
    width: 600px;
  }
}
@media screen and (max-width: 900px) {
  .slide {
    width: 500px;
  }
}
@media screen and (max-width: 800px) {
  .slide {
    width: 400px;
  }
}
@media screen and (max-width: 700px) {
  .slide {
    width: 300px;
  }
}
*/
</style>