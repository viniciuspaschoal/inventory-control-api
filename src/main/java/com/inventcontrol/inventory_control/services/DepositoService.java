package com.inventcontrol.inventory_control.services;

import com.inventcontrol.inventory_control.entities.Deposito;
import com.inventcontrol.inventory_control.repositories.DepositoRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class DepositoService {

    private final DepositoRepository depositoRepository;

    public DepositoService( DepositoRepository depositoRepository){
        this.depositoRepository = depositoRepository;
    }

    public List<Deposito> listAll(){
        return depositoRepository.findAll();
    }

    public Deposito saveDeposit( Deposito newDeposit){

        //Regra de negócio simples
        if (newDeposit.getDescricao() == null || newDeposit.getDescricao().isBlank()){
            // 1. Validar se a sigla foi enviada
            throw new RuntimeException("A descrição não pode estar vazia.");
        }

        //Regra de negócio simples
        if (newDeposit.getSigla() == null || newDeposit.getSigla().isBlank()){
            // 1. Validar se a sigla foi enviada
            throw new RuntimeException("A sigla não pode estar vazia.");
        }

        //Normaliza os valores
        String descricao = newDeposit.getDescricao().trim();
        String sigla = newDeposit.getSigla().trim();

        // 2. VALIDAÇÕES DE DUPLICIDADE (Consultando o banco)
        // Verifica DUPLICIDADE, já existe DESCRIÇÃO ou SIGLA

        List<Deposito> depositoExistente = depositoRepository.findDepositoByDescricaoOrSigla(descricao, sigla);
        if (!depositoExistente.isEmpty()){ //SE A LISTA NÃO ESTIVER VAZIA

            // Pegamos o primeiro registro encontrado para comparar
            Deposito encontrado = depositoExistente.get(0);

            //Retorna erro de campo suplicado
            if (encontrado.getDescricao().equals(descricao)){
                throw new RuntimeException("Já existe um deposito cadastrado com esse nome.");
            } else {
                throw new RuntimeException("A sigla '" + sigla + "' já está em uso.");
            }
        }

        // 3. Se passou pelas validações, manda o Repository salvar
        return depositoRepository.save(newDeposit);
    }
}
