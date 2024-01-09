package com.cbh.userservice.config;

import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration class for creating and configuring a ModelMapper bean.
 */
@Configuration
public class Mapper {

    /**
     * Creates and configures a ModelMapper bean with specific settings.
     *
     * @return The configured ModelMapper bean.
     */
    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setSkipNullEnabled(true)
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setPropertyCondition(Conditions.isNotNull());

        return modelMapper;
    }
}
