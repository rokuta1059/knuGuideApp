package com.kangwonapp.kangwonapi.credit;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@IdClass(Creditcompositekey.class)
public class credit {

    // 학과 ID
    @Id
    private String id;

    // 년도
    @Id
    private Integer year;

    // 구분값
    // 단일전공: single
    // 복수전공: dm
    // 부전공 : minor
    // 공학인증 : engineering
    @Id
    private String classification;

    // 졸업 학점
    private Integer totalcredit;

    // 기초교양
    private Integer basic;
    // 균형교양
    private Integer balanced;
    // 특화교양
    private Integer specialized;
    // 대학별교양
    private Integer college;
    // 전공필수
    private Integer required;
    // 전공선택
    private Integer select;
    // 심화전공
    private Integer deep;
    // 교직이수
    private Integer teaching;
    // 자유선택택
   private Integer freechoice;
}
