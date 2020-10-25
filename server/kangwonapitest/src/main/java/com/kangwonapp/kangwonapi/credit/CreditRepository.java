package com.kangwonapp.kangwonapi.credit;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CreditRepository extends JpaRepository<credit, Creditcompositekey> {

    @Query(value = "SELECT * from credit where credit.id = ?1", nativeQuery = true)
    List<credit> findByDepartmentId(String id);

    @Query(value = "SELECT * from credit where credit.id = ?1 and credit.year = ?2", nativeQuery = true)
    List<credit> findByDepartmentIdAndYear(String id, Integer year);
}
