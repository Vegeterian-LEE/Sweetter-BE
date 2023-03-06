package com.sparta.sweetterbe.config;



import com.sparta.sweetterbe.jwt.JwtAuthFilter;
import com.sparta.sweetterbe.jwt.JwtUtil;
import com.sparta.sweetterbe.security.CustomAccessDeniedHandler;
import com.sparta.sweetterbe.security.CustomAuthenticationEntryPoint;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.CorsUtils;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class WebSecurityConfig implements WebMvcConfigurer {

    private final JwtUtil jwtUtil;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {

        return (web) -> web.ignoring()
                //.requestMatchers(PathRequest.toH2Console())
                .antMatchers("/swagger-ui/**", "/v3/api-docs/**")
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations());
    }
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable();

        //로그인 된 후 토큰없이 자동 인증되는 것을 방지
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);

        //회원가입, 로그인,조회까지는 security 인증 없이도 가능함
        http.authorizeRequests()
                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll()
                .antMatchers("/docs").permitAll()
                .antMatchers("/api/user/signup").permitAll()
                .antMatchers("/api/user/login").permitAll()
               // .antMatchers(HttpMethod.GET, "/api/studies/**").permitAll()
                .anyRequest().authenticated()

                // JWT 인증/인가를 사용하기 위한 설정
                .and()
                .addFilterBefore(new JwtAuthFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);

        //Controller 단 전에 시큐리티에서 검사하므로 따로 Exceptionhandler가 필요하다
        http.exceptionHandling().authenticationEntryPoint(customAuthenticationEntryPoint);
        http.exceptionHandling().accessDeniedHandler(customAccessDeniedHandler);
        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {

        CorsConfiguration config = new CorsConfiguration();

        // 사전에 약속된 출처를 명시
        config.addAllowedOrigin("http://localhost:3000");
        config.addAllowedOrigin("http://api.jingyulee.shop/v3/api-docs");
        config.addAllowedOrigin("https://api.jingyulee.shop/v3/api-docs");
        config.addAllowedOrigin("https://viking-band.s3.ap-northeast-2.amazonaws.com/**");


        // 특정 헤더를 클라이언트 측에서 사용할 수 있게 지정
        // 만약 지정하지 않는다면, Authorization 헤더 내의 토큰 값을 사용할 수 없음
        config.addExposedHeader(jwtUtil.AUTHORIZATION_HEADER);

        // 본 요청에 허용할 HTTP method(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedMethod("*");

        // 본 요청에 허용할 HTTP header(예비 요청에 대한 응답 헤더에 추가됨)
        config.addAllowedHeader("*");

        // 기본적으로 브라우저에서 인증 관련 정보들을 요청 헤더에 담지 않음
        // 이 설정을 통해서 브라우저에서 인증 관련 정보들을 요청 헤더에 담을 수 있도록 해줍니다.
        config.setAllowCredentials(true);

        // allowCredentials 를 true로 하였을 때,
        // allowedOrigin의 값이 * (즉, 모두 허용)이 설정될 수 없도록 검증합니다.
        config.validateAllowCredentials();

        // 어떤 경로에 이 설정을 적용할 지 명시합니다. (여기서는 전체 경로)
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);

        return source;
    }
}
