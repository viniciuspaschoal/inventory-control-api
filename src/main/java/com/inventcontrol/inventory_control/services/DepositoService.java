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

    //Metodo GET
    public List<Deposito> listAll(){
        return depositoRepository.findByAtivoTrue();
    }

    //POST
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

        //* VALIDAÇÕES DE DUPLICIDADE (Consultando o banco)
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

    //DELETE lógico - INATIVAR
    public void inactivate(Long id){
        //Busca o registro ou lança erro se o ID não existir
        Deposito deposito = depositoRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Depósito não encontrado."));

        //Muda o status de para inativo
        deposito.setAtivo(false);

        //Salva a alteração no banco de dados
        depositoRepository.save(deposito);
        
    }

    //UPDATE trocar sigla ou descrição
    public Deposito update(Long id, Deposito updatedData){
        // 1 busca o item que já existe
        Deposito exist = depositoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Depósito não encontrado"));

        if (!exist.getAtivo()){
            throw new RuntimeException("Não é possível editar um depósito inativo.");
        }

        if (updatedData.getDescricao() == null || updatedData.getDescricao().isBlank()){
            throw new RuntimeException("Descrição é obrigatória");
        }

        if (updatedData.getSigla() == null || updatedData.getSigla().isBlank()){
            throw new RuntimeException("Sigla é obrigatória");
        }

        String descricao = updatedData.getDescricao().trim().toUpperCase();
        String sigla = updatedData.getSigla().trim().toUpperCase();

        // 2 Validação: posso usar essa sigla ou descrição?
        List<Deposito> conflicts = depositoRepository.findDepositoByDescricaoOrSigla(descricao, sigla);

        for (Deposito conflict : conflicts){
            if (!conflict.getId().equals(id)){
                if (conflict.getDescricao().equalsIgnoreCase(descricao)){
                    throw new RuntimeException("Descrição já adastrada");
                }
                if (conflict.getSigla().equalsIgnoreCase(sigla)){
                    throw new RuntimeException("Sigla já existente");
                }
            }
        }

        exist.setDescricao(descricao);
        exist.setSigla(sigla);

        return depositoRepository.save(exist);
    }
}
