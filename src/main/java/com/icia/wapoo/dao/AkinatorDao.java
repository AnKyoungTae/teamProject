package com.icia.wapoo.dao;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface AkinatorDao {
    List selectAllQuestions();

    List selectAllAnswers(@Param("questionId") long questionId);

    int insertAkinator(@Param("food_id") int food_id,
                       @Param("question_id") int question_id,
                       @Param("answerValue") int answerValue);

    List<Map<String, Object>> SelectFoodIdListByAnswers(@Param("answers") List<Map<String, Object>> answers);
}
