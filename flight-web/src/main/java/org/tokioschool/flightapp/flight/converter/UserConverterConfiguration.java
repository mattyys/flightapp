package org.tokioschool.flightapp.flight.converter;


import org.modelmapper.AbstractConverter;
import org.modelmapper.Converter;
import org.modelmapper.ModelMapper;
import org.modelmapper.PropertyMap;
import org.springframework.context.annotation.Configuration;
import org.tokioschool.flightapp.flight.domain.Role;
import org.tokioschool.flightapp.flight.domain.User;
import org.tokioschool.flightapp.flight.dto.UserDTO;

import java.util.List;
import java.util.Set;

import static org.springframework.beans.factory.support.InstanceSupplier.using;

@Configuration
public class UserConverterConfiguration {

    private final ModelMapper modelMapper;

    public UserConverterConfiguration(ModelMapper modelMapper){
        this.modelMapper = modelMapper;
        configureUserDTOConverter();

    }

    private void configureUserDTOConverter() {
        modelMapper
            .typeMap(User.class, UserDTO.class)
            .addMappings(
                    new PropertyMap<>() {
                        @Override
                        protected void configure() {
                            Converter<Set<Role>, List<String>> converter =
                                    new AbstractConverter<>() {
                                        @Override
                                        protected List<String> convert(Set<Role> source) {
                                            return source.stream().map(Role::getName).toList();
                                        }
                                    };
                            using(converter).map(source.getRoles(), destination.getRoles());
                        }
                    });

    }
}
