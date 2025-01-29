package com.local_dating.matching_service.domain.vo;

public record MatchingVO(long requId, long recvId, String statusCd, String requStatusCd, String recvStatusCd, String matchPlace, String matchDate, String matchTime) {
}
