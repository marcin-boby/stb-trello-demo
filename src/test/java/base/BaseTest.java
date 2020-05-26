package base;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.junit.jupiter.api.BeforeAll;

import static io.restassured.RestAssured.given;

public class BaseTest {

    protected static final String BASE_URL = "https://api.trello.com/1";
    protected static final String BOARDS = "boards";
    protected static final String LISTS = "lists";
    protected static final String CARDS = "cards";
    protected static final String ORGANIZATION = "organizations";

    private static final String KEY = "26d1b136a1a3cbb8dd38faa9ce2f35bd";
    private static final String TOKEN = "e40616a88790770757b424d0db6d901a30929efa0f7dc5b2579a3c8f0b8839eb";

    protected static RequestSpecBuilder reqBuilder;
    protected static RequestSpecification reqSpec;

    @BeforeAll
    public static void beforeAll() {
        reqBuilder = new RequestSpecBuilder();
        reqBuilder.addQueryParam("key", KEY);
        reqBuilder.addQueryParam("token", TOKEN);
        reqBuilder.setContentType(ContentType.JSON);

        reqSpec = reqBuilder.build();
    }

    protected static void deleteElement(String element, String elementId){
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + element + "/" + elementId)
                .then()
                .statusCode(200);
    }


}
