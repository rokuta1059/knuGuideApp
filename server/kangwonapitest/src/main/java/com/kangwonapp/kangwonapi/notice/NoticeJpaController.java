package com.kangwonapp.kangwonapi.notice;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/department/notice")
public class NoticeJpaController {

    private final NoticeRepository noticeRepository;

    /**
     * 지정된 department의 공지를 받아온다
     * @param department 공지를 받아 올 학과
     * @return 공지 목록
     */
    @GetMapping("/{department}")
    public List<notice> getNotice(@PathVariable String department) {
        if (department.length() <= 3)
            return noticeRepository.findByDepartmentidOrderByDateDesc(department);
        else {
            String[] tokens = department.split("(?<=\\G.{3})");
            List<String> token = new ArrayList(Arrays.asList(tokens));
            return noticeRepository.findByDepartmentidInOrderByDateDesc(token);
        }
    }

    /**
     * 지정된 department의 '공지' 항목만 받아온다
     * @param department 공지를 받아 올 학과
     * @return 공지 목록
     */
    @GetMapping("/{department}/announce")
    public List<notice> getNoticeAnnounce(@PathVariable String department) {
        return noticeRepository.findByAnnounce(department);
    }

//    @GetMapping("/{de}")
//    public List<notice> getNotice1234(@PathVariable String de) {
//        return noticeRepository.findByDepartmentOrderByDateAsc(de);
//    }
}
