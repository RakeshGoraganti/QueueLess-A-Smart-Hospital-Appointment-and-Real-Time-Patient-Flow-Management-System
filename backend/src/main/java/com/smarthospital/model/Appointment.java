package com.smarthospital.model;
import jakarta.persistence.*;
import java.time.*;
@Entity
@Table(indexes={@Index(columnList="appointmentDate,doctor_id"),@Index(columnList="appointmentDate,tokenNumber")})
public class Appointment {
 @Id @GeneratedValue(strategy=GenerationType.IDENTITY) public Long id;
 @ManyToOne(optional=false) public AppUser patient;
 @ManyToOne(optional=false) public DoctorProfile doctor;
 @ManyToOne(optional=false) public Hospital hospital;
 @ManyToOne(optional=false) public Department department;
 @Column(nullable=false) public LocalDate appointmentDate;
 @Column(nullable=false) public LocalTime appointmentTime;
 @Column(nullable=false) public Integer tokenNumber;
 @Enumerated(EnumType.STRING) @Column(nullable=false) public AppointmentStatus status=AppointmentStatus.WAITING;
 @Column(length=2000) public String consultationNotes;
 public String diagnosis;
 public LocalDateTime createdAt=LocalDateTime.now();
 public LocalDateTime completedAt;
 public Appointment() {}
}
