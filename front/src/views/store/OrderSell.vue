<template>
    <div style="width: 1200px;">
        <div style="font-size: 40px;">주문관리</div>
        <div style="text-align: left;">
            <span style="font-size: 20px; margin-left: 20px;">총 주문건수:</span>
            <div class="row align-items-center">
                <div class="col-7">
                    <div class="form-check form-check-inline">
                    <input
                        class="form-check-input"
                        type="radio"
                        name="inlineRadioOptions"
                        id="inlineRadio1"
                        value="ALL"
                        checked
                        @click="setStatusOption('ALL')"
                    />
                    <label class="form-check-label" for="inlineRadio1">전체보기</label>
                    </div>
                    <div class="form-check form-check-inline">
                    <input
                        class="form-check-input"
                        type="radio"
                        name="inlineRadioOptions"
                        id="inlineRadio3"
                        @click="setStatusOption('Y')"
                        value="Y"
                    />
                    <label class="form-check-label" for="inlineRadio3">승인</label>
                    </div>
                    <div class="form-check form-check-inline">
                    <input
                        class="form-check-input"
                        type="radio"
                        name="inlineRadioOptions"
                        id="inlineRadio4"
                        @click="setStatusOption('N')"
                        value="N"
                    />
                    <label class="form-check-label" for="inlineRadio4">비승인</label>
                    </div>
                </div>
                </div>
        </div>
        <table style="border-top: 1.5px solid black; border-bottom: 1.5px solid black; width: 100%;">
            <tr style="border-bottom: 1.5px solid black;">
                <th>주문번호</th>
                <th>주문메뉴</th>
                <th>주문일</th>
                <th>음식가격</th>
                <th>할인가격</th>
                <th>총 결제금액</th>
                <th>주소</th>
                <th>핸드폰번호</th>
                <th>요청사항</th>
                <th>주문관리</th>
            </tr>
            <tr>
                <td>1</td>
                <td>햄버거 X 1개</td>
                <td>2021-09-06</td>
                <td>15000원</td>
                <td>5000원</td>
                <td>10000원</td>
                <td>인천 미추홀구 학익동</td>
                <td>010-1111-1111</td>
                <td>주문자의 요청사항</td>
                <!--버튼클릭시 status 변경-->
                <td>
                    <span class="badge bg-success">주문받기</span>
                    <span class="badge bg-danger">주문취소</span>
                </td>
            </tr>
            <tr>
                <td>1</td>
                <td>햄버거 X 1개</td>
                <td>2021-09-06</td>
                <td>15000원</td>
                <td>5000원</td>
                <td>10000원</td>
                <td>인천 미추홀구 학익동</td>
                <td>010-1111-1111</td>
                <td>주문자의 요청사항</td>
                <!--버튼클릭시 status 변경-->
                <td>
                    <span class="badge bg-success">주문받기</span>
                    <span class="badge bg-danger">주문취소</span>
                </td>
            </tr>
        </table>
    </div>
</template>

<style scoped>
th {
    border-left: 1px solid gray;
    background-color: lightyellow;
}
tr {
    border-bottom: 1px solid gray;
}
td {
    border-left: 1px solid gray;
    padding: 3px 10px;
}
.badge {
    cursor: pointer;
}
</style>