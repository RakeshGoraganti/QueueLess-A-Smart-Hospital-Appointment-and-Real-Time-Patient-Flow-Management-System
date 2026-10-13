package com.smarthospital.controller;
import com.smarthospital.model.*; import com.smarthospital.repo.*; import org.springframework.http.*; import org.springframework.security.core.Authentication; import org.springframework.web.bind.annotation.*; import java.time.*; import java.util.*;
@RestController @RequestMapping("/api/queue")
public class QueueController {
 private final AppointmentRepository appointments; private final DoctorRepository doctors; private final UserRepository users;
 public QueueController(AppointmentRepository a,DoctorRepository d,UserRepository u){appointments=a;doctors=d;users=u;}
 @GetMapping("/doctor/{doctorId}") public List<?> queue(@PathVariable Long doctorId,@RequestParam(defaultValue="") String date,Authentication auth){
  var u=users.findByEmail(auth.getName()).orElseThrow();var d=doctors.findById(doctorId).orElseThrow();
  if(!"ADMIN".equals(u.role)&&!d.user.id.equals(u.id))throw new org.springframework.web.server.ResponseStatusException(HttpStatus.FORBIDDEN,"Only assigned doctor or admin can view this queue");
  LocalDate day=date.isBlank()?LocalDate.now():LocalDate.parse(date);
  return appointments.findByDoctorIdAndAppointmentDateOrderByTokenNumberAsc(doctorId,day).stream().map(a->Map.of("id",a.id,"tokenNumber",a.tokenNumber,"patientName",a.patient.name,"time",a.appointmentTime.toString(),"status",a.status.toString(),"appointmentDate",a.appointmentDate.toString())).toList();
 }
 @PostMapping("/doctor/{doctorId}/call-next") public ResponseEntity<?> next(@PathVariable Long doctorId,Authentication auth){
  var u=users.findByEmail(auth.getName()).orElseThrow();var d=doctors.findById(doctorId).orElseThrow();
  if(!"ADMIN".equals(u.role)&&!d.user.id.equals(u.id))return ResponseEntity.status(403).body(Map.of("message","Not authorized"));
  var next=appointments.findFirstByDoctorIdAndAppointmentDateAndStatusOrderByTokenNumberAsc(doctorId,LocalDate.now(),AppointmentStatus.WAITING);
  if(next.isEmpty())return ResponseEntity.ok(Map.of("message","No waiting patients"));
  Appointment a=next.get();a.status=AppointmentStatus.IN_CONSULTATION;appointments.save(a);return ResponseEntity.ok(a);
 }
 @PatchMapping("/appointments/{id}/complete") public ResponseEntity<?> complete(@PathVariable Long id,@RequestBody Map<String,String> body,Authentication auth){
  var u=users.findByEmail(auth.getName()).orElseThrow();var a=appointments.findById(id).orElse(null);if(a==null)return ResponseEntity.notFound().build();
  if(!"ADMIN".equals(u.role)&&!a.doctor.user.id.equals(u.id))return ResponseEntity.status(403).body(Map.of("message","Not authorized"));
  if(a.status!=AppointmentStatus.IN_CONSULTATION)return ResponseEntity.badRequest().body(Map.of("message","Patient is not currently in consultation"));
  a.status=AppointmentStatus.COMPLETED;a.diagnosis=body.getOrDefault("diagnosis","");a.consultationNotes=body.getOrDefault("notes","");a.completedAt=LocalDateTime.now();appointments.save(a);return ResponseEntity.ok(a);
 }
}
