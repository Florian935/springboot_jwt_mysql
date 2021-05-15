package com.javainuse.controller;

import com.javainuse.config.JwtRequestFilter;
import com.javainuse.config.JwtTokenUtil;
import com.javainuse.model.JwtRequest;
import com.javainuse.model.JwtResponse;
import com.javainuse.model.UserDTO;
import com.javainuse.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/api/auth")
public class JwtAuthenticationController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	@Autowired
	private UserDetailsService userDetailsService;

	@Autowired
	private UserService userService;

	@RequestMapping(value = "/authenticate", method = RequestMethod.POST)
	public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
//		An Authentication implementation that is designed for simple presentation of a username and password.
		UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken =
				new UsernamePasswordAuthenticationToken(
						authenticationRequest.getUsername(), authenticationRequest.getPassword()
		);
//		Tente d'authentifier l'utilisateur en vérifiant en base de données le login/password. Cela se fait grâce aux
//		ProviderManager et notamment grâce à l'implémentation de l'interface UserDetailsService avec la méthode
//		loadUserByUsername.
//		Si l'authenth est réussie, un objet de type UsernamePasswordAuthenticationToken est retourné. La propriété
//		"principal" contient un objet de type ConnectedUser (implémente User) qui contient lui-même les différentes propriétés
//		utiles à utiliser (username, lastname etc). Le password n'est pas setter par spring dans l'objet retourné (question de securité).
		/*Authentication authentication =*/ authenticationManager.authenticate(usernamePasswordAuthenticationToken);

//		Le setting de l'authentication dans le container Security de Spring est à effectuer pendant l'authentication
//		uniquement si on a besoin de l'utilisateur connecté autre part dans l'application, sinon, aucun intérêt, l'authentication
//		ne sera utilisé nul part.
//		En revanche, l'authentication est setter dans le contexte par le filtre sur les requêtes dans le cas ou l'username
//		est récupéré dans le token. Dans ce cas là, on set l'authentication et on peut s'en servir partout dans l'appli
//		en récupérant l'utilisateur connecté dans le Security Container de Spring (utile par exemple pour de l'audit).
//		SecurityContextHolder.getContext().setAuthentication(authentication);
//		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//		String lastname = ((ConnectedUser) authentication.getPrincipal()).getLastname();


		final UserDetails userDetails = userDetailsService
				.loadUserByUsername(authenticationRequest.getUsername());

		final String token = jwtTokenUtil.generateToken(userDetails);

		return ResponseEntity.ok(new JwtResponse(token));
	}
	
	@RequestMapping(value = "/register", method = RequestMethod.POST)
	public ResponseEntity<?> saveUser(@RequestBody UserDTO user) throws Exception {
		return ResponseEntity.ok(userService.save(user));
	}
}