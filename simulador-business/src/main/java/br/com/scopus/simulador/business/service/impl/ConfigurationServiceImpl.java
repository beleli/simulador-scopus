package br.com.scopus.simulador.business.service.impl;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.jerimum.fw.constants.ReturnCode;
import br.com.jerimum.fw.dao.JpaCrudRepository;
import br.com.jerimum.fw.exception.ServiceException;
import br.com.jerimum.fw.exception.ValidationException;
import br.com.jerimum.fw.i18n.I18nUtils;
import br.com.jerimum.fw.service.impl.AbstractCrudService;
import br.com.scopus.simulador.business.i18n.I18nKeys;
import br.com.scopus.simulador.business.service.ConfigurationService;
import br.com.scopus.simulador.dto.ConfigurationDto;
import br.com.scopus.simulador.dto.PageDto;
import br.com.scopus.simulador.dto.util.PageUtil;
import br.com.scopus.simulador.dto.util.PagedSearch;
import br.com.scopus.simulador.repository.ConfigurationRepository;
import br.com.scopus.simulador.repository.entity.Configuration;
import br.com.scopus.simulador.repository.entity.converter.TransactionTypeConverter;

@Service
public class ConfigurationServiceImpl extends AbstractCrudService<ConfigurationDto, Configuration>
    implements ConfigurationService {

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private ConfigurationRepository configurationRepository;

    @Autowired
    private TransactionTypeConverter transactionTypeConverter;

    @Override
    protected ConfigurationDto buildDtoFromEntity(Configuration entity) {
        return new ConfigurationDto(entity.getId(), entity.getTransactionType().getId(),
            entity.getTransactionType().getName(), entity.getName(), entity.getPort(), entity.getTimeout(),
            entity.getBytesAccess());
    }

    @Override
    protected Configuration buildEntityFromDto(ConfigurationDto dto) {
        return new Configuration(dto.getId(),
            transactionTypeConverter.convertToEntityAttribute(dto.getTransactionTypeId()), dto.getName(), dto.getPort(),
            dto.getTimeout(), dto.getBytesAccess());
    }

    @Override
    protected JpaCrudRepository<Configuration, Long> getRepository() {
        return configurationRepository;
    }

    @Override
    @Transactional(readOnly = false)
    public ConfigurationDto save(ConfigurationDto dto) throws ValidationException, ServiceException {
        if (dto == null) {
            String msg = I18nUtils.getMsg(messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        Configuration configuration = configurationRepository.save(buildEntityFromDto(dto));
        if (dto.getId() == null) {
            dto.setId(configuration.getId());
        }

        return dto;
    }

    @Override
    public PageDto<ConfigurationDto> search(PagedSearch<ConfigurationDto> search) {
        if (search == null) {
            String msg = I18nUtils.getMsg(messageSource, I18nKeys.ParametrosInvalidos.getKey());
            throw new ValidationException(ReturnCode.INVALID_PARAMETERS.getCode(), msg);
        }

        ConfigurationDto dto = search.getItem();
        if (StringUtils.isBlank(dto.getName()))
            dto.setName(null);
        
        Page<Configuration> page = configurationRepository.searchByTransactionTypeAndNameOrderByName(
            transactionTypeConverter.convertToEntityAttribute(dto.getTransactionTypeId()), dto.getName(),
            PageUtil.createPageRequest(search));

        return new PageDto<>(buildDtoFromEntity(page.getContent()), page.getTotalElements());
    }

}
