package com.andersen.java_intensive_13.hotel_app.integrationtest;

import com.andersen.java_intensive_13.hotel_app.dto.UserDTO;
import io.restassured.RestAssured;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.Rollback;
import org.springframework.web.client.RestTemplate;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.shaded.org.bouncycastle.asn1.ocsp.ResponderID;

import java.util.Objects;

import static io.restassured.RestAssured.baseURI;
import static org.assertj.core.api.Assertions.assertThat;


@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class UserControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(
            "mysql:latest"
    );

    RestTemplate restTemplate;

    @Autowired
    public UserControllerIntegrationTest(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @BeforeAll
    static void beforeAll(){
        mySQLContainer.start();
    }

    @AfterAll
    static void afterAll(){
        mySQLContainer.stop();
    }

    @BeforeEach
    void setUp(){
        baseURI = "http://localhost:" + port;
    }

    @Rollback
    @Test
    public void createNewUser_shouldReturnNewUser(){
        UserDTO userDTO = UserDTO.builder()
                .firstName("Noah")
                .lastName("Sullivan")
                .userRole("USER")
                .build();

        ResponseEntity<UserDTO> response = restTemplate.exchange(
                baseURI + "/api/v1/users",
                HttpMethod.POST,
                new HttpEntity<UserDTO>(userDTO),
                UserDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody().getId())).isEqualTo(5);
        assertThat(Objects.requireNonNull(response.getBody().getFirstName())).isEqualTo("Noah");
        assertThat(Objects.requireNonNull(response.getBody().getLastName())).isEqualTo("Sullivan");
        assertThat(Objects.requireNonNull(response.getBody().getUserRole())).isEqualTo("USER");
    }

    @Test
    public void getAllUsers_shouldReturnUsers(){
        UserDTO[] userDTOS = restTemplate.getForObject(
                baseURI + "/api/v1/users",
                UserDTO[].class);
        assertThat(userDTOS.length).isEqualTo(4);
    }

    @Test
    public void getUserById_shouldReturnUserById(){
        ResponseEntity<UserDTO> response = restTemplate.exchange(
                baseURI + "/api/v1/users/3",
                HttpMethod.GET,
                null,
                UserDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getId()).isEqualTo(3);
        assertThat(response.getBody()).isNotNull();
    }

    @Rollback
    @Test
    public void deleteUserById_shouldDeleteUserById(){
        ResponseEntity<Void> response = restTemplate.exchange(
                baseURI + "/api/v1/users/3",
                HttpMethod.DELETE,
                null,
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }
}
