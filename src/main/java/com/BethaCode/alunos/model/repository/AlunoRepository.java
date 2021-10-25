package com.BethaCode.alunos.model.repository;

import com.BethaCode.alunos.model.entity.Aluno;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlunoRepository extends JpaRepository<Aluno, Integer> {

}
