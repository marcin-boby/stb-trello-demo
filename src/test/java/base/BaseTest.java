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

    private static final String KEY = "xxx";
    private static final String TOKEN = "xxx";

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

    protected static void deleteElement(final String element, String elementId){
        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + element + "/" + elementId)
                .then()
                .statusCode(200);
    }


}
