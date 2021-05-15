package com.javainuse.service;

import com.javainuse.dao.UserDao;
import com.javainuse.model.ConnectedUser;
import com.javainuse.model.DAOUser;
import com.javainuse.model.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
	
	@Autowired
	private UserDao userDao;

	// Obliger d'autowired un PasswordEncoder pour que DaoAuthenticationProvider puisse l'utiliser lors de l'authentification
	// pour vérifier le mdp en bdd encrypté avec le mdp fourni lors de l'authentification. Ainsi, DaoAuthenticationProvider
	// va savoir comment décrypter le mdp en bdd pour la comparaison du mdp en bdd et le mdp fourni dans la requête.
	@Autowired
	private PasswordEncoder passwordEncoder;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		Optional<DAOUser> user = userDao.findByUsername(username);
		user.orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));
		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority("ADMIN"));
		return user.map(u -> new ConnectedUser(u, authorities)).get();
	}
}