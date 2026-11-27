package com.template.spring.note;

import com.template.spring.common.web.PageResponse;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
// 조회는 읽기 전용, 쓰기는 메서드별 트랜잭션으로 전환한다.
@Transactional(readOnly = true)
public class NoteService {

    private final NoteRepository repository;

    public NoteService(NoteRepository repository) {
        this.repository = repository;
    }

    public PageResponse<NoteResponse> findAll(int page, int size) {
        var pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "id"));
        return PageResponse.from(repository.findAll(pageable).map(NoteResponse::from));
    }

    public NoteResponse findById(long id) {
        return NoteResponse.from(getNote(id));
    }

    @Transactional
    public NoteResponse create(NoteRequest request) {
        return NoteResponse.from(repository.save(new Note(request.title(), request.content())));
    }

    @Transactional
    public NoteResponse update(long id, NoteRequest request) {
        Note note = getNote(id);
        // 트랜잭션 종료 시 JPA 변경 감지로 UPDATE가 실행된다.
        note.update(request.title(), request.content());
        return NoteResponse.from(note);
    }

    @Transactional
    public void delete(long id) {
        repository.delete(getNote(id));
    }

    private Note getNote(long id) {
        return repository.findById(id).orElseThrow(() -> new NoteNotFoundException(id));
    }
}
