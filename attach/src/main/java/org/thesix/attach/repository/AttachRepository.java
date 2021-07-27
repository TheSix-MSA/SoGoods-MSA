package org.thesix.attach.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.thesix.attach.dto.UuidRequestDTO;
import org.thesix.attach.entity.Attach;

import java.util.List;
import java.util.Optional;

public interface AttachRepository extends JpaRepository<Attach, Long> {

    @Modifying
    @Query("DELETE FROM Attach a WHERE a.originalName IN :fileNames")
    void deleteByOriginalName(String[] fileNames);


    @Query("SELECT a FROM Attach a " +
            "WHERE a.main = true " +
            "   AND a.tableName = :tableName" +
            "   AND a.keyValue IN (:keyValues)")
    List<Attach> getAttachesByValues(String tableName, String[] keyValues);

    @Query("SELECT a FROM Attach a " +
            "WHERE a.main IN (:mainList) " +
            "   AND a.tableName = :tableName" +
            "   AND a.keyValue = :keyValue" +
            " ORDER BY a.main DESC ")
    List<Attach> getAttachesByValue(Boolean[] mainList, String tableName, String keyValue);

}
