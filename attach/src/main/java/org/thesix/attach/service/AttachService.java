package org.thesix.attach.service;

import org.thesix.attach.dto.AttachConfimRequestDTO;

public interface AttachService {
    void registerConfimedImages(AttachConfimRequestDTO requestDTO);
}
