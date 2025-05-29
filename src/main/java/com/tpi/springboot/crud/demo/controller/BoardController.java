package com.tpi.springboot.crud.demo.controller;

import com.tpi.springboot.crud.demo.domain.Board;
import com.tpi.springboot.crud.demo.dto.board.BoardDto;
import com.tpi.springboot.crud.demo.service.BoardService;
import org.springframework.web.bind.annotation.*;

@RestController
public class BoardController {
    private final BoardService boardService;

    public BoardController(BoardService boardService) {
        this.boardService = boardService;
    }

    @GetMapping("/boards/{id}") // value = <- 생략가능
    public Board getBoard(@PathVariable("id") Long id ) {
        return boardService.sampleBoard(id);
    }

    @PostMapping("/boards")
    public BoardDto createBoard(@RequestBody Board board) {
        return boardService.createBoard(board);
    }
}
