package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.List;

/**
 * @author Dmitriy Lyashenko
 */
@Data
@Getter
//@Setter
@ToString
public class ActiveKeeperDTO {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("directions")
    private List<String> directions;

    public ActiveKeeperDTO(String keeperUuid, List<String> directions) {
    }

    public void addDirection(String direction){
        this.directions.add(direction);
    }
}
