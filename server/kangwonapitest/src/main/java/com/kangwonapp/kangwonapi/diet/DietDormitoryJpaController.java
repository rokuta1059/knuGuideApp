package com.kangwonapp.kangwonapi.diet;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/diet")
public class DietDormitoryJpaController {

    private final DietDormitoryRepository dietDormitoryRepository;

    @GetMapping
    public List<dietdormitory> getDietByDate() {
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        return dietDormitoryRepository.findByDate(fm.format(new Date()));
    }

    @GetMapping("/{dormitory}")
    public List<dietdormitory> getDietByDormitory(@PathVariable String dormitory) {
        SimpleDateFormat fm = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.set(Calendar.DAY_OF_WEEK,Calendar.MONDAY);
        // System.out.println(c.getTime());
        return dietDormitoryRepository.findByDormitory(dormitory, fm.format(c.getTime()));
    }
}
