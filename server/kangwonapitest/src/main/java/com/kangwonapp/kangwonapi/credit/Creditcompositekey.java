package com.kangwonapp.kangwonapi.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@AllArgsConstructor
@NoArgsConstructor
public class Creditcompositekey implements Serializable {

    private String id;
    private Integer year;
    private String classification;
}
