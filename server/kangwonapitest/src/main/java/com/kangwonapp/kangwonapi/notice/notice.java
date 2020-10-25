package com.kangwonapp.kangwonapi.notice;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.kangwonapp.kangwonapi.department.department;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Formula;

import javax.persistence.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class notice {

    @Id
    @Column(name="id_notice")
    private String id;

    @Column(name="id_department")
    private String departmentid;
    private String announce;
    private String name;
    @JsonFormat(pattern="yyyy-MM-dd")
    private String date;
    private String url;

    @Formula("(select department.name from department where department.id = id_department)")
    private String department_name;

}
