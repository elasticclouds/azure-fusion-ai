package org.genai.fusion.model;


import jakarta.persistence .*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LoanApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String loanNumber;
    private String loanType;
    private String customerName;
    private String email;
    private String address;
    private boolean isKYCVerified;
    private double loanAmount;
    private String loanStatus;

}
