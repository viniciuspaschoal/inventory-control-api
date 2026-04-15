package com.inventcontrol.inventory_control.controllers;

import com.inventcontrol.inventory_control.entities.Deposito;
import com.inventcontrol.inventory_control.entities.GrupoEstoque;
import com.inventcontrol.inventory_control.services.GrupoEstoqueService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grupos")
public class GrupoEstoqueController {

    private final GrupoEstoqueService grupoEstoqueService;

    //Injeção de dependência via construtor
    public GrupoEstoqueController(GrupoEstoqueService grupoEstoqueService){
        this.grupoEstoqueService = grupoEstoqueService;
    }

    // 1. Rota para Listar (GET http://localhost:8080/grupos)
    @GetMapping
    public List<GrupoEstoque> listar() {
        return grupoEstoqueService.listarTodos();
    }

    // 2. Rota para Criar (POST http://localhost:8080/grupos)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public GrupoEstoque criar(@RequestBody GrupoEstoque novoGrupo) {
        // O Controller apenas repassa para o Service validar e salvar
        return grupoEstoqueService.salvar(novoGrupo);
    }

    // DELETE - Inativar
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        // Passa o id para o inactivate no Service
        grupoEstoqueService.inactivate(id);
    }

    // PUT - UPDATE
    @PutMapping("/{id}")
    public ResponseEntity<GrupoEstoque> update(@PathVariable Long id, @PathVariable GrupoEstoque grupoEstoque ){
        return ResponseEntity.ok(grupoEstoqueService.update(id, grupoEstoque));
    }
}
