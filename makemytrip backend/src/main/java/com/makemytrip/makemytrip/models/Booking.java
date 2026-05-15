package com.makemytrip.makemytrip.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import com.fasterxml.jackson.annotation.JsonProperty;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String serviceId;
    private String serviceType;
    private Double totalAmount;
    private String createdAt;
    private String selectionId;
    private String refundStatus;
    private String cancellationReason;
    private Double refundAmount;
    private String canceledAt;
    private String expectedRefundBy;
    private String refundPolicy;

    public Booking() {}

    // --- THE FIX: This tells Jackson to send 'serviceId' as 'targetName' to the frontend ---
    @JsonProperty("targetName")
    public String getTargetName() {
        return (serviceId != null) ? serviceId : "Unknown Destination";
    }

    @JsonProperty("targetName")
    public void setTargetName(String targetName) {
        this.serviceId = targetName;
    }

    // --- Standard Getters & Setters ---
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getServiceId() { return serviceId; }
    public void setServiceId(String serviceId) { this.serviceId = serviceId; }
    public String getServiceType() { return serviceType; }
    public void setServiceType(String serviceType) { this.serviceType = serviceType; }
    public Double getTotalAmount() { return totalAmount; }
    public void setTotalAmount(Double totalAmount) { this.totalAmount = totalAmount; }
    public String getCreatedAt() { return createdAt; }
    public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    public String getSelectionId() { return selectionId; }
    public void setSelectionId(String selectionId) { this.selectionId = selectionId; }
    public String getRefundStatus() { return refundStatus; }
    public void setRefundStatus(String refundStatus) { this.refundStatus = refundStatus; }
    public String getCancellationReason() { return cancellationReason; }
    public void setCancellationReason(String cancellationReason) { this.cancellationReason = cancellationReason; }
    public Double getRefundAmount() { return refundAmount; }
    public void setRefundAmount(Double refundAmount) { this.refundAmount = refundAmount; }
    public String getCanceledAt() { return canceledAt; }
    public void setCanceledAt(String canceledAt) { this.canceledAt = canceledAt; }
    public String getExpectedRefundBy() { return expectedRefundBy; }
    public void setExpectedRefundBy(String expectedRefundBy) { this.expectedRefundBy = expectedRefundBy; }
    public String getRefundPolicy() { return refundPolicy; }
    public void setRefundPolicy(String refundPolicy) { this.refundPolicy = refundPolicy; }
}
