package com.example.SysEve.dao.entities;



import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@ToString
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Setter

@Entity
@Table(name="participants")
public class Participant {
    
    

    public Participant(Long id, String firstName, String lastName, String birthday, String email, int number,
            String image) {
        this.id = id;
        this.firstName = firstName;
        this.lastName = lastName;
        this.birthday = birthday;
        this.email = email;
        this.number = number;
        this.image = image;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String firstName;
    private String lastName;
    private String birthday;
    private String email;
    private int number;
    private String image;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name= "event_participant", 
        joinColumns = @JoinColumn(name= "event_id", 
        referencedColumnName = "id"),
        inverseJoinColumns = @JoinColumn(name = "participant_id", 
        referencedColumnName = "id")
        )
private List<Event> events = new ArrayList<>(); 
    
   


}