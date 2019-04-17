package lt.fivethreads.entities;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;


@Entity
@Getter
@Setter
@Table(name = "USER", uniqueConstraints={@UniqueConstraint(columnNames = "email")})
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @NotNull(message="Email cannot be null.")
    @Email(message="Wrong email format.")
    @Column(name = "email")
    private String email;

    @NotNull (message = "Password cannot be null.")
    @Size(min=6, message = "Password must be longer that 6 symbols.")
    @Column(name = "password")
    private String password;

    @NotNull(message = "FirstName cannot be null.")
    @Column(name = "firstName")
    private String firstname;

    @NotNull(message = "LastName cannot be null.")
    @Column(name = "lastName")
    private String lastName;

    @NotNull(message="Phone cannot be null")
    @Column (name = "phone")
    private String phone;

    @NotNull(message="Role cannot be null")
    @ManyToMany
    @JoinTable(name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles = new HashSet<>();

    @JoinColumn(name = "officeId")
    @ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)
    private Office office;


    public User(){
    }
}
