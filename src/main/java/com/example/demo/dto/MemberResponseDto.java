package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;
import com.example.demo.entity.Member;

@Getter
@Setter
public class MemberResponseDto {
    // Member
    private Long memberId;
    private String name;
    private String gender;
    //    private String residentRegistrationNumber;
    private String phoneNumber;
    private String address;

    public MemberResponseDto(Member member) { // Service : Entity → ResponseDto

        this.memberId = member.getMemberId();
        this.name = member.getName();
        this.gender = member.getGender();
//        this.residentRegistrationNumber = member.getResidentRegistrationNumber();
        this.phoneNumber = member.getPhoneNumber();
        this.address = member.getAddress();


    }
}
