package com.example.pidev.DAO.Repoitories;

import com.example.pidev.DAO.Entities.Category;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CategoryRepo extends JpaRepository<Category, Long> {

}
