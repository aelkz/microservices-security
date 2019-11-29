package com.microservices.apigateway.security.repository;

import com.microservices.apigateway.security.model.ExtractRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ExtractCustomRespository {

    public Page<ExtractRecord> findAllSortedByDate(Pageable pageable);

}
