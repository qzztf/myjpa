package cn.sexycode.mybatis.jpa.samples.model;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * @author qzz
 */
@Entity
@Table()
@Data
public class User {
    @Id
    private String id;

    @Column
    private String name;

//    private Integer age;
}
