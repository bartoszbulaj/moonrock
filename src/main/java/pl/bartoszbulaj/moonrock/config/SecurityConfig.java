package pl.bartoszbulaj.moonrock.config;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	private DataSource dataSource;

	@Autowired
	public SecurityConfig(DataSource dataSource) {
		this.dataSource = dataSource;
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.csrf().disable().headers().frameOptions().disable()//
				.and()//
				.authorizeRequests()//
				.antMatchers("/h2-console/*").hasRole("ADMIN")//
				// TODO below line should be like: .antMatchers("/").hasRole("ADMIN")
				.antMatchers("/").permitAll()//
				// TODO uncomment below
				// .anyRequest().authenticated()
				.and()//
				.formLogin().permitAll()//
				.loginPage("/login")//
				.failureUrl("/login-error")//
				.defaultSuccessUrl("/")//
				.and()//
				.logout()//
				.logoutSuccessUrl("/login");
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.jdbcAuthentication().dataSource(dataSource).passwordEncoder(passwordEncoder())
				.usersByUsernameQuery("SELECT username, password, active FROM users WHERE username = ?")
				.authoritiesByUsernameQuery("SELECT username, role FROM users WHERE username = ?");
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return PasswordEncoderFactories.createDelegatingPasswordEncoder();
	}
}
