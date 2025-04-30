package com.electronic.store;

import com.electronic.store.Repository.RoleRepository;
import com.electronic.store.Repository.UserRepository;
import com.electronic.store.entities.Roles;
import com.electronic.store.entities.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class ElectronicStoreApplication implements CommandLineRunner {

	@Autowired
	private RoleRepository roleRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEncoder;


	public static void main(String[] args) {
		SpringApplication.run(ElectronicStoreApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Roles admin = roleRepository.findByName("ROLE_ADMIN").orElse(null);
		if (admin == null) {
			Roles role = new Roles();
			role.setRoleId(UUID.randomUUID().toString());
			role.setName("ROLE_ADMIN");
			roleRepository.save(role);
		}

		Roles normal = roleRepository.findByName("ROLE_NORMAL").orElse(null);
		if (normal == null) {
			Roles role = new Roles();
			role.setRoleId(UUID.randomUUID().toString());
			role.setName("ROLE_NORMAL");
			roleRepository.save(role);
		}

		User user=userRepository.findByEmail("pavitra@gmail.com").orElse(null);

		if(user==null) {
			User user1 = new User();
			user1.setUserId(UUID.randomUUID().toString());
			user1.setName("Pavitra");
			user1.setEmail("pavitra@gmail.com");
			user1.setPassword(passwordEncoder.encode("pavitra"));
			user1.setRoles(List.of(admin));
			user1.setAbout("I am the Admin");
			user1.setGender("Male");

			userRepository.save(user1);
		}
	}
}
