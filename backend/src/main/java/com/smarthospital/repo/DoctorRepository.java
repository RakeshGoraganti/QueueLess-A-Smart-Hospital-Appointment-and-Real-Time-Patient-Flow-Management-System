package com.smarthospital.repo;
import com.smarthospital.model.DoctorProfile; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List; import java.util.Optional;
public interface DoctorRepository extends JpaRepository<DoctorProfile,Long>{ List<DoctorProfile> findByDepartmentIdAndActiveTrue(Long id); Optional<DoctorProfile> findByUserId(Long id); }
