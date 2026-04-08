package com.local_dating.matching_service.domain.mapper;

import com.local_dating.matching_service.domain.entity.MatchingScheduleRequested;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleRequestDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchingScheduleRequestedMapper {

    MatchingScheduleRequestedMapper INSTANCE = Mappers.getMapper(MatchingScheduleRequestedMapper.class);

    List<MatchingScheduleRequestDTO> entityListToDtoList(List<MatchingScheduleRequested> matchingScheduleRequestedList);
}
