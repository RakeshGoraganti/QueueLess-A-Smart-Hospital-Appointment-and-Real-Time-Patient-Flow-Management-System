package com.smarthospital.model;
import jakarta.persistence.*;
@Entity
public class Department {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @Column(nullable=false) public String name;
 @ManyToOne(optional=false) public Hospital hospital;
 public String description;
 public Department() {}
 public Department(String name,Hospital hospital,String description){this.name=name;this.hospital=hospital;this.description=description;}
}
