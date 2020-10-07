package com.upgrad.mtb.daos;

import com.upgrad.mtb.entity.Theatre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository("theatreDAO")
public interface TheatreDAO extends JpaRepository<Theatre, Integer> {
}
