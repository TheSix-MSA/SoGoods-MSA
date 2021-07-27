package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class NovelsDTO {

    private Long nno;
    private String isbn;
    private String title;
    private String image;
    private String publisher;
    private String email;
    private boolean deleted;


}
