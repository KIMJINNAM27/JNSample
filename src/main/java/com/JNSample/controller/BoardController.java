package com.JNSample.controller;

import com.JNSample.dto.BoardForm;
import com.JNSample.entity.Board;
import com.JNSample.repository.BoardRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@Slf4j
public class BoardController {

    @Autowired
    private BoardRepository boardRepository;

    @GetMapping("/board/new")
    public String newBoardForm () {
        return "board/new";
    }

    @PostMapping("/board/create")
    public String createBoardForm (BoardForm form) {

        log.info(form.toString());

        // 1. DTO to Entity
        Board board = form.toEntity();
        log.info(board.toString());


        // 2. Repository 에게 Entity를 저장하게 명령
        Board saved = boardRepository.save(board);
        log.info(saved.toString());
        return "redirect:/board/" + saved.getId();
    }

    @GetMapping("/board/{id}")
    public String show(@PathVariable Long id, Model model) {
        log.info("id = " + id);

        // 1: id로 데이터를 가져옴
        Board boardEntity = boardRepository.findById(id).orElse(null);

        log.info(boardEntity.toString());

        // 2: 가져온 데이터를 모델에 등록
        model.addAttribute("board", boardEntity);

        log.info(model.toString());

        // 3: 보여줄 페이지 설정
        return "board/show";
    }

    @GetMapping("/board")
    public String index(Model model) {
        List<Board> boardEntityList  = boardRepository.findAll();
        model.addAttribute("boardList", boardEntityList);
        return "board/index";

    }

    @GetMapping("/board/{id}/edit")
    public String edit(@PathVariable Long id, Model model){

        // 수정할 데이터 가져오기
       Board boardEntity = boardRepository.findById(id).orElse(null);

       model.addAttribute("board", boardEntity);

       return "board/edit";
    }

    @PostMapping("/board/update")
    public String update(BoardForm form) {
        log.info(form.toString());

       Board boardEntity= form.toEntity();

       Board target = boardRepository.findById(boardEntity.getId()).orElse(null);

       if (target != null) {
           boardRepository.save(boardEntity);
       }
        return "redirect:/board/" + boardEntity.getId();

    }

    @GetMapping("/board/{id}/delete")
    public String delete(@PathVariable Long id, RedirectAttributes rttr) {

        Board target = boardRepository.findById(id).orElse(null);

        if (target != null) {
            boardRepository.delete(target);
            rttr.addFlashAttribute("msg", "삭제가 완료되었습니다!");
        }
        return "redirect:/board";
    }
}
