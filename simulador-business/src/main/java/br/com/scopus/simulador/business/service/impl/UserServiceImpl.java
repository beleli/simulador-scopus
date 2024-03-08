package br.com.scopus.simulador.business.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.BeanValidationService;
import br.com.scopus.simulador.business.service.EmailService;
import br.com.scopus.simulador.business.service.UserService;
import br.com.scopus.simulador.dto.AlterarSenhaDto;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.UserDto;
import br.com.scopus.simulador.dto.enums.ReturnCode;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.ProfileRoleRepository;
import br.com.scopus.simulador.repository.UserRepository;
import br.com.scopus.simulador.repository.entity.ProfileRole;
import br.com.scopus.simulador.repository.entity.User;
import br.com.scopus.simulador.repository.entity.converter.ProfileConverter;
import br.com.scopus.simulador.repository.entity.enums.Profile;


/**
 * Classe de servico para usuarios.
 * 
 * @author Dali Freire - dfreire@scopus.com.br
 * @since 1.0
 */
@Service
public class UserServiceImpl extends AbstractCrudService<UserDto, User> implements UserService {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRoleRepository profileRoleRepository;
    @Autowired
    private ProfileConverter profileConverter;
    @Autowired
    private MessageSource messageSource;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private BeanValidationService beanValidationService;
    @Autowired
    private EmailService emailService;

    @Override
    public UserDto findByEmail(String login) {
        if (StringUtils.isNotBlank(login)) {
            User entity = this.userRepository.findByEmail(login);
            if (entity != null) {
                return buildDtoFromEntity(entity);
            } else {
                String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioNaoEncontrado.getKey());
                throw new ValidationException(ReturnCode.INVALID_USER.getCode(), msg);
            }
        }

