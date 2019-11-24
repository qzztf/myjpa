package cn.sexycode;

import javax.persistence.*;

@Entity
@Table
public class User {
    @Column(name = "full_name")
    private String name;

    @Id
    @GeneratedValue
    private String id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
