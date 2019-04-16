package com.hackathon.analysis.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
public class homeController {

    @RequestMapping("/")
    public String index(){
        System.out.println("coming......");
        return "index";
    }

    @RequestMapping("/ss")
    public String searchPage(){
        System.out.println("search......");
        return "search";
    }

}
