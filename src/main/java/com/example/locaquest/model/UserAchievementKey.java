package com.example.locaquest.model;

import java.io.Serializable;
import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

@Embeddable
public class UserAchievementKey implements Serializable {
	private static final long serialVersionUID = 1L;

	@Column(name = "user_id")
    private int userId;

    @Column(name = "achv_id")
    private int achvId;

    public UserAchievementKey() {}

    public UserAchievementKey(int userId, int achvId) {
        this.userId = userId;
        this.achvId = achvId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getAchvId() {
        return achvId;
    }

    public void setAchvId(int achvId) {
        this.achvId = achvId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAchievementKey that = (UserAchievementKey) o;
        return userId == that.userId && achvId == that.achvId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(userId, achvId);
    }
}
