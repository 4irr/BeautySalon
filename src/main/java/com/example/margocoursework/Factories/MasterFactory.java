package com.example.margocoursework.Factories;

import com.example.margocoursework.Filters.Filter;
import com.example.margocoursework.Filters.MastersFilter;

public class MasterFactory implements AbstractFactory{
    @Override
    public Filter CreateSectionFilter() {
        return new MastersFilter();
    }
}
