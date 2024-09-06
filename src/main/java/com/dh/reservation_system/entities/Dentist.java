package com.dh.reservation_system.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name = "odontologists")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Dentist {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String lastName;
    private String licenseMedical;

    @OneToMany(mappedBy = "dentist")
    @JsonIgnore
    private List<Appointment> appointments;
}
