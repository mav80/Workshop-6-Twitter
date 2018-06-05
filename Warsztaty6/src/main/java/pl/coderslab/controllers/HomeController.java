package pl.coderslab.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.MessageRepository;
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
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("")
	public String home(Model model, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			//unread messages counter
			//model.addAttribute("unreadMessagesNumber", messageRepository.howManyUnreadMessagesByUserId(user.getId()));
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
		}
		
		model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
		model.addAttribute("tweet", new Tweet()); //new tweet to bind with tweet adding form
		model.addAttribute("tweetCount", tweetRepository.tweetCount());
		model.addAttribute("comments", commentRepository.findAllOrderByCreatedAsc());
		
		model.addAttribute("comment", new Comment()); //new tweet to bind with tweet adding form
		
		//System.out.println(tweetRepository.findAllOrderByCreatedDesc());
		
		//comment count section
		List<Tweet> tweets = tweetRepository.findAllOrderByCreatedDesc();
		
		Map<Integer, Integer> commentCountMap = new HashMap<>();
		for(Tweet tweet: tweets) {
			commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
		}
		
		model.addAttribute("commentCountMap", commentCountMap);
		//end of comment count section
		
		return "index";
	}
	
	
	@PostMapping("")
	public String home(@Valid Tweet tweet, BindingResult result, Comment comment, HttpSession session, Model model, HttpServletRequest request) {
		
		User user;
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
		} else {
			return "redirect:/login";
		}
		
//		if(session.getAttribute("loggedUser") != null ) {
//			user = (User)session.getAttribute("loggedUser"); //w sesji zapisujemy obiekty więc musimy zrobić rzutowanie na usera
//			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
//		}
		
		 
		 if(result.hasErrors()) {
			 //System.out.println(result.getAllErrors());
			 model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
			 model.addAttribute("comments", commentRepository.findAllOrderByCreatedAsc());
			 model.addAttribute("tweetCount", tweetRepository.tweetCount());
			 
			//comment count section
			List<Tweet> tweets = tweetRepository.findAllOrderByCreatedDesc();
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweeto: tweets) {
				commentCountMap.put((int) tweeto.getId(), tweetRepository.findCommentCountById(tweeto.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
				
			 return "index";
		 }
		 
		 tweet.setUser(user);
		 tweetRepository.save(tweet);
		 
		 return "redirect:http://localhost:8080/Warsztaty6-Twitter/";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/mainPageAddComment")
	public String mainPageAddComment(@Valid Comment comment, BindingResult result, Tweet tweet, Model model, HttpSession session, HttpServletRequest request) {
		
		comment.setId(0); //if not set to 0 it will overwrite existing comment
		User user;
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			//model.addAttribute("tweet",tweetRepository.findFirstById(id)); //new tweet to bind with tweet adding form
			
			if(result.hasErrors()) {
				model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
				model.addAttribute("comments", commentRepository.findAll());
				System.out.println(result.getAllErrors());
				return "index";
			}
			 
			//comment.setTweet(tweetRepository.findFirstById(id));
			
			System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + comment);
			
			
			comment.setUser(user);
			commentRepository.save(comment);
			
			model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
			model.addAttribute("comments", commentRepository.findAll());
			comment.setText(null); //resets the comment window after post
			
			return "redirect:/";
			
		} else {
			
			return "redirect:http://localhost:8080/Warsztaty6-Twitter/";
		}
		
		
 
	
		
	}
	
	
	
	
	
	
	
	

}
