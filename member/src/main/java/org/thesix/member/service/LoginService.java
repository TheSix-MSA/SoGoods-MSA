package org.thesix.member.service;

import org.thesix.member.dto.LoginInfoDTO;
import org.thesix.member.dto.TokenDTO;

public interface LoginService {
    TokenDTO Login(LoginInfoDTO dto);
}
