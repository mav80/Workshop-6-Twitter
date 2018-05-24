package pl.coderslab.controllers;

import javax.servlet.http.HttpSession;

import org.hibernate.Session;
import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pl.coderslab.entities.User;
import pl.coderslab.repositories.UserRepository;

@Controller
public class LoginController {
	@Autowired
	UserRepository userRepository;
	@GetMapping("/login")
	public String login() {
		return "loginForm";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String email, @RequestParam String password, HttpSession session, Model model) {
				
		User user = userRepository.findFirstByEmail(email);
		if(user!=null && BCrypt.checkpw(password,  user.getPassword())) {
			
			model.addAttribute("info", "Zalogowano.");
				//System.out.println("It matches.");
				session.setAttribute("loggedUser", user);
			
		} else {
			session.setAttribute("loggedUser", null);
			model.addAttribute("info", "Podałeś błędny email lub hasło.");
			//System.out.println("It doesn't match.");
		}
		
		return "loginForm";
	}
	
	
	@GetMapping("/logout")
	@ResponseBody
	public String logout(HttpSession session) {
		session.setAttribute("loggedUser",  null);
		return "Zostales wylogowany.";
		
	}
	
	
	

}
