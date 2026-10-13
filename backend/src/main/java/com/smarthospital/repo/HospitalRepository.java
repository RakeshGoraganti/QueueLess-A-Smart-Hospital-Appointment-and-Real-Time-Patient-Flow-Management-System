package com.smarthospital.repo;
import com.smarthospital.model.Hospital; import org.springframework.data.jpa.repository.JpaRepository; import java.util.List;
public interface HospitalRepository extends JpaRepository<Hospital,Long>{ List<Hospital> findByActiveTrue(); }
