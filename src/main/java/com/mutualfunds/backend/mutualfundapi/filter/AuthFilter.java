package com.mutualfunds.backend.mutualfundapi.filter;

import com.mutualfunds.backend.mutualfundapi.pojo.entity.User;
import com.mutualfunds.backend.mutualfundapi.services.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

@Component
@Order(1)
@Slf4j
@RequiredArgsConstructor
public class AuthFilter implements Filter {

    private static final String PHONE_NUMBER_HEADER = "phoneNumber";

    private final UserService userService;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        Filter.super.init(filterConfig);
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String phoneNumber = httpServletRequest.getHeader(PHONE_NUMBER_HEADER);
        userService
                .getUserWithPhoneNumber(Long.valueOf(phoneNumber))
                .orElse(userService.createRandomUserWithPhoneNumber(Long.valueOf(phoneNumber)));
        filterChain.doFilter(servletRequest, servletResponse);
    }

    @Override
    public void destroy() {
        Filter.super.destroy();
    }
}
