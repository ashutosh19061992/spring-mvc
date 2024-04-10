package com.example.demo.jparepository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.example.demo.model.Employe;

@Repository

public interface EmployeDao extends JpaRepository<Employe, Integer> {

	@Query("SELECT e FROM Employe e WHERE (e.emailId = :emailId AND e.name = :name) OR e.phoneNo = :phoneNo")
	List<Employe> findByEmailIdAndNameAndPhoneNo(@Param("emailId") String emailId, @Param("name") String name, @Param("phoneNo") String phoneNo);
}
