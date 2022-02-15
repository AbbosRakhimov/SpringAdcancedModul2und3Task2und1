package uz.pdp.securityConfig;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.EnableGlobalAuthentication;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
				.inMemoryAuthentication()
				.withUser("superAdmin").password(passwordEncoder().encode("admin123")).roles("SUPER_ADMIN")
				.and()
				.withUser("moderator").password(passwordEncoder().encode("moderator123")).roles("MODERATOR ")
				.and()
				.withUser("operator").password(passwordEncoder().encode("operator123")).roles("OPERATOR ");
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
				.csrf().disable()
				.authorizeRequests()
//				.antMatchers(HttpMethod.GET, "/warehouse/**").hasAnyRole("WORKER","DIREKTOR","MANAGER")
//				.antMatchers(HttpMethod.GET, "/warehouse").hasAnyRole("DIREKTOR","MANAGER")
//				.antMatchers("/warehouse/**").hasRole("DIREKTOR")
				.anyRequest()
				.authenticated()
				.and()
				.httpBasic();
	}
	
	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
}
