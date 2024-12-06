package dao;

import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.Query;

import entidade.Movimentacao;

public class MovimentacaoDAO extends DAOPrincipal<Movimentacao> {
	
	public MovimentacaoDAO() {
		super(Movimentacao.class);
	}

	public List<Movimentacao> buscarPorCpf(String cpf) {
			EntityManager em = getEntityManager();
			try {
			Query query = em.createQuery("from Movimentacao where conta.cliente.cpf='"+cpf+"'");
			return query.getResultList();
			}
			finally {
			em.close();
			}
	}
}
