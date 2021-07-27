package org.thesix.member.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thesix.member.entity.RefreshToken;


public interface RefreshTokenRepository extends JpaRepository<RefreshToken,String> {

}
