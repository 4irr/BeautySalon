package com.example.margocoursework.Model;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
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
public class Client extends User {
    public Client(String username, String password, String name, String surname, String email, String telNum){
        super(username, password, name, surname, email, telNum);
    }
    public Client(Client client){
        super(client);
    }
    @Override
    public User clone() {
        return new Client(this);
    }
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return Arrays.asList(new SimpleGrantedAuthority("ROLE_USER"));
    }
}
