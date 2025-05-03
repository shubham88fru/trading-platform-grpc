package com.learning.grpc.aggregatorservice;

import com.learning.grpc.aggregatorservice.mockservice.StockMockService;
import com.learning.grpc.aggregatorservice.mockservice.UserMockService;
import com.learning.user.UserInformation;
import net.devh.boot.grpc.server.service.GrpcService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest(properties = {
        "grpc.server.port=-1",
        "grpc.server.in-process-name=integration-test",
        "grpc.client.user-service.address=in-process:integration-test",
        "grpc.client.stock-service.address=in-process:integration-test"
}, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserTradeTest {

    private static final String USER_INFORMATION_ENDPOINT = "http://localhost:%d/user/%d";

    @LocalServerPort
    private int port;

    @Autowired
    private TestRestTemplate restTemplate;

    @Test
    public void userInformationTest() {
        String url = USER_INFORMATION_ENDPOINT.formatted(port, 1);
        ResponseEntity<UserInformation> response = restTemplate.getForEntity(url, UserInformation.class);
        Assertions.assertEquals(HttpStatus.OK, response.getStatusCode());

        UserInformation userInformation = response.getBody();
        Assertions.assertNotNull(userInformation);
        Assertions.assertEquals(1, userInformation.getUserId());
        Assertions.assertEquals("integration-test", userInformation.getName());
        Assertions.assertEquals(100, userInformation.getBalance());

    }

    @TestConfiguration
    static class TestConfig {

        @GrpcService
        public StockMockService stockMockService() {
            return new StockMockService();
        }

        @GrpcService
        public UserMockService userMockService() {
            return new UserMockService();
        }
    }
}
