package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.thesix.funding.entity.Funding;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {

    private Long favno;  // 찜 식별 번호

    private boolean mark;  // 찜 여부

    private String actor;  // 찜한 주체

    private Long funFno;  // 찜한 게시글 식별번호

}
