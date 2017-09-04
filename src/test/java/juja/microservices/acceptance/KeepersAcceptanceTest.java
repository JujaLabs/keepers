package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;
import io.restassured.response.Response;
import net.javacrumbs.jsonunit.core.Option;
import org.eclipse.jetty.http.HttpMethod;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

/**
 * @author Dmitriy Lyashenko
 */
@RunWith(SpringRunner.class)
public class KeepersAcceptanceTest extends BaseAcceptanceTest {

    @Value("${keepers.rest.api.version}")
    private String keepersRestApiVersion;
    @Value("${keepers.baseURL}")
    private String keepersBaseUrl;
    @Value("${keepers.endpoint.addKeeper}")
    private String keepersAddKeeperUrl;
    @Value("${keepers.endpoint.deactivateKeeper}")
    private String keepersDeactivateKeeperUrl;
    @Value("${keepers.endpoint.getDirections}")
    private String keepersGetDirectionsUrl;
    @Value("${keepers.endpoint.getActiveKeepers}")
    private String keepersGetActiveKeepersUrl;

    private String keepersFullAddKeeperUrl;
    private String keepersFullGetDirectionsUrl;

    @Before
    public void localSetup() {
        keepersFullAddKeeperUrl = "/" + keepersRestApiVersion + keepersBaseUrl + keepersAddKeeperUrl;
        keepersFullGetDirectionsUrl = "/" + keepersRestApiVersion + keepersBaseUrl + keepersGetDirectionsUrl+"/";
    }

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperException() throws IOException {

        //Given
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        //When
        Response actualResponse = getRealResponse(keepersFullAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperAlreadyKeepDirectionIsAliveException() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeperOther.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperAlreadyKeepsDirectionIsAlive.json"));

        Response actualResponse = getRealResponse(keepersFullAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/oneKeeperInDB.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperOK() throws IOException {
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        Response actualResponse = getRealResponse(keepersFullAddKeeperUrl, jsonContentRequest, HttpMethod.POST);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isNotEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void getDirections() throws IOException {
        String jsonContentRequest = "";
        String uuid="0000c9999";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirections.json"));

        Response actualResponse = getRealResponse(keepersFullGetDirectionsUrl+uuid, jsonContentRequest, HttpMethod.GET);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }

    @UsingDataSet(locations = "/datasets/getKeeperDirections.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void getDirectionsWithEmptyResult() throws IOException {
        String jsonContentRequest = "";
        String uuid="1111a9999";
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseGetDirectionsWithEmptyResult.json"));

        Response actualResponse = getRealResponse(keepersFullGetDirectionsUrl+uuid, jsonContentRequest, HttpMethod.GET);

        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isEqualTo(jsonContentControlResponse);
    }
}
