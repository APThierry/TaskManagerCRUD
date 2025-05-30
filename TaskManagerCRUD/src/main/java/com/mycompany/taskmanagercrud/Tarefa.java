package com.mycompany.taskmanagercrud;

/**
 * 
 * @author Thierry
 * 
 *Representa uma tarefa no sistema de gerenciamento de tarefas.
 * Que encapsula os dados de uma tarefa, 
 * incluindo seu identificador, título, descrição, prioridade e status de conclusão.
 */
public class Tarefa {
    private String id;
    private String titulo;
    private String descricao;
    private String prioridade;
    private boolean concluida;

     /**
     * Construtor para criar uma nova instância de Tarefa.
     * Inicializa a tarefa com o título, descrição e prioridade fornecidos.
     * O status de conclusão é definido como {@code false} por padrão.
     *
     * @param titulo O título da tarefa.
     * @param descricao A descrição detalhada da tarefa.
     * @param prioridade O nível de prioridade da tarefa.
     */
    public Tarefa(String titulo, String descricao, String prioridade) {
        this.titulo = titulo;
        this.descricao = descricao;
        this.prioridade = prioridade;
        this.concluida = false; // Novas tarefas são inicializadas como não concluídas
    }

    /**
     * Obtém o identificador único da tarefa.
     * @return O ID da tarefa.
     */
    public String getId() {
        return id;
    }

    /**
     * Define o identificador único da tarefa.
     * Este método é tipicamente usado após a tarefa ser persistida no banco de dados,
     * que atribui um ID.
     * @param id O novo ID da tarefa.
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * Obtém o título da tarefa.
     * @return O título da tarefa.
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Define o título da tarefa.
     * @param titulo O novo título da tarefa.
     */
    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    /**
     * Obtém a descrição da tarefa.
     * @return A descrição da tarefa.
     */
    public String getDescricao() {
        return descricao;
    }

    /**
     * Define a descrição da tarefa.
     * @param descricao A nova descrição da tarefa.
     */
    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    /**
     * Obtém a prioridade da tarefa.
     * @return A prioridade da tarefa.
     */
    public String getPrioridade() {
        return prioridade;
    }

    /**
     * Define a prioridade da tarefa.
     * @param prioridade A nova prioridade da tarefa.
     */
    public void setPrioridade(String prioridade) {
        this.prioridade = prioridade;
    }

    /**
     * Verifica se a tarefa está concluída.
     * @return {@code true} se a tarefa estiver concluída, {@code false} caso contrário.
     */
    public boolean isConcluida() {
        return concluida;
    }

    /**
     * Define o status de conclusão da tarefa.
     * @param concluida O novo status de conclusão (true para concluída, false para não concluída).
     */
    public void setConcluida(boolean concluida) {
        this.concluida = concluida;
    }
    
    /**
     * Retorna uma representação em String do objeto Tarefa.
     * Útil para debugging e logging.
     * @return Uma string contendo os detalhes da tarefa.
     */
    @Override
    public String toString() {
        return "Tarefa{" +
               "id='" + id + '\'' +
               ", titulo='" + titulo + '\'' +
               ", descricao='" + descricao + '\'' +
               ", prioridade='" + prioridade + '\'' +
               ", concluida=" + concluida +
               '}';
    }
}
