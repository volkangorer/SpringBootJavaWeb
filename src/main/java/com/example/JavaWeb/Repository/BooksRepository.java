package com.example.JavaWeb.Repository;

import com.example.JavaWeb.Model.Book;
import org.springframework.data.repository.CrudRepository;

public interface BooksRepository extends CrudRepository<Book, Integer> {

}

