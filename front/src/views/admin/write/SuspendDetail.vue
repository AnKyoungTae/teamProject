<template>
<table style="width:1000px; font-size:25px;">
    <tr style="border-bottom:1px solid black;">
        <th>닉네임</th>
        <td>{{ data[0].nickname }}</td>
    </tr>
    <tr style="border-bottom:1px solid black;">
        <th>작성 날짜</th>
        <td>
            {{ 
                data[0].regDate[0] + data[0].regDate[1] + data[0].regDate[2] + data[0].regDate[3] + "년 " + 
                data[0].regDate[5] + data[0].regDate[6] + "월 " +
                data[0].regDate[8] + data[0].regDate[9] + "일 " +
                data[0].regDate[11] + data[0].regDate[12] + "시 " +
                data[0].regDate[14] + data[0].regDate[15] + "분 " +
                data[0].regDate[17] + data[0].regDate[18] + "초"
            }}
        </td>
    </tr>
    <tr v-if="data[0].title != null" style="border-bottom:1px solid black;">
        <th>제목</th>
        <td>{{ data[0].title }}</td>
    </tr>
    <tr style="border-bottom:1px solid black;">
        <th>작성 내용</th>
        <td>{{ data[0].body }}</td>
    </tr>
    <tr>
        <th>신고 내용 [{{ data[0].suspend.length }}개]</th>
        <td>{{ data[0].suspend }}</td>
    </tr>
</table>
</template>

<script>

export default {
    props: ["data"],
    data() {
        return {
        
        };
    },
}
</script>

<style scoped>
td, th {
    padding:15px;
}
th {
    width:250px;
}
</style>