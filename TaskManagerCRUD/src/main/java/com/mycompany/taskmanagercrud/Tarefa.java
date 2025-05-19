package com.mycompany.taskmanagercrud;

/**
 *
 * @author Thierry
 */

public class Tarefa {
    private String id;
    private String titulo;
    private String descricao;
    private String prioridade;
    private boolean concluida;

    public Tarefa(String titulo, String descricao, String prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.concluida = false;
    }

    // Getters
    public String getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getPrioridade() {
        return prioridade;
    }

    public boolean isConcluida() {
        return concluida;
    }

    // Setters
    public void setId(String id) {
        this.id = id;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
}
