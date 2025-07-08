package br.com.deveficiente.mercadolivre.compras;

import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class ProcessadorTarefas {
    //1 ICP: TarefaSucesso
    private final Set<TarefaSucesso> tarefasSucesso;
    //1 ICP: TarefaFalha
    private final Set<TarefaFalha> tarefasFalha;

    public ProcessadorTarefas(Set<TarefaSucesso> tarefasSucesso, Set<TarefaFalha> tarefasFalha) {
        this.tarefasSucesso = tarefasSucesso;
        this.tarefasFalha = tarefasFalha;
    }

    //1 ICP: Pagamento
    public void processar(Pagamento pagamento) {
        if (pagamento.isPagamentoProcessadoComSucesso()) {
            tarefasSucesso.forEach(tarefa -> tarefa.processar(pagamento));
            return;
        }
        tarefasFalha.forEach(tarefa -> tarefa.processar(pagamento));
    }
}
