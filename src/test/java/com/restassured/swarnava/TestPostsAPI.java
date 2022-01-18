package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.baseURI;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

public class TestPostsAPI {
    String baseUrl = "https://gorest.co.in";
    String accessToken = "2ec448c579abb9447efee6a70e4092306cf9ccba9571619bf8dab06ea20ac854";
    int userId;
    int postId;
    String userName;
    String userEmail;

    @Test(priority = 1)
    void testCreateUser() {
        String randomMail = "swarnava" + Math.random() + "@gmail.com";
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Swarnava Chak");
        bodyJson.put("email", randomMail);
        bodyJson.put("gender", "male");
        bodyJson.put("status", "active");
        Response response = given()
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl + "/public/v1/users")
            .then()
                .statusCode(201)
                .log().all()
                .extract().response();
        userId = response.jsonPath().get("data.id");
        userName = response.jsonPath().get("data.name");
        userEmail = response.jsonPath().get("data.email");
    }

    @Test(priority = 2)
    void testCreateUserPost() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("user", "Dhriti Majumder");
        bodyJson.put("title", "Tester");
        bodyJson.put("body", "Hello World");
        Response response = given()
                .pathParam("userId", userId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl + "/public/v1/users/{userId}/posts")
            .then()
                .statusCode(201)
                .log().all()
                .extract().response();
        postId = response.jsonPath().get("data.id");
    }

    @Test(priority = 3)
    void testCreateComment() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Sigmund434");
        bodyJson.put("email", "Ncannie80@hotmail.com");
        bodyJson.put("body", "I was wondering if after all these years you'd like to meet");
        given()
                .pathParam("postId", postId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl + "/public/v1/posts/{postId}/comments")
            .then()
                .statusCode(201)
                .log().all();
    }

    @Test(priority = 4)
    void testCreatePostTodo() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("title", "Tester");
        bodyJson.put("due_on", "2022-05-06");
        bodyJson.put("status", "completed");
        given()
                .pathParam("userId", userId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl + "/public/v1/users/{userId}/todos")
            .then()
                .statusCode(201)
                .log().all();
    }

    @Test(priority = 5)
    void testGetUsers() {
        given()
            .when()
                .get(baseUrl + "/public/v1/users")
            .then()
                .statusCode(200)
                .body("data.size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 6)
    void testGetPosts() {
        given()
            .when()
                .get(baseUrl + "/public/v1/posts")
            .then()
                .statusCode(200)
                .body("data.size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 7)
    void testGetComments() {
        given()
            .when()
                .get(baseUrl + "/public/v1/comments")
            .then()
                .statusCode(200)
                .body("data.size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 8)
    void testGetTodos() {
        given()
            .when()
                .get(baseUrl + "/public/v1/todos")
            .then()
                .statusCode(200)
                .body("data.size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 9)
    void testGetSingleUser() {
        given()
                .pathParam("userId", userId)
            .when()
                .get(baseUrl + "/public/v1/users/{userId}")
            .then()
                .statusCode(200)
                .body("meta", equalTo(null))
                .log().all();
    }

    @Test(priority = 10)
    void testGetPostsByUserId() {
        given()
                .pathParam("userId", userId)
            .when()
                .get(baseUrl + "/public/v1/users/{userId}/posts")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 11)
    void testGetCommentsByPostId() {
        given()
                .pathParam("postId", postId)
            .when()
                .get(baseUrl + "/public/v1/posts/{postId}/comments")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 12)
    void testGetTodosByUserId() {
        given()
                .pathParam("userId", userId)
            .when()
                .get(baseUrl + "/public/v1/users/{userId}/todos")
            .then()
                .statusCode(200)
                .log().all();
    }


    @Test(priority = 13)
    void testPutUser() {
        String randomMail = "swarnava" + Math.random() + "@gmail.com";
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("name", "Swarnava Chak");
        bodyJson.put("email", randomMail);
        given()
                .pathParam("userId", userId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .put(baseUrl + "/public/v1/users/{userId}")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 14)
    void testDeleteUser() {
        given()
                .pathParam("userId", userId)
                .header("Authorization", "Bearer " + accessToken)
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .put(baseUrl + "/public/v1/users/{userId}")
            .then()
                .statusCode(200)
                .log().all();
    }
}
