package com.example.JavaWeb;

import com.example.JavaWeb.Model.Userss;
import com.example.JavaWeb.Repository.UserbookRepository;
import com.example.JavaWeb.Repository.UserssRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

import java.util.List;


@EnableWebSecurity
public class SpringSecurityConfig {

    public static final String roleUser = "USER";
    public static final String roleAdmin = "ADMIN";
    public static final String urlHome = "/";
    public static final String urlAdmin = "/admin";

    @Autowired
    UserssRepository userssRepository;
    @Bean
    public UserDetailsService userDetailsService() throws Exception {
        // ensure the passwords are encoded properly
        User.UserBuilder users = User.withDefaultPasswordEncoder();
        InMemoryUserDetailsManager manager = new InMemoryUserDetailsManager();

        List<Userss> userss = (List<Userss>) userssRepository.findAll();

        for(int i = 0; i<userss.size();i++){
            Userss userss1 = userss.get(i);
            if (userss1.getIs_admin() == 0){

                manager.createUser(users.username(userss1.getUsername()).password(userss1.getPassword()).roles(roleUser).build());
            }else {
                manager.createUser(users.username(userss1.getUsername()).password(userss1.getPassword()).roles(roleAdmin).build());
            }
        }
        ;

        return manager;
    }

    @Bean
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(urlHome).hasAnyRole(roleUser,roleAdmin)
                .antMatchers(urlAdmin).hasRole(roleAdmin)
                .anyRequest().authenticated()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()

        ;

        return http.build();

    }

    /*@Bean
    @Order(1)
    public SecurityFilterChain apiFilterChain(HttpSecurity http) throws Exception {
        http
                .antMatcher("/admin")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasRole("ADMIN")
                )
                .httpBasic(withDefaults());
        return http.build();
    }

    @Bean
    @Order(2)
    public SecurityFilterChain apiFilterChain2(HttpSecurity http) throws Exception {
        http
                .antMatcher("/")
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().hasAnyRole("ADMIN","USER")
                )
                .httpBasic(withDefaults());
        return http.build();
    }


    @Bean
    public SecurityFilterChain formLoginFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                        .anyRequest().authenticated()
                )
                .formLogin(withDefaults());
        return http.build();
    }*/








}
