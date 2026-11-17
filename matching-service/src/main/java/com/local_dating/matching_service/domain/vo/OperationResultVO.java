package com.local_dating.matching_service.domain.vo;

import com.local_dating.matching_service.domain.type.OperationStatus;

import java.util.UUID;

public record OperationResultVO(UUID sagaId, UUID operationId, OperationStatus status, String failureReason) {
}
