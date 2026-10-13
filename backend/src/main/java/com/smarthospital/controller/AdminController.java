package com.smarthospital.controller;
import com.smarthospital.model.*; import com.smarthospital.repo.*; import org.springframework.web.bind.annotation.*; import org.springframework.security.crypto.password.PasswordEncoder; import java.util.*;
@RestController @RequestMapping("/api/admin")
public class AdminController {
 private final HospitalRepository hospitals; private final DepartmentRepository departments; private final UserRepository users; private final DoctorRepository doctors; private final AppointmentRepository appointments; private final PasswordEncoder encoder;
 public AdminController(HospitalRepository h,DepartmentRepository d,UserRepository u,DoctorRepository dr,AppointmentRepository a,PasswordEncoder encoder){hospitals=h;departments=d;users=u;doctors=dr;appointments=a;this.encoder=encoder;}
 @PostMapping("/hospitals") public Hospital createHospital(@RequestBody Hospital h){h.id=null;return hospitals.save(h);}
 @PutMapping("/hospitals/{id}") public Hospital updateHospital(@PathVariable Long id,@RequestBody Hospital h){Hospital old=hospitals.findById(id).orElseThrow();old.name=h.name;old.address=h.address;old.phone=h.phone;old.active=h.active;return hospitals.save(old);}
 @PostMapping("/departments") public Department createDepartment(@RequestBody Map<String,Object> b){Hospital h=hospitals.findById(Long.valueOf(b.get("hospitalId").toString())).orElseThrow();return departments.save(new Department(b.get("name").toString(),h,Objects.toString(b.get("description"),"")));}
 @PostMapping("/doctors") public DoctorProfile createDoctor(@RequestBody Map<String,Object> b){
  String email=Objects.toString(b.get("email"),"").toLowerCase();String password=Objects.toString(b.get("password"),"");
  if(email.isBlank()||password.length()<8||Objects.toString(b.get("name"),"").isBlank()||b.get("departmentId")==null)throw new IllegalArgumentException("Name, email, password (minimum 8 characters), and departmentId are required");
  if(users.existsByEmail(email))throw new IllegalArgumentException("Email already exists");
  Department dep=departments.findById(Long.valueOf(b.get("departmentId").toString())).orElseThrow();
  AppUser user=users.save(new AppUser(b.get("name").toString(),email,encoder.encode(password),"DOCTOR"));
  return doctors.save(new DoctorProfile(user,dep,Objects.toString(b.get("specialization"),"General Physician")));
 }
 @GetMapping("/users") public List<?> allUsers(){return users.findAll().stream().map(u->Map.of("id",u.id,"name",u.name,"email",u.email,"role",u.role,"active",u.active)).toList();}
 @PatchMapping("/users/{id}/active") public AppUser setActive(@PathVariable Long id,@RequestBody Map<String,Boolean> b){AppUser u=users.findById(id).orElseThrow();u.active=Boolean.TRUE.equals(b.get("active"));return users.save(u);}
 @GetMapping("/appointments") public List<Appointment> allAppointments(){return appointments.findAll();}
}
