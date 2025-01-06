//package org.genai.fusion.api;
//
//import com.google.gson.Gson;
//import com.microsoft.semantickernel.services.ServiceNotFoundException;
//import jakarta.servlet.http.HttpServletRequest;
//import org.genai.fusion.semantickernel.SKAssistant;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.web.bind.annotation.CrossOrigin;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.RequestParam;
//import org.springframework.web.bind.annotation.RestController;
//import org.springframework.web.client.RestTemplate;
//import reactor.core.publisher.Flux;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@CrossOrigin(origins = "*")
//public class ChatAPI {
//
//    @Autowired
//    private RestTemplate restTemplate;
//
//    @Autowired
//    private Gson gson;
//
//    Logger log = LoggerFactory.getLogger(ChatAPI.class);
//
//    private final SKAssistant skAssistant;
//
//    public ChatAPI(SKAssistant skAssistant) {
//        this.skAssistant = skAssistant;
//    }
//
//
//    @GetMapping("/api/chat")
//    public Flux<String> chat(@RequestParam(required = false, defaultValue = "1") String chatId,
//                       @RequestParam(required = false, defaultValue = "What is Generative AI in two lines?") String userMessage) {
//
//        try {
//            return this.skAssistant.chat(chatId, userMessage);
//        } catch (ServiceNotFoundException e) {
//            log.error("Error invoking semantic kernel", e);
//            return Flux.just("Something went wrong, please try again later");
//        }
//
////        Map<String, String> response = new HashMap<>();
////        if (userMessage == null || userMessage.isEmpty()) {
////            response.put("message", "Hello Stranger");
////        } else {
////            response.put("message", chatId + " " + userMessage);
////        }
////
////        Gson gson = new Gson();
////        return gson.toJson(response);
//
//    }
//}
