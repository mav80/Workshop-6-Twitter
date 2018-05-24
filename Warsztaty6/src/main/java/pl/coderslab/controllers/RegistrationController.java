package pl.coderslab.controllers;

import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pl.coderslab.entities.User;
import pl.coderslab.repositories.UserRepository;

@Controller
public class RegistrationController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		
		return "userForm";
	}
	
	@PostMapping("/register")
	public String register(@Valid User user, BindingResult result) {
		
		if(result.hasErrors()) {
			return "userForm";
		}
		
		//zaszygrować i zapisać hasło
		user.setPassword(BCrypt.hashpw(user.getPassword(),  BCrypt.gensalt()));
		user.setEnabled(true);
		userRepository.save(user);
		
		return "success";
		
	}
	
	

}
