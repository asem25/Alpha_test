package ru.semavin.alpha_test.models;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "contacts")
public class Contact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String phone;

    @Email(message = "Email should be noy empty")
    private String email;

    @ManyToOne
    @JoinColumn(name = "client_id", nullable = false)
    @ToString.Exclude
    private Client client;


}
