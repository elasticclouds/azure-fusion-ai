package org.genai.fusion.repository;

import org.genai.fusion.model.LoanApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface LoanApplicationRepository extends JpaRepository<LoanApplication, Long>, JpaSpecificationExecutor<LoanApplication> {

    Optional<LoanApplication> findByLoanNumber(String loanNumber);

    Optional<LoanApplication> findByEmail(String email);

}