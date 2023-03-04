package com.sparta.sweetterbe.handler;

import com.sparta.sweetterbe.dto.StatusResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.naming.AuthenticationException;
import javax.persistence.EntityNotFoundException;
import java.nio.file.AccessDeniedException;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public StatusResponseDto<?> handlerException(Exception ex){
        return StatusResponseDto.fail(500, ex.getMessage());
    }

    @ExceptionHandler({IllegalAccessException.class,
            NullPointerException.class,
            UsernameNotFoundException.class,
            AuthenticationException.class,
            EntityNotFoundException.class,
            AccessDeniedException.class
    })
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public StatusResponseDto<?> handle(Exception ex){
        return StatusResponseDto.fail(400, ex.getMessage());
    }


}
