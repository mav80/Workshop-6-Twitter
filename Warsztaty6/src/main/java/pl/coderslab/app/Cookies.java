package pl.coderslab.app;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.util.WebUtils;

import pl.coderslab.entities.User;
import pl.coderslab.repositories.UserRepository;

public class Cookies {
	
	public static void CheckCookiesAndSetLoggedUserAttribute(HttpServletRequest request, HttpServletResponse response, UserRepository userRepository, HttpSession session) {
		String userCookieValue = "";
		String userCookieValueDecoded = "";

		Cookie userCookie = WebUtils.getCookie(request, "userCookie");

		if (userCookie != null) {
			userCookieValue = userCookie.getValue();
		} else {
			session.setAttribute("loggedUser", null);
		}

		try {
			userCookieValueDecoded = URLDecoder.decode(userCookieValue, "utf-8");
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (!userCookieValueDecoded.isEmpty()) {
			User user = userRepository.findFirstByUsername(userCookieValueDecoded);
			if (user != null && !user.isDeleted() && user.isEnabled()) {
				session.setAttribute("loggedUser", user);
			} else {
				session.setAttribute("loggedUser", null);
				userCookie.setPath("/");
				userCookie.setMaxAge(0);
				response.addCookie(userCookie);
			}
		}

	}
}