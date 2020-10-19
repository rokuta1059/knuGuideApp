package com.kangwonapp.kangwonapi.credit;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/department/credit")
public class creditJpaController {

    private final CreditRepository creditRepository;

    @GetMapping
    public List<credit> retrieveAllCredits() {
        return creditRepository.findAll();
    }

    @GetMapping("/{id}")
    public List<credit> retrieveCreditsById(@PathVariable String id) {
        return creditRepository.findByDepartmentId(id);
    }

    @GetMapping("/{id}/{year}")
    public List<credit> retriveCreditsByIdAndYear(@PathVariable String id, @PathVariable Integer year) {
        return creditRepository.findByDepartmentIdAndYear(id, year);
    }

    @GetMapping("{id}/{year}/{classification}")
    public credit retriveCreditById(@PathVariable String id, @PathVariable Integer year, @PathVariable String classification) {
        return creditRepository.findById(new Creditcompositekey(id, year, classification))
                .get();
    }
}
