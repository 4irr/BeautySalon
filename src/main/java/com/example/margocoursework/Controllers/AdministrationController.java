package com.example.margocoursework.Controllers;

import com.example.margocoursework.Factories.AbstractFactory;
import com.example.margocoursework.Factories.RecordFactory;
import com.example.margocoursework.Model.*;
import com.example.margocoursework.Model.Record;
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

import java.time.LocalDate;
import java.util.*;

@Controller
@RequestMapping("/administration")
public class AdministrationController {
    @Autowired
    private PasswordEncoder encoder;
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
    @ModelAttribute(name = "service")
    public Service service(){
        return new Service();
    }
    @ModelAttribute(name = "master")
    public Master master(){return new Master();}
    @ModelAttribute(name = "admin")
    public Admin admin(){return new Admin();}
    @GetMapping
    public String getAdministration(){
        return "administration";
    }
    @GetMapping("/add-master")
    public String addMasterGet(){
        return "master-add";
    }
    @PostMapping("/add-master")
    public String processMaster(@Valid Master master, Errors errors, Model model) {
        if(errors.hasErrors())
            return "master-add";
        master.setPassword(encoder.encode(master.getPassword()));
        if(userRepository.findByUsername(master.getUsername()) != null) {
            model.addAttribute("exists", "Пользователь с таким логином уже зарегистрирован!");
            return "master-add";
        }
        model.addAttribute("success", "Пользователь добавлен успешно");
        userRepository.save(master);
        return "master-add";
    }
    @GetMapping("/add-admin")
    public String addAdminGet(){
        return "admin-add";
    }
    @PostMapping("/add-admin")
    public String processAdmin(@Valid Admin admin, Errors errors, Model model) {
        if(errors.hasErrors())
            return "admin-add";
        admin.setPassword(encoder.encode(admin.getPassword()));
        if(userRepository.findByUsername(admin.getUsername()) != null) {
            model.addAttribute("exists", "Пользователь с таким логином уже зарегистрирован!");
            return "admin-add";
        }
        model.addAttribute("success", "Пользователь добавлен успешно");
        userRepository.save(admin);
        return "admin-add";
    }
    @GetMapping("/services")
    public String getServices(Model model) {
        List<Service> serviceList = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        model.addAttribute("serviceList", serviceList);
        return "services";
    }
    @GetMapping("/services/add-service")
    public String addServiceGet(){
        return "add-service";
    }
    @PostMapping("/services/add-service")
    public String addServicePost(@Valid Service service, Errors errors, Model model){
        if(errors.hasErrors())
            return "add-service";
        serviceRepository.save(service);
        return "redirect:/administration/services";
    }
    @GetMapping("/services/{id}/details")
    public String getServiceDetails(@PathVariable(name = "id") Long id, Model model){
        if(!serviceRepository.existsById(id))
            return "redirect:/administration/services";
        Service service = serviceRepository.findById(id).orElseThrow();
        model.addAttribute("service", service);
        return "service-details";
    }
    @GetMapping("/services/{id}/edit")
    public String editServiceGet(@PathVariable(name = "id") Long id, Model model){
        if(!serviceRepository.existsById(id))
            return "redirect:/administration/services";
        Service service = serviceRepository.findById(id).orElseThrow();
        model.addAttribute("res", service);
        return "edit-service";
    }
    @PostMapping("/services/{id}/edit")
    public String editServicePost(@PathVariable(name = "id") Long id, @Valid Service service, Errors errors, Model model){
        if(errors.hasErrors()) {
            Service editService = serviceRepository.findById(id).orElseThrow();
            model.addAttribute("res", editService);
            model.addAttribute("errors", "Данные введены неверно");
            return "edit-service";
        }
        Service editService = serviceRepository.findById(id).orElseThrow();
        editService.setSection(service.getSection());
        editService.setName(service.getName());
        editService.setCost(service.getCost());
        serviceRepository.save(editService);
        return "redirect:/administration/services";
    }
    @PostMapping("/services/{id}/remove")
    public String removeService(@PathVariable(name = "id") Long id) {
        if(!serviceRepository.existsById(id))
            return "redirect:/administration/services";
        serviceRepository.deleteById(id);
        return "redirect:/administration/services";
    }
    @GetMapping("/users")
    public String getUsers(Model model) {
        List<User> userList = new ArrayList<>();
        userRepository.findAll().forEach(userList::add);
        model.addAttribute("userList", userList);
        return "users";
    }
    @GetMapping("/users/{id}/details")
    public String getUsersDetails(@PathVariable(name = "id") Long id, Model model){
        if(!userRepository.existsById(id))
            return "redirect:/administration/users";
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("userDetails", user);
        return "userDetails";
    }
    @GetMapping("/users/{id}/edit")
    public String editUserGet(@PathVariable(name = "id") Long id, Model model){
        if(!userRepository.existsById(id))
            return "redirect:/administration/users";
        User user = userRepository.findById(id).orElseThrow();
        model.addAttribute("res", user);
        return "edit-user";
    }   
    @PostMapping("/users/{id}/edit")
    public String editUserPost(@PathVariable(name = "id") Long id, @Valid User user, Errors errors, @RequestParam(required = false, name = "section") String section, Model model){
        User editUser = userRepository.findById(id).orElseThrow();
        model.addAttribute("res", editUser);
        if(errors.hasErrors()) {
            model.addAttribute("errors", "Данные введены неверно");
            return "edit-user";
        }
        editUser.setName(user.getName());
        editUser.setSurname(user.getSurname());
        editUser.setEmail(user.getEmail());
        editUser.setTelNum(user.getTelNum());
        if(section != null) {
            if(userRepository.findById(id).orElseThrow().getRecords().size() != 0) {
                model.addAttribute("exists", "Перед изменением специализации мастера необходимо очистить записи!");
                return "edit-user";
            }
            editUser.setSection(section);
        }
        userRepository.save(editUser);
        return "redirect:/administration/users";
    }
    @PostMapping("/users/{id}/remove")
    public String removeUser(@PathVariable(name = "id") Long id, Model model) {
        if(!userRepository.existsById(id))
            return "redirect:/administration/users";
        User user = userRepository.findById(id).orElseThrow();
        if(user.getClass().getSimpleName().equals("Master")) {
            if (user.getRecords().size() != 0) {
                model.addAttribute("recordsExists", "Перед удалением мастера необходимо удалить связанные записи!");
                model.addAttribute("userDetails", user);
                return "userDetails";
            }
        }
        else {
            List<Record> records = recordRepository.findAllByClient(user);
            if(records.size()!=0){
                model.addAttribute("recordsExists", "Перед удалением клиента необходимо удалить связанные записи!");
                model.addAttribute("userDetails", user);
                return "userDetails";
            }
        }
        userRepository.deleteById(id);
        return "redirect:/administration/users";
    }
    @GetMapping("/records")
    public String getRecords(Model model) {
        List<Record> recordList = new ArrayList<>();
        recordRepository.findAll().forEach(recordList::add);
        model.addAttribute("recordList", recordList);
        return "records";
    }
    @GetMapping("/records/sort")
    public String sortServices(@RequestParam(name = "section") String section, @RequestParam(required = false, name = "date") String date, @RequestParam(required = false, name = "from") LocalDate from, @RequestParam(required = false, name = "to") LocalDate to, Model model) {
        if(date == null)
            date = "";
        List<Record> recordList = new ArrayList<>();
        recordRepository.findAll().forEach(recordList::add);
        AbstractFactory factory = new RecordFactory();
        recordList = factory.CreateSectionFilter().sectionFilter(section, recordList);
        if(date.equals("asc"))
            recordList.sort(new Comparator<Record>() {
                @Override
                public int compare(Record o1, Record o2) {
                    if(o1.getDate().isAfter(o2.getDate()))
                        return 1;
                    else if(o1.getDate().isBefore(o2.getDate()))
                        return -1;
                    else
                        return 0;
                }
            });
        if(date.equals("desc"))
            recordList.sort(new Comparator<Record>() {
                @Override
                public int compare(Record o1, Record o2) {
                    if(o1.getDate().isBefore(o2.getDate()))
                        return 1;
                    else if(o1.getDate().isAfter(o2.getDate()))
                        return -1;
                    else
                        return 0;
                }
            });
        if(from != null && to != null)
            recordList = recordList.stream().filter(obj -> ((obj.getDate().isAfter(from) || obj.getDate().equals(from)) && (obj.getDate().isBefore(to) || obj.getDate().equals(to)))).toList();
        model.addAttribute("recordList", recordList);
        model.addAttribute("section", section);
        return "records";
    }
    @GetMapping("/records/{id}/details")
    public String getRecordDetails(@PathVariable(name = "id") Long id, Model model){
        if(!recordRepository.existsById(id))
            return "redirect:/administration/records";
        Record record = recordRepository.findById(id).orElseThrow();
        List<User> masters = new ArrayList<>();
        userRepository.findAll().forEach(masters::add);
        for(var el : masters)
            if (el.getClass().getSimpleName().equals("Master"))
                if(el.getRecords().contains(record))
                    model.addAttribute("master", el);
        model.addAttribute("record", record);
        return "recordDetails";
    }
    @GetMapping("/records/{id}/edit")
    public String editRecordGet(@PathVariable(name = "id") Long id, Model model){
        if(!recordRepository.existsById(id))
            return "redirect:/administration/records";
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        Record record = recordRepository.findById(id).orElseThrow();
        userRepository.findAll().forEach(masters::add);
        serviceRepository.findAllBySection(record.getService().getSection()).forEach(serviceList::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("res", record);
        return "edit-record";
    }
    @PostMapping("/records/{id}/edit")
    public String editRecordPost(@PathVariable(name = "id") Long id, Record record, @RequestParam(name = "master") Long masterId, Model model){
        List<Service> serviceList = new ArrayList<>();
        List<User> masters = new ArrayList<>();
        serviceRepository.findAllBySection(record.getService().getSection()).forEach(serviceList::add);
        Record editRecord = recordRepository.findById(id).orElseThrow();
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        model.addAttribute("masters", masters);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("res", editRecord);
        User master = userRepository.findById(masterId).orElseThrow();
        for (var el : master.getRecords()){
            if(el.getDate().equals(record.getDate()) && el.getTime().getHour() == record.getTime().getHour() && !editRecord.equals(el)) {
                model.addAttribute("exists", "Данное время уже забронировано!");
                return "edit-record";
            }
        }
        User editMaster = new Master();
        for (var el : masters)
            for (var rec : el.getRecords())
                if(rec.equals(editRecord))
                    editMaster = el;
        if(master.getUserID() != editMaster.getUserID()) {
            editMaster.getRecords().remove(editRecord);
            userRepository.save(editMaster);
            master.getRecords().add(editRecord);
            userRepository.save(master);
        }
        editRecord.setService(record.getService());
        editRecord.setDate(record.getDate());
        editRecord.setTime(record.getTime());
        recordRepository.save(editRecord);
        model.addAttribute("recorded", "Запись была сохранена!");
        return "edit-record";
    }
    @PostMapping("/records/{id}/remove")
    public String removeRecord(@PathVariable(name = "id") Long id, Model model) {
        Record record = recordRepository.findById(id).orElseThrow();
        List<User> masters = new ArrayList<>();
        userRepository.findAll().forEach(masters::add);
        masters = masters.stream().filter(obj -> (obj.getClass()).getSimpleName().equals("Master")).toList();
        masters = new ArrayList<>(masters);
        for (var el : masters)
            for (var rec : el.getRecords())
                if(rec.equals(record)) {
                    el.getRecords().remove(record);
                    userRepository.save(el);
                }
        recordRepository.deleteById(id);
        return "redirect:/administration/records";
    }
    @GetMapping("/profit")
    public String getProfit(){
        return "profit";
    }
    @PostMapping("/profit")
    public String processProfit(@RequestParam(name = "beginning") LocalDate beginning, @RequestParam(name = "ending") LocalDate ending, Model model){
        List<Record> records = new ArrayList<>();
        recordRepository.findAll().forEach(records::add);
        float sum = 0;
        for(var el : records){
            if( (el.getDate().isAfter(beginning) || el.getDate().equals(beginning)) && (el.getDate().isBefore(ending) || el.getDate().equals(ending) ))
                sum += el.getService().getCost();
        }
        if(sum==0)
            model.addAttribute("cost", "0");
        else
            model.addAttribute("cost", sum);
        return "profit";
    }
    @GetMapping("/statistics")
    public String getStatistics(){
        return "statistics";
    }
    @PostMapping("/statistics")
    public String postStatistics(@RequestParam(name = "beginning") LocalDate beginning, @RequestParam(name = "ending") LocalDate ending, Model model){
        List<Service> serviceList = new ArrayList<>();
        serviceRepository.findAll().forEach(serviceList::add);
        List<Record> records = new ArrayList<>();
        recordRepository.findAll().forEach(records::add);
        records = new ArrayList<>(records.stream().filter(obj -> ( (obj.getDate().isAfter(beginning) || obj.getDate().equals(beginning))) && (obj.getDate().isBefore(ending) || obj.getDate().equals(ending))).toList());
        Map<Service, Integer> serviceMap = new HashMap<>();
        Map<String, Long> sectionMap = new HashMap<>();
        for(var el : serviceList) {
            Integer sum = 0;
            for(var rec : records){
                if(rec.getService().equals(el))
                    sum++;
            }
            serviceMap.put(el, sum);
        }
        sectionMap.put("Волосы", records.stream().filter(obj -> (obj.getService().getSection().equals("Волосы"))).count());
        sectionMap.put("Ресницы", records.stream().filter(obj -> (obj.getService().getSection().equals("Ресницы"))).count());
        sectionMap.put("Брови", records.stream().filter(obj -> (obj.getService().getSection().equals("Брови"))).count());
        sectionMap.put("Уход за кожей", records.stream().filter(obj -> (obj.getService().getSection().equals("Уход за кожей"))).count());
        sectionMap.put("Массаж", records.stream().filter(obj -> (obj.getService().getSection().equals("Массаж"))).count());
        sectionMap.put("Ногти", records.stream().filter(obj -> (obj.getService().getSection().equals("Ногти"))).count());
        model.addAttribute("sectionMap", sectionMap);
        model.addAttribute("serviceList", serviceList);
        model.addAttribute("serviceMap", serviceMap);
        model.addAttribute("processed", true);
        return "statistics";
    }
}
