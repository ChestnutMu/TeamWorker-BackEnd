package com.info.model;


import javax.persistence.*;

/**
 * Created by king on 2017/8/20.
 */
@Entity
@Table(name = "test")
public class Test implements java.io.Serializable{
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "url")
    private String url;

    public Test() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
