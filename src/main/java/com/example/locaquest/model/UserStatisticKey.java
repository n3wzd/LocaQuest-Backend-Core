package com.example.locaquest.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Embeddable
public class UserStatisticKey implements Serializable {
	private static final long serialVersionUID = 1L;

    @Column(name = "user_id")
    private int userId;

	@Column(name = "stat_date")
    private String statDate;

    public UserStatisticKey() {}

    public UserStatisticKey(int userId, String statDate) {
        this.userId = userId;
        this.statDate = statDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserStatisticKey that = (UserStatisticKey) o;
        return userId == that.userId && statDate == that.statDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, statDate);
    }
}
