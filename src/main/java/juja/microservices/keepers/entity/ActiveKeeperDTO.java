package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Dmitriy Roy
 */
@Data
@Getter
@ToString
public class ActiveKeeperDTO {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("directions")
    private List<String> directions = new ArrayList<>();

    public ActiveKeeperDTO(String uuid, List<String> directions) {
        this.uuid = uuid;
        this.directions.addAll(directions);
    }

    public void addDirection(String direction){
        System.out.println("directions = " + directions);
        System.out.println("direction = " + direction);
        this.directions.add(direction);
    }
}
