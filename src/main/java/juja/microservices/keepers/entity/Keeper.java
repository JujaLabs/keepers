package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Getter
@EqualsAndHashCode
public class Keeper {
    @Id
    private String id;

    private String uuid;
    private String from;
    private String direction;
    private Date startDate;
    private Date dismissDate;
    private boolean isActive;

    @JsonCreator
    public Keeper (//@JsonProperty(value = "id", required = true) String id,
                   @JsonProperty("uuid") String uuid,
                   @JsonProperty("from") String from,
                   @JsonProperty("direction") String direction,
                   @JsonProperty("startDate") Date startDate,
                   @JsonProperty("dismissDate") Date dismissDate,
                   @JsonProperty("isActive") boolean isActive
    ) {

//        this.id = id;
        this.uuid = uuid;
        this.from = from;
        this.direction = direction;
        this.startDate = startDate;
        this.dismissDate = dismissDate;
        this.isActive = isActive;
    }
    @JsonCreator
    public Keeper (@JsonProperty("id") String id,
                   @JsonProperty("uuid") String uuid,
                   @JsonProperty("from") String from,
                   @JsonProperty("direction") String direction,
                   @JsonProperty("startDate") Date startDate,
                   @JsonProperty("dismissDate") Date dismissDate,
                   @JsonProperty("isActive") boolean isActive
    ) {

        this.id = id;
        this.uuid = uuid;
        this.from = from;
        this.direction = direction;
        this.startDate = startDate;
        this.dismissDate = dismissDate;
        this.isActive = isActive;
    }


    public String getId() {
        return id;
    }

    public String getUuid() {
        return uuid;
    }

    public String getFrom() {
        return from;
    }

    public String getDirection() {
        return direction;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDismissDate() {
        return dismissDate;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public String toString() {
        return "Keeper{" +
                "id='" + id + '\'' + "\n" +
                ", uuid='" + uuid + '\'' + "\n" +
                ", from='" + from + '\'' + "\n" +
                ", direction='" + direction + '\'' + "\n" +
                ", startDate=" + startDate + "\n" +
                ", dismissDate=" + dismissDate + "\n" +
                ", isActive=" + isActive +
                '}';
    }
}