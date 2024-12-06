package servico;

import dao.DAOPrincipal;

public interface ServicoBase<T>{
	DAOPrincipal<T> getDAO();
	
	default T inserir(T entidade) {
		return getDAO().inserir(entidade);
	}
	T alterar(T entidade);
	void excluir(Long id);
}
