package com.inventcontrol.inventory_control.controllers;

import com.inventcontrol.inventory_control.entities.GrupoEstoque;
import com.inventcontrol.inventory_control.services.GrupoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoEstoqueController {

    private final GrupoEstoqueService service;

    //Injeção de dependência via construtor
    public GrupoEstoqueController(GrupoEstoqueService service){
        this.service = service;
    }

    // 1. Rota para Listar (GET http://localhost:8080/grupos)
    @GetMapping
    public List<GrupoEstoque> listar() {
        return service.listarTodos();
    }

    // 2. Rota para Criar (POST http://localhost:8080/grupos)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoEstoque criar(@RequestBody GrupoEstoque novoGrupo) {
        // O Controller apenas repassa para o Service validar e salvar
        return service.salvar(novoGrupo);
    }
}
