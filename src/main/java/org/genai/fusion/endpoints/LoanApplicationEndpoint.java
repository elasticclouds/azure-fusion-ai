package org.genai.fusion.endpoints;

import com.vaadin.flow.server.auth.AnonymousAllowed;
import com.vaadin.hilla.BrowserCallable;
import com.vaadin.hilla.crud.CrudRepositoryService;
import org.genai.fusion.model.LoanApplication;
import org.genai.fusion.repository.LoanApplicationRepository;


import java.util.List;
import java.util.Optional;

@BrowserCallable
@AnonymousAllowed
public class LoanApplicationEndpoint extends CrudRepositoryService<LoanApplication, Long, LoanApplicationRepository> {

    public LoanApplicationEndpoint(LoanApplicationRepository repository) {
        super(repository);
    }

    public List<LoanApplication> findAll() {
        return getRepository().findAll();

    }

    public Optional<LoanApplication> findByld(Long id) {
        return getRepository().findById(id);

    }

    public Optional<LoanApplication> findByLoanNumber(String loanNumber) {
        return getRepository().findByLoanNumber(loanNumber);

    }

    public Optional<LoanApplication> findByEmail(String email) {
        return getRepository().findByEmail(email);

    }

    public LoanApplication save(LoanApplication loanApplication) {
        return getRepository().save(loanApplication);

    }

    public void delete(LoanApplication loanApplication) {
        getRepository().delete(loanApplication);

    }

}