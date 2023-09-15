package com.example.margocoursework.Filters;

import java.util.List;

public interface Filter<T> {
    public List<T> sectionFilter(String section, List<T> collection);
}
