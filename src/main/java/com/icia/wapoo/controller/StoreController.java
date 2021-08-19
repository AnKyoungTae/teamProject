package com.icia.wapoo.controller;

import com.icia.wapoo.S3.S3Service;
import com.icia.wapoo.jwt.service.JwtService;
import com.icia.wapoo.model.Store;
import com.icia.wapoo.service.StoreService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.ILoggerFactory;
import org.springframework.http.HttpRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/store")
@Slf4j
public class StoreController {

    private final JwtService jwtService;

    private final StoreService storeService;


    @PostMapping("/addstore")
    public ResponseEntity addStore(Store store
            , @RequestPart(value = "fileList", required = false) List<MultipartFile> files, HttpServletRequest request){

        String token = jwtService.resolveToken(request);
        System.out.println(token);
        Map<String, Object> claims = jwtService.getUserInfo(token);
        System.out.println("클레임있나여" +claims);
        store.setOwner_id(((Integer) claims.get("memberId")).intValue());
        int result = storeService.registerStore(store, files);
        System.out.println("결과값은?" + result);
        return new ResponseEntity("OK", HttpStatus.OK);
    }
}
