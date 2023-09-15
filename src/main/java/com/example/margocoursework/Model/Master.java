package com.example.margocoursework.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Master extends User {
    public Master(String username, String password, String name, String surname, String email, String telNum){
        super(username, password, name, surname, email, telNum);
    }
    public Master(Master master){
        super(master);
        this.section = master.section;
        this.recordList = new ArrayList<>(master.recordList);
    }
    @Override
    public User clone() {
        return new Master(this);
    }

    private String section;
    @OneToMany(fetch = FetchType.EAGER)
    private List<Record> recordList = new ArrayList<>();
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_MASTER"));
    }
    @Override
    public List<Record> getRecords() {
        return this.recordList;
    }
    @Override
    public String getSection() { return this.section; }
    @Override
    public void setSection(String section) { this.section = section; }
}
