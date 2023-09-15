package com.example.margocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Service {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long serviceId;
    private String section;
    @Size(min=5, message = "Минимальная длина значения - 5 символов")
    @Pattern(regexp = "^[^0-9]*$", message = "Поле может содержать только символы")
    private String name;
    private float cost;
}
