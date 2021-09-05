package com.icia.wapoo.controller;

import com.icia.wapoo.service.AkinatorService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/akinator")
@RequiredArgsConstructor
public class AkinatorController {

    @Autowired
    AkinatorService akinatorService;

    @GetMapping("/getAkinators")
    public ResponseEntity getAkinators() {
        List<Map<String, Object>> list = akinatorService.getAkinatorList();

        return new ResponseEntity(list, HttpStatus.OK);
    }

    @GetMapping("/getAkinatorMeta")
    public ResponseEntity getAkinatedFoodMetaInfo() {
        List<Map<String, Integer>> MetaList = akinatorService.getAkinatorMetaList();
        return new ResponseEntity(MetaList, HttpStatus.OK);
    }

    @PostMapping("/getFoods")
    public ResponseEntity getFoodList(@RequestBody Map<String, Object> data) {
        Double LAT = (Double) data.get("LAT");
        Double LON = (Double) data.get("LON");
        System.out.println(LAT);
        List<Integer> foodIdList = (List<Integer>) data.get("filteredIdList");
        List<Map<String, Object>> akinatorFoodList = akinatorService.getFoodList(foodIdList,LAT,LON);
        return new ResponseEntity(akinatorFoodList, HttpStatus.OK);

    }
}
