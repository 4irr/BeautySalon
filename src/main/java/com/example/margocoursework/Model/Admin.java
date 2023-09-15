package com.example.margocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
public class Admin extends User {
    public Admin(String username, String password, String name, String surname, String email, String telNum){
        super(username, password, name, surname, email, telNum);
    }
    public Admin(Admin admin){
        super(admin);
    }
    @Override
    public User clone() {
        return new Admin(this);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));
    }
}
