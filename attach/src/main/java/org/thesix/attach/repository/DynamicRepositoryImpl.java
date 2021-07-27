package org.thesix.attach.repository;

import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.entity.Attach;

import java.util.List;

public class DynamicRepositoryImpl extends QuerydslRepositorySupport implements DynamicRepository {

    public DynamicRepositoryImpl() {
        super(Attach.class);
    }

    @Override
    public List<Attach> getAttachesByValues(UuidRequestDTO requestDTO) {



        return null;
    }
}
