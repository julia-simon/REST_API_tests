package tests;


import models.lombok.*;
import org.junit.jupiter.api.Test;

import static io.qameta.allure.Allure.step;
import static io.restassured.RestAssured.given;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static specs.Specs.*;

public class ReqresTests extends TestBase {
    String userName = "morpheus";
    String userJob = "leader";
    String userEmail = "eve.holt@reqres.in";
    String userPassword = "pistol";
    String errorMessage = "Missing password";
    String userToken = "QpwL5tke4Pnpja7X4";
    Integer userId = 4;

    @Test
    void updateUserTest() {
        UserUpdate requestBody = new UserUpdate();
        requestBody.setName(userName);
        requestBody.setJob(userJob);

        UserUpdateResponse response = step("Make request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .put("/user/2")
                        .then()
                        .spec(userUpdateResponseSpec)
                        .extract().as(UserUpdateResponse.class));
        step("Check name in response", () ->
                assertEquals(userName, response.getName()));

        step("Check job in response", () ->
                assertEquals(userJob, response.getJob()));
    }

    @Test
    void deleteUserTest() {
        step("Make request and check response", () ->
                given()
                        .spec(requestSpec)
                        .when()
                        .delete("/user/2")
                        .then()
                        .spec(response204Spec));
    }

    @Test
    void unsuccessfulRegisterTest() {
        UserRegistration requestBody = new UserRegistration();
        requestBody.setEmail(userEmail);

        ErrorResponse response = step("Make request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(errorResponseSpec)
                        .extract().as(ErrorResponse.class));

        step("Check response", () ->
                assertEquals(errorMessage, response.getError()));
    }

    @Test
    void successfulRegisterTest() {
        UserRegistration requestBody = new UserRegistration();
        requestBody.setEmail(userEmail);
        requestBody.setPassword(userPassword);

        UserRegistrationResponse response = step("Make request", () ->
                given()
                        .spec(requestSpec)
                        .body(requestBody)
                        .when()
                        .post("/register")
                        .then()
                        .spec(userRegistrationResponseSpec)
                        .extract().as(UserRegistrationResponse.class));
        step("Check response", () ->
        {
            assertEquals(userToken, response.getToken());
            assertEquals(userId, response.getId());
        });
    }

    @Test
    void singleUserTest() {
        Integer userId = 2;
        String name = "fuchsia rose";

        SingleUserResponse response = step("Make request", () ->
                given()
                        .spec(requestSpec)
                        .when()
                        .get("/user/2")
                        .then()
                        .spec(userIdCheckResponseSpec)
                        .extract().as(SingleUserResponse.class));

        step("Check response", () ->
        {
            assertEquals(userId, response.getData().getId());
            assertEquals(name, response.getData().getName());
        });
    }
}




