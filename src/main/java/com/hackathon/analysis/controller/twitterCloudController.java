package com.hackathon.analysis.controller;

import com.hackathon.analysis.service.twitterCloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
@RequestMapping(value = "/searchCloud")
public class twitterCloudController {

    @Autowired
    twitterCloudService twitterCloudService;

    @RequestMapping(method = RequestMethod.GET)
    public String songList(@RequestParam(value = "pNum", required = false) Integer pNum,
                           @RequestParam(value = "pSize", required = false) Integer pSize,
                           @RequestParam(value = "keywords", required = false) String keywords, ModelMap map){
        if(keywords == null) {
            return "searchcloud";
        }
        map.addAttribute("pageSina",twitterCloudService.searchCloud(pNum,pSize,keywords));
        return "searchcloud";
    }
}
