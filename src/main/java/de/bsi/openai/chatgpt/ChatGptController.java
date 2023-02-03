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


@RequestMapping("/")
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

	@CrossOrigin(origins = "http://localhost:3000")
	@PostMapping(path = "/v1/api/prompt")
	public ResponseEntity<String> gpt(@RequestBody Map<String, String> request) {
		try {
			String response = chatWithGpt3(request.get("prompt"));
			return ResponseEntity.ok()
					.header("Access-Control-Allow-Origin", "*")
					.body(response);
		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.header("Access-Control-Allow-Origin", "*")
					.body("Error in communication with OpenAI.");
		}
	}
}
