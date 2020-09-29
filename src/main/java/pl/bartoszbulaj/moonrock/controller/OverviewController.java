package pl.bartoszbulaj.moonrock.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import pl.bartoszbulaj.moonrock.entity.OverviewWebsocket;

@Controller
@RequestMapping("/")
public class OverviewController {

	@GetMapping
	public String showOverview() {
		return "index.html";
	}

	@GetMapping("/go")
	public String sendMessage() {
		new OverviewWebsocket().setOnConsolePrinter();
		String message = "{\"op\": \"subscribe\", \"args\": [\"orderBookL2_25:XBTUSD\"]}";
		new OverviewWebsocket().sendMessage(message);
		return "redirect:/";
	}

	@GetMapping("/stop")
	public String stopMessage() {
		new OverviewWebsocket().setOffConsolePrinter();
		return "redirect:/";
	}

}
