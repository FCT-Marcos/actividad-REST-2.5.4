package com.marcos.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

import com.marcos.config.filter.JwtTokenValidator;
import com.marcos.util.JwtUtils;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity, JwtUtils jwtUtils) throws Exception {
		return httpSecurity.csrf(csrf -> csrf.disable())
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.authorizeHttpRequests(http -> {

					// Publico
					http.requestMatchers(HttpMethod.POST, "/api/v1/auth/**").permitAll();
					http.requestMatchers(HttpMethod.GET, "/api/v1/page/public").permitAll();
					
					// Protegido
					http.requestMatchers(HttpMethod.GET, "/api/v1/page/protected").authenticated();

					// Los enpoints no definidos denegar el acceso
					http.anyRequest().denyAll();
				})
		         .exceptionHandling(exception -> exception
		         	.authenticationEntryPoint((request, response, authException) -> {
		         		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, authException.getMessage());
		         	})
		         )
				.addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
				.build();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

	@Bean
	public InMemoryUserDetailsManager getUserDetailsManager() {
		return new InMemoryUserDetailsManager();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}