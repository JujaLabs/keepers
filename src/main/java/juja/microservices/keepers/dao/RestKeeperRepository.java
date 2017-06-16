package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import javax.inject.Inject;
import java.util.Arrays;
import java.util.List;

@Repository
public class RestKeeperRepository implements KeeperRepository {

    @Inject
    private RestTemplate restTemplate;

    @Value("${user.baseURL}")
    private String urlBase;
    @Value("${endpoint.keepers}")
    private String urlGetKeepers;

    @Override
    public List<Keeper> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        String urlTemplate = urlBase + urlGetKeepers;
        List<Keeper> result = null;
        try {
            ResponseEntity<Keeper[]> response = this.restTemplate.getForEntity(urlTemplate, Keeper[].class);
            result = Arrays.asList(response.getBody());
        } catch (HttpClientErrorException ex) {
            throw new UserMicroserviceExchangeException("User microservice Exchange Error: " + ex.getMessage());
        }
        return result;
    }
}