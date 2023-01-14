package com.example.JavaWeb.Controller;

import com.example.JavaWeb.Model.Book;
import com.example.JavaWeb.Model.Reserve;
import com.example.JavaWeb.Model.Userbook;
import com.example.JavaWeb.Repository.BooksRepository;
import com.example.JavaWeb.Repository.UserbookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
public class HomeController {
    public static final String resultOk = "ok";
    public static final String resultError = "error";
    public static final String resultDateNull = "dateNull";

    @Autowired BooksRepository booksRepository;

    @Autowired UserbookRepository userBookRepository;
    public static String username;

    @GetMapping("/")
    public String user(Model model){
        List<Book> books = (List<Book>) booksRepository.findAll();
        model.addAttribute("books", books);
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String role = ((UserDetails)principal).getAuthorities().toString();
        username = ((UserDetails)principal).getUsername();
        model.addAttribute("reserve",new Reserve());

        if(role.contains("ROLE_USER")){
            return "user";

        }else{
            return "redirect:/admin";
        }
    }

    @PostMapping("/reserve/{id}")
    public String createReserve(@PathVariable(value = "id") Integer id,@ModelAttribute Reserve reserve, HttpSession session){
        Book book = booksRepository.findById(id).get();


        if(book.getRemain()>0){

            if (reserve.getDate().equals("")){
                session.setAttribute("result",resultDateNull);
            }else {
                Userbook userBook = new Userbook();
                userBook.setBookname(book.getName());
                userBook.setUsername(username);
                userBook.setIs_reserved(1);
                userBook.setReservationDate(reserve.getDate());
                userBookRepository.save(userBook);

                book.setRemain(book.getRemain()-1);
                booksRepository.save(book);

                session.setAttribute("result",resultOk);

            }

        }else {
            session.setAttribute("result",resultError);
        }




        return "result";
    }


    @GetMapping("/showFormForSelect/{id}")
    public String selectForm(@PathVariable(value = "id") Integer id, HttpSession session){
        Book book = booksRepository.findById(id).get();

        if(book.getRemain()>0){
            Userbook userBook = new Userbook();
            userBook.setBookname(book.getName());
            userBook.setUsername(username);
            userBook.setIs_reserved(0);
            userBookRepository.save(userBook);

            book.setRemain(book.getRemain()-1);
            booksRepository.save(book);

            session.setAttribute("result",resultOk);

        }else {
            session.setAttribute("result",resultError);
        }




        return "redirect:/result";

    }

    @GetMapping("/result")
    public String result(){
        return "result";
    }



    @GetMapping("/admin")
    public String home(Model model) {
        List<Book> books = (List<Book>) booksRepository.findAll();
        model.addAttribute("books", books);


        return "admin";
    }

    @GetMapping("/customer")
    public String customer(Model model) {
        List<Userbook> userBooks = (List<Userbook>) userBookRepository.findAll();
        model.addAttribute("userBooks",userBooks);

        return "customer";
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
