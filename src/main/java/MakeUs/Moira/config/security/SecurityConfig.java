package MakeUs.Moira.config.security;

import MakeUs.Moira.domain.user.UserRole;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;


@RequiredArgsConstructor
@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    private final JwtTokenProvider jwtTokenProvider;

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                // 기본 적인 설정
                .httpBasic().disable() // rest api 이므로 기본설정 사용안함. 기본설정은 비인증시 로그인폼 화면으로 리다이렉트 된다.
                .csrf().disable()
                .headers().frameOptions().disable()     // h2-console을 위해서 추가
                .and()// rest api이므로 csrf 보안이 필요없으므로 disable처리.
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS) // jwt token으로 인증하므로 세션은 필요없으므로 생성안함.

                // 다음 리퀘스트에 대한 사용권한 체크
                .and()
                    .authorizeRequests()
                        .antMatchers("/login","/profile", "/h2-console/**").permitAll() // 가입 및 local에서 h2는 누구나 접근가능
                        .antMatchers(HttpMethod.GET, "/exception/**").permitAll() // 얘네로 시작하는 GET요청 리소스는 누구나 접근가능
                        //.antMatchers("/*/users").hasRole("ADMIN")
                        .anyRequest().hasRole(UserRole.USER.name()) // 그외 나머지 요청은 모두 인증된 회원만 접근 가능
                        //.anyRequest().permitAll() // 그외 나머지 요청은 모두 인증된 회원만 접근 가능

                // accessDeniedHandler 추가
                .and()
                    .exceptionHandling().accessDeniedHandler(new CustomAccessDeniedHandler())

                // CustomAuthEntryPoint 설정 추가
                .and()
                    .exceptionHandling().authenticationEntryPoint(new CustomAuthEntryPoint())

                // jwt token 필터를 id/password 인증 필터(UsernamePasswordAuthenticationFilter) 전에 넣는다
                .and()
                    .addFilterBefore(new JwtAuthenticationFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);

    }

    // 스웨거 창은 제외시킨다.
    @Override
    public void configure(WebSecurity web) {
        web
                .ignoring()
                .antMatchers("/v2/api-docs", "/swagger-resources/**",
                "/swagger-ui.html", "/webjars/**", "/swagger/**");

    }
}
