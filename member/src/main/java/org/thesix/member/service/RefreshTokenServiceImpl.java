package org.thesix.member.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;
import org.thesix.member.dto.RefreshDTO;
import org.thesix.member.entity.RefreshToken;
import org.thesix.member.repository.RefreshTokenRepository;

@Service
@Log4j2
@RequiredArgsConstructor
public class RefreshTokenServiceImpl implements RefreshTokenService{

    private final RefreshTokenRepository tokenRepository;

    @Override
    public long registRefreshToken(RefreshDTO token) {
        RefreshToken result = tokenRepository.save(refreshDTOToEntity(token));
        return result.getExpireDate();
    }
}
