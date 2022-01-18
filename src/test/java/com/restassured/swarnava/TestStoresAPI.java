package com.restassured.swarnava;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.json.simple.JSONObject;
import org.testng.annotations.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

public class TestStoresAPI {
    String baseUrl =  "https://fakestoreapi.com";

    @Test(priority = 1)
    void testPostUser() {
        JSONObject bodyJson = new JSONObject();
        JSONObject nameJsonObj = new JSONObject();
        JSONObject addressJsonObj = new JSONObject();
        JSONObject geoJsonObj = new JSONObject();
        bodyJson.put("email", "hello@gmail.com");
        bodyJson.put("username", "swarmo");
        bodyJson.put("password", "3A44d@");
        bodyJson.put("phone", "5487958689");
        nameJsonObj.put("firstname", "Swarna");
        nameJsonObj.put("lastname", "Chak");
        addressJsonObj.put("city", "");
        addressJsonObj.put("street", "");
        addressJsonObj.put("number", "");
        addressJsonObj.put("zipcode", "");
        geoJsonObj.put("lat", "-37.3159");
        geoJsonObj.put("long", "81.1496");
        addressJsonObj.put("geolocation", geoJsonObj);
        bodyJson.put("name", nameJsonObj);
        bodyJson.put("address", addressJsonObj);
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl+"/users")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 2)
    void testPostProduct() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("title", "test product");
        bodyJson.put("price", 13.5);
        bodyJson.put("description", "lorem ipsum set");
        bodyJson.put("image", "https://i.pravatar.cc");
        bodyJson.put("category", "electronic");
        given()
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .post(baseUrl+"/products")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 3)
    void testGetProducts(){
        given()
            .when()
                .get(baseUrl+"/products")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 4)
    void testGetSingleProduct(){
        given()
                .pathParam("productId", 1)
            .when()
                .get(baseUrl+"/products/{productId}")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 5)
    void testGetCategory(){
        given()
            .when()
                .get(baseUrl+"/products/categories")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .body("$", hasItems("electronics", "jewelery", "men's clothing", "women's clothing"))
                .log().all();
    }

    @Test(priority = 6)
    void testGetByCategory(){
        String category = "jewelery";
        given()
            .when()
                .pathParam("category", category)
                .get(baseUrl+"/products/category/{category}")
            .then()
                .statusCode(200)
                .body("size()", greaterThan(1))
                .log().all();
    }

    @Test(priority = 7)
    void testGetProductByLimit(){
        int limit = 3;
        given()
            .when()
                .param("limit", limit)
                .get(baseUrl+"/products")
            .then()
                .statusCode(200)
                .body("size()", equalTo(limit))
                .log().all();
    }

    @Test(priority = 8)
    void testPutProduct() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("title", "test product2");
        bodyJson.put("price", 14.6);
        bodyJson.put("description", "lorem ipsum set2");
        bodyJson.put("image", "https://i.pravatar.cc");
        bodyJson.put("category", "electronic");
        given()
                .pathParam("productId", 7)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .put(baseUrl+"/products/{productId}")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 9)
    void testPatchProduct() {
        JSONObject bodyJson = new JSONObject();
        bodyJson.put("title", "test product2");
        bodyJson.put("price", 14.6);
        bodyJson.put("description", "lorem ipsum set2");
        bodyJson.put("image", "https://i.pravatar.cc");
        bodyJson.put("category", "electronic");
        given()
                .pathParam("productId", 7)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
                .body(bodyJson.toJSONString())
            .when()
                .patch(baseUrl+"/products/{productId}")
            .then()
                .statusCode(200)
                .log().all();
    }

    @Test(priority = 10)
    void testDeleteProduct() {
        given()
                .pathParam("productId", 7)
                .header("Content-Type", "application/json")
                .contentType(ContentType.JSON)
                .accept(ContentType.JSON)
            .when()
                .delete(baseUrl+"/products/{productId}")
            .then()
                .statusCode(200)
                .log().all();
    }
}
