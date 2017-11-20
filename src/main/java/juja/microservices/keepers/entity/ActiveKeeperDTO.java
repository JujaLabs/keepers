package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Roy
 */
@Data
public class ActiveKeeperDTO {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("directions")
    private List<String> directions = new ArrayList<>();

    public ActiveKeeperDTO(String uuid, List<String> directions) {
        this.uuid = uuid;
        if (directions != null && !directions.isEmpty()) {
            this.directions.addAll(directions);
        }
    }

    public void addDirection(String direction) {
        this.directions.add(direction);
    }
}
