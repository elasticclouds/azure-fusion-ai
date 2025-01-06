package org.genai.fusion.endpoints;

import org.genai.fusion.model.Prompt;
import org.genai.fusion.repository.PromptRepository;
import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;

import java.util.List;
import java.util.Optional;

/**
 * Service class for managing Prompt entities.
 * 
 * @see SVC101
 */
@BrowserCallable
@AnonymousAllowed
public class PromptEndpoint extends CrudRepositoryService<Prompt, Long, PromptRepository> {

    private PromptEndpoint(PromptRepository repository) {
        super(repository);
    }

    public List<Prompt> findAll() {
        return getRepository().findAll();
    }

    public Optional<Prompt> findById(Long id) {
        return getRepository().findById(id);
    }

    public Prompt save(Prompt prompt) {
        return getRepository().save(prompt);
    }

    public void deleteById(Long id) {
        getRepository().deleteById(id);
    }
}