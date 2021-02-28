package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

@Entity(name="usergroup")
public class Group implements Serializable {

    public Group(){}

    public Group(String name, String description) {
        this.name=name;
        this.description = description;
    }

    @JsonView(Views.Private.class)
    public Long getId(){return id;}

    public void setOwner(User user){this.owner=user;}

    @JsonView(Views.Private.class)
    public String getName(){return name;}

    @JsonView(Views.Private.class)
    public String getDescription(){return description;}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "fk_user")
    private User owner;

    @Column(name = "fk_user", insertable = false, updatable = false)
    private Long userId;

}
