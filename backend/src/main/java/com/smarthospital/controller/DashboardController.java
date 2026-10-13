package com.smarthospital.controller;
import com.smarthospital.model.AppointmentStatus; import com.smarthospital.repo.*; import org.springframework.web.bind.annotation.*; import java.time.LocalDate; import java.util.*;
@RestController
public class DashboardController {
 private final UserRepository users;private final HospitalRepository hospitals;private final DepartmentRepository departments;private final DoctorRepository doctors;private final AppointmentRepository appointments;
 public DashboardController(UserRepository u,HospitalRepository h,DepartmentRepository d,DoctorRepository dr,AppointmentRepository a){users=u;hospitals=h;departments=d;doctors=dr;appointments=a;}
 @GetMapping("/api/dashboard/stats") public Map<String,Object> stats(){return Map.of("patients",users.findAll().stream().filter(u->"PATIENT".equals(u.role)).count(),"doctors",doctors.count(),"hospitals",hospitals.count(),"departments",departments.count(),"todayAppointments",appointments.countByAppointmentDate(LocalDate.now()),"waiting",appointments.countByStatus(AppointmentStatus.WAITING),"completed",appointments.countByStatus(AppointmentStatus.COMPLETED),"cancelled",appointments.countByStatus(AppointmentStatus.CANCELLED));}
}
