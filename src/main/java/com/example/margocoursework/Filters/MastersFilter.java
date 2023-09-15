package com.example.margocoursework.Filters;

import com.example.margocoursework.Model.User;

import java.util.ArrayList;
import java.util.List;

public class MastersFilter implements Filter<User>{
    @Override
    public List<User> sectionFilter(String section, List<User> collection) {
        if(!section.equals("Все"))
            return new ArrayList<>(collection.stream().filter(obj -> (obj.getSection().equals(section))).toList());
        else
            return new ArrayList<>(collection);
    }
}
