package lt.fivethreads.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;

    @Column(name = "firstName")
    private String firstname;

    @Column(name = "lastName")
    private String lastName;

    @Column (name = "phone")
    private String phone;

    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JoinColumn(name = "officeId")
    @ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)
    @JsonIgnore
    private Office office;


    public User(){
    }
    public User(String firstname, String lastName, String email, String password, String phone) {
        this.firstname = firstname;
        this.lastName=lastName;
        this.email = email;
        this.password = password;
        this.phone=phone;
    }
}
