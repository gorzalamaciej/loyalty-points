package com.reward.reward.configuration;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@Component
@ConfigurationProperties("app")
public class LoyaltyMatrixProperties {
    private List<LoyaltyMatrix> loyalty = new ArrayList<>();

    public LoyaltyMatrixProperties() {
    }

    public LoyaltyMatrixProperties(List<LoyaltyMatrix> loyalty) {
        this.loyalty = loyalty;
    }

    public static class LoyaltyMatrix {
        private String description;
        private Integer minimum;
        private Integer maximum;
        private Integer amount;

        public LoyaltyMatrix() {
        }

        public LoyaltyMatrix(String description, Integer minimum, Integer maximum, Integer amount) {
            this.description = description;
            this.minimum = minimum;
            this.maximum = maximum;
            this.amount = amount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public Integer getMinimum() {
            return minimum;
        }

        public void setMinimum(Integer minimum) {
            this.minimum = minimum;
        }

        public Integer getMaximum() {
            return maximum;
        }

        public void setMaximum(Integer maximum) {
            this.maximum = maximum;
        }

        public Integer getAmount() {
            return amount;
        }

        public void setAmount(Integer amount) {
            this.amount = amount;
        }

        @Override
        public String toString() {
            return "LoyaltyMatrix{" +
                    "description='" + description + '\'' +
                    ", minimum=" + minimum +
                    ", maximum=" + maximum +
                    ", amount=" + amount +
                    '}';
        }
    }
}
