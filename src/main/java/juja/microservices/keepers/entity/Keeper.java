package juja.microservices.keepers.entity;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;

import org.springframework.data.annotation.Id;

import java.text.SimpleDateFormat;

import java.time.LocalDateTime;
import java.time.ZoneId;

import java.util.Date;

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

    @Override
    public String toString(){
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String startDate = this.startDate != null ? dateFormat.format(this.startDate.getTime()): "";
        String dismissDate = this.dismissDate != null ? dateFormat.format(this.dismissDate.getTime()): "";

        String lineSeparator = System.lineSeparator();
        return "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(id != null ? id : "").concat(lineSeparator)
                .concat("   from = ").concat(from != null ? from : "").concat(lineSeparator)
                .concat("   uuid = ").concat(uuid != null ? uuid : "").concat(lineSeparator)
                .concat("   direction = ").concat(direction != null ? direction : "").concat(lineSeparator)
                .concat("   startDate = ").concat(startDate).concat(lineSeparator)
                .concat("   dismissDate = ").concat(dismissDate).concat(lineSeparator)
                .concat("   isActive = ").concat(String.valueOf(isActive)).concat(lineSeparator)
                .concat("}");
    }
}
