package base;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;

import org.hamcrest.Matchers;
import org.testng.annotations.BeforeClass;

public class BaseSetup {

    @BeforeClass
    public void setup(){

        RequestSpecification requestSpecification = new RequestSpecBuilder()
                .setBaseUri("http://restful-booker.herokuapp.com")
                .addHeader("Content-Type","application/json")
                .addHeader("Accept","application/json")
                .addFilter(new RequestLoggingFilter())
                .addFilter(new ResponseLoggingFilter())
                .build();

        ResponseSpecification responseSpecification = new ResponseSpecBuilder()
                .expectResponseTime(Matchers.lessThan(20000L))
                .build();

        RestAssured.requestSpecification = requestSpecification;
        RestAssured.responseSpecification=responseSpecification;
    }
}
