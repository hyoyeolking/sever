package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;

@Getter
@Setter
public class RentalSearchResponseDto {
    private String name;
    private String phoneNumber;
    private String title;
    private String writer;

    public RentalSearchResponseDto(Member member, Book book) {
        this.name = member.getName();
        this.phoneNumber = member.getPhoneNumber();
        this.title = book.getTitle();
        this.writer = book.getWriter();
    }
}
