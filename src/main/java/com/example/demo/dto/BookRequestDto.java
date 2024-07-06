package com.example.demo.dto;

import lombok.Getter;

@Getter
public class BookRequestDto {

    // Book
    private Long bookId;
    private String title;
    private String writer;
    private String language;
    private String publisher;
}