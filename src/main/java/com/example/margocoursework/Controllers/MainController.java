package com.example.margocoursework.Controllers;

import com.example.margocoursework.Factories.AbstractFactory;
import com.example.margocoursework.Factories.MasterFactory;
import com.example.margocoursework.Model.Record;
import com.example.margocoursework.Model.Service;
import com.example.margocoursework.Model.User;
import com.example.margocoursework.Repositories.RecordRepository;
import com.example.margocoursework.Repositories.ServiceRepository;
import com.example.margocoursework.Repositories.UserRepository;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class MainController {
    @Autowired
    PasswordEncoder encoder;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private RecordRepository recordRepository;
        @ModelAttribute(name = "user")
        public User user(@AuthenticationPrincipal User user){
            return user;
        }
    @ModelAttribute(name = "map")
    public Map<String, String> map(){
        Map<String, String> map = new HashMap<>();
        map.put("главная", "home");
        map.put("услуги", "client-services");
        map.put("каталог услуг", "client-services");
        map.put("мастера", "masters");
        map.put("контакты", "contacts");
        map.put("вход", "login");
        return map;
    }
    @GetMapping("/")
    public String getMain(){
        return "home";
    }
    @GetMapping("/home")
    public String getHome() {
        return "home";
    }
    @GetMapping("/login")
    public String login() {
        return "login";
    }
    @GetMapping("/contacts")
    public String getContacts(){
        return "contacts";
    }
    @GetMapping("/masters")
    public String getMasters(Model model) {
        List<User> masters = new ArrayList<>();
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("mastersList", masters);
        return "masters";
    }
    @GetMapping("/masters/sort")
    public String sortMasters(@RequestParam(name = "section") String section, @RequestParam(required = false, name = "alphabet") String alphabet, @RequestParam(required = false, name = "rating") String rating, Model model){
        if(alphabet == null)
            alphabet = "";
        if(rating == null)
            rating = "";
        List<User> mastersList = new ArrayList<>();
        userRepository.findAll().forEach(mastersList::add);
        mastersList = mastersList.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        AbstractFactory factory = new MasterFactory();
        mastersList = factory.CreateSectionFilter().sectionFilter(section, mastersList);
        if(alphabet.equals("asc"))
            mastersList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getName().compareTo(o2.getName());
                }
            });
        if(alphabet.equals("desc"))
            mastersList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return -o1.getName().compareTo(o2.getName());
                }
            });
        if(rating.equals("asc"))
            mastersList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o2.getRecords().size()-o1.getRecords().size();
                }
            });
        if(rating.equals("desc"))
            mastersList.sort(new Comparator<User>() {
                @Override
                public int compare(User o1, User o2) {
                    return o1.getRecords().size()-o2.getRecords().size();
                }
            });
        model.addAttribute("mastersList", mastersList);
        model.addAttribute("section", section);
        model.addAttribute("filtered", "filtered");
        return "masters";
    }
    @GetMapping("/search")
    public String processSearch(@RequestParam(name = "value") String value, @ModelAttribute(name = "map") HashMap<String, String> map, Model model) {
        value = value.trim().toLowerCase();
        if(map.get(value) == null)
            return "notFound";
        List<User> masters = new ArrayList<>();
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("mastersList", masters);
        List<Service> serviceList = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        model.addAttribute("serviceList", serviceList);
        return map.get(value);
    }
    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal User user, Model model) {
        User userProfile = user.clone();
        model.addAttribute("userProfile", userProfile);
        return "profile";
    }
    @GetMapping("/profile/edit")
    public String editProfile(@AuthenticationPrincipal User user, Model model) {
        User userProfile = user.clone();
        model.addAttribute("userProfile", userProfile);
        return "edit-profile";
    }
    @PostMapping("/profile/edit")
    public String editProfilePost(@AuthenticationPrincipal User user, @Valid User userToEdit, Errors errors, Model model){
        User userProfile = user.clone();
        model.addAttribute("userProfile", userProfile);
        if(errors.hasErrors()) {
            model.addAttribute("error", "Данные введены неверно");
            return "edit-profile";
        }
        User editUser = userRepository.findById(userToEdit.getUserID()).orElseThrow();
        editUser.setName(userToEdit.getName());
        editUser.setSurname(userToEdit.getSurname());
        editUser.setEmail(userToEdit.getEmail());
        editUser.setTelNum(userToEdit.getTelNum());
        userRepository.save(editUser);
        model.addAttribute("user", editUser);
        model.addAttribute("success", "Данные были изменены успешно");
        return "profile";
    }
    @GetMapping("/change-password")
    public String getChangePassword(@AuthenticationPrincipal User user, Model model) {
        User userProfile = user.clone();
        model.addAttribute("userProfile", userProfile);
        return "change-password";
    }
    @PostMapping("/change-password")
    public String postChangePassword(@AuthenticationPrincipal User authUser, @ModelAttribute(name = "user") User user, @RequestParam(name = "password") String password, @RequestParam(name = "repPassword") String repPassword, Model model) {
        User userProfile = authUser.clone();
        model.addAttribute("userProfile", userProfile);
        if(!password.equals(repPassword)) {
            model.addAttribute("error", "Пароли не совпадают");
            return "change-password";
        }
        User editUser = userRepository.findById(user.getUserID()).orElseThrow();
        editUser.setPassword(encoder.encode(password));
        userRepository.save(editUser);
        model.addAttribute("success", "Данные были изменены успешно");
        return "profile";
    }
    @GetMapping("/master/records")
    public String masterRecords() {
        return "master-records";
    }
    @GetMapping("/client/records")
    public String userRecords(Model model, @AuthenticationPrincipal User user) {
        List<Record> records = new ArrayList<>();
        recordRepository.findAllByClient(user).forEach(records::add);
        List<User> mastersList = new ArrayList<>();
        userRepository.findAll().forEach(mastersList::add);
        mastersList = mastersList.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        mastersList = new ArrayList<>(mastersList);
        Map<Record, User> recordUser = new HashMap<>();
        for (var el : mastersList){
            for(var rec : records){
                if(el.getRecords().contains(rec))
                    recordUser.put(rec, el);
            }
        }
        model.addAttribute("records", records);
        model.addAttribute("recordUser", recordUser);
        return "user-records";
    }
}
