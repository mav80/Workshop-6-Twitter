package pl.coderslab.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.coderslab.entities.User;
import pl.coderslab.repositories.TweetRepository;

@Controller
public class HomeController {
	
	@Autowired
	TweetRepository tweetRepository;
	
	@GetMapping("")
	public String home(Model model, HttpSession session) {
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
		}
		
		model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc()); 
		//System.out.println(tweetRepository.findAllOrderByCreatedDesc());
		
		return "index";
	}

}
