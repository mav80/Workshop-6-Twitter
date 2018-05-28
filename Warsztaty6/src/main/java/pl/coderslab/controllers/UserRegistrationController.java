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
public class UserRegistrationController {
	
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		
		return "userRegisterForm";
	}
	
	@PostMapping("/register")
	public String register(@Valid User user, BindingResult result, Model model) {
		
		if(result.hasErrors() || userRepository.findFirstByEmail(user.getEmail()) != null || userRepository.findFirstByUsername(user.getUsername()) != null) {
			
			if(userRepository.findFirstByEmail(user.getEmail()) != null) {
				model.addAttribute("error", "Taki email już istnieje w bazie.");
			}
			
			if(userRepository.findFirstByUsername(user.getUsername()) != null) {
				if(model.containsAttribute("error")) {
					model.addAttribute("error", "Taki login oraz email już istnieją w bazie.");
				} else {
					model.addAttribute("error", "Taki login już istnieje w bazie.");
				}
			}
			
			return "userRegisterForm";
		}
		
		//zaszygrować i zapisać hasło
		user.setPassword(BCrypt.hashpw(user.getPassword(),  BCrypt.gensalt()));
		user.setEnabled(true);
		userRepository.save(user);
		
		return "userFormSuccess";
		
	}
	
	

}
