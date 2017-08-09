package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.junit4.SpringRunner;
import java.io.IOException;
import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
public class KeepersAcceptanceTest extends BaseAcceptanceTest{

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperException() throws IOException {

        //Given
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        //When
        Response actualResponse = getRealResponse("/v1/keepers", jsonContentRequest, HttpMethod.POST);

        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperAlreadyKeepDirectionIsAliveException() throws IOException {

        //Given
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeperOther.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperAlreadyKeepsDirectionIsAlive.json"));

        //When
        Response actualResponse = getRealResponse("/v1/keepers", jsonContentRequest, HttpMethod.POST);

        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperOK() throws IOException {

        //Given
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        //When
        Response actualResponse = getRealResponse("/v1/keepers", jsonContentRequest, HttpMethod.POST);

        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isNotEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void getDirections() throws IOException {
        //Given
        String jsonContentRequest = "";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirections.json"));
        //When
        Response actualResponse = getRealResponse("/v1/keepers/0000c9999", jsonContentRequest, HttpMethod.GET);
        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void getDirectionsWithEmptyResult() throws IOException {
        //Given
        String jsonContentRequest = "";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirectionsWithEmptyResult.json"));
        //When
        Response actualResponse = getRealResponse("/v1/keepers/1111a9999", jsonContentRequest, HttpMethod.GET);
        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }
}
