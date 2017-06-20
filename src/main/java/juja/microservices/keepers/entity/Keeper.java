package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.ToString;

import org.springframework.data.annotation.Id;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.Date;

/**
 * @author Dmitriy Lyashenko
 */

@Getter
@ToString
public class Keeper {

    @Id
    private String id;

    private String from;
    private String uuid;
    private String direction;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;

    @JsonFormat(shape= JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date dismissDate;
    private boolean isActive;

    @JsonCreator
    public Keeper(@JsonProperty("from") String from,
                  @JsonProperty("uuid") String uuid,
                  @JsonProperty("direction") String direction,
                  @JsonProperty("startDate") Date startDate) {
        this.from = from;
        this.uuid = uuid;
        this.direction = direction;
        this.startDate = startDate;
        this.isActive = true;
    }

    public void setDismissDate(LocalDateTime dismissDate) {
        this.dismissDate = Date.from(dismissDate.atZone(ZoneId.systemDefault()).toInstant());
        this.isActive = false;
    }
}
