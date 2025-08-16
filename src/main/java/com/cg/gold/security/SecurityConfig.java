package com.cg.gold.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

	private final JwtRequestFilter jwtRequestFilter;
	private final JwtAuthenticationEntryPoint authenticationEntryPoint;
	private final JwtAccessDeniedHandler accessDeniedHandler;

	public SecurityConfig(@Lazy JwtRequestFilter jwtRequestFilter, JwtAuthenticationEntryPoint authenticationEntryPoint,
			JwtAccessDeniedHandler accessDeniedHandler) {
		this.jwtRequestFilter = jwtRequestFilter;
		this.authenticationEntryPoint = authenticationEntryPoint;
		this.accessDeniedHandler = accessDeniedHandler;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public UserDetailsService userDetailsService() {
		UserDetails user = User.builder().username("user").password(passwordEncoder().encode("password")).roles("USER")
				.build();

		UserDetails admin = User.builder().username("admin").password(passwordEncoder().encode("adminpass"))
				.roles("ADMIN").build();

		UserDetails vendor = User.builder().username("vendor").password(passwordEncoder().encode("vendorpass"))
				.roles("VENDOR").build();

		return new InMemoryUserDetailsManager(user, admin, vendor);
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
		return configuration.getAuthenticationManager();
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http.csrf(csrf -> csrf.disable())
				.exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint)
						.accessDeniedHandler(accessDeniedHandler))
				.authorizeHttpRequests(auth -> auth.requestMatchers("/authenticate").permitAll()

						// USER-SPECIFIC ACCESS
						.requestMatchers(HttpMethod.GET, "/api/v2/users/check_balance/**",
								"/api/v2/users/*/virtual_gold_holdings", "/api/v2/users/*/physical_gold_holding",
								"/api/v2/users/*/transaction_history", "/api/v2/users/*/payments",
								"/api/v2/virtual_gold_holding/users/**",
								"/api/v2/physical_gold_transactions/by_user/**", "/api/v2/payments/by_user/**",
								"/api/v2/transaction_history/by_user/**", "/api/v2/vendor", "/api/v2/vendor_branches")
						.hasRole("USER")

						.requestMatchers(HttpMethod.PUT, "/api/v2/users/update/**", "/api/v2/users/*/update_address/**")
						.hasRole("USER")

						.requestMatchers(HttpMethod.POST, "/api/v2/virtual_gold_holding/convertToPhysical/**")
						.hasRole("USER")

						// VENDOR ACCESS
						.requestMatchers("/api/v2/vendor/**", "/api/v2/vendor_branches/**",
								"/api/v2/virtual_gold_holding/byUserAndVendor/**")
						.hasRole("VENDOR")

						// ADMIN-ONLY ACCESS
						.requestMatchers("/api/v2/users", "/api/v2/vendor", "/api/v2/vendor_branches",
								"/api/v2/users/**", "/api/v2/address/**", "/api/v2/virtual_gold_holding/**",
								"/api/v2/physical_gold_transactions/**", "/api/v2/payments/**",
								"/api/v2/transaction_history/**")
						.hasRole("ADMIN")

						// ANYONE WITH A VALID TOKEN
						.anyRequest().authenticated())
				.sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);

		return http.build();
	}

}
