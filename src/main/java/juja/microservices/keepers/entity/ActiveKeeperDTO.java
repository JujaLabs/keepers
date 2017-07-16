package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.Getter;
import lombok.NonNull;
import lombok.ToString;

import java.util.List;

/**
 * @author Dmitriy Lyashenko
 */
@Data
@Getter
@ToString
public class ActiveKeeperDTO {

    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("directions")
    private List<String> directions;
}
