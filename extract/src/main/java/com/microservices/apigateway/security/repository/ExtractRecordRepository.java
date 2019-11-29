package com.microservices.apigateway.security.repository;

import com.microservices.apigateway.security.model.ExtractRecord;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExtractRecordRepository extends JpaRepository<ExtractRecord, Long>, ExtractCustomRespository { }
