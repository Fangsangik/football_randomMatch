package com.side.football_project.domain.vendor.entity;

import com.side.football_project.domain.stadium.entity.Address;
import com.side.football_project.domain.stadium.entity.Stadium;
import com.side.football_project.domain.user.type.UserRole;
import com.side.football_project.domain.vendor.type.VendorStatus;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "vendors")
public class Vendor {


    //TODO : S3 이미지 업로드 기능 추가 필요
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, unique = true)
    private String email;
    
    @Column(nullable = false)
    private String password;
    
    @Column(nullable = false)
    private String name;
    
    @Column(nullable = false)
    private String phoneNumber;
    
    @Column(nullable = false)
    private int age;

    @Embedded
    private Address address;

    @Column(nullable = false, unique = true)
    private String businessNumber;
    
    @Column(nullable = false)
    private String companyName;
    
    @Column(nullable = false)
    private String businessAddress;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VendorStatus status;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private LocalDateTime appliedAt;
    
    private LocalDateTime approvedAt;
    
    private LocalDateTime rejectedAt;
    
    private String rejectionReason;

    @OneToMany(mappedBy = "vendor", cascade = CascadeType.ALL)
    private List<Stadium> stadiums = new ArrayList<>();
    
    @Builder
    public Vendor(String email, String password, String name, String phoneNumber, int age,
                  String businessNumber, String companyName, String businessAddress, String description, String address) {
        this.email = email;
        this.password = password;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.age = age;
        this.businessNumber = businessNumber;
        this.companyName = companyName;
        this.businessAddress = businessAddress;
        this.description = description;
        this.status = VendorStatus.PENDING;
        this.appliedAt = LocalDateTime.now();
        this.address = new Address();
    }
    
    public void approve() {
        this.status = VendorStatus.APPROVED;
        this.approvedAt = LocalDateTime.now();
        this.rejectedAt = null;
        this.rejectionReason = null;
    }
    
    public void reject(String reason) {
        this.status = VendorStatus.REJECTED;
        this.rejectedAt = LocalDateTime.now();
        this.rejectionReason = reason;
        this.approvedAt = null;
    }

    public UserRole getRole() {
        return UserRole.VENDOR;
    }

    public void updateInfo(String name, String phoneNumber, String businessAddress, String description) {
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.businessAddress = businessAddress;
        this.description = description;
    }
}