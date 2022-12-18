package com.reward.reward.service;

import com.reward.reward.configuration.LoyaltyMatrixProperties;
import com.reward.reward.dto.AccumulatePointsRequest;
import com.reward.reward.model.Order;
import com.reward.reward.repository.OrderRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class LoyaltyPointsService {

    @Value("${app.max-date-range}")
    private Integer maxDateRange;

    private final LoyaltyMatrixProperties loyaltyMatrixProperties;
    private final OrderRepository orderRepository;

    public LoyaltyPointsService(LoyaltyMatrixProperties loyaltyMatrixProperties, OrderRepository orderRepository) {
        this.loyaltyMatrixProperties = loyaltyMatrixProperties;
        this.orderRepository = orderRepository;
    }

    public AbstractMap.SimpleImmutableEntry<Integer, Map<Integer, Integer>> getPoints(Long accountId) {
        Integer totalPoints = calculateAll(getOrdersByDatePeriod(0, maxDateRange, accountId));

        Map<Integer, Integer> pointsPerMonth = new HashMap<>();

        for (int i = 0; i < maxDateRange; i++) {
            pointsPerMonth.put(i, calculateAll(getOrdersByDatePeriod(i, i + 1, accountId)));
        }

        return new AbstractMap.SimpleImmutableEntry<>(totalPoints, pointsPerMonth);
    }

    private List<Order> getOrdersByDatePeriod(int startMonth, int endMonth, Long accountId) {
        return new ArrayList<>(orderRepository.findOrdersFromLastDateRangeAndAccount(LocalDate.now().minusMonths(startMonth),
                LocalDate.now().minusMonths(endMonth), accountId));
    }

    public Integer calculate(AccumulatePointsRequest request) {
        Order order = orderRepository.findByOrderId(request.getOrderId());
        return calculateLoyaltyPoints(order.getAmount());
    }

    public Integer calculateAll(List<Order> order) {
        return order.stream()
                .map(m -> calculateLoyaltyPoints(m.getAmount()))
                .mapToInt(Integer::intValue)
                .sum();
    }

    public Integer calculateLoyaltyPoints(BigDecimal orderAmount) {
        int result = 0;

        BigDecimal tempVal = BigDecimal.ZERO;

        for (LoyaltyMatrixProperties.LoyaltyMatrix loyaltyMatrix : loyaltyMatrixProperties.getLoyalty()) {
            if(orderAmount.compareTo(BigDecimal.valueOf(loyaltyMatrix.getMaximum())) >= 0) {
                result += (loyaltyMatrix.getMaximum() - loyaltyMatrix.getMinimum()) * loyaltyMatrix.getAmount();
                tempVal = orderAmount.subtract(BigDecimal.valueOf(loyaltyMatrix.getMaximum()));
            }

            if (orderAmount.compareTo(BigDecimal.valueOf(loyaltyMatrix.getMinimum())) >= 0 &&
                    orderAmount.compareTo(BigDecimal.valueOf(loyaltyMatrix.getMaximum())) <= 0) {
                if(tempVal.equals(BigDecimal.ZERO)) {
                    tempVal = orderAmount.subtract(BigDecimal.valueOf(loyaltyMatrix.getMinimum()));
                }

                result += tempVal.multiply(BigDecimal.valueOf(loyaltyMatrix.getAmount())).intValue();
            }
        }
        return result;
    }
}
