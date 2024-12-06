package servico;

import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.List;

import dao.DAOPrincipal;
import dao.MovimentacaoDAO;
import entidade.Movimentacao;
import util.CalcularTransacoes;

public class MovimentacaoServico implements ServicoBase<Movimentacao> {
	static MovimentacaoDAO dao = new MovimentacaoDAO();
	
	@Override
	public DAOPrincipal<Movimentacao> getDAO(){
		return dao;
	}
	
	@Override
	public Movimentacao inserir(Movimentacao movimentacao) {
		movimentacao.setDataTransacao(LocalDateTime.now());
		adicionarTarifa(movimentacao);
		if(validarMovimentacao(movimentacao)) {
			notificarSaldo(CalcularTransacoes.calcularSaldo(movimentacao.getConta().getCliente().getCpf()));
			Movimentacao contaBanco = dao.inserir(movimentacao);
			return contaBanco;
		}
		return null;
	}
	
	@Override
	public Movimentacao alterar(Movimentacao movimentacao) {
		return movimentacao;
	}
	
	@Override
	public void excluir(Long id) {
    }
	

	public static Movimentacao adicionarTarifa(Movimentacao movimentacao) {
		if(movimentacao.getTipoTransacao() == "pix" || movimentacao.getTipoTransacao() == "pagamento") {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 5);
		}
			
		else if(movimentacao.getTipoTransacao() == "saque") {
			movimentacao.setValorOperacao(movimentacao.getValorOperacao() + 2);
		}
		return movimentacao;
	}
	
    public static void notificarSaldo(Double saldo) {
    	if(saldo < 100) {
    		System.out.println("SALDO ABAIXO DE 100 REAIS");
    	}
    }
    
    public static boolean validarMovimentacao(Movimentacao movimentacao) {
    	if(validarLimiteTransacoes(movimentacao.getConta().getCliente().getCpf())){
    		return false;
    	}
    	if(movimentacao.getTipoTransacao() == "saque" || movimentacao.getTipoTransacao() == "pix" || movimentacao.getTipoTransacao() == "pagamento" || movimentacao.getTipoTransacao() == "debito") {
    		if(verificarSaldo(CalcularTransacoes.calcularSaldo(movimentacao.getConta().getCliente().getCpf()), movimentacao.getValorOperacao())) {
    			return false;
    		}
    		if(validarHorario(movimentacao)) {
    			return false;
    		}
    		if(validarLimitePix(movimentacao) || validarLimiteSaque(movimentacao)) {
    			return false;
    		}
    	}
    	return true;
    }
    
	public static boolean verificarSaldo(Double saldo, Double valorOperacao) {
		if (valorOperacao > saldo) {
			return true;
		} else {
			return false;
		}
	}
    
    public static boolean validarLimiteTransacoes(String cpfCorrentista) {
		List<Movimentacao> transacoes = dao.buscarPorCpf(cpfCorrentista);
		int transacoesDia = 0;
		for (Movimentacao movimentacao : transacoes) {
			if(movimentacao.getDataTransacao().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
				transacoesDia += 1;
			}
		}
		if(transacoesDia > 10) {
			return true;
		}
		return false;
    }
    
    public static boolean validarHorario(Movimentacao movimentacao) {
    	if(movimentacao.getTipoTransacao().equals("pix") && (movimentacao.getDataTransacao().getHour() > 21 || movimentacao.getDataTransacao().getHour() < 6)){
    		return true;
    	}
    	return false;
    }
    
    public static boolean validarLimitePix(Movimentacao movimentacao) {
    	if(movimentacao.getTipoTransacao().equals("pix") && (CalcularTransacoes.somarTransacoesDiariasPix(movimentacao.getConta().getCliente().getCpf()) + movimentacao.getValorOperacao()) > 300) {
    		return true;
    	}
    	return false;
    }
    
    public static boolean validarLimiteSaque(Movimentacao movimentacao) {
    	if(movimentacao.getTipoTransacao().equals("saque") && (CalcularTransacoes.somarTransacoesDiariasPix(movimentacao.getConta().getCliente().getCpf()) + movimentacao.getValorOperacao()) > 5000) {
    		return true;
    	}
    	return false;
    }
}


