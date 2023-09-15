package com.example.margocoursework.Filters;

import com.example.margocoursework.Model.Service;

import java.util.ArrayList;
import java.util.List;

public class ServiceFilter implements Filter<Service>{
    @Override
    public List<Service> sectionFilter(String section, List<Service> collection) {
        if(section.equals("Все"))
            return collection;
        else
            return new ArrayList<>(collection.stream().filter(obj -> (obj.getSection().equals(section))).toList());
    }
}
