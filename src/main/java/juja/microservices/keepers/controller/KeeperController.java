package juja.microservices.keepers.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.exceptions.UserMicroserviceExchangeException;
import juja.microservices.keepers.service.KeeperService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import java.net.HttpURLConnection;

@RestController
@RequestMapping(value = "/keepers", produces = "application/json")
public class KeeperController {

    @Inject
    private KeeperService keeperService;

    @GetMapping
    @ApiOperation(
            value = "Get all active keepers",
            notes = "This method returns all active keepers"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns all active keepers"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method")
    })
    public ResponseEntity<?> getAllActiveKeepers() throws UserMicroserviceExchangeException {
        return ResponseEntity.ok(keeperService.getAllActiveKeepers());
    }
}

