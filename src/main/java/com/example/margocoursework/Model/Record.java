package com.example.margocoursework.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Record {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long recordId;
    @ManyToOne
    private Service service;
    @OneToOne
    private User client;
    private LocalDate date;
    private LocalTime time;

}
