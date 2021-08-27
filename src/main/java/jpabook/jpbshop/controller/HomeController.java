package jpabook.jpbshop.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final Logger logger = LoggerFactory.getLogger(getClass());

    @GetMapping("/")
    public String home(){

        logger.info("home controller");

        return "home";
    }
}
