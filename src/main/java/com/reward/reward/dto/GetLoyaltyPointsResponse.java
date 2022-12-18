package com.reward.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Map;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class GetLoyaltyPointsResponse extends ResponseError {
    private Map<Integer, Integer> pointsInCurrentMonth;
    private Integer totalPoints;
}
