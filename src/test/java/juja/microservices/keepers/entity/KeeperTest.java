package juja.microservices.keepers.entity;

import org.junit.Assert;
import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;

import java.util.Date;

/**
 * @author Dmitriy Lyashenko
 */

public class KeeperTest {

    @Test
    public void newKeeper(){
        //Given
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12,0).atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);

        //Then
        String lineSeparator = System.lineSeparator();
        String expected = "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(lineSeparator)
                .concat("   from = 123qwe").concat(lineSeparator)
                .concat("   uuid = asdqwe").concat(lineSeparator)
                .concat("   direction = teems").concat(lineSeparator)
                .concat("   startDate = 2017-04-01").concat(lineSeparator)
                .concat("   dismissDate = ").concat(lineSeparator)
                .concat("   isActive = true").concat(lineSeparator)
                .concat("}");
        Assert.assertEquals(expected, keeper.toString());
    }

    @Test
    public void newKeeperSetDismiss(){
        //Given
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12,0).atZone(ZoneId.systemDefault()).toInstant());
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);

        //When
        keeper.setDismissDate(LocalDateTime.of(2017, Month.MAY, 1, 12,0));

        //Then
        String lineSeparator = System.lineSeparator();
        String expected = "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(lineSeparator)
                .concat("   from = 123qwe").concat(lineSeparator)
                .concat("   uuid = asdqwe").concat(lineSeparator)
                .concat("   direction = teems").concat(lineSeparator)
                .concat("   startDate = 2017-04-01").concat(lineSeparator)
                .concat("   dismissDate = 2017-05-01").concat(lineSeparator)
                .concat("   isActive = false").concat(lineSeparator)
                .concat("}");
        Assert.assertEquals(expected, keeper.toString());
    }
}