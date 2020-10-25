package com.kangwonapp.kangwonapi.notice;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NoticeRepository extends JpaRepository<notice, Long> {

    @Query(value = "SELECT * from notice where notice.departmentid = ?1 order by notice.date", nativeQuery = true)
    List<notice> findByDepartmentALL(String department);

    @Query(value = "SELECT * from notice where notice.announce = '공지' and notice.departmentid = ?1 order by notice.date", nativeQuery = true)
    List<notice> findByAnnounce(String department);

    List<notice> findByDepartmentidOrderByDateDesc(String department);

    List<notice> findByDepartmentidInOrderByDateDesc(List<String> department);
}
