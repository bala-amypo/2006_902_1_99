package com.example.demo.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "vendors")
public class Vendor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String vendorName;

    @Column(nullable = false)
    private String contactEmail;

    private String phone;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "vendor")
    private Set<Asset> assets = new HashSet<>();

    // Automatically set when entity is first saved
    @PrePersist
    protected void onCreate() {
        this.createdAt = LocalDateTime.now();
    }

    // ✅ REQUIRED BY TEST
    public void setId(Long id) {
        this.id = id;
    }

    // -------- Getters & Setters --------

    public Long getId() {
        return id;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    // ✅ FIX: Added setter to resolve compilation error
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Asset> getAssets() {
        return assets;
    }

    public void setAssets(Set<Asset> assets) {
        this.assets = assets;
    }
}
