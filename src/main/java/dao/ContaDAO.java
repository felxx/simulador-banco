package dao;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import entidade.Conta;
import entidade.Movimentacao;

public class ContaDAO extends DAOPrincipal<Conta>  {
	
	public ContaDAO() {
		super(Conta.class);
	}
	
	public List<Conta> buscarPorCpf(String cpf) {
		EntityManager em = getEntityManager();
		try {
		Query query = em.createQuery("from Conta where cliente.cpf='"+cpf+"'");
		return query.getResultList();
		}
		finally {
		em.close();
		}
}
}
