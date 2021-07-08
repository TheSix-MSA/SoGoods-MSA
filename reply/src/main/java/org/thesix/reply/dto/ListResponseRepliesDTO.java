package org.thesix.reply.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ListResponseRepliesDTO {
    private List<RepliesResponseDTO> repliesDTOList;
    private PageMaker pageMaker;
}
