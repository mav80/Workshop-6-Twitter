package pl.coderslab.controllers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import pl.coderslab.app.Cookies;
import pl.coderslab.app.MessageUtils;
import pl.coderslab.entities.User;
import pl.coderslab.repositories.MessageRepository;
import pl.coderslab.repositories.TweetRepository;
import pl.coderslab.repositories.UserRepository;

@Controller
public class AdminController {
	
	@Autowired
	TweetRepository tweetRepository;
	@Autowired
	UserRepository userRepository;
	@Autowired
	MessageRepository messageRepository;
	
	@GetMapping("/panelAdmin")
	public String adminPanel(Model model, HttpSession session, HttpServletRequest request,
			@RequestParam(defaultValue="") String userSearchNameLike,
			@RequestParam(defaultValue="") String userSearchEmailLike,
			@RequestParam(defaultValue="-1") long userSearchId) {
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			//unread messages counter
			MessageUtils.countUnreadMessagesAndSetInfoIfAny(model, user, messageRepository);
			
			if(!userSearchNameLike.isEmpty()) {
				model.addAttribute("users", userRepository.findByUsernameLike(userSearchNameLike));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findByUsernameLike(userSearchNameLike));
			}
			if(!userSearchEmailLike.isEmpty()) {
				model.addAttribute("users", userRepository.findByEmailLike(userSearchEmailLike));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findByEmailLike(userSearchEmailLike));
			}
			if(userSearchId > 0) {
				model.addAttribute("users", userRepository.findFirstById(userSearchId));
				System.out.println("Wyniki wyszukiwania:");
				System.out.println(userRepository.findFirstById(userSearchId));
			}
			
			
			
			if(!userSearchNameLike.isEmpty() || !userSearchEmailLike.isEmpty() || userSearchId > 0) {
				model.addAttribute("searchResultMessage", "Oto wyniki wyszukiwania:");
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			return "panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
	}
	
	
	
	
	
	
	
	@GetMapping("/adminToggleBanUser/{id}")
	public String adminPanelBanUser(Model model, HttpSession session, HttpServletRequest request,
			@PathVariable long id) { 
		
		Cookies.CheckCookiesAndSetLoggedUserAttribute(request, userRepository, session); //static method to check user cookie and set session attribute accordingly to avoid repeating code
		User user = (User) session.getAttribute("loggedUser");
		
		if(user != null && user.isAdmin()) {
			
			User userToBan = userRepository.findFirstById(id);
			
			if(userToBan != null) {
				if(userToBan.isEnabled()) {
					userToBan.setEnabled(false);
					userRepository.save(userToBan);
					model.addAttribute("operationInfo", "Użytkownika " + userToBan.getUsername() + " zbanowano pomyślnie.");
				} else {
					userToBan.setEnabled(true);
					userRepository.save(userToBan);
					model.addAttribute("operationInfo", "Użytkownika " + userToBan.getUsername() + " odbanowano pomyślnie.");
				}
			} else {
				model.addAttribute("operationInfo", "Użytkownik o takim id nie istnieje.");
			}
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			
			return "redirect:/panelAdmin";
		} else {
			model.addAttribute("infoError", "Musisz mieć uprawnienia administratora aby wejść do panelu admina!");
			return "userLoginForm";
		}
		
		
	}
	
	
	
	
	
	
	
	
	
	

}
