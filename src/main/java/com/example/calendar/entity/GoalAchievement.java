package com.example.calendar.entity;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "goal_achievement")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class GoalAchievement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long achievementId;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private Users user;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer totalGoals = 0;

    @Column(nullable = false, columnDefinition = "INT DEFAULT 0")
    private Integer completedGoals = 0;

    @Column(precision = 5, scale = 2)
   private BigDecimal achievementRate;

    @Column(nullable = false)
    private Integer month;

    @Column(nullable = false)
    private Integer year;
}
