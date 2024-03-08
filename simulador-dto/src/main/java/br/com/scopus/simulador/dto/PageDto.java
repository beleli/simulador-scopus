package br.com.scopus.simulador.dto;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PageDto<T> implements Serializable {

    private static final long serialVersionUID = 3898488337817428956L;

    private List<T> data;
    private long totalRegister;
}
