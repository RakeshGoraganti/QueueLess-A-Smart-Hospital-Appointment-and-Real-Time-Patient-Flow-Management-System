package com.smarthospital.repo;
import com.smarthospital.model.*; import org.springframework.data.jpa.repository.JpaRepository; import java.time.*; import java.util.*;
public interface AppointmentRepository extends JpaRepository<Appointment,Long>{
 List<Appointment> findByPatientIdOrderByAppointmentDateDescAppointmentTimeDesc(Long id);
 List<Appointment> findByDoctorIdAndAppointmentDateOrderByTokenNumberAsc(Long id,LocalDate date);
 List<Appointment> findByAppointmentDate(LocalDate date);
 boolean existsByDoctorIdAndAppointmentDateAndAppointmentTimeAndStatusNot(Long doctorId,LocalDate date,LocalTime time,AppointmentStatus status);
 Optional<Appointment> findFirstByDoctorIdAndAppointmentDateAndStatusOrderByTokenNumberAsc(Long id,LocalDate date,AppointmentStatus status);
 long countByAppointmentDate(LocalDate date);
 long countByStatus(AppointmentStatus status);
}
