package com.microservices.apigateway.security.repository;

import com.microservices.apigateway.security.model.ExtractRecord;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import java.util.List;

@Repository
public class ExtractRecordRepositoryImpl implements ExtractCustomRespository {

    @PersistenceContext
    EntityManager entityManager;

    @Override
    @Transactional(readOnly = true)
    public Page<ExtractRecord> findAllSortedByDate(Pageable pageable) {
        TypedQuery<ExtractRecord> query = entityManager.createNamedQuery("extractSorted", ExtractRecord.class);

        List<ExtractRecord> list = query.getResultList();

        if (list.isEmpty()) {
            return Page.empty();
        }

        int totalRows = list.size();

        System.out.println(totalRows + " banking record(s) found!");

        Page<ExtractRecord> result = new PageImpl<ExtractRecord>(list, pageable, totalRows);

        return result;
    }
}
