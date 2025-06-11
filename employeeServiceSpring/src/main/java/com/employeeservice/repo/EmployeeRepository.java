//package com.integratemodule.repo;
//
//import org.springframework.data.jpa.repository.JpaRepository;

//import com.integratemodule.model.Employee;
//
//public interface EmployeeRepository extends JpaRepository<Employee, String> {
//    // Custom methods (if needed)
//    // boolean existsByEmpName(String name);
//}

package com.employeeservice.repo;


import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import com.employeeservice.model.Employee;


public interface EmployeeRepository extends JpaRepository<Employee, String> {

	// built-in:
    Page<Employee> findAll(Pageable pageable);
    
    Optional<Employee> findByEmpIdAndPhone(String empId, String phone);
 // filter by position, and let Spring Data handle paging+sorting:
    Page<Employee> findByPosition(String position, Pageable pageable);

    // example custom @Query + paging:
//    @Query("SELECT e FROM Employee e WHERE e.position = :pos")
//    Page<Employee> findByPosition(
//        @Param("pos") String position,
//        Pageable pageable);
    
}