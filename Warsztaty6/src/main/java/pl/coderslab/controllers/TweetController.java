package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.ModelAndView;

import pl.coderslab.app.Cookies;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class TweetController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;
	
	@GetMapping("/tweet/{id}")
	public String tweetView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			model.addAttribute("tweet",tweetRepository.findFirstById(id)); //new tweet to bind with tweet adding form
			model.addAttribute("comments", commentRepository.findAllByTweetIdOrderByCreatedAsc(id));
			
			model.addAttribute("comment", new Comment()); //new tweet to bind with tweet adding form
			
			return "userTweetView";
		}
		
		return "redirect:http://localhost:8080/Warsztaty6-Twitter/";
		
	}
	
	
	
	
	@PostMapping("/tweet/{id}")
	public ModelAndView tweetView(@Valid Comment comment, BindingResult result, Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		comment.setId(0); //if not set to 0 it will overwrite existing comment
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", id);
		modelAndView.setViewName("userTweetView");
		
		User user;
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			model.addAttribute("tweet",tweetRepository.findFirstById(id)); //new tweet to bind with tweet adding form
			
			if(result.hasErrors()) {
				model.addAttribute("comments", commentRepository.findAllByTweetIdOrderByCreatedAsc(id));
				//System.out.println(result.getAllErrors());
				return modelAndView;
			}
			 
			comment.setTweet(tweetRepository.findFirstById(id));
			comment.setUser(user);
			commentRepository.save(comment);
			
			model.addAttribute("comments", commentRepository.findAllByTweetIdOrderByCreatedAsc(id));
			comment.setText(null); //resets the comment window after post
			
			return modelAndView;
			
		} else {
			modelAndView.clear();
			modelAndView.setViewName("redirect:/login");
			
			return modelAndView;
		}
		
		
 
	
		
	}
	
	

}