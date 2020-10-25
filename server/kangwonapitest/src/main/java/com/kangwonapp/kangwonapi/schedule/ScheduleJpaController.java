package com.kangwonapp.kangwonapi.schedule;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/schedule")
public class ScheduleJpaController {

    private final ScheduleRepository scheduleRepository;

    @GetMapping
    public List<schedule> getSchedule() {
        return scheduleRepository.findAll();
    }

}
