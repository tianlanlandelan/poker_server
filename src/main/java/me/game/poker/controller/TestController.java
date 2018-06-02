package me.game.poker.controller;

import me.game.poker.entity.Poker;
import me.game.poker.utils.PokerUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yangkaile on 2018/4/6.
 */
@RestController
@RequestMapping("/test")
public class TestController {
    @ResponseBody
    @RequestMapping(method = RequestMethod.GET)
    List<Poker> getAll(){
        List<Poker> list= PokerUtils.parsePokers(PokerUtils.getRandomPokerIds());
        return list;
    }
}


