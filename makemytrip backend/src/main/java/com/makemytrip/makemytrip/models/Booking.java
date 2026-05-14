package com.makemytrip.makemytrip.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "bookings")
public class Booking {
    @Id
    private String id;
    private String userId;
    private String serviceId;
    private String serviceType;
    private Double totalAmount;
    private String createdAt;
    
    // --- NEW FIELDS FOR INTERNSHIP TASKS ---
    private String selectionId;        // For Seat/Room selection
    private String refundStatus;       // For 50% Refund tracker
    private String cancellationReason; // For cancellation dropdown
    
    public Booking() {}

    // --- FRONTEND COMPATIBILITY ALIAS LAYER ---
    // This allows the Controller and Frontend to read 'targetName' smoothly 
    // by pointing it directly to your existing database 'serviceId' column.
    public String getTargetName() { 
        return this.serviceId; 
    }
    
    public void setTargetName(String targetName) { 
        this.serviceId = targetName; 
    }

    // --- GETTERS & SETTERS ---
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
}
