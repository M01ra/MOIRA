package MakeUs.Moira.config.security;



import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.GenericFilterBean;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;


public class JwtAuthenticationFilter extends GenericFilterBean {

    private final JwtTokenProvider jwtTokenProvider;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider) {
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // Request로 들어오는 Jwt Token의 유효성을 검증하는 filter를 filterChain에 등록합니다.
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
//    @Override
//    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
//
//        // 1. 요청의 헤더에서 토큰을 가져온다.
//        String token = jwtTokenProvider.resolveToken((HttpServletRequest) request);
//
//        try {
//            // 1. 헤더에 토큰 없으면 예외 처리
//            // 2. token의 signature 체크 + 유효기간 설정했는지 체크 -> 이후, 토큰이 만료되었으면 예외 처리
//            // 3. 토큰이 유효한데 권한이 딸리는 경우 -> 권한거부로 알아서 처리
//            if (token == null) throw new NoTokenRequestException();
//            if (!jwtTokenProvider.isValidatedToken(token)) throw new ExpiredTokenException();
//
//            // Authentication 객체에 인증정보를 넣어준다.
//            // SecurityContextHolder에 Authentication을 등록한다.
//            Authentication auth = jwtTokenProvider.getAuthentication(token);
//            SecurityContextHolder.getContext().setAuthentication(auth);
//
//            // 실행.
//            filterChain.doFilter(request, response);
//
//        } catch (NoTokenRequestException e) {
//            request.setAttribute("exception", CustomJwtException.NO_TOKEN_REQUEST_ERROR.getCode());
//        } catch (UnsupportedJwtException e) {
//            request.setAttribute("exception", CustomJwtException.INVALID_SIGN_TOKEN_ERROR.getCode());
//        } catch (NullPointerException e) {
//            request.setAttribute("exception", CustomJwtException.NO_EXPIRATION_ERROR.getCode());
//        } catch (ExpiredTokenException e) {
//            request.setAttribute("exception", CustomJwtException.EXPIRED_TOKEN_ERROR.getCode());
//        }
