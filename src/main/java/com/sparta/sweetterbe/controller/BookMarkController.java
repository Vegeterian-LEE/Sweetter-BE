package com.sparta.sweetterbe.controller;

import com.sparta.sweetterbe.dto.BookMarkResponseDto;
import com.sparta.sweetterbe.dto.StatusResponseDto;
import com.sparta.sweetterbe.security.UserDetailsImpl;
import com.sparta.sweetterbe.service.BookMarkService;
import io.swagger.v3.oas.annotations.Parameter;
import lombok.RequiredArgsConstructor;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mark")
public class BookMarkController {
    private final BookMarkService bookMarkService;
    @PostMapping("/toggle/{postId}")
    public StatusResponseDto<BookMarkResponseDto> toggleMark(
            @PathVariable Long postId,
            @Parameter(hidden = true) @AuthenticationPrincipal UserDetailsImpl userDetails
    ) {
        return StatusResponseDto.success(bookMarkService.toggleMark(postId, userDetails));
    }
}
