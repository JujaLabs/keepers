package juja.microservices.keepers.dao;

import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;

import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

/**
 * @author Dmitriy Lyashenko
 */

public class KeepersRepositoryTest {

    private KeepersRepository repository = mock(KeepersRepository.class);

    @Test
    public void save() {
        //When
        when(repository.save(any(KeeperRequest.class))).thenReturn(anyString());

        //Then
        Assert.assertEquals("",
                repository.save(new KeeperRequest("123qwe", "asdqwe", "teems")));

    }

    @Test
    public void findOneById(){
        //Given
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", "2017-05-25");

        //When
        when(repository.findOneByUUId("456rty")).thenReturn(keeper);

        //Then
        Assert.assertEquals(keeper, repository.findOneByUUId("456rty"));
    }
}