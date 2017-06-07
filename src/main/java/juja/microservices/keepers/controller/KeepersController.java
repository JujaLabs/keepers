package juja.microservices.keepers.controller;

import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.service.KeepersService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;

/**
 * @author Vadim Dyachenko
 */

@RestController
public class KeepersController {

    @Inject
    private KeepersService keepersService;

    @PostMapping(value = "/keepers", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> addKeeper(@Valid @RequestBody KeeperRequest request) {
        //TODO Should be implemented feature KPR-F1
        return null;
    }

    @PutMapping(value = "/keepers/end", consumes = "application/json", produces = "application/json")
    public ResponseEntity<?> endKeeper(@Valid @RequestBody KeeperRequest request) {
        //TODO Should be implemented feature KPR-F2
        return null;
    }

    @GetMapping(value = "/keepers/{uuid}", produces = "application/json")
    public ResponseEntity<?> getDirections(@PathVariable String uuid) {
        //TODO Should be implemented feature KPR-F3
        return null;
    }

    @GetMapping(value = "/keepers", produces = "application/json")
    public ResponseEntity<?> getKeepers() {
        //TODO Should be implemented feature KPR-F4
        return null;
    }
}
