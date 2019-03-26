package lt.fivethreads.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.internal.NotNull;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class Apartment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "number")
    private Long number;

    @JoinColumn(name = "officeId")
    @ManyToOne(targetEntity = Office.class, fetch = FetchType.LAZY)
    @NotNull
    @JsonIgnore
    private Office office;

    @Column(name = "officeId", insertable = false, updatable = false)
    private Long officeId;

    public Apartment(){}
    public Apartment(Long number) {
        this.number = number;
    }
}
