package com.icia.wapoo.dao;

import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.Payment;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
@Mapper
public interface OrderDao {
    int insertOrder(Map<String, Object> orderData);

    int insertOrderInfo(@Param("orderId") Integer orderId,
                        @Param("quantity") Integer quantity,
                        @Param("foodId") Integer foodId);

    int selectPriceByFoodId(@Param("foodId") Integer foodId);

    int insertPayment(Payment payment);

    int updateOrderState(
            @Param("orderId") int order_id,
            @Param("status") String status);

    int updateOrderInfosStatus(@Param("order_id") int order_id,
                          @Param("status") String status);

    Map<String, Object> selectOrderByOrderId(
            @Param("orderId") Integer orderId);

    List<Map<String, Object>> selectAllOrderedFoodByOrderId(@Param("order_id") Integer orderId);

    String selectFoodnameByFoodId(@Param("foodId") int foodId);

    void updateOrderPayment(@Param("order_id") int order_id,
                            @Param("total") Integer total);
}
