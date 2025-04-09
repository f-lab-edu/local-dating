package com.local_dating.matching_service.domain.mapper;

import com.local_dating.matching_service.domain.entity.MatchingSchedule;
import com.local_dating.matching_service.domain.vo.MatchingScheduleVO;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchingScheduleMapper {

    MatchingScheduleMapper INSTANCE = Mappers.getMapper(MatchingScheduleMapper.class);

    List<MatchingScheduleVO> matchingSchedulesToMatchingScheduleVOs(List<MatchingSchedule> matchingSchedules);

    MatchingScheduleVO matchingScheduleDTOToMatchingScheduleVO(MatchingScheduleDTO matchingScheduleDTO);

    List<MatchingScheduleVO> matchingScheduleDTOsToMatchingScheduleVOs(List<MatchingScheduleDTO> matchingScheduleDTOs);

    List<MatchingScheduleDTO> matchingScheduleVOsToMatchingScheduleDTOs(List<MatchingScheduleVO> matchingScheduleVOs);

    MatchingSchedule matchingScheduleVOToMatchingSchedule(MatchingScheduleVO matchingScheduleVO);
}
