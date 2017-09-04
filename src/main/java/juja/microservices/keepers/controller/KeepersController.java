package juja.microservices.keepers.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.service.KeepersService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.HttpURLConnection;
import java.util.Collections;
import java.util.List;

/**
 * @author Vadim Dyachenko
 * @author Dmitriy Lyashenko
 * @author Dmitriy Roy
 * @author Konstantin Sergey
 * @author Oleksii Petrokhalko
 */
@RestController
@RequestMapping(value = "/" + "${keepers.rest.api.version}" + "${keepers.baseURL}", produces = "application/json")
public class KeepersController {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private KeepersService keepersService;

    @PostMapping(value = "${keepers.endpoint.addKeeper}", consumes = "application/json")
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

    @PutMapping(value = "${keepers.endpoint.deactivateKeeper}", consumes = "application/json")
    @ApiOperation(
            value = "Makes keeper inactive",
            notes = "This method makes keeper inactive"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns array with one keeper id"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method"),
            @ApiResponse(code = HttpURLConnection.HTTP_UNSUPPORTED_TYPE, message = "Unsupported request media type")
    })
    public ResponseEntity<?> deactivateKeeper(@Valid @RequestBody KeeperRequest request) {
        List<String> ids = keepersService.deactivateKeeper(request);
        logger.info("Deactivate keeper, ids = {}", ids.toString());
        return ResponseEntity.ok(ids);
    }

    @GetMapping(value = "${keepers.endpoint.getDirections}" + "/{uuid}")
    @ApiOperation(
            value = "Get a list of all directions of active keeper",
            notes = "This method returns a list of all directions of active keeper"
    )
    @ApiResponses(value = {
            @ApiResponse(code = HttpURLConnection.HTTP_OK, message = "Returns a list of all directions of active keeper"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "Bad request"),
            @ApiResponse(code = HttpURLConnection.HTTP_BAD_METHOD, message = "Bad method")
    })
    public ResponseEntity<?> getDirections(@PathVariable String uuid) {
        logger.debug("Received get directions by uuid request. Requested uuid: {}", uuid);

        List<String> result = keepersService.getDirections(uuid);

        logger.info("Number of returned keeper directions is {}", result.size());
        logger.debug("Request for active directions for keeper returned {}", result.toString());
        return ResponseEntity.ok(result);
    }

    @GetMapping(value = "${keepers.endpoint.getActiveKeepers}")
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
