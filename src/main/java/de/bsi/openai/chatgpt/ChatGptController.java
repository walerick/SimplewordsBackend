package de.bsi.openai.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.fasterxml.jackson.databind.ObjectMapper;

import de.bsi.openai.FormInputDTO;
import de.bsi.openai.OpenAiApiClient;
import de.bsi.openai.OpenAiApiClient.OpenAiService;

import java.util.Map;

@Controller
public class ChatGptController {
	
	private static final String MAIN_PAGE = "index";
	
	@Autowired
	private ObjectMapper jsonMapper;
	@Autowired
	private OpenAiApiClient client;
	
	private String chatWithGpt3(String message) throws Exception {
		var completion = CompletionRequest.defaultWith(message);
		var postBodyJson = jsonMapper.writeValueAsString(completion);
		var responseBody = client.postToOpenAiApi(postBodyJson, OpenAiService.GPT_3);
		var completionResponse = jsonMapper.readValue(responseBody, CompletionResponse.class);
		return completionResponse.firstAnswer().orElseThrow();
	}
	@CrossOrigin
	@GetMapping(path = "/")
	public String index() {

		return MAIN_PAGE;
	}

	@CrossOrigin
	@PostMapping(path = "/")
	public String chat(Model model, @ModelAttribute FormInputDTO dto) {
		try {
			model.addAttribute("request", dto.prompt());
			model.addAttribute("response", chatWithGpt3(dto.prompt()));

		} catch (Exception e) {
			model.addAttribute("response", "Error in communication with OpenAI ChatGPT API.");
		}
		return MAIN_PAGE;
	}
	@CrossOrigin
	@PostMapping(path = "/vi/api/prompt")
	public ResponseEntity<String> gpt(@RequestBody Map<String, String> request) {
		try {
			String response = chatWithGpt3(request.get("prompt"));
			return new ResponseEntity<>(response, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>("Error in communication with OpenAI.", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}
