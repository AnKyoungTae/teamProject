import { createRouter, createWebHistory } from "vue-router";
const routes = [
  {
    path: "/", //홈 화면
    name: "Home",
    component: () => import("../views/Home.vue"),
  },
  {
    path: "/cart",
    name: "Cart",
    component: () => import("../views/cart/Cart.vue"),
  },
  {
    path: "/shopCategory",
    name: "ShopCategory",
    component: () => import("../views/food/ShopCategory.vue"),
  },
  {
    path: "/shoplist",
    name: "ShopList",
    component: () => import("../views/food/ShopList.vue"),
  },
  {
    path: "/shopDetail",
    name: "ShopDetail",
    component: () => import("../views/food/FoodDetail.vue"),
    props: true,
  },
  {
    path: "/event",
    name: "Event",
    component: () => import("../views/event/Event.vue"),
  },
  {
    path: "/eventadd",
    name: "EventAdd",
    component: () => import("../views/event/EventAdd.vue"),
  },
  {
    path: "/serviceCenter",
    name: "ServiceCenter",
    component: () => import("../views/serviceCenter/ServiceCenter.vue"),
    children: [
      {
        path: "/notice",
        name: "Notice",
        component: () => import("../components/service/Notice.vue"),
      },
      {
        path: "/qna",
        name: "Q&A",
        component: () => import("../components/service/Q&A.vue"),
      },
      {
        path: "/faq",
        name: "FAQ",
        component: () => import("../components/service/FAQ.vue"),
      },
      {
        path: "/writeForm",
        name: "WriteForm",
        component: () => import("../components/service/WriteForm.vue"),
      },
      {
        path: "/boardList",
        name: "BoardList",
        component: () => import("../components/service/BoardList.vue"),
      },
    ],
  },
  {
    path: "/myPage",
    name: "MyPage",
    component: () => import("../views/myPage/MyPage"),
  },
  {
    path: "/storeregister",
    name: "StoreRegister",
    component: () => import("../views/store/StoreRegister.vue"),
  },
  {
    path: "/store",
    name: "StoreHome",
    component: () => import("../views/store/StoreHome.vue"),
    children: [
      {
        path: "/review",
        name: "Review",
        component: () => import("../components/adminComponent/Review.vue"),
      },
      {
        path: "/storemap",
        name: "StoreMap",
        component: () => import("../components/adminComponent/StoreMap.vue"),
      },
      {
        path: "/storemenu",
        name: "StoreMenu",
        component: () => import("../components/adminComponent/StoreMenu.vue"),
      },
    ],
  },
  {
    path: "/manageStore",
    name: "ManageStore",
    component: () => import("../views/admin/ManageStore.vue"),
  },
  {
    path: "/admincouponadd",
    name: "AdminCouponAdd",
    component: () => import("../views/admin/AdminCouponAdd.vue"),
  },
  {
    path: "/admincoupon",
    name: "AdminCoupon",
    component: () => import("../views/admin/AdminCoupon.vue"),
  },
  {
    path: "/storeMenus",
    name: "StoreMenus",
    component: () => import("../views/store/StoreMenus.vue"),
  },
  {
    path: "/addMenu",
    name: "addMenu",
    component: () => import("../views/store/StoreAddMenu.vue"),
  },
  {
    path: "/manageMember",
    name: "ManageMember",
    component: () => import("../views/admin/ManageMember.vue"),
  },
  {
    path: "/kakaoPaySuccess",
    name: "KakaoPaySuccess",
    component: () => import("../views/cart/KakaoPay.vue"),
  },
  {
    path: "/orderInfo",
    name: "OrderInfo",
    component: () => import("../views/cart/OrderInfo.vue"),
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.afterEach(() => {
  // ${//these hooks do not get a next function and cannot affect the navigation}
  // jwt 디코드후, 롤 확인
  setTimeout(() => {
    //
  }, 0);
  console.log("페이지를 이동합니다");
});

export default router;
