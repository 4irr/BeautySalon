package com.example.margocoursework.Filters;

import com.example.margocoursework.Model.Record;

import java.util.ArrayList;
import java.util.List;

public class RecordsFilter implements Filter<Record>{
    @Override
    public List<Record> sectionFilter(String section, List<Record> collection) {
        if(!section.equals("Все"))
            return new ArrayList<>(collection.stream().filter(obj -> (obj.getService().getSection().equals(section))).toList());
        else
            return new ArrayList<>(collection);
    }
}
