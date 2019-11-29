package com.microservices.apigateway.security.service;

import com.microservices.apigateway.security.model.ExtractRecord;
import com.microservices.apigateway.security.repository.ExtractRecordRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;

@Service
@Transactional
public class ExtractService {

    private final ExtractRecordRepository repository;

    public ExtractService(ExtractRecordRepository repository
    ) {
        this.repository = repository;
    }

    @Transactional(readOnly = true)
    public Page<ExtractRecord> findAllSorted(Pageable pageable) {
        Page<ExtractRecord> result = repository.findAllSortedByDate(pageable);
        return result;
    }


    public ExtractRecord save(ExtractRecord product) {
        product.setDate(LocalDateTime.now());

        return repository.save(product);
    }

}
