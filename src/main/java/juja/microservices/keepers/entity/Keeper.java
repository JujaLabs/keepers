package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Date;

/**
 * @author Vadim Dyachenko
 * KPR-F4 Dmitriy Roy
 */
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

//    @JsonCreator
//    public Keeper() {
//
//    }

//    @JsonCreator
//    public Keeper (String uuid,
//                   String from,
//                   String direction,
//                   Date startDate,
//                   Date dismissDate,
//                   boolean isActive
//    ) {
//        this.uuid = uuid;
//        this.from = from;
//        this.direction = direction;
//        this.startDate = startDate;
//        this.dismissDate = dismissDate;
//        this.isActive = isActive;
//    }

//    public String getUuid() {
//        return uuid;
//    }
//
//    public String getDirection() {
//        return direction;
//    }

}