package com.inventcontrol.inventory_control.controllers;

import com.inventcontrol.inventory_control.entities.Deposito;
import com.inventcontrol.inventory_control.services.DepositoService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/deposito")
public class DepositoController {

    private final DepositoService depositoService;

    //Injeção de dependencia via construtor
    public DepositoController(DepositoService depositoService){
        this.depositoService = depositoService;
    }

    // 1. Rota para Listar (GET http://localhost:8080/deposito)
    @GetMapping
    public List<Deposito> listar(){
        return depositoService.listAll();
    }

    // 2. Rota para Criar (POST http://localhost:8080/deposito)
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Deposito create(@RequestBody Deposito newDeposit){
        // O Controller apenas repassa para o Service validar e salvar
        return depositoService.saveDeposit(newDeposit);
    }

    // 2. Rota para Deletar/Inativar um depósito (DELETE http://localhost:8080/deposito)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        // Passa o id para o inactivate no Service
        depositoService.inactivate(id);
    }
}
