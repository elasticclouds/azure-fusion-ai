package org.genai.fusion.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.Getter;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

/**
 * Represents a Prompt entity.
 * 
 * @see MODEL101
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Prompt {

    public Prompt(String promptNumber, String prompt) {
        this.promptNumber = promptNumber;
        this.prompt = prompt;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 100)
    private String promptNumber;

    @Column(length = 1000)
    private String prompt;
}