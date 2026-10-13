package com.smarthospital.controller;
import com.smarthospital.repo.*; import org.springframework.web.bind.annotation.*; import java.util.*;
@RestController
public class PublicController {
 private final HospitalRepository hospitals; private final DepartmentRepository departments; private final DoctorRepository doctors;
 public PublicController(HospitalRepository h,DepartmentRepository d,DoctorRepository dr){hospitals=h;departments=d;doctors=dr;}
 @GetMapping("/api/hospitals") public List<?> hospitals(){return hospitals.findByActiveTrue();}
 @GetMapping("/api/hospitals/{id}/departments") public List<?> departments(@PathVariable Long id){return departments.findByHospitalId(id);}
 @GetMapping("/api/departments/{id}/doctors") public List<?> doctors(@PathVariable Long id){return doctors.findByDepartmentIdAndActiveTrue(id).stream().map(d->Map.of("id",d.id,"name",d.user.name,"specialization",d.specialization,"department",d.department.name,"hospital",d.department.hospital.name)).toList();}
 @GetMapping("/api/doctors") public List<?> allDoctors(){return doctors.findAll().stream().filter(d->d.active).map(d->Map.of("id",d.id,"name",d.user.name,"specialization",d.specialization,"department",d.department.name,"hospital",d.department.hospital.name)).toList();}
}
