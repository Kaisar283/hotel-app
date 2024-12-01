package com.andersen.java_intensive_13.hotel_app.integrationtest;

import com.andersen.java_intensive_13.hotel_app.dto.ApartmentDTO;
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

import java.util.Objects;

import static com.andersen.java_intensive_13.hotel_app.utils.PrepatedEntity.preparedApartmentDTOList;
import static io.restassured.RestAssured.baseURI;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ApartmentControllerIntegrationTest {

    @LocalServerPort
    private Integer port;

    @Container
    @ServiceConnection
    static MySQLContainer<?> mySQLContainer = new MySQLContainer<>(
            "mysql:latest"
    );

    RestTemplate restTemplate;

    @Autowired
    public ApartmentControllerIntegrationTest(RestTemplate restTemplate) {
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
        RestAssured.baseURI = "http://localhost:" + port;
    }

    @Test
    void connectionEstablished(){
        assertTrue(mySQLContainer.isCreated());
        assertTrue(mySQLContainer.isRunning());
    }

    @Rollback
    @Test
    public void createNewApartment_shouldCreateNewApartment(){
        ApartmentDTO apartmentDTO = preparedApartmentDTOList().get(0);

        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                baseURI+"/api/v1/apartments",
                HttpMethod.POST,
                new HttpEntity<ApartmentDTO>(apartmentDTO),
                ApartmentDTO.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getBody()).isNotNull();
        assertThat(Objects.requireNonNull(response.getBody()).getId()).isEqualTo(7);
        assertThat(response.getBody().getPrice()).isEqualTo(3000D);
    }

    @Test
    public void getApartmentById_shouldReturnApartment(){
        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                baseURI+"/api/v1/apartments/5",
                HttpMethod.GET,
                null,
                ApartmentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(5);
    }

    @Test
    public void getAllApartments_shouldReturnApartmentDTOs(){
        ApartmentDTO[] apartmentDTOS = restTemplate.getForObject(
                baseURI+"/api/v1/apartments", ApartmentDTO[].class);
        assertThat(apartmentDTOS.length).isEqualTo(6);
    }

    @Test
    public void getAvailableApartments_shouldReturnOnlyAvailableApts(){
        ApartmentDTO[] apartmentDTOS = restTemplate.getForObject(
                baseURI+"/api/v1/apartments/available", ApartmentDTO[].class);
        assertThat(apartmentDTOS.length).isEqualTo(4);
        assertThat(apartmentDTOS[0].getIsReserved()).isEqualTo(false);
    }

    @Rollback
    @Test
    public void deleteApartmentById_shouldReturnOkResponse(){
        ResponseEntity<Void> response = restTemplate.exchange(
                baseURI+"/api/v1/apartments/2",
                HttpMethod.DELETE,
                null,
                Void.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Rollback
    @Test
    public void releaseApartmentById_shouldReleaseApartment(){
        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                baseURI+"/api/v1/apartments/4/release",
                HttpMethod.POST,
                null,
                ApartmentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIsReserved()).isEqualTo(false);
        assertThat(response.getBody().getUserDTO()).isNull();
    }

    @Rollback
    @Test
    public void reserveApartment_shouldReleaseApartment(){
        UserDTO userDTO = UserDTO.builder()
                .id(3L)
                .firstName("Ethan")
                .lastName("Bennett")
                .userRole("USER")
                .build();

        ResponseEntity<ApartmentDTO> response = restTemplate.exchange(
                baseURI+"/api/v1/apartments/3/reserve",
                HttpMethod.POST,
                new HttpEntity<UserDTO>(userDTO),
                ApartmentDTO.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody().getIsReserved()).isEqualTo(true);
        assertThat(response.getBody().getUserDTO()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(3);
    }
}
