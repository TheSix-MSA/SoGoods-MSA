package org.thesix.funding.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.thesix.funding.dto.FavoriteDTO;
import org.thesix.funding.entity.Favorite;

import java.util.Optional;

public interface FavoriteRepository extends JpaRepository<Favorite, Long> {

    @Query("select count(f) from Favorite f where f.funding.fno = :fno")
    Optional<Long> getFavoriteCntById(Long fno);

}
