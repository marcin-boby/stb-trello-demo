package organisation;

import base.BaseTest;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.*;

public class OrganisationAlternativeTest extends BaseTest {

    private static Stream<Arguments> createOrganizationData() {
        return Stream.of(
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia qa", "https://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia qa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "aqa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademia_qa", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademiaqa123", "http://akademiaqa.pl"));
    }

    @DisplayName("Create organization with valida data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationData")
    public void createOrganization(String displayName, String desc, String name, String website) {

        Response response = given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(200)
                .extract()
                .response();

        JsonPath json = response.jsonPath();
        assertThat(json.getString("displayName")).isEqualTo(displayName);

        final String organizationId = json.getString("id");

        given()
                .spec(reqSpec)
                .when()
                .delete(BASE_URL + "/" + ORGANIZATION + "/" + organizationId)
                .then()
                .statusCode(200);
    }

    private static Stream<Arguments> createOrganizationWrongData() {
        return Stream.of(
                Arguments.of(""),
                Arguments.of("This is display name", "Akademia QA is awesome!", "ak", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "AQA", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "   ", "http://akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademiaqa123", "akademiaqa.pl"),
                Arguments.of("This is display name", "Akademia QA is awesome!", "akademiaqa123", "htt://akademiaqa.pl"));

    }

    @DisplayName("Create organization with invalida data")
    @ParameterizedTest(name = "Display name: {0}, desc: {1}, name: {2}, website: {3}")
    @MethodSource("createOrganizationWrongData")
    public void createOrganizationWrongData(String displayName, String desc, String name, String website) {

        given()
                .spec(reqSpec)
                .queryParam("displayName", displayName)
                .queryParam("desc", desc)
                .queryParam("name", name)
                .queryParam("website", website)
                .when()
                .post(BASE_URL + "/" + ORGANIZATION)
                .then()
                .statusCode(400)
                .extract()
                .response();
    }
}
