package util;

import java.time.LocalDateTime;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Conta;
import entidade.Movimentacao;

public class Cashback {
	
	static MovimentacaoDAO dao = new MovimentacaoDAO();
	
	public static Movimentacao voltarCashback(Conta conta) {
		if(LocalDateTime.now().getDayOfMonth() == 1) {
			Movimentacao movimentacao = new Movimentacao();
			movimentacao.setDescricao("Cashback caiu na sua conta!");
			movimentacao.setValorOperacao(saldoCashback(conta.getCliente().getCpf()));
			movimentacao.setTipoTransacao("cashback");
			movimentacao.setConta(conta);
			
		}
		return null;
	}
	
	public static double saldoCashback(String cpfCorrentista) {
		double valorCashback = 0.;
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getDataTransacao().getMonthValue() == (LocalDateTime.now().getMonthValue()-1)) {
				if(movimentacao.getTipoTransacao().equals("debito")) {
					valorCashback += (movimentacao.getValorOperacao()*0.2);
				}
			}
		}
		return valorCashback;
	}
}
