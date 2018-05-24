package pl.coderslab.controllers;

import javax.servlet.http.HttpSession;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import pl.coderslab.entities.User;

@Controller
public class HomeController {
	@GetMapping("")
	
	public String home(Model model, HttpSession session) {
		
		if(session.getAttribute("loggedUser") != null ) {
			
			User user = (User)session.getAttribute("loggedUser"); //w sesji zapisujemy obiekty więc musimy zrobić rzutowanie na usera
			
			model.addAttribute("info", "Jesteś zalogowany jako " + user.getUsername());
			
		}
		return "index";
	}

}
