package com.local_dating.matching_service.presentation.dto;

public record MatchingDTO(long id, long requId, long recvId, String statusCd, String requStatusCd, String recvStatusCd, String matchPlace, String matchDate, String matchTime) {
}
