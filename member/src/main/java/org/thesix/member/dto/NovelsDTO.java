package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.member.entity.Member;

import javax.persistence.*;

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
