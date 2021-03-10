package MakeUs.Moira.config.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

//    // Jwt Provier 주입
//    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
//        this.jwtTokenProvider = jwtTokenProvider;
//    }

    // Request로 들어오는 Jwt Token의 유효성을 검증(jwtTokenProvider.validateToken)하는 filter를 filterChain에 등록합니다.
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {

        // 1. 요청의 헤더에서 토큰을 가져온다.
        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);

        // 2. 토큰이 null이 아님 + 토큰이 유효한경유
        if (token != null && jwtTokenProvider.isValidatedToken(token)) {
            // Authentication 객체에 인증정보를 넣어준다.
            // SecurityContextHolder에 Authentication을 등록한다.
            Authentication auth = jwtTokenProvider.getAuthentication(token);
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

        // 3. 실행.
        filterChain.doFilter(request, response);
    }
}
