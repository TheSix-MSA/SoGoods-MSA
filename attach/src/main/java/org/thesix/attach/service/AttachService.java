package org.thesix.attach.service;

import org.thesix.attach.dto.AttachConfimRequestDTO;

import java.io.UnsupportedEncodingException;

public interface AttachService {
    void registerConfimedImages(AttachConfimRequestDTO requestDTO) throws UnsupportedEncodingException;
}
