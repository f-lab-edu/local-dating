package com.local_dating.matching_service.infrastructure.repository;

import com.local_dating.matching_service.domain.entity.Matching;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MatchingeRepository extends JpaRepository<Matching, Long> {

    public void findById(long id);
}
