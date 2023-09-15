package com.example.margocoursework.Services;

import com.example.margocoursework.Model.Service;
import com.example.margocoursework.Repositories.ServiceRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class ServiceByIdConverter implements Converter<Long, Service> {
    @Autowired
    private ServiceRepository serviceRepository;

    @Override
    public Service convert(Long id) {
        return serviceRepository.findById(id).orElseThrow();
    }
}
