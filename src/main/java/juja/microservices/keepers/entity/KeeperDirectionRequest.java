package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

import java.util.List;

@Getter
public class KeeperDirectionRequest {
    private String direction;

    @JsonCreator
    public KeeperDirectionRequest() {
    }

    @JsonCreator
    public KeeperDirectionRequest(@JsonProperty("direction") String direction) {
        this.direction = direction;
    }
}