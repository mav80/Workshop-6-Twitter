package pl.coderslab.controllers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
	public String home(Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response,
			@RequestParam(required = false) Integer tweetsPerPage,
			@RequestParam(required = false) Integer pageNumber) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			//unread messages counter
			//model.addAttribute("unreadMessagesNumber", messageRepository.howManyUnreadMessagesByUserId(user.getId()));
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
		}
		
		List<Tweet> tweetsToShow = new ArrayList<>();
		//tweetsToShow = tweetRepository.findAllFromNotDeletedUsersOrderByCreatedDesc(); //not necessary since we have pagination below
		
		model.addAttribute("tweets", tweetsToShow);
		model.addAttribute("tweet", new Tweet()); //new tweet to bind with tweet adding form
		int tweetCount = tweetRepository.tweetCountFromNotDeletedUsers();
		model.addAttribute("tweetCount", tweetCount);
		
		
		//pagination
		
		if(tweetsPerPage == null || tweetsPerPage < 10) {
			tweetsPerPage = 10;
		}
		
		if(pageNumber == null || pageNumber < 1) {
			pageNumber = 1;
		}
		
		int numberOfPages = tweetCount / tweetsPerPage;

		if(tweetCount % tweetsPerPage != 0) {
			System.out.println("Modulo tweetCount % tweetsPerPage: " + (tweetCount % tweetsPerPage));
			numberOfPages++;
		}
		
		if(pageNumber > numberOfPages) {
			pageNumber = numberOfPages;
		}
		
		
		
		tweetsToShow = tweetRepository.findAllFromNotDeletedUsersOrderByCreatedDescLimitOffset(tweetsPerPage, (pageNumber -1) * tweetsPerPage);
		model.addAttribute("tweets", tweetsToShow);
		
		model.addAttribute("tweetsPerPage", tweetsPerPage);
		model.addAttribute("numberOfPages", numberOfPages);
	
		
		//end pagination
		
		
		
		

		
		//here we make separate list of comments for every tweet in our collection
//		Map<Long, List<Comment>> tweetsComments = new HashMap<>();
//		
//		List<Comment> allComments = new ArrayList<>();
//		allComments =  commentRepository.findAllFromNotDeletedUsersOrderByCreatedAscOnlyForTweetsFromNotDeletedUsers();
//		
//		for(Tweet singleTweet : tweetsToShow) {	
//			List<Comment> singleTweetComments = new ArrayList<>();
//			
//			for(Comment comment: allComments) {
//				if(comment.getTweet().getId() == singleTweet.getId()) {
//					singleTweetComments.add(comment);
//				}
//			}
//			tweetsComments.put(singleTweet.getId(), singleTweetComments);
//		}
//		model.addAttribute("tweetsComments", tweetsComments);
		//end of separate tweet comments
		
		model.addAttribute("comments", commentRepository.findAllFromNotDeletedUsersOrderByCreatedAscOnlyForTweetsFromNotDeletedUsers());
		model.addAttribute("comment", new Comment()); //new tweet to bind with tweet adding form
		
		//comment count section
		List<Tweet> tweets = tweetRepository.findAllFromNotDeletedUsersOrderByCreatedDesc();
		
		Map<Integer, Integer> commentCountMap = new HashMap<>();
		for(Tweet tweeto: tweets) {
			commentCountMap.put((int) tweeto.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweeto.getId()));
		}
		
		model.addAttribute("commentCountMap", commentCountMap);
		//end of comment count section
		
		
		
		
		
		System.out.println("\n\n\n\n\n\n\n\n\n\n\n\nuuuuu tweetsPerPage = " + tweetsPerPage + ", pageNumber = " + pageNumber + ", tweetów łącznie: " + tweetCount + 
				", liczba stron: " + numberOfPages);
		
		return "index";
	}
	
	
	@PostMapping("")
	public String home(@Valid Tweet tweet, BindingResult result, Comment comment, HttpSession session, Model model, HttpServletRequest request, HttpServletResponse response) {
		
		User user;
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
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
			List<Tweet> tweets = tweetRepository.findAllFromNotDeletedUsersOrderByCreatedDesc();
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweeto: tweets) {
				commentCountMap.put((int) tweeto.getId(), tweetRepository.findCommentCountFromNotDeletedUsersById(tweeto.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
				
			 return "index";
		 }
		 
		 tweet.setUser(user);
		 tweetRepository.save(tweet);
		 
		 return "redirect:/";
		
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	@PostMapping("/mainPageAddComment")
	public String mainPageAddComment(@Valid Comment comment, BindingResult result, Tweet tweet, Model model, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
		
		comment.setId(0); //if not set to 0 it will overwrite existing comment
		User user;
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, response, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
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
			//System.out.println("\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n\n" + comment);
			
			comment.setUser(user);
			commentRepository.save(comment);
			
			model.addAttribute("tweets", tweetRepository.findAllOrderByCreatedDesc());
			model.addAttribute("comments", commentRepository.findAll());
			comment.setText(null); //resets the comment window after post
			
			return "redirect:/";
			
		} else {
			
			model.addAttribute("infoError", "Aby dodawać komentarze musisz się najpierw zalogować!");
			return "userLoginForm";
		}
		
		
 
	
		
	}
	
	
	
	
	
	
	
	

}
