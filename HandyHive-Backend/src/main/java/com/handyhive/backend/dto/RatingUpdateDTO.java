package com.handyhive.backend.dto;

import com.fasterxml.jackson.annotation.JsonAlias;

/**
 * DTO for updating an existing Rating.
 * Accepts both new and old client field names.
 */
public class RatingUpdateDTO {

    @JsonAlias({"ratingScore"})
    private Double score;

    @JsonAlias({"feedback"})
    private String comment;

    public Double getScore() { return score; }
    public void setScore(Double score) { this.score = score; }

    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
}
