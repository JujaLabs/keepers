package juja.microservices.acceptance;

import com.lordofthejars.nosqlunit.annotation.UsingDataSet;
import com.lordofthejars.nosqlunit.core.LoadStrategyEnum;

import io.restassured.response.Response;

import net.javacrumbs.jsonunit.core.Option;

import org.eclipse.jetty.http.HttpMethod;

import org.junit.Test;
import org.junit.runner.RunWith;

import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import java.io.IOException;

import static net.javacrumbs.jsonunit.core.util.ResourceUtils.resource;
import static net.javacrumbs.jsonunit.fluent.JsonFluentAssert.assertThatJson;

/**
 * @author Dmitriy Lyashenko
 */

@RunWith(SpringRunner.class)
public class KeepersAT extends BaseAcceptanceTest{

    @Inject
    private MongoTemplate mongoTemplate;

    @UsingDataSet(locations = "/datasets/initEmptyDb.json", loadStrategy = LoadStrategyEnum.CLEAN_INSERT)
    @Test
    public void addKeeperException() throws IOException {

        //Given
        String jsonContentRequest = convertToString(resource("acceptance/request/addKeeper.json"));
        String jsonContentControlResponse = convertToString(
                resource("acceptance/response/responseAddKeeperException.json"));

        //When
        Response actualResponse = getRealResponse("/keepers", jsonContentRequest, HttpMethod.POST);

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
        Response actualResponse = getRealResponse("/keepers", jsonContentRequest, HttpMethod.POST);

        //Then
        assertThatJson(actualResponse.asString())
                .when(Option.IGNORING_ARRAY_ORDER)
                .isNotEqualTo(jsonContentControlResponse);
    }
}
