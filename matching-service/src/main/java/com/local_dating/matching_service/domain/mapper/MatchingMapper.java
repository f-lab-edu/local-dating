package com.local_dating.matching_service.domain.mapper;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.presentation.dto.MatchingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface MatchingMapper {

    MatchingMapper INSTANCE = Mappers.getMapper(MatchingMapper.class);
    MatchingVO matchingDTOToMatchingVO(MatchingDTO matchingDTO);
    Matching matchingVOtoMatching(MatchingVO matchingVO);

    @Mapping(target = "id", ignore = true) // 엔티티 생성 시 키 자동생성을 위해서 무시
    @Mapping(target = "statusCd", source = "statusCd")
    @Mapping(target = "decisionStartDate", source = "decisionStartDate")
    @Mapping(target = "decisionEndDate", source = "decisionEndDate")
    @Mapping(target = "inUser", source = "inUser")
    @Mapping(target = "inDate", source = "inDate")
    @Mapping(target = "modUser", source = "modUser")
    @Mapping(target = "modDate", source = "modDate")
    Matching matchingVOtoMatching(MatchingVO matchingVO, String statusCd
            , LocalDateTime decisionStartDate, LocalDateTime decisionEndDate
            , long inUser, LocalDateTime inDate, long modUser, LocalDateTime modDate);

    List<MatchingVO> matchingstoMatchingVOs(List<Matching> matchings);

    MatchingVO matchingtoMatchingVO(Matching matching);

    List<MatchingDTO> matchingVOsToMatchingDTOs(List<MatchingVO> matchingVOs);

    MatchingDTO matchingVOToMatchingDTO(Optional<MatchingVO> matchingVO);

    MatchingDTO matchingVOToMatchingDTO(MatchingVO matchingVO);
}
