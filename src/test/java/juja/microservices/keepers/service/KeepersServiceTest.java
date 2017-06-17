package juja.microservices.keepers.service;

import juja.microservices.keepers.dao.KeepersRepository;
import juja.microservices.keepers.entity.Keeper;
import juja.microservices.keepers.entity.KeeperRequest;
import juja.microservices.keepers.exception.UnsupportedKeeperException;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import org.mockito.InjectMocks;

import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.context.junit4.SpringRunner;

import javax.inject.Inject;

import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.when;

/**
 * @author Dmitriy Lyashenko
 */

@RunWith(SpringRunner.class)
@WebMvcTest(KeepersService.class)
public class KeepersServiceTest {

    @Inject
    @InjectMocks
    private KeepersService service;

    @MockBean
    private KeepersRepository repository;

    @Test(expected = UnsupportedKeeperException.class)
    public void addKeeperNotExist(){
        //Given
        when(repository.findOneByUUId(anyString())).thenReturn(null);

        //When
        service.addKeeper(new KeeperRequest("123qwe", "asdqwe", "teems"));
    }

    @Test
    public void addKeeper(){
        //Given
        KeeperRequest keeperRequest = new KeeperRequest("123qwe", "asdqwe", "teems");
        Keeper keeper = new Keeper("123qwe", "asdqwe", "teems", "2017-05-25");
        when(repository.findOneByUUId(anyString())).thenReturn(keeper);
        when(repository.save(keeperRequest)).thenReturn("SomeID");
        String expected = "SomeID";

        //When
        String result = service.addKeeper(keeperRequest);

        //Then
        Assert.assertEquals(expected, result);
    }
}