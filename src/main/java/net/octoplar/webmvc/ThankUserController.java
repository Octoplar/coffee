package net.octoplar.webmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.SessionAttribute;

import javax.servlet.http.HttpSession;
import java.util.Map;
import java.util.concurrent.Callable;

/**
 * Created by Octoplar.
 */
@Controller
public class ThankUserController {

    @RequestMapping(value = "/thank_user", method = RequestMethod.GET)
    public Callable<String> welcome(Map<String, Object> model,
                                    @SessionAttribute String orderId,
                                    HttpSession session) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                //close session
                session.invalidate();
                model.put("orderInfoUrl", "/orderInfo/"+orderId);
                return "thank_user";
            }
        };
    }
}
