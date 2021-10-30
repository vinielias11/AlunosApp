package com.BethaCode.alunos.model.repository;

import com.BethaCode.alunos.model.entity.Nota;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NotaRepository extends JpaRepository<Nota, Integer> {
}
