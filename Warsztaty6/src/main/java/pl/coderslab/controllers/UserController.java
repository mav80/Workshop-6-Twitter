package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.app.Cookies;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	
	@GetMapping("/panelUser")
	public String userPanel(Model model, HttpSession session, HttpServletRequest request, @RequestParam(defaultValue="") String showAll) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
			
//			if(showAll.equals("true") ) {
//				model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
//				model.addAttribute("modelInfo", "Oto wszystkie Twoje tweety:");
//			}
			
			
		}

		return "panelUser";
	}
	

}
