package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import org.springframework.data.annotation.Id;

/**
 * @author Dmitriy Lyashenko
 */

@Getter
public class Keeper {

    @Id
    private String id;

    private String from;
    private String uuid;
    private String direction;
    private String startDate;
    private String dismissDate;
    private boolean isActive;

    @JsonCreator
    public Keeper(@JsonProperty("from") String from,
                  @JsonProperty("uuid") String uuid,
                  @JsonProperty("direction") String direction,
                  @JsonProperty("startDate") String startDate) {
        this.from = from;
        this.uuid = uuid;
        this.direction = direction;
        this.startDate = startDate;
        this.isActive = true;
    }

    public void setDismissDate(String dismissDate) {
        this.dismissDate = dismissDate;
        this.isActive = false;
    }

    @Override
    public String toString(){

        String lineSeparator = System.lineSeparator();
        return "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(id != null ? id : "").concat(lineSeparator)
                .concat("   from = ").concat(from != null ? from : "").concat(lineSeparator)
                .concat("   uuid = ").concat(uuid != null ? uuid : "").concat(lineSeparator)
                .concat("   direction = ").concat(direction != null ? direction : "").concat(lineSeparator)
                .concat("   startDate = ").concat(startDate != null ? startDate : "").concat(lineSeparator)
                .concat("   dismissDate = ").concat(dismissDate != null ? dismissDate : "").concat(lineSeparator)
                .concat("   isActive = ").concat(String.valueOf(isActive)).concat(lineSeparator)
                .concat("}");
    }
}
