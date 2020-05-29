package organisation;

import base.BaseTest;
import com.github.javafaker.Faker;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;

public class OrganisationTest extends BaseTest {

    private Faker faker;
    private String displayName;
    private String desc;
    private String webSite;

    @BeforeEach
    public void beforeEach(){
        faker = new Faker();
        displayName = faker.name().title();
        desc = faker.lorem().sentence();
        webSite = faker.internet().url();
    }

    @Test
    public void createCorrectEmptyOrganisation() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        String boardId = json.get("id");

        assertThat(json.getString("displayName")).isEqualTo(displayName);

        deleteElement(ORGANIZATION, boardId);
    }

    @Test
    public void createCorrectOrganisationWithAllData() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", "t1_")
                .queryParam("website", webSite)
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        String boardId = json.get("id");

        assertThat(json.getString("displayName")).isEqualTo(displayName);
        assertThat(json.getString("desc")).isEqualTo(desc);
        assertThat(json.getString("name")).contains("t1_");
        assertThat(json.getString("website")).isEqualTo(webSite);

        deleteElement(ORGANIZATION, boardId);
    }

    @Test
    public void createEmptyOrganisation() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", "")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createOrganisationDescWrongType() {

        given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", 1)
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createOrganisationNameToShort() {

        given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("name", "te")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createOrganisationNameUpperCase() {

        given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("name", "TEST")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }

    @Test
    public void createOrganisationNameNotUnique() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("name", "t1_")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(200)
                .extract()
                .response();

        given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("name", "t1_")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        String boardId = json.get("id");

        deleteElement(ORGANIZATION, boardId);
    }

    @Test
    public void createOrganisationWrongUrl() {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("website", "htt://test.com")
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
    }
}
