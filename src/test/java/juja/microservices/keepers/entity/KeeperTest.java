package juja.microservices.keepers.entity;

import org.junit.Test;

import java.time.LocalDateTime;
import java.time.Month;
import java.time.ZoneId;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

/**
 * @author Dmitriy Lyashenko
 */
public class KeeperTest {

    @Test
    public void newKeeper() {
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0)
                .atZone(ZoneId.of("Europe/Paris")).minusHours(1).toInstant());
        String expectedFrom = "123qwe";
        String expectedUUiD = "asdqwe";
        String expectedDirection = "teems";

        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);

        assertEquals(null, keeper.getId());
        assertEquals(expectedFrom, keeper.getFrom());
        assertEquals(expectedUUiD, keeper.getUuid());
        assertEquals(expectedDirection, keeper.getDirection());
        assertEquals(startDate, keeper.getStartDate());
        assertEquals(null, keeper.getDismissDate());
        assertEquals(true, keeper.isActive());
    }

    @Test
    public void newKeeperSetDismiss() {
        Date startDate = Date.from(LocalDateTime.of(2017, Month.APRIL, 1, 12, 0).
                atZone(ZoneId.of("Europe/Paris")).minusHours(1).toInstant());
        String expectedFrom = "123qwe";
        String expectedUUiD = "asdqwe";
        String expectedDirection = "teems";

        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", startDate);
        keeper.setDismissDate(LocalDateTime.of(2017, Month.MAY, 1, 12, 0));

        assertEquals(null, keeper.getId());
        assertEquals(expectedFrom, keeper.getFrom());
        assertEquals(expectedUUiD, keeper.getUuid());
        assertEquals(expectedDirection, keeper.getDirection());
        assertEquals(startDate, keeper.getStartDate());
        assertNotNull(keeper.getDismissDate());
        assertEquals(false, keeper.isActive());
    }
}