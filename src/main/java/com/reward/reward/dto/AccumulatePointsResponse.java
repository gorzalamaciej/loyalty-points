package com.reward.reward.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AccumulatePointsResponse extends ResponseError {
    private String orderId;
    private Integer points;
    private String accountId;
}
