package com.JNSample.api;

import com.JNSample.dto.BoardForm;
import com.JNSample.entity.Board;
import com.JNSample.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController // RestAPI 용 컨트롤러! (JSON 반환)
@Slf4j
public class BoardApiController {

    @Autowired
    private BoardRepository boardRepository;

    // GET

    @GetMapping("/api/board")
    public List<Board> index() {
        return boardRepository.findAll();
    }

    @GetMapping("/api/board/{id}")
    public Board index(@PathVariable Long id) {
        return boardRepository.findById(id).orElse(null);
    }

    // POST

    @PostMapping("/api/board")
    public Board create(@RequestBody BoardForm dto) {
        Board board = dto.toEntity();
        return  boardRepository.save(board);

    }

    // PATCH

    // PATCH
    @PatchMapping("/api/board/{id}")
    public ResponseEntity<Board> update(@PathVariable Long id,
                                          @RequestBody BoardForm dto) {
        // 1: DTO -> 엔티티
        Board board = dto.toEntity();
        log.info("id: {}, board: {}", id, board.toString());
        // 2: 타겟 조회
        Board target = boardRepository.findById(id).orElse(null);
        // 3: 잘못된 요청 처리
        if (target == null || id != target.getId()) {
            // 400, 잘못된 요청 응답!
            log.info("잘못된 요청! id: {}, board: {}", id, board.toString());
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        // 4: 업데이트 및 정상 응답(200)
        target.patch(board);
        Board updated = boardRepository.save(target);
        return ResponseEntity.status(HttpStatus.OK).body(updated);
    }
    // DELETE

    @DeleteMapping("/api/board/{id}")
    public ResponseEntity<Board> delete(@PathVariable Long id) {
        // 대상 찾기
        Board target = boardRepository.findById(id).orElse(null);

        // 잘못된 요청 처리
        if (target == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }

        // 대상 삭제
        boardRepository.delete(target);
        return ResponseEntity.status(HttpStatus.OK).build();
    }
}

