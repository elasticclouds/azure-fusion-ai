package org.genai.fusion.api;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import jakarta.servlet.http.HttpServletRequest;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.util.HashMap;
import java.util.Map;

@RestController
@CrossOrigin(origins = "*")
public class HelloAPI {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private Gson gson;

    String jsonString = """
            {
                "messages": [
                        {
                            "content": "You are an expert assistant who answers questions with politeness and professionalism. Your primary role is to assist users by providing accurate information, answering questions, and engaging in helpful conversation. Always maintain a friendly and respectful tone in your responses.",
                            "role": "system"
                        },
                        {
                            "content": "What is Generative AI in 2 lines",
                            "role": "user"
                        }
                    ],
                    "skill_parameters": {
                        "emb_type": "openai",
                        "model_name": "gpt4-8k",
                        "temperature": 0.0,
                        "presence_penalty": 0,
                        "frequency_penalty": 0,
                        "retrieval_chain": "custom",
                        "top_k": 5,
                        "top_p": 1
                    },
                    "stream_response": false
                }
                """;

    @GetMapping("/api/hello")
    public String hello(@RequestParam(required = false, defaultValue = "Hello World") String name,
            HttpServletRequest request) {

        // System.out
        // .println("==========================================================================================");
        // JsonObject prompt = gson.fromJson(jsonString, JsonObject.class);
        // // Print the default prompt details
        // System.out.println("Default Prompt: " + prompt.toString());

        // JsonObject message =
        // prompt.getAsJsonArray("messages").get(1).getAsJsonObject();

        // System.out.println("Default message: " + message.toString());

        // message.addProperty("content", name);

        // System.out.println("Updated prompt: " + prompt.toString());

        // // Submit the prompt to the API
        // String url = "https://api.lab45.ai/v1.1/skills/completion/query";

        // // Get the Authorization header
        // String authorizationHeader = request.getHeader("Authorization");
        // System.out.println("Authorization: " + authorizationHeader);

        // HttpHeaders headers = new HttpHeaders();
        // headers.set("Content-Type", "application/json");
        // headers.set("Authorization", authorizationHeader);

        // HttpEntity<String> promptRequest = new HttpEntity<>(prompt.toString(),
        // headers);
        // ResponseEntity<String> promptResponse = restTemplate.exchange(url,
        // HttpMethod.POST, promptRequest,
        // String.class);

        // System.out
        // .println("==========================================================================================");
        // System.out.println("Prompt Response: " + promptResponse.getBody());
        // System.out
        // .println("==========================================================================================");

        // return promptResponse.getBody();

        Map<String, String> response = new HashMap<>();
        if (name == null || name.isEmpty()) {
            response.put("message", "Hello Stranger");
        } else {
            response.put("message", name);
        }

        Gson gson = new Gson();
        return gson.toJson(response);

    }
}
