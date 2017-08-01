package net.octoplar.webmvc;

import net.octoplar.backend.entity.CoffeeOrderItem;
import net.octoplar.backend.service.CoffeeTypeService;
import net.octoplar.backend.service.ConfigurationService;
import net.octoplar.backend.util.MapToCoffeeItemsListConverter;
import net.octoplar.backend.validation.ValidationException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.Callable;

/**
 * Created by Octoplar.
 */
@Controller
@RequestMapping("/order_webmvc")
public class CoffeeListController {

    @Autowired
    private CoffeeTypeService coffeeTypeService;

    @Autowired
    private ConfigurationService configurationService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private MapToCoffeeItemsListConverter converter;

    //async
    @GetMapping
    public Callable<String> showList(Map<String, Object> model){
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                //table items
                model.put("coffeeList", coffeeTypeService.readAvailable());
                //free cup message
                String freeCup=configurationService.getParam("free_cup");
                Locale locale = LocaleContextHolder.getLocale();
                String freeCupMessage=messageSource.getMessage("coffeeList.freeCupMessage", new Object[]{freeCup}, locale);
                model.put("freeCupMessage", freeCupMessage);
                return "coffee_list";
            }
        };
    }

    /**
     * async. Takes parameters from request body and put them into session to continue flow on next view.
     * next view is determined by controller:
     * all ok-> status=202 + link to next view in response body
     * validation fault-> status=400 + error message in response body
     * */
    @PostMapping
    @ResponseBody
    public Callable<ResponseEntity<String>> order(@RequestBody Map<String, String> body, HttpSession session){
        return new Callable<ResponseEntity<String>>() {
            @Override
            public ResponseEntity<String> call() throws Exception {

                try {
                    Set<CoffeeOrderItem> items = converter.convertToItems(body);
                    session.setAttribute("items", items);
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    ResponseEntity<String> result=new ResponseEntity<>("/address_input", headers, HttpStatus.ACCEPTED);
                    return result;
                }
                catch (ValidationException e){
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.TEXT_PLAIN);
                    ResponseEntity<String> result=new ResponseEntity<>("bad_request", headers, HttpStatus.BAD_REQUEST);
                    return result;
                }

            }
        };
    }



}

