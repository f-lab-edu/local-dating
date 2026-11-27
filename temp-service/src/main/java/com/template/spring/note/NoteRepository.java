package com.template.spring.note;

import org.springframework.data.jpa.repository.JpaRepository;

// 이 서비스가 소유하는 메모 테이블에만 접근한다.
interface NoteRepository extends JpaRepository<Note, Long> {
}
