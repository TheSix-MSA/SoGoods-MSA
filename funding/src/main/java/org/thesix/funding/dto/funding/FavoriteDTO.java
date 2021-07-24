package org.thesix.funding.dto.funding;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteDTO {

    private Long favno;  // 찜 식별 번호

    private String actor;  // 찜한 주체

    private Long funFno;  // 찜한 게시글 식별번호

}
