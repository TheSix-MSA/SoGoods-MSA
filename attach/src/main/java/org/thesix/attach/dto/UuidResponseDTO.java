package org.thesix.attach.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UuidResponseDTO {

    private Object key;

    private String fileName;

    private Boolean main;

    private String imgSrc;

    private String thumbSrc;
}
