package com.hackathon.analysis.controller;

import com.hackathon.analysis.model.sinalocation;
import com.hackathon.analysis.model.twitterCloud;
import com.hackathon.analysis.repository.sinaRepository;
import com.hackathon.analysis.service.twitterCloudService;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Controller
public class testController {

    @Autowired
    private twitterCloudService twitterCloudService;

    @RequestMapping("/tabletest")
    public String index(){
        System.out.println("tabletest......");
        return "tabletest";
    }


//    public ResponseEntity<?> getAll(@RequestParam(value = "pageNum") Integer pNum,
//                                    @RequestParam(value = "pageSize",defaultValue = "10") Integer pSize,
//                                    @RequestParam(value = "name") String keywords) {
    @RequestMapping("/tabletest/GetDepartment")
    @ResponseBody
    public ResponseEntity<?> GetDepartment(HttpServletRequest request) {
        int limit = 0;
        int offset=0;
        String searchString = request.getParameter("searchStringid");
        //txt_search_departmentname = "java";
        System.out.println("keywords" + searchString + ":"+ offset + ":" + limit);
        //searchString = "java";
        Page<twitterCloud> page = twitterCloudService.searchCloud(limit,offset,searchString);
        //List<CadreInfo> list = cadreInfoService.getAll(name);//获取列表数据
        //PageInfo<CadreInfo> pageInfo = new PageInfo<>(list);
        Map map = new HashMap();
        int total = page.getNumber();//获取列表长度（有多少条数据）
        map.put("total",page.getTotalPages());//返回列表总条数
        map.put("rows",page.getContent());//返回列表数据
        return ResponseEntity.ok(map);
    }

}
