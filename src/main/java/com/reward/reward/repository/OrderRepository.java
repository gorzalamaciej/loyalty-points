package com.reward.reward.repository;

import com.reward.reward.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;


public interface OrderRepository extends JpaRepository<Order, Long> {

    Order findByOrderId(String orderId);

    @Query("select o from Order o where o.orderDate BETWEEN :startDate AND :endDate and o.account.id = :accountId")
    List<Order> findOrdersFromLastDateRangeAndAccount(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate,
                                                      @Param("accountId") Long accountId);

}
