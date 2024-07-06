package com.example.demo;

import lombok.RequiredArgsConstructor;
import com.example.demo.dto.*;
import com.example.demo.entity.Book;
import com.example.demo.entity.Member;
import com.example.demo.entity.Rental;
import com.example.demo.repository.BookRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.RentalRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final MemberRepository memberRepository;
    private final RentalRepository rentalRepository;

    // 도서 등록 기능
    public BookResponseDto createBook(BookRequestDto bookRequestDto) {
        // RequestDto -> Entity
        Book book = new Book(bookRequestDto);

        // DB에 저장
        Book saveBook = bookRepository.save(book);

        // Entity -> ResponseDto
        BookResponseDto bookResponseDto = new BookResponseDto(book);

        return bookResponseDto;
    }

    @Transactional
    // 선택한 도서 정보 조회 기능
    public BookResponseDto updateBook(Long bookId) {

        // 해당 도서가 book DB에 존재하는지 확인
        Book book = findBook(bookId);

        return new BookResponseDto(book);
    }

    // 도서 목록 조회 기능
    public List<BookResponseDto> getBook() {
        // DB 조회
        return bookRepository.findAllByOrderByCreatedAtAsc()
                .stream()
                .map(BookResponseDto::new).toList();
    }


    // 도서관 회원 등록 기능
    public MemberResponseDto createMember(MemberRequestDto memberRequestDto) {

        // RequestDto → Entity
        Member member = new Member(memberRequestDto);

        // DB에 저장
        Member saveMember = memberRepository.save(member);

        // Entity → ResponseDto
        MemberResponseDto memberResponseDto = new MemberResponseDto(member);

        return memberResponseDto;
    }

    // 대출 등록 기능
    @Transactional
    public RentalResponseDto createRental(RentalRequestDto rentalRequestDto) {
        Member member = memberRepository.findById(rentalRequestDto.getMemberId()).orElseThrow(() ->
                new IllegalArgumentException("선택한 회원은 존재하지 않습니다.")
        );

        Book book = bookRepository.findById(rentalRequestDto.getBookId()).orElseThrow(() ->
                new IllegalArgumentException("선택한 책은 존재하지 않습니다.")
        );

        Rental rental = new Rental(rentalRequestDto, member, book);
        rentalRepository.save(rental);

        return new RentalResponseDto(rental);
    }

    // 도서 대출 목록 조회 기능
    public List<RentalSearchResponseDto> getRentalList(Long memberId, List<Long> longList) {
        List<RentalSearchResponseDto> list = new ArrayList<>();

        Member member = memberRepository.findById(memberId).orElseThrow(() ->
                new IllegalArgumentException("선택한 회원은 존재하지 않습니다.")
        );

        for (int i = 0; i < longList.size(); i++) {
            Book book = bookRepository.findById(longList.get(i)).orElseThrow(() ->
                    new IllegalArgumentException("선택한 책은 존재하지 않습니다.")
            );
            RentalSearchResponseDto responseDto = new RentalSearchResponseDto(member, book);
            list.add(responseDto);
        }

        return list;
    }


//-----------------------------------메 서 드-----------------------------------------

    // 도서 조회 findBook 메서드
    private Book findBook(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new IllegalArgumentException("선택한 책은 존재하지 않습니다.")
        );
    }

    // 선택한 도서 대출 기능 - 회원 여부 확인 메서드
    private boolean isMember(Long memberId) {
        if (memberId == null) {
            throw new IllegalArgumentException("해당 아이디가 존재하지 않습니다");
            // ID가 null이 아닌 경우에 수행할 로직
        }
        return memberRepository.existsById(memberId);
    }

    // 선택한 도서 대출 기능 - 반납하지 않은 책 확인 유무 메서드
    public boolean hasBooks(Long memberId) {
        // 회원이 대출한 도서 목록을 조회합니다.
        List<Rental> rentalsForMember = rentalRepository.findByMemberId(memberId);

        // 대출된 도서 중 반납되지 않은 도서가 있는지 확인합니다.
        for (Rental rental : rentalsForMember) {
            if (!rental.isAvailable()) {
                // 반납되지 않은 도서가 있으면 true를 반환합니다.
                return true;
            }
        }
        // 반납되지 않은 도서가 없으면 false를 반환합니다.
        return false;
    }

    private Rental findRental(Long rentalId) {
        return rentalRepository.findById(rentalId).orElseThrow(() ->
                new IllegalArgumentException("선택한 책은 존재하지 않습니다.")
        );
    }

    // 회원이 대출한 책의 ID들을 조회하는 메서드
    public List<Long> findBookIdsByMemberId(Long memberId) {
        return rentalRepository.findBookIdsByMemberId(memberId);
    }
}
