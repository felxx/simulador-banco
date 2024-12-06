package util;

import java.time.LocalDateTime;
import java.util.List;

import dao.MovimentacaoDAO;
import entidade.Movimentacao;

public class CalcularTransacoes {
	
	static MovimentacaoDAO dao = new MovimentacaoDAO();
	
	public static Double calcularSaldo(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		Double saldo = 0.0;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getTipoTransacao().equals("deposito")) {
				saldo += movimentacao.getValorOperacao();
			}		
			else {
				saldo -= movimentacao.getValorOperacao();
			}
		}
		return saldo;
	}
	
	public static Double calcularMediaSaldo(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		Double mediaSaldo = calcularSaldo(cpfCorrentista) / transacoes.size();
		return mediaSaldo;
	}
	
	public static double somarTransacoesDiariasPix(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		double valorTransacoesPix = 0.;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getDataTransacao().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
				if(movimentacao.getTipoTransacao().equals("pix")) {
					valorTransacoesPix += movimentacao.getValorOperacao();
				}
			}
		}
		return valorTransacoesPix;
	}
	
	public static double somarTransacoesDiariasSaque(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		double valorTransacoesSaque = 0.;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getDataTransacao().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
				if(movimentacao.getTipoTransacao().equals("saque")) {
					valorTransacoesSaque += movimentacao.getValorOperacao();
				}
			}
		}
		return valorTransacoesSaque;
	}
}
