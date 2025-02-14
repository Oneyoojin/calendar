package com.example.calendar.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_google_accounts")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserGoogleAccounts {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long googleAccountId;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, unique = true, length = 255)
    private String googleAccountKey;

    @Column(length = 100)
    private String displayName;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT TRUE")
    private Boolean isActive = true;
}
