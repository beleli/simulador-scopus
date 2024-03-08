package br.com.scopus.simulador.business.service;

import java.util.List;

import br.com.jerimum.fw.service.CrudService;
import br.com.scopus.simulador.dto.ComboBoxDto;
import br.com.scopus.simulador.dto.ExerciseMassDto;
import br.com.scopus.simulador.dto.ExerciseMassItemDto;
import br.com.scopus.simulador.repository.entity.TestMass;

/**
 * Interface de servico para exercitar massa de teste.
 * 
 * @author Carlos Alberto Beleli Junior - cabjunior@scopus.com.br
 * @since 1.0
 */
public interface ExerciseMassService extends CrudService<ExerciseMassDto, TestMass> {

    /**
     * Lista de campos de entrada
     * 
     * @param layoutOutputTransactionId
     * @return List<ExerciseMassItemDto>
     */
    List<ExerciseMassItemDto> getInputList(Long layoutOutputTransactionId);

    /**
     * Lista de mecanismos de simulação
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxMechanisms();
    
    /**
     * Lista de transações roteadoras
     * 
     * @return List<ComboBoxDto>
     */
    List<ComboBoxDto> getComboBoxRouters(Long layoutOutputTransactionId);

    /**
     * Executa a transação.
     * 
     * @param dto
     * @return dto
     * @throws Exception
     */
    ExerciseMassDto execute(ExerciseMassDto dto) throws Exception;

}
