export default {
  namespaced: true,
  state: {
    pagingNotices: [],
    pagingQueAn: [],
    pagingFAQ: [],
    //게시물 저장
    notices: [],
    queAn: [],
    FAQ: [],
  },
  getters: {
    getnumNoticeSize(state) {
      //notice 버튼 숫자 리스트
      let list = [];

      for (
        var i = state.pagingNotices.startPage;
        i <= state.pagingNotices.endPage;
        i++
      ) {
        list.push(i);
      }
      return list;
    },
    getNoticePaging(state) {
      //notice 페이지 정보
      return state.pagingNotices;
    },
    getnumSize(state) {
      //Q&A 버튼 숫자 리스트
      let list = [];

      for (
        var i = state.pagingQueAn.startPage;
        i <= state.pagingQueAn.endPage;
        i++
      ) {
        list.push(i);
      }
      return list;
    },
    getQueAnPaging(state) {
      //Q&A 페이지 정보
      return state.pagingQueAn;
    },
    getFAQNumSize(state) {
      //FAQ 버튼 숫자 리스트
      let list = [];

      for (
        var i = state.pagingFAQ.startPage;
        i <= state.pagingFAQ.endPage;
        i++
      ) {
        list.push(i);
      }
      return list;
    },
    getFAQPaging(state) {
      //FAQ 페이지 정보
      return state.pagingFAQ;
    },
    getNoticeTotal(state) {
      return state.notices.length;
    },
    getQueAnTotal(state) {
      return state.queAn.length;
    },
    getFAQTotal(state) {
      return state.FAQ.length;
    },
  },
  mutations: {
    setCenternotices(state, payload) {
      //Notices페이지 저장
      return state.notices.push(payload);
    },
    setCenterQueAn(state, payload) {
      //QueAn 페이지 저장
      return state.queAn.push(payload);
    },
    setCenterFAQ(state, payload) {
      //FAQ 페이지 저장
      return state.FAQ.push(payload);
    },
    setPagingNotices(state, payload) {
      //버튼 Notices페이징 저장 정보
      return (state.pagingNotices = payload);
    },
    setPagingQueAn(state, payload) {
      //버튼 QueAn페징 저장 정보
      return (state.pagingQueAn = payload);
    },
    setPagingFAQ(state, payload) {
      //버튼 FAQ페이징 저장 정보
      return (state.pagingFAQ = payload);
    },
    nullCenterNotices(state) {
      //notices페이지 삭제
      console.log(state.notices);
      return (state.notices = []);
    },
    nullCenterQueAn(state) {
      //QueAn페이지 삭제
      return (state.queAn = []);
    },
    nullCenterFAQ(state) {
      //FAQ페이지 삭제
      return (state.FAQ = []);
    },
    nullPagingpagingQueAn(state) {
      //FAQ페이징 삭제
      return (state.pagingQueAn = []);
    },
    nullPagingNotice(state) {
      //notice페이징 삭제
      console.log(state.pagingNotices);
      return (state.pagingNotices = []);
    },
    nullPagingFAQ(state) {
      //pagingFAQ페이징 삭제
      return (state.pagingFAQ = []);
    },
  },
  actions: {},
};
