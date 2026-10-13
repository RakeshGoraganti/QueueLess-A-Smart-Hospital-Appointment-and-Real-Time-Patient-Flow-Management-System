package com.smarthospital.model;
import jakarta.persistence.*;
@Entity
public class Hospital {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String name;
 public String address;
 public String phone;
 public boolean active=true;
 public Hospital() {}
 public Hospital(String name,String address,String phone){this.name=name;this.address=address;this.phone=phone;}
}
