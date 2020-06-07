package cn.sexycode.myjpa.samples.model;

import lombok.Data;

import javax.persistence.*;

/**
 * @author qzz
 */
@Entity
@Table
@Data
public class User {
    @Id
    @GeneratedValue
    private String id;

    @Column
    private String fullName;

//    private Integer age;
}
