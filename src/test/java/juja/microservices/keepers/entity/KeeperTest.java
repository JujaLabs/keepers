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
        String expected = "Keeper(id=null, from=123qwe, uuid=asdqwe, direction=teems, " +
                "startDate=Sat Apr 01 12:00:00 EEST 2017, dismissDate=null, isActive=true)";
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
        String expected = "Keeper(id=null, from=123qwe, uuid=asdqwe, direction=teems, " +
                "startDate=Sat Apr 01 12:00:00 EEST 2017, dismissDate=Mon May 01 12:00:00 EEST 2017, isActive=false)";
        Assert.assertEquals(expected, keeper.toString());
    }
}