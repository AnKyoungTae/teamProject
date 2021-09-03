package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Review {
    private int reviewId;
    private String regDate;
    private String updateDate;
    private String status;
    private int score;
    private String body;
    private int writerid;
    private int storeId;
    private String foodPhoto;
    private String reply;
}

