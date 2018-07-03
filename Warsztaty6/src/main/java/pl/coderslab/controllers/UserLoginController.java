package pl.coderslab.controllers;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import org.springframework.web.util.WebUtils;

import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class UserLoginController {
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/login")
	public String login() {
		return "userLoginForm";
	}
	
	@PostMapping("/login")
	public String login(@RequestParam String username, @RequestParam String password, HttpSession session, Model model, HttpServletResponse response) {
				
		User user = userRepository.findFirstByUsername(username);
		if(user!=null && !user.isDeleted() && user.isEnabled() &&  BCrypt.checkpw(password,  user.getPassword())) {
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
				
			
		} else {
			session.setAttribute("loggedUser", null);
			model.addAttribute("infoError", "Dane logowania niepoprawne!");
			//System.out.println("It doesn't match.");
			
			if(!user.isEnabled()) {
				model.addAttribute("infoError", "Ten użytkownik został zbanowany.");
			}
			
			if(user.isDeleted()) {
				model.addAttribute("infoError", "Taki użytkownik nie istnieje w bazie.");
			}

			return "userLoginForm";
		}
		
		//return "userLoginForm";
		return "redirect:/";
	}
	
	
	@GetMapping("/logout")
	//@ResponseBody
	public String logout(HttpSession session, HttpServletRequest request,  HttpServletResponse response) {
		//cookie section
		Cookie userCookie = WebUtils.getCookie(request, "userCookie");
		if(userCookie!=null) {
			userCookie.setPath("/");
			userCookie.setMaxAge(0);
			response.addCookie(userCookie);
		}
		//end of cookie section
		
		session.setAttribute("loggedUser",  null);
		return "redirect:/";
		
	}
	
	
	

}
