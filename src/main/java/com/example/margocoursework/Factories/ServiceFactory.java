package com.example.margocoursework.Factories;

import com.example.margocoursework.Filters.Filter;
import com.example.margocoursework.Filters.ServiceFilter;

public class ServiceFactory implements AbstractFactory{
    @Override
    public Filter CreateSectionFilter() {
        return new ServiceFilter();
    }
}
