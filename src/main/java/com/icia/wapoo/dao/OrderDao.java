package com.icia.wapoo.dao;

import com.icia.wapoo.model.Food;
import com.icia.wapoo.model.GraphDay;
import com.icia.wapoo.model.GraphFood;
import com.icia.wapoo.model.Payment;
import com.icia.wapoo.model.StoreOrder;

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
    
    //주문표
    List<StoreOrder> storeOrder(int storeId, String status);
    
    //가게별 주문 수
    int getTotalOrder(int storeId, String status);
  //주문 음식 1개 삭제
    int deleteOrder (int orderInfoId);
    
    //주문 1건 삭제
    int deleteAllOrder(int orderId);
    
    //전체 주문 승인
    int approveOrder(int orderId);
    
    //가게 음식 판매 순위
    List<GraphFood> getFoodSaleAmount(int storeId, String date);
    
    //요일별 매출
    List<GraphDay> getDayAmount(int storeId, String date);
    
    
}
