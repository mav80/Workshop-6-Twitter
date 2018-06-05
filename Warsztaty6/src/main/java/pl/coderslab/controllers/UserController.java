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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Comment;
import pl.coderslab.entities.Message;
import pl.coderslab.entities.Tweet;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class UserController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/panelUser")
	public String userPanel(Model model, HttpSession session, HttpServletRequest request, @RequestParam(defaultValue="") String showAll) {

		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
			
//			if(showAll.equals("true") ) {
//				model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(user.getId()));
//				model.addAttribute("modelInfo", "Oto wszystkie Twoje tweety:");
//			}
			
			
		}

		return "panelUser";
	}
	
	
	
	@GetMapping("/userView/{id}")
	public String singleUserView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(session.getAttribute("loggedUser") != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
			model.addAttribute("viewedUser", userRepository.findFirstById(id));
			model.addAttribute("message", new Message()); //new message to bind with message adding form
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
		}

		return "userViewSingle";
		
	}
	
	
	@PostMapping("/userView/{id}")
	public String singleUserViewPost(@Valid Message message, BindingResult result, Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		message.setId(0); //if not set to 0 it will overwrite existing message
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		
		if(session.getAttribute("loggedUser") != null ) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
			model.addAttribute("tweets", tweetRepository.findByUserIdOrderByCreatedDesc(id));
			model.addAttribute("viewedUser", userRepository.findFirstById(id));
			
			//comment count section
			List<Tweet> tweets = tweetRepository.findByUserIdOrderByCreatedDesc(id);
			
			Map<Integer, Integer> commentCountMap = new HashMap<>();
			for(Tweet tweet: tweets) {
				commentCountMap.put((int) tweet.getId(), tweetRepository.findCommentCountById(tweet.getId()));
			}
			
			model.addAttribute("commentCountMap", commentCountMap);
			//end of comment count section
			
			
			
			
			
			
			if(result.hasErrors()) {
				System.out.println(result.getAllErrors());
				return "userViewSingle";
			}
			
		
			
			
			message.setSender(user);
			User receiver = userRepository.findFirstById(id);
			if(receiver != null) {
				message.setReceiver(receiver);
			} else {
				model.addAttribute("messageSentStatus", "Błąd wysyłania wiadomości, użytkownik docelowy nie istnieje.");
				return "userViewSingle";
			}
			
			if(receiver.getId() == user.getId()) {
				model.addAttribute("messageSentStatus", "Nie możesz wysłać wiadomości do samego siebie!");
				return "userViewSingle";
			}
			
			messageRepository.save(message);
			
			//clear the message so its content won't reappear after sending
			message.setText(null);
			message.setTopic(null);
			
			model.addAttribute("messageSentStatus", "Wiadomość została poprawnie wysłana.");
			
			return "userViewSingle";
			
		}
		
		model.addAttribute("messageSentStatus", "Błąd wysyłania wiadomości, nadawca nie istnieje.");

		return "userViewSingle";
		
	}
	

}
