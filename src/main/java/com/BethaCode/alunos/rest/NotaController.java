package com.BethaCode.alunos.rest;

import com.BethaCode.alunos.model.dto.NotaDTO;
import com.BethaCode.alunos.model.entity.Aluno;
import com.BethaCode.alunos.model.entity.Disciplina;
import com.BethaCode.alunos.model.entity.Nota;
import com.BethaCode.alunos.model.repository.AlunoRepository;
import com.BethaCode.alunos.model.repository.DisciplinaRepository;
import com.BethaCode.alunos.model.repository.NotaRepository;
import com.BethaCode.alunos.util.BigDecimalConverter;
import lombok.RequiredArgsConstructor;
import org.apache.tomcat.jni.Local;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping("/api/notas")
@RequiredArgsConstructor
public class NotaController {

    private final NotaRepository notaRepository;
    private final AlunoRepository alunoRepository;
    private final DisciplinaRepository disciplinaRepository;
    private final BigDecimalConverter bigDecimalConverter;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Nota salvar(@RequestBody NotaDTO notaDTO) {
        LocalDate dataNota = LocalDate.parse(notaDTO.getDataNota(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Integer idAluno = notaDTO.getIdAluno();
        Aluno aluno = alunoRepository
                        .findById(idAluno)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                "O aluno " + idAluno + " não existe em nosso sistema."));

        Integer idDisciplina = notaDTO.getIdDisciplina();
        Disciplina disciplina = disciplinaRepository
                                    .findById(idDisciplina)
                                    .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                                            "A disciplina " + idDisciplina + " não existe em nosso sistema."));

        Nota nota = new Nota();
        nota.setDataNota(dataNota);
        nota.setAluno(aluno);
        nota.setDisciplina(disciplina);
        nota.setNota(bigDecimalConverter.converter(notaDTO.getNota()));

        return notaRepository.save(nota);
    }

    @GetMapping("{id}")
    public Nota acharPorId(@PathVariable Integer id) {
        return notaRepository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota " + id + " não cadastrada"));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        notaRepository
                .findById(id)
                .map(nota -> {
                    notaRepository.delete(nota);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota " + id + " não cadastrada"));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @RequestBody NotaDTO dadoAtualizado) {

        LocalDate dataNota = LocalDate.parse(dadoAtualizado.getDataNota(), DateTimeFormatter.ofPattern("dd/MM/yyyy"));

        Integer idAluno = dadoAtualizado.getIdAluno();
        Aluno aluno = alunoRepository
                .findById(idAluno)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "O aluno " + idAluno + " não existe em nosso sistema"));

        Integer idDisciplina = dadoAtualizado.getIdDisciplina();
        Disciplina disciplina = disciplinaRepository
                .findById(idDisciplina)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,
                        "A disciplina " + idDisciplina + " não existe em nosso sistema"));

        notaRepository
                .findById(id)
                .map(nota -> {
                    nota.setDataNota(dataNota);
                    nota.setNota(bigDecimalConverter.converter(dadoAtualizado.getNota()));
                    nota.setDisciplina(disciplina);
                    nota.setAluno(aluno);
                    return notaRepository.save(nota);
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Nota " + id + " não cadastrada."));
    }

}
