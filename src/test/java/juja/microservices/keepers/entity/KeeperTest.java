package juja.microservices.keepers.entity;

import org.junit.Assert;
import org.junit.Test;

/**
 * @author Dmitriy Lyashenko
 */

public class KeeperTest {

    @Test
    public void newKeeperEmpty(){
        //Given
        Keeper keeper = new Keeper(null, null, null, null);

        //Then
        String lineSeparator = System.lineSeparator();
        String expected = "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(lineSeparator)
                .concat("   from = ").concat(lineSeparator)
                .concat("   uuid = ").concat(lineSeparator)
                .concat("   direction = ").concat(lineSeparator)
                .concat("   startDate = ").concat(lineSeparator)
                .concat("   dismissDate = ").concat(lineSeparator)
                .concat("   isActive = true").concat(lineSeparator)
                .concat("}");
        Assert.assertEquals(expected, keeper.toString());
    }

    @Test
    public void newKeeper(){
        //Given
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", "2017-05-25");

        //Then
        String lineSeparator = System.lineSeparator();
        String expected = "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(lineSeparator)
                .concat("   from = 123qwe").concat(lineSeparator)
                .concat("   uuid = asdqwe").concat(lineSeparator)
                .concat("   direction = teems").concat(lineSeparator)
                .concat("   startDate = 2017-05-25").concat(lineSeparator)
                .concat("   dismissDate = ").concat(lineSeparator)
                .concat("   isActive = true").concat(lineSeparator)
                .concat("}");
        Assert.assertEquals(expected, keeper.toString());
    }

    @Test
    public void newKeeperSetDismiss(){
        //Given
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", "2017-05-25");

        //When
        keeper.setDismissDate("2017-07-22");

        //Then
        String lineSeparator = System.lineSeparator();
        String expected = "Keeper{".concat(lineSeparator)
                .concat("   id = ").concat(lineSeparator)
                .concat("   from = 123qwe").concat(lineSeparator)
                .concat("   uuid = asdqwe").concat(lineSeparator)
                .concat("   direction = teems").concat(lineSeparator)
                .concat("   startDate = 2017-05-25").concat(lineSeparator)
                .concat("   dismissDate = 2017-07-22").concat(lineSeparator)
                .concat("   isActive = false").concat(lineSeparator)
                .concat("}");
        Assert.assertEquals(expected, keeper.toString());
    }
}