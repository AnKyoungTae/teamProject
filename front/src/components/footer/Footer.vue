<template>
    <div class="footer">
        <div class="wapoo-wrap">
            <div class="wapoo-logo">
                <div>
                    <a href="#">
                        와푸
                    </a>
                </div>
                <p class="logo-p">뭘 먹어야할지 고민일 때, 배달 주문 플렛폼 서비스 와푸</p>
            </div>
            <div class="wapoo-info1">
                <p style="padding-top: 30px; text-align: left">
                    <strong>인천일보아카데미</strong>
                </p>
                <span>인천 미추홀구 매소홀로488번길 6-32 태승빌딩 5층</span><br />
            </div>
            <div class="wapoo-info">
                <p style="padding-top: 30px;">
                    <strong>CONNECT</strong>
                </p>
                <span>GitHub</span><br />
                <span>Vue.js</span><br />
                <span>Kakao Map</span>
            </div>
            <div class="wapoo-last">
                <p style="padding-top: 30px;">
                    <strong>WAPOO</strong>
                </p>
                <span>공지사항</span>
            </div>
        </div>
    </div>
</template>

<style scoped>
@import url("https://fonts.googleapis.com/css2?family=Black+Han+Sans&display=swap");
    .footer {
        background-color: #355f6e;
        padding-bottom: 65px;
        color: #91afba;
    }
    .wapoo-wrap {
        position: relative;
        max-width: 1200px; 
        margin: 32px auto 0 auto;
    }
    .wapoo-info1 {
        float: left;
        padding-left: 50px;
    }
    .wapoo-info1 p {
        font-size: 24px;
        color: white;
    }
    .wapoo-logo {
        top: 0;
        left: 20px;
        float: left;
        text-align: left;
        width: 250px;
    }
    .wapoo-logo a {
        display: block;
        height: 29px;
        text-decoration: none;
        font-size: 50px;
        font-weight: 600;
        font-family: "Black Han Sans", sans-serif;
    }
    .logo-p {
        margin-top: 50px;
        font-size: 22px;
    }
    .wapoo-info {
        display: block;
        color: #91afba;
        line-height: 1.6;
        padding-left: 80px;
        text-align: right;
        float: left;
    }
    .wapoo-info strong {
        display: inline-block;
        color: white;
        font-size: 20px;
    }
    .wapoo-last {
    }
    .wapoo-last strong {
        color: white;
        font-size: 20px;
    }
</style>