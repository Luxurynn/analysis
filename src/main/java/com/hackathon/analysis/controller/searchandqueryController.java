package com.hackathon.analysis.controller;

import com.hackathon.analysis.model.QueryEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * Created by syy on 18/04/2019.
 */

@Controller
@RequestMapping
public class searchandqueryController {
    public final String CRAWLER_SCRIPT_PATH = "pythonScript/WeiboSpider-search/";
    public final String ANALYSIS_SCRIPT_PATH = "pythonScript/wordCloudScript/";
    public final String CRAWLER_RESULT_PATH = "pythonScript/WeiboSpider-search/";
    public final String CRAWLER_RESULT_FILE = CRAWLER_RESULT_PATH + "result.json";
    public final String CRAWLER_FILTERED_RESULT_FILE = CRAWLER_RESULT_PATH + "filtered_result.json";
    public final String ANALYSIS_RESULT_FILE = ANALYSIS_SCRIPT_PATH + "Weibo_Oracle_Java_Final.jpg";

    public final String ANALYSIS_RESULT_PATH = "pythonScript/wordCloudScript/Twitter_Oracle_Java_Final.jpg";

    @GetMapping("/searchanalysis")
    public String searchanalysis(Model model){

        return "searchanalysis";
    }


    @GetMapping("/queryPage")
    public String queryFormPage(Model model){
        model.addAttribute("queryEntity", new QueryEntity());
        return "query";
    }

    @PostMapping("/resultPage")
    public String queryResult(@ModelAttribute QueryEntity queryEntity) {
        // Clean up files from previous query
        cleanUp();

        // Run crawler script
        Process crawlerProcess = runPythonCrawlerScript(queryEntity.getQuery(), CRAWLER_SCRIPT_PATH);

        // Execute after the size of the generated json file doesn't change from some time
        try {
            new ExecuteAfter() {
                private long previousFileSize = -1;
                private File file = new File(CRAWLER_RESULT_FILE);
                @Override
                public void execute() {
                    // stop crawler script
                    crawlerProcess.destroy();

                    // Filter out useless messages from raw data
                    getContentFromJson();

                    // Run word cloud script
                    runPythonAnalysisScript(ANALYSIS_SCRIPT_PATH);
                }

                @Override
                public boolean condition() {
                    long size = file.length();
                    System.out.println(size);
                    if (size == previousFileSize)
                        return true;
                    else {
                        previousFileSize = size;
                        return false;
                    }
                }
            }.executeAfterConditionMet(120, 15);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "result";
    }

    private Process runPythonCrawlerScript(String query, String scriptDir) {
        Process process = null;
        try {
            // initialize redis
            String[] redisInitCmd = {"python3", "sina/redis_init.py", query};
            Runtime.getRuntime().exec(redisInitCmd, null, new File(scriptDir));

            Thread.sleep(2000);

            // run actual crawler script
            String crawlerCmd = "scrapy crawl weibo_spider";
            process = Runtime.getRuntime().exec(crawlerCmd, null, new File(scriptDir));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return process;
    }

    private void runPythonAnalysisScript(String scriptDir) {
        try {
            String cmd = "python3 final_sina.py";
            Process process = Runtime.getRuntime().exec(cmd, null, new File(scriptDir));

            new ExecuteAfter() {
                @Override
                public void execute() {
                    File resultImg = new File(ANALYSIS_RESULT_PATH);
                    System.out.println("WordCloud script run successfully!");
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

    private void cleanUp() {
        deleteFileIfExist(CRAWLER_RESULT_FILE);
        deleteFileIfExist(CRAWLER_FILTERED_RESULT_FILE);
        deleteFileIfExist(ANALYSIS_RESULT_FILE);
    }

    private void deleteFileIfExist(String fileName) {
        File file = new File(fileName);
        if (file.exists()) {
            System.out.println("Deleting existing file: " + fileName);
            file.delete();
        }
    }

    // Get contents of Weibo messages out of raw data
    // Filter out useless info such as user id, page url, etc.
    private void getContentFromJson() {
        File originalJson = new File(CRAWLER_RESULT_FILE);
        File resultJson = new File(CRAWLER_FILTERED_RESULT_FILE);
        BufferedReader reader = null;
        StringBuilder filteredResult = new StringBuilder();

        try {
            reader = new BufferedReader(new FileReader(originalJson));
            String line = null;
            while ((line = reader.readLine()) != null) {
                String[] fields = line.split(", ");
                // Content will always be the last part of a line
                String last = fields[fields.length - 1];

                int index = last.indexOf("\"content\":");
                if (index != -1) {
                    String content = last.substring("\"content\": \"".length()).trim();
                    content = content.substring(0, content.length() - 2);
                    System.out.println(content);
                    filteredResult.append(content + "\n");
                }
            }
            reader.close();

            writeFile(filteredResult.toString(), resultJson);
        }
        catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeFile(String content, File file){
        try{
            FileWriter fw = new FileWriter(file.getAbsoluteFile(), true);
            BufferedWriter bw = new BufferedWriter(fw);
            bw.write(content);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    class ExecuteAfter {
        private long waitInterval = 500;  // 500 ms

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
                    Thread.sleep(waitInterval);
                    timeWaited += waitInterval;
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

        public void executeAfterConditionMet(int maxWaitingPeriod, int checkInterval) throws Exception {
            long timeWaited = 0;
            while (!condition() && timeWaited < maxWaitingPeriod * 1000) {
                try {
                    Thread.sleep((long)checkInterval * 1000);
                    timeWaited += checkInterval * 1000;
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

