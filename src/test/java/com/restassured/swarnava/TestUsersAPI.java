package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TestUsersAPI {
    int userId;

    @Test(priority = 1)
    void testPostUser() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Guddu Ostad");
        bodyJson.put("job", "Tester");
        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .post("https://reqres.in/api/users/")
                .then()
                .statusCode(201)
                .log().all()
                .extract().response();
        userId = Integer.parseInt(response.jsonPath().get("id"));
    }

    @Test(priority = 2)
    void testPostUserRegister() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("email", "eve.holt@reqres.in");
        bodyJson.put("password", "Pistol");
        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
    }

    @Test(priority = 3)
    void testPostUserLogin() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("email", "eve.holt@reqres.in");
        bodyJson.put("password", "Pistol");
        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .post("https://reqres.in/api/login")
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
    }

    @Test(priority = 4)
    void testPutUser() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Dhritimoy Majumder");
        bodyJson.put("job", "Frontend Developer");
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .put("https://reqres.in/api/users/" + userId)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 5)
    void testPatchUser() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Dhritimoy Majumder");
        bodyJson.put("job", "Backend Developer");
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .patch("https://reqres.in/api/users/" + userId)
                .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 6)
    void testDeleteUser() {
        //int id = 130;
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Dhritimoy Majumder");
        bodyJson.put("job", "Developer");

        when()
                .delete("https://reqres.in/api/users/" + userId)
                .then()
                .statusCode(204)
                .log().all();
    }

    @Test(priority = 7)
    void testGetUsers() {
        given()
                .param("page", "2")
                .when()
                .get("https://reqres.in/api/users").then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 8)
    void testGetSingleUser() {
        int id = 2;
        given()
                .param("page", "2")
                .when()
                .get("https://reqres.in/api/users/" + id).then()
                .statusCode(200)
                .body("data.id", equalTo(id))
                .log().all();
    }

    @Test(priority = 9)
    void testGetDelayResponse() {
        int actualSec = 3;
        int expectedMsMinimum = actualSec * 1000;
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Guddu Ostad");
        bodyJson.put("job", "Tester");
        Response response = given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
                .when()
                .get("https://reqres.in/api/users?delay=" + actualSec)
                .then()
                .statusCode(200)
                .log().all()
                .extract().response();
        Assert.assertTrue((response.getTime() > expectedMsMinimum));
    }
}
