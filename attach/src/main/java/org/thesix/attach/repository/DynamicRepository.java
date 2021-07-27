package org.thesix.attach.repository;

import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.entity.Attach;

import java.util.List;

public interface DynamicRepository {

    List<Attach> getAttachesByValues(UuidRequestDTO requestDTO);

}
