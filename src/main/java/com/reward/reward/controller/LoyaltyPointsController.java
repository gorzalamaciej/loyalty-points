package com.reward.reward.controller;

import com.reward.reward.dto.AccumulatePointsRequest;
import com.reward.reward.dto.CalculateLoyaltyPointsResponse;
import com.reward.reward.dto.ErrorCode;
import com.reward.reward.dto.GetLoyaltyPointsResponse;
import com.reward.reward.service.LoyaltyPointsService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@AllArgsConstructor
@RestController
@RequestMapping("/loyalty-points")
public class LoyaltyPointsController {

    private LoyaltyPointsService loyaltyPointsService;

    @GetMapping
    public ResponseEntity<GetLoyaltyPointsResponse> getLoyaltyPoints(
            @PathVariable("accountId") Long accountId) {
        GetLoyaltyPointsResponse response = new GetLoyaltyPointsResponse();
        if (accountId == null) {
            response.setCode(ErrorCode.EMPTY_DATA);
            response.setMessage("Empty body");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setTotalPoints(loyaltyPointsService.getPoints(accountId).getKey());
        response.setPointsInCurrentMonth(loyaltyPointsService.getPoints(accountId).getValue());

        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<CalculateLoyaltyPointsResponse> calculateLoyaltyPoints(
            @RequestBody @Valid AccumulatePointsRequest request) {
        CalculateLoyaltyPointsResponse response = new CalculateLoyaltyPointsResponse();
        if (request == null) {
            response.setCode(ErrorCode.EMPTY_DATA);
            response.setMessage("Empty body");
            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
        response.setTotalPoints(loyaltyPointsService.calculate(request));
        return new ResponseEntity<>(response, new HttpHeaders(), HttpStatus.OK);
    }

}
