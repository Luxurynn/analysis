package com.hackathon.analysis.repository;

import com.hackathon.analysis.model.twitterCloud;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface twitterCloudRepository extends ElasticsearchRepository<twitterCloud, Long> {

}
