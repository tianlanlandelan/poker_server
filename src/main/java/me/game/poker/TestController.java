package me.game.poker;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by yangkaile on 2018/4/6.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @RequestMapping(method = RequestMethod.GET)
    String getAll(){
        return "111";
    }
}
