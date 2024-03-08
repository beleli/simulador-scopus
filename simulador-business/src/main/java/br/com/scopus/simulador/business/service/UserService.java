package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.AlterarSenhaDto;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.entity.User;

/**
 * Interface de servico para usuarios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
public interface UserService extends CrudService<UserDto, User> {

    /**
     * Retorna o usuario que possui o email informado como parametro.
     * 
     * @param email
     * @return {@link List}
     */
    UserDto findByEmail(String email);

    /**
     * Retorna a lista de permissoes do perfil informado como parametro.
     * 
     * @param id
     * @return {@link List}
     */
    List<String> consultarPermissoesPorIdPerfil(Integer id);

    /**
     * Altera a senha do usuário
     * 
     * @param alterarSenhaDTO
     */
    void alterarSenha(AlterarSenhaDto alterarSenhaDTO);

    /**
     * Gera uma nova senha para o usuário, encaminha por e-mail
     * 
     * @param alterarSenhaDTO
     */
    void recuperarSenha(String login);

    /**
     * Consulta paginada de usuários iniciados por nome ou email
     * 
     * @param PagedSearch {@link UserDto}
     * @return {@link PageDto}
     */
    PageDto<UserDto> consultarPorNomeOuEmail(PagedSearch<UserDto> pagedSearch);

    /**
     * Bloqueia acesso do usuario ao sistema
     * 
     * @param id
     */
    void bloquearUsuario(Long id);

    /**
     * Insere novo usuário gerando nova senha e enviando por e-mail
     * 
     * @param user
     * @return 
     */
    UserDto salvarUsuario(UserDto user) throws ValidationException, ServiceException;

    /**
     * Lista de Profile
     * 
     * @return List<ComboBoxDto>
     */
    public List<ComboBoxDto> getComboBoxProfiles();

    /**
     * Consulta usuário por id
     * 
     * @param id
     * @return UserDto
     */
    public UserDto consultarUsuario(Long id);

    /**
     * Verifica se existe um usuario com o id informado como parametro.
     * 
     * @param id
     * @return boolean
     */
    boolean usuarioExiste(Long id);

    /**
     * Consulto o combo de usuário por nome.
     * 
     * @param nome
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> consultarComboPorNome(String nome);

    PageDto<UserDto> findByUser(PagedSearch<UserDto> search);
}

