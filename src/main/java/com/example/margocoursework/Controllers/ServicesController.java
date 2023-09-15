package com.example.margocoursework.Controllers;

import com.example.margocoursework.Factories.AbstractFactory;
import com.example.margocoursework.Factories.ServiceFactory;
import com.example.margocoursework.Model.Service;
import com.example.margocoursework.Model.User;
import com.example.margocoursework.Model.Record;
import com.example.margocoursework.Repositories.RecordRepository;
import com.example.margocoursework.Repositories.ServiceRepository;
import com.example.margocoursework.Repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;

@Controller
@RequestMapping("/services")
public class ServicesController {
    @ModelAttribute(name = "user")
    public User user(@AuthenticationPrincipal User user){
        return user;
    }
    @ModelAttribute(name = "map")
    public Map<String, String> map(){
        Map<String, String> map = new HashMap<>();
        map.put("hair", "Волосы");
        map.put("lashes", "Ресницы");
        map.put("brows", "Брови");
        map.put("skin", "Уход за кожей");
        map.put("massage", "Массаж");
        map.put("nails", "Ногти");
        return map;
    }
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RecordRepository recordRepository;
    @GetMapping
    public String getServices(Model model){
        List<Service> serviceList = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        model.addAttribute("serviceList", serviceList);
        return "client-services";
    }
    @GetMapping("/hair")
    public String getHair(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Волосы").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "hair");
        return "hair";
    }
    @GetMapping("/lashes")
    public String getLashes(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Ресницы").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "lashes");
        return "lashes";
    }
    @GetMapping("/brows")
    public String getBrows(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Брови").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "brows");
        return "brows";
    }
    @GetMapping("/skin")
    public String getSkin(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Уход за кожей").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "skin");
        return "skin";
    }
    @GetMapping("/massage")
    public String getMassage(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Массаж").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "massage");
        return "massage";
    }
    @GetMapping("/nails")
    public String getNails(Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection("Ногти").forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", "nails");
        return "nails";
    }
    @GetMapping("/sort")
    public String sortServices(@RequestParam(name = "section") String section, @RequestParam(required = false, name = "price") String price, @RequestParam(required = false, name = "from") Float from, @RequestParam(required = false, name = "to") Float to, Model model){
        if(price == null)
            price = "";
        List<Service> serviceList = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        AbstractFactory factory = new ServiceFactory();
        serviceList = factory.CreateSectionFilter().sectionFilter(section, serviceList);
        if(price.equals("asc"))
            serviceList.sort(new Comparator<Service>() {
                @Override
                public int compare(Service o1, Service o2) {
                    if(o1.getCost() > o2.getCost())
                        return 1;
                    else if(o1.getCost() < o2.getCost())
                        return -1;
                    else
                        return 0;
                }
            });
        if(price.equals("desc"))
            serviceList.sort(new Comparator<Service>() {
                @Override
                public int compare(Service o1, Service o2) {
                    if(o1.getCost() > o2.getCost())
                        return -1;
                    else if(o1.getCost() < o2.getCost())
                        return 1;
                    else
                        return 0;
                }
            });
        if(from != null && to != null)
            serviceList = serviceList.stream().filter(obj -> (obj.getCost() > from && obj.getCost() < to)).toList();
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", section);
        model.addAttribute("filtered", "filtered");
        return "client-services";
    }
    @PostMapping("/{section}/record")
    public String processRecord(@PathVariable(name = "section") String section, @RequestParam(name = "service") Long serviceId, @RequestParam(name = "master") Long masterId, @RequestParam(name = "date") LocalDate date, @RequestParam(name = "time") LocalTime time, @AuthenticationPrincipal User user, Model model) {
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("section", section);
        if(user==null)
            return "login";
        if(!user.getClass().getSimpleName().equals("Client")){
            model.addAttribute("notClient", "Запись может быть обработана только для клиентов!");
            return section;
        }
        User master = userRepository.findById(masterId).orElseThrow();
        for (var el : master.getRecords()){
            if(el.getDate().equals(date) && el.getTime().getHour() == time.getHour()) {
                model.addAttribute("exists", "Данное время уже забронировано!");
                return section;
            }
        }
        Record record = new Record();
        record.setService(serviceRepository.findById(serviceId).orElseThrow());
        record.setClient(user);
        record.setDate(date);
        record.setTime(time);
        master.getRecords().add(recordRepository.save(record));
        userRepository.save(master);
        model.addAttribute("recorded", "Ваша запись была сохранена!");
        return section;
    }
}
