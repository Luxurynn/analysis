package com.hackathon.analysis.controller;

import com.hackathon.analysis.service.sinalocationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/search")
public class HelloController {

    @Autowired
    sinalocationService sinalocationService;

    @RequestMapping(method = RequestMethod.GET)
    public String songList(@RequestParam(value = "pNum", required = false) Integer pNum,
                           @RequestParam(value = "pSize", required = false) Integer pSize,
                           @RequestParam(value = "keywords", required = false) String keywords, ModelMap map){
        if(keywords == null) {
            return "sinalocation";
        }
        map.addAttribute("pageSina",sinalocationService.searchSina(pNum,pSize,keywords));
        return "sinalocation";
    }
}
