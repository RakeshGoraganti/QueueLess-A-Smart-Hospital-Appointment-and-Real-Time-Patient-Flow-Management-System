package com.smarthospital.controller;
import com.smarthospital.model.*; import com.smarthospital.repo.*; import jakarta.transaction.Transactional; import jakarta.validation.constraints.*; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/appointments")
public class AppointmentController {
 private final AppointmentRepository appointments; private final UserRepository users; private final DoctorRepository doctors; private final HospitalRepository hospitals; private final DepartmentRepository departments;
 public AppointmentController(AppointmentRepository a,UserRepository u,DoctorRepository d,HospitalRepository h,DepartmentRepository dep){appointments=a;users=u;doctors=d;hospitals=h;departments=dep;}
 public record BookingRequest(@NotNull Long doctorId,@NotNull LocalDate date,@NotNull LocalTime time){}
 @PostMapping @Transactional public ResponseEntity<?> book(@RequestBody BookingRequest r,Authentication auth){
  if(r.date().isBefore(LocalDate.now()))return ResponseEntity.badRequest().body(Map.of("message","Appointment date cannot be in the past"));
  var user=users.findByEmail(auth.getName()).orElseThrow();if(!"PATIENT".equals(user.role))return ResponseEntity.status(403).body(Map.of("message","Only patients can book appointments"));
  var doctor=doctors.findById(r.doctorId()).orElse(null);if(doctor==null||!doctor.active)return ResponseEntity.badRequest().body(Map.of("message","Doctor not found"));
  if(appointments.existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(doctor.id,r.date(),r.time(),AppointmentStatus.CANCELLED))return ResponseEntity.status(409).body(Map.of("message","This time slot is already booked"));
  int token=appointments.findByDoctorIdAndAppointmentDateOrderByTokenNumberAsc(doctor.id,r.date()).stream().mapToInt(a->a.tokenNumber).max().orElse(0)+1;
  Appointment a=new Appointment();a.patient=user;a.doctor=doctor;a.department=doctor.department;a.hospital=doctor.department.hospital;a.appointmentDate=r.date();a.appointmentTime=r.time();a.tokenNumber=token;a.status=AppointmentStatus.WAITING;
  appointments.save(a);return ResponseEntity.status(201).body(a);
 }
 @GetMapping("/mine") public List<Appointment> mine(Authentication auth){var u=users.findByEmail(auth.getName()).orElseThrow();if(!"PATIENT".equals(u.role))throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN,"Patient access only");return appointments.findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(u.id);}
 @PatchMapping("/{id}/cancel") @Transactional public ResponseEntity<?> cancel(@PathVariable Long id,Authentication auth){
  var u=users.findByEmail(auth.getName()).orElseThrow();var a=appointments.findById(id).orElse(null);if(a==null)return ResponseEntity.notFound().build();
  if(!"ADMIN".equals(u.role)&&!a.patient.id.equals(u.id))return ResponseEntity.status(403).body(Map.of("message","Not your appointment"));
  if(a.status==AppointmentStatus.IN_CONSULTATION||a.status==AppointmentStatus.COMPLETED)return ResponseEntity.badRequest().body(Map.of("message","Appointment can no longer be cancelled"));
  a.status=AppointmentStatus.CANCELLED;appointments.save(a);return ResponseEntity.ok(a);
 }
}
