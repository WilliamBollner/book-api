package com.furb.bookapi.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.furb.bookapi.model.user.AuthDTO;
import com.furb.bookapi.model.user.LoginResponseDTO;
import com.furb.bookapi.model.user.User;
import com.furb.bookapi.repository.UserRepository;
import com.furb.bookapi.security.TokenService;

import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

	@Autowired
	private UserRepository userRepository;
	@Autowired
	private TokenService tokenService;

	@PostMapping("/login")
	public ResponseEntity<LoginResponseDTO> login(@RequestBody @Validated AuthDTO data) {
		var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
		var auth = this.authenticationManager.authenticate(usernamePassword);
		var token = tokenService.gerenateToken((User) auth.getPrincipal());

		return ResponseEntity.ok(new LoginResponseDTO(token));
	}

	@PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody @Validated User user) {
		if (this.userRepository.findByUsername(user.getUsername()) != null)
			return ResponseEntity.badRequest().build();

		String encryptedPassword = new BCryptPasswordEncoder().encode(user.getPassword());

		this.userRepository.save(new User(user.getUsername(), encryptedPassword));

		return ResponseEntity.ok().build();
	}

	@PostMapping("/logout")
	public ResponseEntity<String> logout(HttpServletRequest request) {
		return ResponseEntity.ok("Logged out successfully");
	}

}
