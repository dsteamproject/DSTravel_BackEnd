package com.example.repository;

import com.example.entity.MailDto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MailDtoRepository extends JpaRepository<MailDto,String>{
    
}
