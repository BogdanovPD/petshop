package com.test.petshop.config;

import com.test.petshop.converter.PetDtoToEntityConverter;
import com.test.petshop.converter.PetEntityToDtoConverter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ConversionServiceFactoryBean;
import org.springframework.core.convert.ConversionService;
import org.springframework.dao.annotation.PersistenceExceptionTranslationPostProcessor;

import java.util.Set;

@Configuration
@RequiredArgsConstructor
public class ConvertersConfig {

    private final PetEntityToDtoConverter petEntityToDtoConverter;
    private final PetDtoToEntityConverter petDtoToEntityConverter;

    @Bean
    public ConversionService petConverterService() {
        ConversionServiceFactoryBean bean = new ConversionServiceFactoryBean();
        bean.setConverters(Set.of(petEntityToDtoConverter, petDtoToEntityConverter));
        bean.afterPropertiesSet();
        return bean.getObject();
    }
}
