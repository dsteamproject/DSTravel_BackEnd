package com.example.repository;

import java.util.List;

import com.example.entity.City;
import com.example.entity.Worldcup;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface WorldcupRepository extends JpaRepository<Worldcup,Long>{

    public List<Worldcup> findByTd_city(City city);

    public int countByTd_city(City city);

    public int countByTd_no(Integer no);

}
