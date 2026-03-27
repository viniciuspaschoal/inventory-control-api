package com.inventcontrol.inventory_control.services;

import com.inventcontrol.inventory_control.entities.GrupoEstoque;
import com.inventcontrol.inventory_control.repositories.GrupoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoEstoqueService {

    private final GrupoEstoqueRepository repository;

    public GrupoEstoqueService(GrupoEstoqueRepository repository){
        this.repository = repository;
    }

    public List<GrupoEstoque> listarTodos(){
        return repository.findAll();
    }

    public GrupoEstoque salvar(GrupoEstoque novoGrupo){

        //Regra de negócio simples
        if (novoGrupo.getDescricao() == null || novoGrupo.getDescricao().isBlank()){
            // 1. Validar se o nome foi enviado
            throw new RuntimeException("A descrição não pode estar vazia.");
        }

        if (novoGrupo.getSigla() == null || novoGrupo.getSigla().isBlank()){
            throw new RuntimeException("A sigla é obrigatória.");
        }

        //Normaliza os valores
        String descricao = novoGrupo.getDescricao().trim();
        String sigla = novoGrupo.getSigla().trim();

        // 2. VALIDAÇÕES DE DUPLICIDADE (Consultando o banco)

        // Verifica DUPLICIDADE, já existe DESCRIÇÃO ou SIGLA
        Optional<GrupoEstoque> grupoExistente = repository.findByDescricaoOrSigla(descricao, sigla);
        if (grupoExistente.isPresent()) {
            //Retorna erro dependendo do campo duplicado
            if (grupoExistente.get().getDescricao().equals(descricao)){
                throw new RuntimeException("Já existe um grupo cadastrado com esse nome.");
            } else {
                throw new RuntimeException("A sigla '" + sigla + "' já está em uso");
            }
        }


        // 3. Se passou pelas validações, manda o "braço" (Repository) salvar
        return repository.save(novoGrupo);
    }
}
