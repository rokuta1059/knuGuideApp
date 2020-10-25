package com.kangwonapp.kangwonapi.schedule;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class schedule {

    @Id
    private Integer id;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date startdate;
    @JsonFormat(pattern="yyyy-MM-dd")
    private Date enddate;
    private String description;
}
