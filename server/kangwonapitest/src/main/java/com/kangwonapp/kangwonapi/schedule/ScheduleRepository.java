package com.kangwonapp.kangwonapi.schedule;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<schedule, Integer> {

    List<schedule> findAll();
}
