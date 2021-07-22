package org.thesix.funding.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FavoriteResponseDTO{

    private List<FavoriteDTO> favoriteDTOList; // 찜 상세 리스트

    private Long favoriteCnt; // 총 찜 개수

}
