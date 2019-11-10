package cn.sexycode.myjpa.samples.model;

//import com.baomidou.mybatisplus.annotation.TableName;
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
//@TableName
@Data
public class User {
    @Id
    private String id;

    @Column(name = "full_name")
    private String fullName;

//    private Integer age;
}
