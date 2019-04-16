package com.hackathon.analysis.controller;

import com.hackathon.analysis.repository.sinaRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.awt.print.Pageable;
import java.util.ArrayList;
import java.util.List;
import com.hackathon.analysis.model.sinalocation;


@Controller
public class sinaLocationController {

    @Autowired
    private sinaRepository sinaRepository;


    //http://localhost:8080/sinalocation?query=province
    //根据关键字"商品"去查询列表，name或者description包含的都查询
    @GetMapping("sinalocation")
    @ResponseBody
    public List<sinalocation> getList(String query){

        MatchQueryBuilder matchQuery = QueryBuilders.matchQuery("province", query);
        List<sinalocation> list = new ArrayList<>();
        sinaRepository.search(matchQuery)
               .forEach(e -> list.add(e));
        return list;
    }


}
