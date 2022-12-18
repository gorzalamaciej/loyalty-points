package com.reward.reward.controller;

import com.reward.reward.dto.CalculateLoyaltyPointsResponse;
import com.reward.reward.dto.AccumulatePointsRequest;
import com.reward.reward.dto.AccumulatePointsResponse;
import org.junit.Ignore;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;

import java.util.UUID;

import static org.junit.Assert.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Disabled("Disabled until add mock beans e.g. repository to processed tests from db")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class LoyaltyPointsControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void testGetLoyaltyPoints() {
        ResponseEntity<CalculateLoyaltyPointsResponse> response =
                restTemplate.getForEntity("/loyalty-points", CalculateLoyaltyPointsResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(45, response.getBody().getTotalPoints());
    }

    @Test
    public void testGetLoyaltyPoints_NotFound() {
        assertThrows(RestClientException.class, () -> {
            restTemplate.getForEntity("/loyalty-points", CalculateLoyaltyPointsResponse.class);
        });
    }

    @Test
    public void testGetLoyaltyPoints_BadRequest() {
        assertThrows(RestClientException.class, () -> {
            restTemplate.getForEntity("/loyalty-points/1111/1111", CalculateLoyaltyPointsResponse.class);
        });
    }

    @Test
    public void testGetLoyaltyPoints_Authorized() {
        // Utworzenie zautoryzowanego wywołania za pomocą TestRestTemplate
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth("abc123");
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<CalculateLoyaltyPointsResponse> response =
                restTemplate.exchange("/loyalty-points", HttpMethod.GET, request, CalculateLoyaltyPointsResponse.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(45, response.getBody().getTotalPoints());
    }

    @Test
    public void testGetLoyaltyPoints_Unauthorized() {
        HttpHeaders headers = new HttpHeaders();
        HttpEntity<Void> request = new HttpEntity<>(headers);

        assertThrows(RestClientException.class, () -> {
            restTemplate.exchange("/loyalty-points", HttpMethod.GET, request, CalculateLoyaltyPointsResponse.class);
        });
    }

    @Test
    public void testAddLoyaltyPoints() {
        AccumulatePointsRequest request = new AccumulatePointsRequest("12345-12345-12345", UUID.randomUUID().toString());

        AccumulatePointsResponse response =
                restTemplate.postForObject("/loyalty-points", request, AccumulatePointsResponse.class);

        assertEquals(45, response.getPoints());

        //verify(loyaltyPointsService, times(1)).addLoyaltyPoints("12345", "abcdef");
    }

    @Test
    public void testAddLoyaltyPoints_BadRequest() {

    }

    @Test
    public void testAddLoyaltyPoints_NotFound() {

    }
}
