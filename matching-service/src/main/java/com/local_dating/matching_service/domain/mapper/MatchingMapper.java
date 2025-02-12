package com.local_dating.matching_service.domain.mapper;

import com.local_dating.matching_service.domain.entity.Matching;
import com.local_dating.matching_service.domain.vo.MatchingVO;
import com.local_dating.matching_service.presentation.dto.MatchingDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.time.LocalDateTime;

@Mapper(componentModel = "spring")
public interface MatchingMapper {

    MatchingMapper INSTANCE = Mappers.getMapper(MatchingMapper.class);
    MatchingVO matchingDTOToMatchingVO(MatchingDTO matchingDTO);
    Matching matchingVOtoMatching(MatchingVO matchingVO);

    @Mapping(target = "inUser", source = "inUser")
    @Mapping(target = "inDate", source = "inDate")
    @Mapping(target = "modUser", source = "modUser")
    @Mapping(target = "modDate", source = "modDate")
    Matching matchingVOtoMatching(MatchingVO matchingVO, String statusCd, long inUser, LocalDateTime inDate, long modUser, LocalDateTime modDate);
}
