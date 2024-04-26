package com.JNSample.dto;

import com.JNSample.entity.Board;
import lombok.*;

@AllArgsConstructor
@ToString
@Setter
@NoArgsConstructor
public class BoardForm {
    private Long id;
    private String title;
    private String content;
    public Board toEntity() {
        return new Board(id, title, content);
    }
}
