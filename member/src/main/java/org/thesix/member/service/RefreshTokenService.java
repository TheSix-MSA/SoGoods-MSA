package org.thesix.member.service;

import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.entity.RefreshToken;

public interface RefreshTokenService {

    long registRefreshToken(RefreshDTO token);

    default RefreshToken refreshDTOToEntity(RefreshDTO dto){

        return RefreshToken.builder()
                .email(dto.getEmail())
                .expireDate(dto.getExpireDate())
                .build();
    }

    default RefreshDTO entityToRefreshDTO(RefreshToken token) {

        return RefreshDTO.builder()
                .email(token.getEmail())
                .expireDate(token.getExpireDate())
                .build();
    }

}
