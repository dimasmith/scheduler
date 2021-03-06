package scheduler.rest.common;

import com.jayway.restassured.RestAssured;
import com.jayway.restassured.http.ContentType;
import com.jayway.restassured.parsing.Parser;
import com.jayway.restassured.response.Response;
import org.apache.commons.io.IOUtils;
import org.apache.http.HttpStatus;
import scheduler.rest.common.routes.AuthRoutes;
import scheduler.rest.common.routes.Route;
import scheduler.rest.common.routes.UserRoutes;

import java.io.IOException;

import static com.jayway.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.core.IsNot.not;
import static scheduler.rest.common.routes.Route.buildRoute;


public class RestTestHelper {

    public static String readJson(final String resourcePath) {
        try {
            return IOUtils.toString(RestTestHelper.class.getResource(resourcePath));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static UserData generateAndLoginUser(){
        UserData userData = DataGenerator.generateUserData();
        register(userData.getLogin(), userData.getPassword(), userData.getName());
        login(userData.getLogin(), userData.getPassword());
        return userData;
    }

    public static Response register(final String login, final String password, final String userName) {
        RestAssured.reset();
        SecurityCookieFilter securityCookieFilter = new SecurityCookieFilter();
        RestAssured.filters(securityCookieFilter);

        String requestBody = String.format(readJson(ResourcePath.USER_REGISTRATION_DATA_JSON),
                login,
                userName,
                password,
                password
        );

        return doJsonPut(requestBody, UserRoutes.USER_REGISTRATION, HttpStatus.SC_OK);
    }

    public static Response login(final String login, final String password) {
        return login(login, password, HttpStatus.SC_OK);
    }

    public static Response login(final String login, final String password, final int expectedStatusCode) {
        RestAssured.defaultParser = Parser.JSON;
        return given()
                .log()
                .ifValidationFails()
                .contentType(ContentType.JSON)
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .post(String.format("%s?login=%s&password=%s", buildRoute(AuthRoutes.LOGIN), login, password));
    }

    public static Response logout() {
        return doGet(AuthRoutes.LOGOUT);
    }

    public static Response doGet(final Route route) {
        return doGet(route, HttpStatus.SC_OK);
    }

    public static Response doGet(final Route route, final int expectedStatusCode) {
        return given()
//                .log().ifValidationFails()
                .when()
                .response().then().statusCode(expectedStatusCode)
                .log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .get(buildRoute(route));
    }

    public static Response doJsonPost(final String jsonBody, final Route route, final int expectedStatusCode) {
        RestAssured.defaultParser = Parser.JSON;
        return given()
                .log()
                .ifValidationFails()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .post(buildRoute(route));
    }

    public static Response doJsonPut(final String jsonBody, final Route route, final int expectedStatusCode) {
        RestAssured.defaultParser = Parser.JSON;
        return given()
                .log()
                .ifValidationFails()
                .contentType(ContentType.JSON)
                .body(jsonBody)
                .when()
                .response().log().ifStatusCodeMatches(not(equalTo(expectedStatusCode)))
                .then().statusCode(expectedStatusCode)
                .put(buildRoute(route));
    }
}
