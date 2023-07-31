// 이 SecurityConfig 클래스를 통해 웹 사이트에 인증 및 인가를 지원하며, 비밀번호를 암호화하고 JWT를 사용하여 인증을 처리합니다.
// 웹 사이트의 보안 설정을 구현할 때 이 클래스를 사용하여 필요한 기능을 추가할 수 있습니다.
package com.ASAF.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

// 어노테이션을 사용하여 Java-based 설정 파일임을 적용하고, 웹용 Spring Security 기능과 전역적인 메소드 레벨 보안을 켭니다.
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    // 생성자를 통해 UserDetailsService 및 JwtTokenProvider에 대한 의존성을 주입합니다. 이 두 인스턴스는 인증 작업에 사용됩니다.
    private final UserDetailsService userDetailsService;
    private final JwtTokenProvider jwtTokenProvider;

    @Autowired
    public SecurityConfig(UserDetailsService userDetailsService, JwtTokenProvider jwtTokenProvider) {
        this.userDetailsService = userDetailsService;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    // PasswordEncoder 및 AuthenticationManager의 Bean을 정의합니다.
    // 여기서는 BCryptPasswordEncoder를 사용하여 비밀번호를 인코딩하고, 슈퍼클래스의 authenticationManagerBean 메서드를 재정의하여 AuthenticationManager를 생성합니다
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    // AuthenticationManagerBuilder를 통해 인증 작업을 구성합니다. UserDetailsService와 PasswordEncoder를 사용하여 사용자 정보를 생성하고 비밀번호를 인코딩합니다
    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    // HttpSecurity 객체를 통해 보안 설정을 구성합니다. 이 설정에는 각 요청에서 다음 동작이 포함됩니다.
    //CSRF protection 비활성화
    //미리 정의된 경로(/login, /register, /js/**, /css/**, /img/**)로 들어온 모든 요청에 대해 허용
    //위므로 제외한 모든 요청에 대해 인증 필요
    //사용자가 로그인할 수 있는 /login 페이지 등록
    //로그인 성공 시, JwtTokenProvider를 사용하여 JWT 토큰 생성하고 응답 헤더에 토큰 추가
    //로그아웃 기능 활성화

    // 오버라이딩된 configure(HttpSecurity http) 메서드에 JwtFilter를 추가하고 JwtAuthenticationEntryPoint를 설정하세요.
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .csrf().disable()
                .authorizeRequests()
                .antMatchers("/api/auth/**","/member","api/auth/signup").permitAll()
                // 인증이 필요한 API 경로 추가
                .antMatchers("/bus/**").authenticated()
//                .anyRequest().authenticated()
                .and()
                .exceptionHandling()
                .authenticationEntryPoint(new JwtAuthenticationEntryPoint())
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .addFilterBefore(new JwtFilter(jwtTokenProvider), UsernamePasswordAuthenticationFilter.class);
    }
}
