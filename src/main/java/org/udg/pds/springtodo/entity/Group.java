package org.udg.pds.springtodo.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonView;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;

@Entity(name="usergroup")
public class Group implements Serializable {

    public Group(){}

    public Group(String name, String description) {
        this.name=name;
        this.description = description;
        this.members=new ArrayList<>();
    }

    @JsonView(Views.Private.class)
    public Long getId(){return id;}

    public void setOwner(User user){this.owner=user;}

    public void addMember(User user){members.add(user);}

    @JsonView(Views.Private.class)
    public String getName(){return name;}

    @JsonView(Views.Private.class)
    public String getDescription(){return description;}

    @JsonIgnore
    public User getOwner(){return owner;}

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

    @ManyToMany(cascade = CascadeType.ALL)
    private Collection<User> members;

}
