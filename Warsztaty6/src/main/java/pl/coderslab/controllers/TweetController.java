package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
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
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.MessageRepository;
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
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/tweet/{id}")
	public String tweetView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
		}
		
		Tweet tweet = tweetRepository.findFirstFromNotDeletedUserById(id);
		
		if(tweet != null) {
			model.addAttribute("tweet", tweet); //new tweet to bind with tweet adding form
			model.addAttribute("comments", commentRepository.findAllByTweetIdOrderByCreatedAsc(id));
			model.addAttribute("comment", new Comment()); //new comment to bind with comment adding form
		}
		
		
		return "userTweetView";
		
	}
	
	
	
	
	@PostMapping("/tweet/{id}")
	public ModelAndView tweetView(@Valid Comment comment, BindingResult result, Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		comment.setId(0); //if not set to 0 it will overwrite existing comment
		
		ModelAndView modelAndView = new ModelAndView();
		modelAndView.addObject("id", id);
		modelAndView.setViewName("userTweetView");
		
		User user;
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		Tweet tweet = tweetRepository.findFirstFromNotDeletedUserById(id);
		
		if(session.getAttribute("loggedUser") != null & tweet != null) {
			user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
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
			model.addAttribute("infoError", "Wystąpił błąd podczas dodawania komentarza.");
			modelAndView.setViewName("redirect:/");
			
			return modelAndView;
		}
		
		
 
	
		
	}
	
	

}
