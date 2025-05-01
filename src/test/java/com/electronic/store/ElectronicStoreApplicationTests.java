package com.electronic.store;

import com.electronic.store.Security.JwtHelper;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.entities.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class ElectronicStoreApplicationTests {

	@Test
	void contextLoads() {
	}

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JwtHelper jwtHelper;

	@Test
	void testToken(){
		User user=userRepository.findByEmail("pavitrapandey@gmail.com").get();
	String token = jwtHelper.generateToken(user);
		System.out.println("Token is "+token);
		System.out.println("Getting user from token "+jwtHelper.getUsernameFromToken(token));

	System.out.println("Is token expired "+jwtHelper.isTokenExpired(token));}

}
