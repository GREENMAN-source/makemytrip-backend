package com.makemytrip.makemytrip.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.ArrayList;
import java.util.List;

@Document(collection = "reviews")
public class Review {
    @Id
    private String id;
    private String userId;
    private String targetId; // ID of the Hotel or Flight being reviewed
    private int rating; // 1 to 5
    private String comment;
    private List<String> photos; // URLs for uploaded photos
    private List<String> replies; // User replies
    private boolean isFlagged;
    private int helpfulVotes; // For the "Most Helpful" sorting
    private long createdAt; // Timestamp for "Newest" sorting

    public Review() {
        this.photos = new ArrayList<>();
        this.replies = new ArrayList<>();
        this.helpfulVotes = 0;
        this.isFlagged = false;
        this.createdAt = System.currentTimeMillis();
    }

    // --- Getters and Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getTargetId() { return targetId; }
    public void setTargetId(String targetId) { this.targetId = targetId; }
    public int getRating() { return rating; }
    public void setRating(int rating) { this.rating = rating; }
    public String getComment() { return comment; }
    public void setComment(String comment) { this.comment = comment; }
    public List<String> getPhotos() { return photos; }
    public void setPhotos(List<String> photos) { this.photos = photos; }
    public List<String> getReplies() { return replies; }
    public void setReplies(List<String> replies) { this.replies = replies; }
    public boolean isFlagged() { return isFlagged; }
    public void setFlagged(boolean flagged) { isFlagged = flagged; }
    public int getHelpfulVotes() { return helpfulVotes; }
    public void setHelpfulVotes(int helpfulVotes) { this.helpfulVotes = helpfulVotes; }
    public long getCreatedAt() { return createdAt; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
}