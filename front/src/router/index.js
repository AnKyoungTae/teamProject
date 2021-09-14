import { createRouter, createWebHistory } from "vue-router";
import store from "../store";
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
    meta: ["SELLER", "ADMIN"],
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
        meta: ["SELLER", "BUYER", "ADMIN"],
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
    component: () => import("../views/myPage/MyPage.vue"),
    meta: ["SELLER", "BUYER", "ADMIN"],
  },
  {
    path: "/storeregister",
    name: "StoreRegister",
    component: () => import("../views/store/StoreRegister.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/store",
    name: "StoreHome",
    component: () => import("../views/store/StoreHome.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/storeOrder",
    name: "StoreOrder",
    component: () => import("../views/store/StoreOrder.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/storeGraph",
    name: "StoreGraph",
    component: () => import("../views/store/StoreGraph.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/manageStore",
    name: "ManageStore",
    component: () => import("../views/admin/ManageStore.vue"),
    meta: ["ADMIN"],
  },
  {
    path: "/admincoupon",
    name: "AdminCoupon",
    component: () => import("../views/admin/AdminCoupon.vue"),
    meta: ["ADMIN"],
  },
  {
    path: "/adminService",
    name: "AdminService",
    component: () => import("../views/admin/AdminService.vue"),
  },
  {
    path: "/storeMenus",
    name: "StoreMenus",
    component: () => import("../views/store/StoreMenus.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/addMenu",
    name: "addMenu",
    component: () => import("../views/store/StoreAddMenu.vue"),
    meta: ["SELLER"],
  },
  {
    path: "/manageMember",
    name: "ManageMember",
    component: () => import("../views/admin/ManageMember.vue"),
    meta: ["ADMIN"],
  },
  {
    path: "/kakaoPaySuccess",
    name: "KakaoPaySuccess",
    component: () => import("../views/kakaopay/KakaoPaySuccess.vue"),
  },
  {
    path: "/kakaoPayCancel",
    name: "KakaoPayCancel",
    component: () => import("../views/kakaopay/KakaoPayCancel.vue"),
  },
  {
    path: "/kakaoPaySuccessFail",
    name: "KakaoPaySuccessFail",
    component: () => import("../views/kakaopay/KakaoPaySuccessFail.vue"),
  },
  {
    path: "/orderInfo",
    name: "OrderInfo",
    component: () => import("../views/cart/OrderInfo.vue"),
  },
  {
    path: "/password",
    name: "Password",
    component: () => import("../views/Email/Password.vue"),
  },
  {
    path: "/akinator",
    name: "Arkinator",
    component: () => import("../views/akinator/Arkinator.vue"),
  },
  {
    path: "/ordersell",
    name: "OrderSell",
    component: () => import("../views/store/OrderSell.vue"),
    meta: ["SELLER"],
  },
];

const router = createRouter({
  history: createWebHistory(),
  routes,
});

router.beforeEach((to, from, next) => {
  // ${//these hooks do not get a next function and cannot affect the navigation}

  // console.log("페이지를 이동합니다");
  let role = store.getters["auth/getUserRole"];
  let authList = Object.values(to.meta);
  if (authList.length > 0) {
    //권한이 필요한 페이지 접근시
    let authenticated = authList.some((auth) => {
      return auth == role;
    });
    if (authenticated) {
      next();
    } else {
      alert("권한이 없습니다!");
    }
  } else {
    next();
  }
});

export default router;
