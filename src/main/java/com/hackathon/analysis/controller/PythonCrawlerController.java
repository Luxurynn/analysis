package com.hackathon.analysis.controller;

import com.hackathon.analysis.model.QueryEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 * Created by syy on 18/04/2019.
 */

@Controller
@RequestMapping("/analysis")
public class PythonCrawlerController {
    public final String CRAWLER_SCRIPT_PATH = "pythonScript/TweetScraper-master/";
    public final String ANALYSIS_SCRIPT_PATH = "pythonScript/wordCloudScript/";
    public final String CRAWLER_RESULT_PATH = "pythonScript/wordCloudScript/";

    public final String ANALYSIS_RESULT_PATH = "pythonScript/wordCloudScript/Twitter_Oracle_Java_Final.jpg";

    @GetMapping("/queryPage")
    public String queryFormPage(Model model){
        model.addAttribute("queryEntity", new QueryEntity());
        return "query";
    }

    @PostMapping("/resultPage")
    public String queryResult(@ModelAttribute QueryEntity queryEntity) {
        runPythonCrawlerScript(queryEntity.getQuery(), CRAWLER_SCRIPT_PATH);

        runPythonAnalysisScript(ANALYSIS_SCRIPT_PATH);

        return "result";
    }

    private void runPythonCrawlerScript(String query, String scriptDir) {
        try {
            String cmd = "scrapy crawl TweetScraper -a query=\"" + query + "\"";
            Process process = Runtime.getRuntime().exec(cmd, null, new File(scriptDir));
            InputStream in = process.getInputStream();

//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            System.out.println("input reader get");
//            String line = reader.readLine();
//            System.out.println("output:" + line);
//            while (line != null) {
//                System.out.println("output:" + line);
//                line = reader.readLine();
//            }

            in.close();
            System.out.println("Script run successfully!");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void runPythonAnalysisScript(String scriptDir) {
        try {
            String cmd = "python3 final_twitter.py";
            Process process = Runtime.getRuntime().exec(cmd, null, new File(scriptDir));

            InputStream in = process.getInputStream();

//            BufferedReader reader = new BufferedReader(new InputStreamReader(in));
//            System.out.println("input reader get");
//            String line = reader.readLine();
//            System.out.println("output:" + line);
//            while (line != null) {
//                System.out.println("output:" + line);
//                line = reader.readLine();
//            }

            in.close();

            new executeAfter() {
                @Override
                public void execute() {
                    File resultImg = new File(ANALYSIS_RESULT_PATH);
                    System.out.println("Script run successfully!");
                }

                @Override
                public boolean condition() {
                    File resultImg = new File(ANALYSIS_RESULT_PATH);
                    return resultImg.exists();
                }
            }.executeAfterConditionMet(10);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    class executeAfter {
        private long waitPeriod = 500;  // 500 ms

        public void execute() {}

        public boolean condition() { return true; }

        public void executeAfterSeconds(int seconds) {
            try {
                Thread.sleep((long)seconds * 1000);
                execute();
            }
            catch (InterruptedException e) {

            }
        }

        public void executeAfterConditionMet(int maxWaitingPeriod) throws Exception {
            long timeWaited = 0;
            while (!condition() && timeWaited < maxWaitingPeriod * 1000) {
                try {
                    Thread.sleep(waitPeriod);
                    timeWaited += waitPeriod;
                } catch (InterruptedException e) {
                }
            }

            if (condition()) {
                execute();
            }
            else {
                throw new Exception("Condition not met after " + maxWaitingPeriod + " seconds");
            }
        }
    }
}
