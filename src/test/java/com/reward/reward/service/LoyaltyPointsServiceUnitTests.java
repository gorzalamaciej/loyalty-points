package com.reward.reward.service;

import com.reward.reward.configuration.LoyaltyMatrixProperties;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.List;


@SpringBootTest
public class LoyaltyPointsServiceUnitTests {

    @TestConfiguration
    static class LoyaltyPointsServiceUnitTestsContextConfiguration {

        @Bean
        public LoyaltyPointsService calculatorService(LoyaltyMatrixProperties loyaltyMatrixPropertiesMock) {
            return new LoyaltyPointsService(loyaltyMatrixPropertiesMock, null);
        }

        @Bean
        public LoyaltyMatrixProperties loyaltyMatrixPropertiesMock() {
            return new LoyaltyMatrixProperties(List.of(
                    new LoyaltyMatrixProperties.LoyaltyMatrix("Above 50, under 100, then give 1 point", 50, 100, 1),
                    new LoyaltyMatrixProperties.LoyaltyMatrix("Above 100, then give 2 point", 100, 200, 2),
                    new LoyaltyMatrixProperties.LoyaltyMatrix("Above 200, then give 4 point", 200, 100000, 4)
            ));
        }
    }

    @Autowired
    private LoyaltyPointsService calculatorService;

    @Test
    void testCalculateLoyaltyPoints_whenOrderAmountIsLessThanMinimum_shouldReturnZero() {
        BigDecimal orderAmount = BigDecimal.valueOf(49.99);
        int result = calculatorService.calculateLoyaltyPoints(orderAmount);
        Assertions.assertEquals(0, result);
    }

    @Test
    void testCalculateLoyaltyPoints_whenOrderAmountEqualsMinimum_shouldReturnZero() {
        BigDecimal orderAmount = BigDecimal.valueOf(50);
        int result = calculatorService.calculateLoyaltyPoints(orderAmount);
        Assertions.assertEquals(0, result);
    }

    @Test
    void testCalculateLoyaltyPoints_orderAmountGreaterThan50_returnsCorrectResult() {
        BigDecimal orderAmount = BigDecimal.valueOf(60);
        int result = calculatorService.calculateLoyaltyPoints(orderAmount);
        Assertions.assertEquals(10, result);
    }

    @Test
    void testCalculateLoyaltyPoints_orderAmountGreaterThan100_returnsCorrectResult() {
        BigDecimal orderAmount = BigDecimal.valueOf(120);
        int result = calculatorService.calculateLoyaltyPoints(orderAmount);
        Assertions.assertEquals(90, result);
    }
}
