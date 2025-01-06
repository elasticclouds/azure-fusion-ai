package org.genai.fusion.semantickernel;

import com.microsoft.semantickernel.semanticfunctions.annotations.DefineKernelFunction;
import com.microsoft.semantickernel.semanticfunctions.annotations.KernelFunctionParameter;
import org.genai.fusion.endpoints.LoanApplicationEndpoint;
import org.genai.fusion.model.LoanApplication;
import org.genai.fusion.semantickernel.search.SKContentRetriever;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.genai.fusion.semantickernel.LocaleParser.parseLocale;

@Component
public class SKPlugins {

    Logger log = LoggerFactory.getLogger(SKPlugins.class);

    public static final String DAY_MONTH_DAY_YEAR = "EEEE, MMMM d, yyyy";

    private final SKContentRetriever contentRetriever;
    private final LoanApplicationEndpoint loanApplicationEndpoint;

    public SKPlugins(SKContentRetriever contentRetriever, LoanApplicationEndpoint loanApplicationEndpoint) {
        this.contentRetriever = contentRetriever;
        this.loanApplicationEndpoint = loanApplicationEndpoint;
    }

    /**
     * Searches for information related to various loan-related topics such as
     * applying for a loan,
     * required documents, approval process, interest rates, loan approval, loan
     * repayment, profile
     * update, customer support, and contact information.
     *
     * @param query the query to answer
     * @return a Mono that emits a list of strings containing the search results
     */
    @DefineKernelFunction(name = "SearchFromQuestion", description = "find information related apply for a loan, documents required, approval process, interest rats, loan approval, loan repayment, profile update, customer support, contact", returnType = "string")
    public Mono<List<String>> searchInAnIndex(
            @KernelFunctionParameter(description = "the query to answer", name = "query") String query) {
        log.debug("invoked search for policies for query {}", query);
        return contentRetriever.searchKnowledgeBase(query);
    }

    /**
     * Retrieves Loan Application details for the provided loan number.
     *
     * @param loanNumber the loan number to retrieve details for
     * @return the LoanApplication details for the provided loan number, or null if
     *         not found
     */
    @DefineKernelFunction(name = "GetLoanApplicationDetailsByLoanNumber", description = "Retrieves Loan Application details for the provided loan number", returnType = "org.genai.fusion.model.LoanApplication")
    public LoanApplication getLoanApplicationByLoanNumber(
            @KernelFunctionParameter(description = "The loan number to retrieve details for", name = "loanNumber") String loanNumber) {
        log.debug("Retrieving Loan Application details for loan number {}", loanNumber);
        return loanApplicationEndpoint.findByLoanNumber(loanNumber).orElse(null);
    }

    /**
     * Retrieves Loan Application details for the provided email address.
     *
     * @param email the email address to retrieve details for
     * @return the LoanApplication details for the provided email address, or null
     *         if not found
     */
    @DefineKernelFunction(name = "GetLoanApplicationDetailsByEmail", description = "Retrieves Loan Application details for the provided email address", returnType = "org.genai.fusion.model.LoanApplication")
    public LoanApplication getLoanApplicationByEmail(
            @KernelFunctionParameter(description = "The email address to retrieve details for", name = "email") String email) {
        log.debug("Retrieving Loan Application details for email address {}", email);
        return loanApplicationEndpoint.findByEmail(email).orElse(null);
    }

    /**
     * Updates the address for a given loan application identified by the loan
     * number.
     *
     * @param loanNumber the loan number to update the address for
     * @param address    the new address to set
     * @return the updated LoanApplication details, or null if the loan application
     *         is not found
     */
    @DefineKernelFunction(name = "UpdateLoanApplicationAddressByLoanNumber", description = "Update the address for a given application loan number", returnType = "org.genai.fusion.model.LoanApplication")
    public LoanApplication updateLoanApplicationAddressByLoanNumber(
            @KernelFunctionParameter(description = "The loan number to update the address for", name = "loanNumber") String loanNumber,
            @KernelFunctionParameter(description = "The new address to set", name = "address") String address) {
        log.debug("Updating address for loan number {}", loanNumber);
        LoanApplication loanApplication = loanApplicationEndpoint.findByLoanNumber(loanNumber).orElse(null);
        if (loanApplication != null) {
            loanApplication.setAddress(address);
            loanApplication.setKYCVerified(true);
            loanApplication.setLoanStatus("APPROVED");
            loanApplicationEndpoint.save(loanApplication);
            log.debug("Address updated successfully for loan number {}", loanNumber);
            return loanApplication;
        } else {
            log.debug("Loan Application not found for loan number {}", loanNumber);
            return null;
        }
    }

    /**
     * Creates a Loan Application for a customer.
     *
     * @param customerName the customer's name
     * @param email        the customer's email
     * @param loanNumber   the loan number
     * @param loanType     the type of loan
     * @param loanAmount   the loan amount
     * @param address      the customer's address
     * @return the created LoanApplication
     */
    @DefineKernelFunction(name = "CreateLoanApplication", description = "Create a Loan Application for a customer", returnType = "org.genai.fusion.model.LoanApplication")
    public LoanApplication createLoanApplication(
            @KernelFunctionParameter(description = "The customer's name", name = "customerName") String customerName,
            @KernelFunctionParameter(description = "The customer's email", name = "email") String email,
            @KernelFunctionParameter(description = "The loan number", name = "loanNumber") String loanNumber,
            @KernelFunctionParameter(description = "The type of loan", name = "loanType") String loanType,
            @KernelFunctionParameter(description = "The loan amount", name = "loanAmount") double loanAmount,
            @KernelFunctionParameter(description = "The customer's address", name = "address") String address) {
        log.debug("Creating Loan Application for customer {}", customerName);
        LoanApplication loanApplication = new LoanApplication();
        loanApplication.setLoanNumber(loanNumber);
        loanApplication.setLoanType(loanType);
        loanApplication.setCustomerName(customerName);
        loanApplication.setEmail(email);
        loanApplication.setLoanAmount(loanAmount);
        loanApplication.setAddress(address);
        loanApplication.setKYCVerified(false);
        loanApplication.setLoanStatus("SUBMITTED");
        loanApplicationEndpoint.save(loanApplication);
        log.debug("Loan Application created successfully for loan number {}", loanNumber);
        return loanApplication;
    }

    /**
     * Get the current date and time for the system default timezone.
     *
     * @return a ZonedDateTime object with the current date and time.
     */
    public ZonedDateTime now() {
        return ZonedDateTime.now(ZoneId.systemDefault());
    }

    /**
     * Get the current date and time in the local time zone.
     * <p>
     * Example: {{time.now}} => Sunday, January 12, 2025 9:15 AM
     *
     * @return The current date and time in the local time zone.
     */
    @DefineKernelFunction(name = "now", description = "Get the current date and time in the local time zone")
    public String now(
            @KernelFunctionParameter(name = "locale", description = "Locale to use when formatting the date", required = false) String locale) {
        return DateTimeFormatter.ofPattern(DAY_MONTH_DAY_YEAR + " h:mm a")
                .withLocale(parseLocale(locale))
                .format(now());
    }

}
