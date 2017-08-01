package net.octoplar;


import net.octoplar.backend.service.CoffeeTypeService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by Octoplar on 31.07.2017.
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class CacheTests {
    @Autowired
    private CoffeeTypeService cts;


    /*
    * Simple cache test.
    * If all ok and cache works CoffeeTypeService must return same cached collection every time.
     */
    @Test
    public void coffeeTypeCacheTest(){
        Object all=cts.readAll();
        Object available=cts.readAvailable();

        for (int i = 0; i < 10; i++) {
            //must return same object
            assert(all==cts.readAll());
            assert(available==cts.readAvailable());
        }
    }
}

