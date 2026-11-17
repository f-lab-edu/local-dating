package com.local_dating.matching_service.domain.entity;

import com.local_dating.matching_service.domain.type.OperationStatus;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@Entity
@RequiredArgsConstructor
public class MatchSaga {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "saga_id", length = 100)
    private String sagaId;

    @Column(name = "operation_id", length = 100)
    private String operationId;

    @Column(name = "requester_id")
    private Long requesterId;

    @Column(name = "receiver_id")
    private Long receiverId;

    @Column(name = "amount")
    private Long amount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private OperationStatus status;

    public static MatchSaga pending(UUID sagaId, UUID operationId,
                                    Long requesterId, Long receiverId, Long amount) {
        return new MatchSaga(sagaId, operationId, requesterId, receiverId, amount);
    }

    public MatchSaga(UUID sagaId, UUID operationId,
                     Long requesterId, Long receiverId, Long amount) {

        this.sagaId = sagaId.toString();
        this.operationId = operationId.toString();
        this.requesterId = requesterId;
        this.receiverId = receiverId;
        this.amount = amount;
        this.status = OperationStatus.PENDING;
    }
}
