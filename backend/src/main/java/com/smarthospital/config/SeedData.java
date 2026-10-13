package com.smarthospital.config;
import com.smarthospital.model.*; import com.smarthospital.repo.*; import org.springframework.boot.CommandLineRunner; import org.springframework.context.annotation.Bean; import org.springframework.context.annotation.Configuration; import org.springframework.security.crypto.password.PasswordEncoder;
@Configuration
public class SeedData {
 @Bean CommandLineRunner seed(UserRepository users,HospitalRepository hospitals,DepartmentRepository departments,DoctorRepository doctors,PasswordEncoder encoder){return args->{
  AppUser admin=users.findByEmail("admin@hospital.com").orElseGet(()->users.save(new AppUser("Hospital Admin","admin@hospital.com",encoder.encode("Admin@123"),"ADMIN")));
  AppUser patient=users.findByEmail("patient@hospital.com").orElseGet(()->users.save(new AppUser("Demo Patient","patient@hospital.com",encoder.encode("Patient@123"),"PATIENT")));
  AppUser doc=users.findByEmail("doctor@hospital.com").orElseGet(()->users.save(new AppUser("Dr. Demo","doctor@hospital.com",encoder.encode("Doctor@123"),"DOCTOR")));
  Hospital h=hospitals.findAll().stream().findFirst().orElseGet(()->hospitals.save(new Hospital("City Care Hospital","Hyderabad, Telangana","+91 98765 43210")));
  Department d=departments.findAll().stream().findFirst().orElseGet(()->departments.save(new Department("General Medicine",h,"General health and primary care")));
  if(doctors.findByUserId(doc.id).isEmpty())doctors.save(new DoctorProfile(doc,d,"General Physician"));
 };}
}
