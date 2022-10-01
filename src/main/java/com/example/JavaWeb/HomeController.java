package com.example.JavaWeb;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.Banner;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.sound.midi.ShortMessage;
import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private BooksRepository repo;

    @Autowired UserBookRepository userBookRepository;



    String username;

    @GetMapping("/")
    public String user(Model model){
        List<Books> books = (List<Books>) repo.findAll();
        model.addAttribute("books", books);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((UserDetails)principal).getAuthorities().toString();
        System.out.println(role);

        if(role.contains("ROLE_USER")){
            return "user";

        }else{
            return "redirect:/admin";
        }


    }

    @GetMapping("/showFormForSelect/{id}")
    public String selectForm(@PathVariable(value = "id") Integer id, Model model){
        Optional<Books> books = repo.findById(id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        username = ((UserDetails)principal).getUsername();


        UserBook userBook = new UserBook();
        userBook.setUsers_name(username);
        userBook.setBook_name(books.get().getBook_name());
        userBookRepository.save(userBook);

        return "redirect:/result";

    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }

    @PostMapping("/add")
    public String addUserBook(@ModelAttribute("userBook") UserBook userBook){
        userBookRepository.save(userBook);


        return "add";
    }


    @GetMapping("/admin")
    public String home(Model model) {
        List<Books> books = (List<Books>) repo.findAll();
        List<UserBook> userBooks = (List<UserBook>) userBookRepository.findAll();
        model.addAttribute("books", books);
        model.addAttribute("userBooks",userBooks);


        return "admin";
    }




    /*@RequestMapping("/")
    public String home(){

        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();

        if (principal instanceof UserDetails) {
            String username = ((UserDetails)principal).getUsername();
            String role = ((UserDetails)principal).getAuthorities().toString();
            System.out.println("here");
            System.out.println(username);
            System.out.println(role);
        } else {
            String username = principal.toString();
            System.out.println("here1");
            System.out.println(username);
        }
        return "home";

    }*/
}
