package org.thesix.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ResponseListDTO {

    private RequestListDTO requestListDTO;
    private PageMaker pageMaker;
    private List<Object[]> memberList;
}
