package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MatchingeRepository extends JpaRepository<Matching, Long> {

    //public void findById(long id);

    public Optional<Matching> findByIdAndRequId(long id, long requId);

    public Optional<Matching> findByIdAndRecvId(long id, long recvId);

    public List<Matching> findByRequIdOrRecvIdAndStatusCdNot(long requId, long recvId, String status);

    public Optional<Matching> findById(long id);
    //public List<Matching> findByRecvIdAndStatusCdNot(long id, String status);
    //public List<Matching> findByRecvIdAndStatusCdNot090(long id);

    public List<Matching> findByRecvIdAndStatusCdNotIn(long id, List<String> statusCd);

    public List<Matching> findByRequIdAndStatusCdNotIn(long id, List<String> statusCd);
}
