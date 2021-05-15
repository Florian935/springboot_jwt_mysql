package com.javainuse.controller;

import com.javainuse.model.ConnectedUser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/datas")
public class DataController {

	@RequestMapping({ "/hello" })
	public String firstPage() {
		final Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		return ((ConnectedUser) auth.getPrincipal()).getLastname();
	}

	@GetMapping("")
	public String getAllDatas() {
		return "Les datas";
	}

	@GetMapping("/{id}")
	public String getDataById(@PathVariable String id) {
		return new StringBuilder("Les données avec l'id ")
				.append(id).toString();
	}


}