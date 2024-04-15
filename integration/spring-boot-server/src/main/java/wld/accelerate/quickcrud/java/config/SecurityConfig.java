package wld.accelerate.quickcrud.java.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;

@Configuration
public class SecurityConfig {

    @Autowired
    CorsConfigurationSource corsConfigurationSource;

    @Autowired
    AccessDecisionVoterAuthorizationManagerAdapter accessDecisionVoterAuthorizationManagerAdapter;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        //http.authorizeHttpRequests(authorizationManagerRequestMatcherRegistry ->authorizationManagerRequestMatcherRegistry.anyRequest().authenticated());
        http.anonymous(httpSecurityAnonymousConfigurer -> httpSecurityAnonymousConfigurer.authorities(Collections.singletonList(new SimpleGrantedAuthority("ROLE_ANON"))));
        http.httpBasic(Customizer.withDefaults());
        http.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource));
        http.csrf(AbstractHttpConfigurer::disable);
        http.addFilter(new AuthorizationFilter(accessDecisionVoterAuthorizationManagerAdapter));
        return http.build();
    }
}
