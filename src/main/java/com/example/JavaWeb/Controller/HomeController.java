package com.example.JavaWeb.Controller;

import com.example.JavaWeb.Model.Book;
import com.example.JavaWeb.Model.User;
import com.example.JavaWeb.Repository.BooksRepository;
import com.example.JavaWeb.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class HomeController {
    @Autowired
    private BooksRepository booksRepository;

    @Autowired UserRepository userRepository;



    String username;

    @GetMapping("/")
    public String user(Model model){
        List<Book> books = (List<Book>) booksRepository.findAll();
        model.addAttribute("books", books);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((UserDetails)principal).getAuthorities().toString();


        if(role.contains("ROLE_USER")){
            return "user";

        }else{
            return "redirect:/admin";
        }


    }

    @GetMapping("/showFormForSelect/{id}")
    public String selectForm(@PathVariable(value = "id") Integer id, Model model){
        Optional<Book> book = booksRepository.findById(id);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        username = ((UserDetails)principal).getUsername();


        User userBook = new User();
        userBook.setBookname(book.get().getName());
        userBook.setUsername(username);
        userRepository.save(userBook);

        return "redirect:/result";

    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }

    @PostMapping("/add")
    public String addUserBook(@ModelAttribute("userBook") User userBook){
        userRepository.save(userBook);


        return "add";
    }


    @GetMapping("/admin")
    public String home(Model model) {
        List<Book> books = (List<Book>) booksRepository.findAll();
        List<User> userBooks = (List<User>) userRepository.findAll();
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
