package com.javainuse.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Order(1)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

	@Autowired
	private JwtRequestFilter jwtRequestFilter;

//		@Bean
//		@Override
//		public AuthenticationManager authenticationManagerBean() throws Exception {
//			return super.authenticationManagerBean();
//		}

	@Bean
	public AuthenticationManager customAuthenticationManager() throws Exception {
		return authenticationManager();
	}

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		// We don't need CSRF for this example
		httpSecurity.csrf().disable()
				// dont authenticate this particular request
				.authorizeRequests()
				// 2 exemples différents d'autorisation d'URL:
				// 		-le premier en commentée permet d'être précis sur les URL à autoriser pour tout le monde.
//						-le second permet d'être plus général sur l'autorisation des URL et moins verbeux
//				.antMatchers("/api/auth/authenticate", "/**/register").permitAll()
				.antMatchers("/api/auth/**").permitAll()
				.antMatchers("/api/actuator/**").permitAll()
				.antMatchers("/api/health").permitAll()
				// 2 exemples ici encore:
//						-Soit on décrit le path avec le wildcard qui permet de dire que toutes les requêtes commençant
//				par "/api/ auront besoins d'une authentication
//						-Soit on indique, avec anyRequest(), que toutes les URL, autres que "/api/auth", ont besoin d'une
//				authentification.
				.antMatchers("/api/datas/hello").authenticated()
				// all other requests need to be authenticated
//				.anyRequest().authenticated().and()

				// hasAnyAuthority: permet de contraindre l'accès aux utilisateurs étant authentifié +
				// ayant au moins un des rôles spécifiés.
				.antMatchers("/api/**").hasAnyAuthority("ADMIN", "USER").and()

				// make sure we use stateless session; session won't be used to
				// store user's state.
				.exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		;

		// Add a filter to validate the tokens with every request
		// addFilterBefore(filter, class): add a filter before the position of the specified filter class (second parameter)
		// So here, we add our filter before the authenticationFilter: all the URL who need authentication (URI with
		// .authenticated() method), will need to validate our custom jwtRequestFilter before access the URL. If the filter
		// is validate, the SecurityContext is setted with the authenticated user and can access to the URI, else, if the
		// filter isn't validated, the user can't access to the URI because the SecurityContext isn't setted and the API
		// return the 401 status (unauthorized).
		// A noter: si URI n'a pas .authenticated(), elle ne sera pas bloquée par notre filtre (pas de status 401)
		httpSecurity.addFilterBefore(jwtRequestFilter, UsernamePasswordAuthenticationFilter.class);
	}
}