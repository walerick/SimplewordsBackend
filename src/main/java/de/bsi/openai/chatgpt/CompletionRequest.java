package de.bsi.openai.chatgpt;

public record CompletionRequest(String model, String prompt, 
		double temperature, int max_tokens) {
	
	public static CompletionRequest defaultWith(String prompt) {
		return new CompletionRequest("text-davinci-003", "Summarize this to me in not more than 5 words" + prompt, 0.7, 100);
	}
	
}
