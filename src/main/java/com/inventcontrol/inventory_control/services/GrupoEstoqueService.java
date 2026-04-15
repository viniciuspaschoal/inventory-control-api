package com.inventcontrol.inventory_control.services;

import com.inventcontrol.inventory_control.entities.Deposito;
import com.inventcontrol.inventory_control.entities.GrupoEstoque;
import com.inventcontrol.inventory_control.repositories.GrupoEstoqueRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class GrupoEstoqueService {

    private final GrupoEstoqueRepository grupoEstoqueRepository;

    public GrupoEstoqueService(GrupoEstoqueRepository grupoEstoqueRepository){
        this.grupoEstoqueRepository = grupoEstoqueRepository;
    }

    public List<GrupoEstoque> listarTodos(){
        return this.grupoEstoqueRepository.findByAtivoTrue();
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
        List<GrupoEstoque> grupoExistente = this.grupoEstoqueRepository.findGrupoEstoqueByDescricaoOrSigla(descricao, sigla);
        if (!grupoExistente.isEmpty()) {

            // Pegamos o primeiro registro encontrado para comparar
            GrupoEstoque encontrado = grupoExistente.get(0);

            //Retorna erro de campo suplicado
            if (encontrado.getDescricao().equals(descricao)){
                throw new RuntimeException("Já existe um grupo cadastrado com esse nome.");
            } else {
                throw new RuntimeException("A sigla '" + sigla + "' já está em uso");
            }
        }


        // 3. Se passou pelas validações, manda o "braço" (Repository) salvar
        return this.grupoEstoqueRepository.save(novoGrupo);
    }

    //DELETE - Inativar
    public void inactivate(Long id){
        //Busca o registro ou lança erro se o ID não existir
        GrupoEstoque grupoEstoque = this.grupoEstoqueRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Esse Grupo Estoque não foi encontrado"));

        //Muda o status para negativo
        grupoEstoque.setAtivo(false);

        // Salva a alteração no Banco de Dados
        this.grupoEstoqueRepository.save(grupoEstoque);
    }

    // UPDATE
    public GrupoEstoque update(Long id, GrupoEstoque updateData){
        //Buscar por um ítem que já existe
        GrupoEstoque exist = this.grupoEstoqueRepository.findById(id)
                .orElseThrow( () -> new RuntimeException("Grupo Estoque não encontrado"));

        if (!exist.getAtivo()){
            throw new RuntimeException("Não é possível alterar um grupo inativo");
        }

        if (updateData.getDescricao() == null || updateData.getDescricao().isBlank()){
            throw new RuntimeException("Descrição é obrigatória");
        }

        if (updateData.getSigla() == null || updateData.getSigla().isBlank()){
            throw new RuntimeException("Sigla é obrigatória");
        }

        String descricao = updateData.getDescricao().trim().toUpperCase();
        String sigla = updateData.getSigla().trim().toUpperCase();

        // Validação: posso usar essa sigla ou descrição?
        List<GrupoEstoque> conflicts = this.grupoEstoqueRepository.findGrupoEstoqueByDescricaoOrSigla(descricao, sigla);

        for (GrupoEstoque conflict : conflicts){
            if (!conflict.getId().equals(id)){
                if (conflict.getDescricao().equalsIgnoreCase(descricao)){
                throw new RuntimeException("Descrição já cadastrada");
                }

                if (conflict.getSigla().equalsIgnoreCase(sigla)){
                    throw new RuntimeException("Sigla já existente");
                }
            }
        }

        exist.setDescricao(descricao);
        exist.setSigla(sigla);

        return this.grupoEstoqueRepository.save(exist);
    }
}