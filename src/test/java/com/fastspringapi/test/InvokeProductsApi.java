package com.fastspringapi.test;
import com.fastspringapi.util.BaseClass;
import com.jayway.restassured.RestAssured;
import com.jayway.restassured.response.Response;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

import static com.fasspringapi.server.ProductApiServerConstants.AUTH_PASSWORD;
import static com.fasspringapi.server.ProductApiServerConstants.AUTH_USERNAME;

public class InvokeProductsApi extends BaseClass{

    private static final Logger LOGGER = LoggerFactory.getLogger(InvokeProductsApi.class);

    @Test
    public void testGetAllProducts() {
        Response responseAllProducts = RestAssured.given()
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME, AUTH_PASSWORD)
                .when()
                .get(server.getHost() + "/products")
                .then()
                .log()
                .ifValidationFails()
                .extract()
                .response();
        LOGGER.info(responseAllProducts.prettyPrint());
    }

    @Test
    public void testPostSingleProduct() throws FileNotFoundException {


        ClassLoader classLoader = getClass().getClassLoader();
        Scanner scanner = new Scanner( new File(classLoader.getResource("createProduct.json").getFile()), "UTF-8" );
        String createRequestBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        Response responseSingleProduct = RestAssured.given()
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME, AUTH_PASSWORD)
                .when()
                .body(createRequestBody)
                .post(server.getHost() + "/products")
                .then()
                .log()
                .ifValidationFails()
                .extract()
                .response();
        LOGGER.info(responseSingleProduct.prettyPrint());

        assertThat("Checking the response code of the request", responseSingleProduct.statusCode(), is(200));

        assertThat("Checking if the response result is success", responseSingleProduct.path("products[0].result").toString(), is("success"));

        assertThat("Checking if the product name is right", responseSingleProduct.path("products[0].product").toString(), is("amruta-20181212"));

    }


    @Test
    public void testErrorPostSingleProduct() throws FileNotFoundException {


        ClassLoader classLoader = getClass().getClassLoader();
        Scanner scanner = new Scanner( new File(classLoader.getResource("createProductError.json").getFile()), "UTF-8" );
        String createRequestBody = scanner.useDelimiter("\\A").next();
        scanner.close();

        Response responseSingleProduct = RestAssured.given()
                .auth()
                .preemptive()
                .basic(AUTH_USERNAME, AUTH_PASSWORD)
                .when()
                .body(createRequestBody)
                .post(server.getHost() + "/products")
                .then()
                .log()
                .ifValidationFails()
                .extract()
                .response();
        LOGGER.info(responseSingleProduct.prettyPrint());

        assertThat("Checking the response code of the request", responseSingleProduct.statusCode(), is(400));

        assertThat("Checking if the response result is success", responseSingleProduct.path("products[0].result").toString(), is("error"));

        assertThat("Checking if the product name is right", responseSingleProduct.path("products[0].product").toString(), is("amruta-20181212"));

    }
}
