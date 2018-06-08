package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.Message;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.CommentRepository;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class MessageController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	CommentRepository commentRepository;
	@Autowired
	MessageRepository messageRepository;
	
	
	@GetMapping("/messages")
	public String allMessagesTopicsView(Model model, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			model.addAttribute("messages",messageRepository.findAllByReceiverIdOrderByCreatedDesc(user.getId()));
			model.addAttribute("messagesSent",messageRepository.findAllBySenderIdOrderByCreatedDesc(user.getId()));
			
			return "userMessagesViewAll";
		}
		
		model.addAttribute("infoError", "Aby odczytać wiadomości musisz się najpierw zalogować!");
		return "userLoginForm";
		
	}
	
	
	
	@GetMapping("/message/{id}")
	public String singleMessageView(Model model, @PathVariable Long id, HttpSession session, HttpServletRequest request) {
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		
		if(session.getAttribute("loggedUser") != null ) {
			User user = (User)session.getAttribute("loggedUser");
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			Message messageToDisplay = messageRepository.findFirstById(id);
			
			if(messageRepository.findFirstById(id) != null) {
				
				model.addAttribute("message",messageToDisplay);
				
				//user can view messages sent by him so we make sure that viewing his own message won't sen "vieved" fild as true
				if(messageToDisplay.getSender().getId() != user.getId()) {
					messageToDisplay.setViewed(true);
					messageRepository.save(messageToDisplay);
				}
				
			}
			
			
			
			return "userMessagesViewSingle";
		}
		
		model.addAttribute("infoError", "Aby odczytać wiadomość musisz się najpierw zalogować!");
		return "userLoginForm";
		
	}

}
