package juja.microservices.keepers.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.service.KeepersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.inject.Inject;
import javax.validation.Valid;
import java.net.HttpURLConnection;

import java.util.Collections;
import java.util.List;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 */
@RestController
public class KeepersController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersService keepersService;

    @PostMapping(value = "/keepers", consumes = "application/json", produces = "application/json")
    @ApiOperation(
            value = "Add new keeper",
            notes = "This method add new keeper"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array with one keeper id"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> addKeeper(@Valid @RequestBody KeeperRequest request) {
        logger.debug("Controller.addKeeper after in, parameters: {}", request.toString());
        String keeperId = keepersService.addKeeper(request);
        List<String> ids = Collections.singletonList(keeperId);
        logger.info("Added new 'Keeper', ids = {}", ids.toString());
        logger.debug("Controller.addKeeper before out, parameters: {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @PutMapping(value = "/keepers", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> updateKeeper(@Valid @RequestBody KeeperRequest request) {
        //TODO Should be implemented feature KPR-F2
        return null;
    }

    @GetMapping(value = "/keepers/{uuid}", produces = "application/json")
    public ResponseEntity<?> getDirections(@PathVariable String uuid) {
        //TODO Should be implemented feature KPR-F3
        return null;
    }

    @GetMapping(value = "/keepers", produces = "application/json")
    @ApiOperation(
            value = "Get all active keepers",
            notes = "This method have to get all active keepers"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns maps with active keepers id and their active directions"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> getActiveKeepers() {
        return ResponseEntity.ok(keepersService.getActiveKeepers());
    }
}
