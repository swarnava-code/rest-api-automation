package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TESTUsersAPI {
    int userId;

    @Test(priority = 1)
    void testPostUser() {
        JSONObject request = new JSONObject();
        request.put("name", "Guddu Ostad");
        request.put("job", "Tester");
        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request.toJSONString())
                .when()
                .post("https://reqres.in/api/users/")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();
        userId = Integer.parseInt(response.jsonPath().get("id"));
    }

    @Test(priority = 2)
    void testPutUser() {
        JSONObject request = new JSONObject();
        request.put("name", "Dhritimoy Majumder");
        request.put("job", "Frontend Developer");
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request.toJSONString())
                .when()
                .put("https://reqres.in/api/users/"+userId)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 3)
    void testPatchUser() {
        //int id = 130;
        JSONObject request = new JSONObject();
        request.put("name", "Dhritimoy Majumder");
        request.put("job", "Backend Developer");
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(request.toJSONString())
                .when()
                .patch("https://reqres.in/api/users/"+userId)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 4)
    void testDeleteUser() {
        //int id = 130;
        JSONObject request = new JSONObject();
        request.put("name", "Dhritimoy Majumder");
        request.put("job", "Developer");

        when()
                .delete("https://reqres.in/api/users/"+userId)
                .then()
                .statusCode(204)
                .log().all();
    }

    @Test(priority = 5)
    void testGetUsers() {
        given()
                .param("page", "2")
                .when()
                .get("https://reqres.in/api/users").then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 6)
    void testGetSingleUser() {
        int id = 2;
        given()
                .param("page", "2")
                .when()
                .get("https://reqres.in/api/users/"+id).then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .log().all();
    }
}
