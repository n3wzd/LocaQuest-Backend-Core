package com.example.locaquest.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "user_statistics")
public class UserStatistic {

    @EmbeddedId
    private UserStatisticKey id;

    @Column(name = "exp", nullable = false)
    private int exp = 0;

    @Column(name = "steps", nullable = false)
    private int steps = 0;

    @Column(name = "distance", nullable = false)
    private int distance = 0;

    public UserStatistic() {}

    public UserStatistic(int userId, String statDate) {
    	DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        this.id = new UserStatisticKey(userId, LocalDate.parse(statDate, formatter));
    }
    
    public UserStatistic(int userId, LocalDate statDate) {
        this.id = new UserStatisticKey(userId, statDate);
    }
}
