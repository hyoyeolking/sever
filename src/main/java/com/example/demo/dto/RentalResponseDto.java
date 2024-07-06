package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.entity.Rental;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class RentalResponseDto {
    // Rental
    private Long rentalId;
    private String is_return;
    private LocalDateTime returnedDate;
    private LocalDate dueDate;
    private LocalDateTime createdAt;

    public RentalResponseDto(Rental rental) {
        this.rentalId = getRentalId();
        this.is_return = getIs_return();
        this.returnedDate = getReturnedDate();
        this.dueDate = getDueDate();
        this.createdAt = getCreatedAt();
    }
}
