package com.unimi.apiserver.resource;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
public class RestResource
{
	@RequestMapping("/api/users/me")
	public ResponseEntity<UserProfile> profile()
	{
		User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String email = user.getUsername() + "@mailserver.com";
		UserProfile profile = new UserProfile();
		profile.setName(user.getUsername());
		profile.setEmail(email);
		GrantedAuthority adminAuthority = (GrantedAuthority) () -> "ROLE_ADMIN";
		if (user.getAuthorities().contains(adminAuthority)) {
			List<Ordine> ordini = profile.getOrdiniDisponibili();
			ordini.add(new Ordine("4C", 'A'));
			profile.setOrdiniDisponibili(ordini);
		}
		return ResponseEntity.ok(profile);
	}
}
