package com.ac.start.ctrl;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;

import javax.annotation.PostConstruct;

@Controller
public class AppCtrl {

    @ResponseBody
    @RequestMapping("/")
    public String app() {
        return "OK";
    }
}
