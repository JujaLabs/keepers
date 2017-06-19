package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.data.annotation.Id;

import java.util.Date;

@Data
@AllArgsConstructor
public class Keeper {
    @JsonProperty("id")
    private String id;
    @JsonProperty("uuid")
    private String uuid;
    @JsonProperty("from")
    private String from;
    @JsonProperty("direction")
    private String direction;
    @JsonProperty("startDate")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date startDate;
    @JsonProperty("dismissDate")
    @JsonFormat(shape=JsonFormat.Shape.STRING, pattern="yyyy-MM-dd")
    private Date dismissDate;
    @JsonProperty("isActive")
    private boolean isActive;

    @JsonCreator
    public Keeper (String uuid,
                   String from,
                   String direction,
                   Date startDate,
                   Date dismissDate,
                   boolean isActive
    ) {
        this.uuid = uuid;
        this.from = from;
        this.direction = direction;
        this.startDate = startDate;
        this.dismissDate = dismissDate;
        this.isActive = isActive;
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