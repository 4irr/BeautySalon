package com.example.margocoursework.Repositories;

import com.example.margocoursework.Model.Record;
import com.example.margocoursework.Model.User;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface RecordRepository extends CrudRepository<Record, Long> {
    public List<Record> findAllByClient(User client);
}
