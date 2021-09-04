package com.icia.wapoo.service;

import com.icia.wapoo.dao.AkinatorDao;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RequiredArgsConstructor
@Service
public class AkinatorService {
    @Autowired
    private final AkinatorDao akinatorDao;

    public List<Map<String, Object>> getAkinatorList() {
        List<Map<String, Object>> questionList = akinatorDao.selectAllQuestions();
        List akinatorList = new ArrayList();
        for(Map<String, Object> eachQuestion : questionList) {
            long questionId = (long) eachQuestion.get("questionId");
            List<Map<String, Object>> answerList = akinatorDao.selectAllAnswers(questionId);
            eachQuestion.put("answers", answerList);
            akinatorList.add(eachQuestion);
        }
        return akinatorList;
    }

    @Transactional
    public int addAkinator(Map<String, Object> akinator, int foodId) {
        int result = 0;
        for(int i = 1; i<=akinator.size(); i++){
            result += akinatorDao.insertAkinator(foodId,
                    i,
                    ((Integer) akinator.get(Integer.toString(i))).intValue());
        }
        if(result != akinator.size()) {
            System.out.println("result "+result+" akinator.size() "+akinator.size());
            throw new RuntimeException("addAkinator : 아키네이터 추가가 실패했습니다.");
        }
        return 1;
    }

    public List<Map<String, Integer>> getAkinatorMetaList() {
        return akinatorDao.selectAllAkinator();
    }

    public List<Map<String, Object>> getFoodList(List<Integer> foodIdList, Double LAT, Double LON) {
        List<Map<String, Object>> foodList = akinatorDao.getAllFoodByFoodId(foodIdList, LAT, LON);
        return foodList;
    }
}
