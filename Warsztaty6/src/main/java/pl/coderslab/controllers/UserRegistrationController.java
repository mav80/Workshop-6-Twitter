package pl.coderslab.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.mindrot.jbcrypt.BCrypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class UserRegistrationController {
	
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/register")
	public String register(Model model) {
		model.addAttribute("user", new User());
		
		return "userRegisterForm";
	}
	
	@PostMapping("/register")
	public String register(@Valid User user, BindingResult result, Model model, HttpSession session, HttpServletResponse response) {
		
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
		
		//zaszyfrować i zapisać hasło
		user.setPassword(BCrypt.hashpw(user.getPassword(),  BCrypt.gensalt()));
		user.setEnabled(true);
		userRepository.save(user);
		
		//let's login user after successful registration and redirect him to the main page
		
		//unread messages counter
		MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
		
		model.addAttribute("info", "Zalogowano.");
		//System.out.println("It matches.");
		session.setAttribute("loggedUser", user);
		
		//cookie section
		try {
			Cookie userCookie = new Cookie("userCookie",URLEncoder.encode(user.getUsername(),"utf-8"));
			userCookie.setPath("/");
			userCookie.setMaxAge(60 * 60 * 24 * 7 * 4); //set cookie expiry time to ~1 month
			response.addCookie(userCookie);
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		//end of cookie section
		
		model.addAttribute("operationInfo", "Gratulacje " + user.getUsername() + ", dziękujemy za dołączenie do naszej społeczności. :)");
		
		return "redirect:/";
		
	}
	
	

}
