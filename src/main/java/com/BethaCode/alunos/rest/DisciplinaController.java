package com.BethaCode.alunos.rest;

import com.BethaCode.alunos.model.entity.Disciplina;
import com.BethaCode.alunos.model.repository.DisciplinaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/disciplinas")
public class DisciplinaController {

    private final DisciplinaRepository repository;

    @Autowired
    public DisciplinaController(DisciplinaRepository repository) {
        this.repository = repository;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Disciplina salvar(@Valid @RequestBody Disciplina disciplina) {
        return repository.save(disciplina);
    }

    @GetMapping("{id}")
    public Disciplina acharPorId(@PathVariable Integer id) {
        return repository
                .findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina " + id + " não cadastrada."));
    }

    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletar(@PathVariable Integer id) {
        repository
                .findById(id)
                .map(disciplina ->  {
                    repository.delete(disciplina);
                    return Void.TYPE;
                })
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Disciplina " + id + " não cadastrada."));
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void atualizar(@PathVariable Integer id, @Valid @RequestBody Disciplina disciplinaAtualizada) {
        repository
                .findById(id)
                .map(disciplina -> {
                    disciplina.setDescricao(disciplinaAtualizada.getDescricao());
                    disciplina.setNumeroHoras(disciplinaAtualizada.getNumeroHoras());
                    return repository.save(disciplina);
                })
                .orElseThrow(() -> new HttpClientErrorException(HttpStatus.NOT_FOUND, "Disciplina " + id + " não cadastrada"));
    }

}
