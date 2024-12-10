package util;

import java.util.List;

import dao.ContaDAO;
import entidade.Conta;

public class TotalContas {
	
	static ContaDAO dao = new ContaDAO();
	
	public static boolean total(String cpfCorrentista) {
		List<Conta> contas = dao.buscarPorCpf(cpfCorrentista);
		int numeroContas = contas.size();
		if(numeroContas > 2) {
			return false;
		}
		return true;
	}
}
