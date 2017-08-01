package net.octoplar.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Octoplar.
 */
@Controller
public class HomePageController {

    @RequestMapping(value = "/", method = RequestMethod.GET)
    public Callable<String> welcome(Map<String, Object> model) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                return "welcome";
            }
        };
    }
}

