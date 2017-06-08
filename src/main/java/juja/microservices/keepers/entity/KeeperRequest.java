package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.hibernate.validator.constraints.NotEmpty;

/**
 * @author Vadim Dyachenko
 */
@Getter
public class KeeperRequest {

    @NotEmpty
    private String from;
    @NotEmpty
    private String uuid;
    @NotEmpty
    private String direction;

    @JsonCreator
    public KeeperRequest(@JsonProperty("from") String from,
                         @JsonProperty("uuid") String uuid,
                         @JsonProperty("direction") String direction) {

        this.from = from;
        this.uuid = uuid;
        this.direction = direction;
    }
}