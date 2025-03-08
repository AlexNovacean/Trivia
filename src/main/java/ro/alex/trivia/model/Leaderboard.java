package ro.alex.trivia.model;

import java.util.Objects;

public class Leaderboard implements Comparable<Leaderboard> {
    private String displayName;
    private Double score;
    private String avatar;

    public Leaderboard(String displayName, Double score, String avatar) {
        this.displayName = displayName;
        this.score = score;
        this.avatar = avatar;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public Double getScore() {
        return score;
    }

    public void setScore(Double score) {
        this.score = score;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    @Override
    public int compareTo(Leaderboard o) {
        return o.getScore().compareTo(this.getScore());
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Leaderboard that = (Leaderboard) o;
        return Objects.equals(displayName, that.displayName);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(displayName);
    }
}
