package org.genai.fusion.endpoints;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;

import jakarta.annotation.security.PermitAll;

@PermitAll
@BrowserCallable
@AnonymousAllowed
public class HelloEndpoint {
    public String sayHello(String name) {

        if (name == null || name.isEmpty()) {
            return "Hello World!";
        } else {
            return "Hello " + name;
        }
    }
}
