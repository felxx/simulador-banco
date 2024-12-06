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
	
	public static Double calcularMediaTransacoes(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		Double mediaTransacoes = calcularSaldo(cpfCorrentista) / transacoes.size();
		return mediaTransacoes;
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
	
	public static double calcularLimiteCrédito(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		double ultimoMes = 0.;
		double penultimoMes = 0.;
		double antepenultimoMes = 0.;
		
		for(Movimentacao movimentacao : transacoes) {
			if(movimentacao.getDataTransacao().getMonthValue() == LocalDateTime.now().getMonthValue()-1){
				if(movimentacao.getTipoTransacao().equals("deposito")){
					ultimoMes += movimentacao.getValorOperacao();
				}
				else {
					ultimoMes -= movimentacao.getValorOperacao();
				}
			}
			else if(movimentacao.getDataTransacao().getMonthValue() == LocalDateTime.now().getMonthValue()-2){
				if(movimentacao.getTipoTransacao().equals("deposito")) {
					penultimoMes += movimentacao.getValorOperacao();
				}
				else {
					penultimoMes -= movimentacao.getValorOperacao();
				}
			}
			else if(movimentacao.getDataTransacao().getMonthValue() == LocalDateTime.now().getMonthValue()-3){
				if(movimentacao.getTipoTransacao().equals("deposito")) {
					antepenultimoMes += movimentacao.getValorOperacao();
				}
				else {
					antepenultimoMes -= movimentacao.getValorOperacao();
				}
			}
		}
		double mediaSaldoMeses = (ultimoMes + penultimoMes + antepenultimoMes) / 3;
		return mediaSaldoMeses;
	}
}
