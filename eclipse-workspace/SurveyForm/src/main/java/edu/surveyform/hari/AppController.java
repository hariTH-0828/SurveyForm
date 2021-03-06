package edu.surveyform.hari;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class AppController {
	
	@Autowired
	private UserRepository repo;

	@GetMapping("")
	@RequestMapping(value="/", method= RequestMethod.GET)
	public String viewHomePage() {
		return "login";
	}

	@GetMapping("/login")
//	@RequestMapping(value = "/login", method = RequestMethod.GET)
	public String showLoginPage(){
		if(isAuthenticated()){
			return "redirect:login_success";
		}
		return "login";
	}

	private boolean isAuthenticated() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if(authentication == null || AnonymousAuthenticationToken.class.isAssignableFrom(authentication.getClass())){
			return false;
		}
		return authentication.isAuthenticated();
	}

	@GetMapping("/register")
	public String showSignUpForm(Model model) {
		if(isAuthenticated()){
			return "redirect:login_success";
		}
		model.addAttribute("user", new User());
		return "register_form";
	}
	
	@PostMapping("/process_register")
	public String processRegistration(User user) {
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String encodedPassword = encoder.encode(user.getPassword());
		user.setPassword(encodedPassword);
		
		repo.save(user);
		return "register_success";
	}

	@GetMapping("/login_success")
	@RequestMapping(value = "/login_success", method = RequestMethod.GET)
	public String successLogin(){
		return "login_success";
	}
	
	/*@GetMapping("/list_users")
	public String viewUsersList(Model model) {
		List<User> listUsers = repo.findAll();
		model.addAttribute("listUsers", listUsers);
		return "users";
	}*/
}
