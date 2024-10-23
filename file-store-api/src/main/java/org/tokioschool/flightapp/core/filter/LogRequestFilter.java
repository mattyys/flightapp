package org.tokioschool.flightapp.core.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.MDC;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Optional;

@Component
@Slf4j
@Order(value = Ordered.LOWEST_PRECEDENCE)//se marca ela prioridad como el ultimo filtro a aplicar
public class LogRequestFilter extends OncePerRequestFilter {
  @Override
  protected void doFilterInternal(
      HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
      throws ServletException, IOException {

    long start = System.currentTimeMillis();

    considerSecurity();

    try {
      filterChain.doFilter(request, response);
    } finally {
      long timeInMs = System.currentTimeMillis() - start;
      log.info(
          "Request: time.ms:{} method:{} status:{} path:{}",
          timeInMs,
          request.getMethod(),
          response.getStatus(),
          getRequestPath(request));
    }
  }

  protected void considerSecurity() {
    if (SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
      MDC.put("user-ID", SecurityContextHolder.getContext().getAuthentication().getName());
    }
  }

  private String getRequestPath(HttpServletRequest request) {
    StringBuilder path = new StringBuilder(request.getRequestURI());
    Optional.ofNullable(request.getQueryString()).ifPresent(qs -> path.append("?").append(qs));
    return path.toString();
  }
}
