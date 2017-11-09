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
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
public class KeepersAcceptanceTest extends BaseAcceptanceTest {
    private final String keepersAddKeeperUrl = "/v1/keepers";
    private final String keepersDeactivateKeeperUrl = "/v1/keepers";
    private final String keepersGetDirectionsUrl = "/v1/keepers/";
    private final String keepersGetActiveKeepersUrl = "/v1/keepers";


    @Test
    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void addKeeperException() throws IOException {

        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        Response actualResponse = getRealResponse(keepersAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void addKeeperAlreadyKeepDirectionIsAliveException() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeperOther.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperAlreadyKeepsDirectionIsAlive.json"));

        Response actualResponse = getRealResponse(keepersAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void addKeeperOK() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        Response actualResponse = getRealResponse(keepersAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isNotEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void getDirections() throws IOException {
        String jsonContentRequest = "";
        String uuid = "0000c9999";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirections.json"));

        Response actualResponse = getRealResponse(keepersGetDirectionsUrl + uuid, jsonContentRequest, HttpMethod.GET);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void getDirectionsWithEmptyResult() throws IOException {
        String jsonContentRequest = "";
        String uuid = "1111a9999";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirectionsWithEmptyResult.json"));

        Response actualResponse = getRealResponse(keepersGetDirectionsUrl + uuid, jsonContentRequest, HttpMethod.GET);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/getActiveKeepers.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void getActiveKeepers() throws IOException {
        String jsonContentRequest = "";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetActiveKeepers.json"));

        Response actualResponse = getRealResponse(keepersGetActiveKeepersUrl, jsonContentRequest, HttpMethod.GET);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @Test
    @UsingDataSet(locations = "/datasets/severalKeepers.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void deactivateKeeper() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/deactivateKeeper.json"));

        Response actualResponse = getRealResponse(keepersDeactivateKeeperUrl, jsonContentRequest, HttpMethod.PUT);

        System.out.println(String.format("%s DeactivateKeeper command result json: %s ", System.lineSeparator(), actualResponse.asString()));
    }

    @Test
    @UsingDataSet(locations = "/datasets/severalKeepers.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    public void deactivateKeeperNonExistentException() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/deactivateNonexistentKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseDeactivateKeeperNonExistentException.json"));

        Response actualResponse = getRealResponse(keepersDeactivateKeeperUrl, jsonContentRequest, HttpMethod.PUT);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }
}