        String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
        throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
    }

    @Override
    protected UserDto buildDtoFromEntity(User entity) {
        if (entity != null) {
            return new UserDto(entity.getId(), entity.getProfile().getId(), entity.getName(),
                entity.getProfile().getName(), entity.getEmail(), entity.getEnabled(), entity.getChangePassword(),
                entity.getPassword());
        }
        return null;
    }

    @Override
    protected User buildEntityFromDto(UserDto dto) {
        if (dto != null) {
            return new User(dto.getId(), profileConverter.convertToEntityAttribute(dto.getIdProfile()), dto.getName(),
                dto.getEmail(), dto.getEnabled(), dto.getChangePassword(), dto.getPassword());
        }
        return null;
    }

    @Override
    protected UserRepository getRepository() {
        return this.userRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public void alterarSenha(AlterarSenhaDto dto) {
        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (dto.getEmail() == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        User entity = this.userRepository.findByEmail(dto.getEmail());
        if (entity == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioNaoEncontrado.getKey());
            throw new ValidationException(ReturnCode.INVALID_USER.getCode(), msg);
        }

        if (!StringUtils.equals(entity.getPassword(), dto.getPassword())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.SenhaInvalida.getKey());
            throw new ValidationException(ReturnCode.INVALID_PASSWORD.getCode(), msg);
        }

        if (!entity.getEnabled()) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioInativo.getKey());
            throw new ValidationException(ReturnCode.USER_DISABLED.getCode(), msg);
        }

        entity.setChangePassword(false);
        entity.setPassword(dto.getNewPassword());
        saveEntity(entity);
    }

    @Override
    public List<ComboBoxDto> getComboBoxProfiles() {
        return getListProfile();
    }

    private List<ComboBoxDto> getListProfile() {
        List<ComboBoxDto> result = new ArrayList<ComboBoxDto>();
        for (Profile p : Profile.values()) {
            result.add(new ComboBoxDto(p.getId().longValue(), p.getName()));
        }
        return result;
    }

    @Override
    public List<String> consultarPermissoesPorIdPerfil(Integer id) {
        if (id != null) {
            List<ProfileRole> profileRoles = profileRoleRepository
                .findByProfile(profileConverter.convertToEntityAttribute(id));
            List<String> roles = new ArrayList<>();

            for (ProfileRole profileRole : profileRoles) {
                roles.add(profileRole.getRole().getName());
            }

            return roles;
        }

        String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
        throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
    }

    @Override
    @Transactional(readOnly = false)
    public void recuperarSenha(String login) {
        if (StringUtils.isBlank(login)) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        User entity = this.userRepository.findByEmail(login);
        if (entity == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioNaoEncontrado.getKey());
            throw new ValidationException(ReturnCode.INVALID_USER.getCode(), msg);
        }

        if (!entity.getEnabled()) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioInativo.getKey());
            throw new ValidationException(ReturnCode.USER_DISABLED.getCode(), msg);
        }

        String newPassword = RandomStringUtils.randomAlphanumeric(8);
        entity.setPassword(this.passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);

        this.saveEntity(entity);

        if (!this.emailService.enviarEmailRecuperarSenha(newPassword, login)) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ErroAoEnviarEmail.getKey());
            throw new ValidationException(ReturnCode.EMAIL_SEND_ERROR.getCode(), msg);
        }
    }

    @Override
    public void bloquearUsuario(Long id) {
        if (id == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        User user = this.userRepository.findById(id);
        if (user != null) {
            user.setEnabled(false);
            this.saveEntity(user);
        }
    }

    @Override
    @Transactional(readOnly = false)
    public UserDto salvarUsuario(UserDto user) throws ValidationException, ServiceException {
        if (user == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        if (this.getRepository().findQuantityEmails(user.getEmail(), user.getId() == null ? -1L : user.getId()) > 0) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.EmailJaCadastrado.getKey());
            throw new ValidationException(ReturnCode.EMAIL_REGISTERED.getCode(), msg);
        }

        if (user.getId() == null) {
            user.setId(this.inserirNovoUsuario(user));
        } else {
            this.atualizaUsuario(user);
        }
        return user;
    }

    @Transactional(readOnly = false)
    private Long inserirNovoUsuario(UserDto user) throws ValidationException, ServiceException {

        User entity = this.buildEntityFromDto(user);

        String newPassword = RandomStringUtils.randomAlphanumeric(8);
        entity.setPassword(this.passwordEncoder.encode(newPassword));
        entity.setChangePassword(true);
        entity.setEnabled(true);

        this.beanValidationService.validate(entity);
        this.saveEntity(entity);

        if (!this.emailService.enviarEmailCadastro(newPassword, user.getEmail())) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.UsuarioInativo.getKey());
            throw new ValidationException(ReturnCode.USER_DISABLED.getCode(), msg);
        }

        return entity.getId();
    }

    @Transactional(readOnly = false)
    private void atualizaUsuario(UserDto user) throws ValidationException, ServiceException {
        this.beanValidationService.validate(buildEntityFromDto(user));
        this.updateDto(user);
    }

    @Override
    public UserDto consultarUsuario(Long id) {
        if (id == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        User user = this.userRepository.findById(id);
        return this.buildDtoFromEntity(user);
    }

    @Override
    public boolean usuarioExiste(Long id) {
        return userRepository.exists(id);
    }

    @Override
    public PageDto<UserDto> consultarPorNomeOuEmail(PagedSearch<UserDto> pagedSearch) {
        UserDto dto = pagedSearch.getItem();

        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        String name = dto.getName() == null ? "" : dto.getName();
        String email = dto.getEmail() == null ? "" : dto.getEmail();

        Page<User> lista = this.userRepository.findByNameIgnoreCaseContainingAndEmailIgnoreCaseContainingAndEnabled(
            name, email, dto.getEnabled(),
            PageUtil.createPageRequest(pagedSearch, new Sort(Sort.Direction.ASC, "name")));

        PageDto<UserDto> dtos = new PageDto<>(this.buildDtoFromEntity(lista.getContent()), lista.getTotalElements());
        return dtos;
    }

    @Override
    public List<ComboBoxDto> consultarComboPorNome(String nome) {

        if (StringUtils.isEmpty(nome)) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }
        // List<User> lista =
        // this.userRepository.findByNameIgnoreCaseContainingAndEnabledTrue(nome);
        List<User> lista = this.userRepository.searchByName(nome);
        List<ComboBoxDto> dtos = new ArrayList<>();
        for (User user : lista) {
            dtos.add(new ComboBoxDto(user.getId(), user.getName()));
        }

        return dtos;
    }

    @Override
    public PageDto<UserDto> findByUser(PagedSearch<UserDto> pagedSearch) {
        UserDto dto = pagedSearch.getItem();

        if (dto == null) {
            String msg = I18nUtils.getMsg(this.messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        String nome = dto.getName() == null || dto.getName().trim().equals("") ? null : dto.getName().trim();
        String email = dto.getEmail() == null || dto.getEmail().trim().equals("") ? null : dto.getEmail().trim();

        Page<User> page = this.userRepository.findByEmailAndNameOrderByName(email, nome,
            PageUtil.createPageRequest(pagedSearch));

        return new PageDto<>(buildDtoFromEntity(page.getContent()), page.getTotalElements());
    }
}
