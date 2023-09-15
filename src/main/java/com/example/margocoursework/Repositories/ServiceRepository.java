package com.example.margocoursework.Repositories;

import com.example.margocoursework.Model.Service;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface ServiceRepository extends CrudRepository<Service, Long> {
    List<Service> findAllBySection(String section);
}
