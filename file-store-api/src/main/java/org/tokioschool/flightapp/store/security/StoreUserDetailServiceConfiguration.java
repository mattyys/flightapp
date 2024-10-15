package org.tokioschool.flightapp.store.security;


import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.tokioschool.flightapp.store.config.StoreConfigurationProperties;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class StoreUserDetailServiceConfiguration {

    private final StoreConfigurationProperties storeConfigurationProperties;

    @Bean
    public UserDetailsService storeUserDetailsService(){

        List<UserDetails> users = storeConfigurationProperties.users()
                .stream().map(user ->
                        User.builder()
                                .username(user.username())
                                .password(user.password())
                                .authorities(user.authorities().toArray(new String[0]))
                                .build())
                .toList();

        return new InMemoryUserDetailsManager(users);
    }
}
