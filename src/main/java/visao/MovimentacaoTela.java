package visao;

import java.time.LocalDateTime;
import controle.MovimentacaoControle;
import dao.ClienteDAO;
import dao.ContaDAO;
import entidade.Cliente;
import entidade.Conta;
import entidade.ContaTipo;
import entidade.Movimentacao;
import servico.MovimentacaoServico;
import util.ValidarCPF;
import util.Cashback;
import util.TotalContas;

public class MovimentacaoTela {
	public static void main(String[] args) {
		ClienteDAO clienteDAO = new ClienteDAO();
		ContaDAO contaDao = new ContaDAO();
		
		Cliente cliente = new Cliente();
		cliente.setCpf("28366150011");
		cliente.setNome("José Antônio da Silva");
		if(ValidarCPF.validar(cliente.getCpf())) {
			clienteDAO.inserir(cliente);
			
			Conta conta = new Conta();
			conta.setDataAbertura(LocalDateTime.now());
			conta.setCliente(cliente);
			conta.setContaTipo(ContaTipo.CONTA_CORRENTE);
			
			if(TotalContas.total(cliente.getCpf())) {
				contaDao.inserir(conta);
		
				MovimentacaoControle controle = new MovimentacaoControle();
				
				Movimentacao movimentacaoCashback = Cashback.voltarCashback(conta);
				
				Movimentacao movimentacao = new Movimentacao();
				movimentacao.setDescricao("saque de 6000.0");
				movimentacao.setTipoTransacao("deposito");
				movimentacao.setValorOperacao(1000.);
				movimentacao.setConta(conta);
				
				controle.inserir(movimentacaoCashback);
				controle.inserir(movimentacao);
			}
		}
	}

}
