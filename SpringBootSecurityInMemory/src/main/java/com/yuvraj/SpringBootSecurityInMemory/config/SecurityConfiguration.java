package com.yuvraj.SpringBootSecurityInMemory.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractAuthenticationFilterConfigurer;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		return httpSecurity.authorizeHttpRequests(registry->{
			registry.requestMatchers("/openForAll").permitAll();
			registry.requestMatchers("/admin/**").hasRole("ADMIN");
			registry.requestMatchers("/user/**").hasRole("USER");
			registry.anyRequest().authenticated();
			}	
		).formLogin(AbstractAuthenticationFilterConfigurer::permitAll)
		 .build();
	}
	
	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails normalUser = User.builder() // org.springframework.security.core.userdetails.User;
				.username("user1")
				.password(passwordEncoder().encode("123"))
				.roles("USER")
				.build();
		UserDetails adminUser = User.builder()
				.username("admin")
				.password(passwordEncoder().encode("456"))
				.roles("USER","ADMIN")
				.build();
		return new InMemoryUserDetailsManager(normalUser, adminUser);
	}
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
