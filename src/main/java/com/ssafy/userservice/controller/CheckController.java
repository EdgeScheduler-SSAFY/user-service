package com.ssafy.userservice.controller;

import jakarta.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.cfg.Environment;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@Slf4j
@RestController
@RequiredArgsConstructor
public class CheckController {

    /**
     * 사용자 인증 후에만 접근 가능한 API 엔드포인트 sample
     * access token 없이 혹은 유효하지 않은 access token 으로 접근하려면 에러 발생
     * @param request
     * @return
     */
    @GetMapping("/check")
    public String check(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());

        return String.format("Hi, there. This is a message from First Service on PORT %s"
            , request.getServerPort());
    }

    /**
     * 사용자 인증 필요 없이 접근 가능한 API 엔드포인트 sample
     * access token 없이 접근 가능
     * @param request
     * @return
     */
    @GetMapping("/uncheck")
    public String uncheck(HttpServletRequest request) {
        log.info("Server port={}", request.getServerPort());

        return String.format("Hi, there. This is a message from First Service on PORT %s"
            , request.getServerPort());
    }
}
