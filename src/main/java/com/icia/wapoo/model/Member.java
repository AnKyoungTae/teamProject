package com.icia.wapoo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
public class Member{
    private int memberId;
    private String status;
    private String name;
    private String phone;
    private String email;
    private String nickname;
    private String role;
    private String profileUrl;
}
