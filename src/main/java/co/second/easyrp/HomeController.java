package co.second.easyrp;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class HomeController {
	
	@RequestMapping("/")
	public String home() {
		return "home/home";
	}
	
	// 로그인으로 이동
	@RequestMapping("/login")
	public String loginPage() {
		return "authentication/login";
	}
	
}
