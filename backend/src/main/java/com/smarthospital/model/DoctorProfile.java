package com.smarthospital.model;
import jakarta.persistence.*;
@Entity
public class DoctorProfile {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @OneToOne(optional=false) public AppUser user;
 @ManyToOne(optional=false) public Department department;
 public String specialization;
 public int slotMinutes=15;
 public boolean active=true;
 public DoctorProfile() {}
 public DoctorProfile(AppUser user,Department department,String specialization){this.user=user;this.department=department;this.specialization=specialization;}
}
