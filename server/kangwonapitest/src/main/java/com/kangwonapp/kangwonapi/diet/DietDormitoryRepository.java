package com.kangwonapp.kangwonapi.diet;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DietDormitoryRepository extends JpaRepository<dietdormitory, String> {

    List<dietdormitory> findByDate(String date);

    @Query(value = "SELECT * from dietdormitory where dietdormitory.dormitory = ?1 and dietdormitory.date >= ?2 order by dietdormitory.date", nativeQuery = true)
    List<dietdormitory> findByDormitory(String dormitory, String date);

    @Query(value = "SELECT * from dietdormitory where dietdormitory.date = ?1 and dietdormitory.dormitory = ?2 order by dietdormitory.date DESC ", nativeQuery = true)
    List<dietdormitory> findByDateAndDormitory(String date, String dormitory);
}
