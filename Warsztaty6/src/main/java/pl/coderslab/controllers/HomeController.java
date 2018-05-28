package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import pl.coderslab.app.Cookies;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class HomeController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("")
	public String home(Model model, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
		}
		
		model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
		model.addAttribute("tweet", new Tweet()); //new tweet to bind with tweet adding form
		model.addAttribute("comments", commentRepository.findAll()); //new tweet to bind with tweet adding form
		//System.out.println(tweetRepository.findAllOrderByCreatedDesc());
		
		return "index";
	}
	
	
	@PostMapping("")
	public String home(@Valid Tweet tweet, BindingResult result, HttpSession session, Model model, HttpServletRequest request) {
		
		User user;
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
		} else {
			return "redirect:/login";
		}
		
//		if(session.getAttribute("loggedUser") != null ) {
//			user = (User)session.getAttribute("loggedUser"); //w sesji zapisujemy obiekty więc musimy zrobić rzutowanie na usera
//			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
//		}
		
		 
		 if(result.hasErrors()) {
			 System.out.println(result.getAllErrors());
			 model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
			 return "index";
		 }
		 
		 tweet.setUser(user);
		 tweetRepository.save(tweet);
		 
		 return "redirect:http://localhost:8080/Warsztaty6-Twitter/";
		
	}

}
