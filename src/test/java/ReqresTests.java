import org.junit.jupiter.api.Test;

import static io.restassured.module.jsv.JsonSchemaValidator.matchesJsonSchemaInClasspath;
import static org.hamcrest.Matchers.is;
import static io.restassured.RestAssured.given;
import static io.restassured.http.ContentType.JSON;

public class ReqresTests extends TestBase {
    @Test
    void updateUserTest() {
        String requestBody = "{\n" +
                "    \"name\": \"morpheus\",\n" +
                "    \"job\": \"zion resident\"\n" +
                "}";
        String userName = "morpheus";
        String userJob = "zion resident";

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .put("/user/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("name", is(userName))
                .body("job ", is(userJob));
    }

    @Test
    void deleteUserTest() {
        given()
                .log().uri()
                .when()
                .delete("/user/2")
                .then()
                .log().status()
                .statusCode(204);
    }

    @Test
    void unsuccessfulRegisterTest() {
        String requestBody = "{\n" +
                "    \"email\": \"sydney@fife\"\n" +
                "}";

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(400)
                .body("error", is("Missing password"));
    }

    @Test
    void successfulRegisterTest() {
        String requestBody = "{ \"email\": \"eve.holt@reqres.in\", \"password\": \"pistol\" }";

        given()
                .log().uri()
                .log().body()
                .contentType(JSON)
                .body(requestBody)
                .when()
                .post("/register")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body("id", is(4))
                .body("token", is("QpwL5tke4Pnpja7X4"));
    }

    @Test
    void singleUserJsonSchemaTest() {
        given()
                .log().uri()
                .when()
                .get("/users/2")
                .then()
                .log().status()
                .log().body()
                .statusCode(200)
                .body(matchesJsonSchemaInClasspath("schema/status-response-scheme.json"));
    }
}