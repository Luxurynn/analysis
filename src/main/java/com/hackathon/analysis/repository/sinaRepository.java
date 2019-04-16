package com.hackathon.analysis.repository;

import com.hackathon.analysis.model.sinalocation;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface sinaRepository extends ElasticsearchRepository<sinalocation, Long> {

}
