package com.local_dating.matching_service.domain.mapper;

import com.local_dating.matching_service.domain.entity.MatchingSchedule;
import com.local_dating.matching_service.domain.vo.MatchingScheduleVO;
import com.local_dating.matching_service.presentation.dto.MatchingScheduleDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface MatchingScheduleMapper {

    MatchingScheduleMapper INSTANCE = Mappers.getMapper(MatchingScheduleMapper.class);

    List<MatchingScheduleVO> matchingSchedulesToMatchingScheduleVOs(List<MatchingSchedule> matchingSchedules);

    MatchingScheduleVO matchingScheduleDTOToMatchingScheduleVO(MatchingScheduleDTO matchingScheduleDTO);

    MatchingScheduleVO matchingScheduleToMatchingScheduleVO(MatchingSchedule matchingSchedule);

    MatchingScheduleDTO matchingScheduleVOToMatchingScheduleDTO(MatchingScheduleVO matchingScheduleVO);

    List<MatchingScheduleVO> matchingScheduleDTOsToMatchingScheduleVOs(List<MatchingScheduleDTO> matchingScheduleDTOs);

    List<MatchingScheduleDTO> matchingScheduleVOsToMatchingScheduleDTOs(List<MatchingScheduleVO> matchingScheduleVOs);

    MatchingSchedule matchingScheduleVOToMatchingSchedule(MatchingScheduleVO matchingScheduleVO);

    @Mapping(target = "scheduleStatus", source = "scheduleStatus")
    MatchingSchedule matchingScheduleVOToMatchingSchedule(MatchingScheduleVO matchingScheduleVO, String scheduleStatus);


}
