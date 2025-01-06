package org.genai.fusion.endpoints;

import com.microsoft.semantickernel.services.ServiceNotFoundException;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import org.genai.fusion.semantickernel.SKAssistant;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;

@BrowserCallable
@AnonymousAllowed
public class ChatEndpoint {

    Logger log = LoggerFactory.getLogger(ChatEndpoint.class);

    private final SKAssistant skAssistant;

    public ChatEndpoint(SKAssistant skAssistant) {
        this.skAssistant = skAssistant;
    }

    public Flux<String> chat(String chatId, String userMessage) {
        try {
            return this.skAssistant.chat(chatId, userMessage);
        } catch (ServiceNotFoundException e) {
            log.error("Error invoking semantic kernel", e);
            return Flux.just("Something went wrong, please try again later");
        }
    }
}