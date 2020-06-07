package cn.sexycode.myjpa.samples.model;

import javax.persistence.*;

/**
 * @author qzz
 */
@Entity
@Table
public class User {
    @Id
    @GeneratedValue
    private String id;

    @Column
    private String fullName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    @Override
    public String toString() {
        return "User{" + "id='" + id + '\'' + ", fullName='" + fullName + '\'' + '}';
    }
}
