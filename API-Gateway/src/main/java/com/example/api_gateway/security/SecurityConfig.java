package com.example.api_gateway.security;

	import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;
	
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.SecurityWebFiltersOrder;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.web.server.SecurityWebFilterChain;




@Configuration
@EnableWebFluxSecurity
public class SecurityConfig {



    @Bean
    public SecurityWebFilterChain springSecurityFilterChain(ServerHttpSecurity http) {
        http
            .csrf(ServerHttpSecurity.CsrfSpec::disable)
            .formLogin(ServerHttpSecurity.FormLoginSpec::disable)

            // ❌ Disable HTTP Basic auth (important)
            .httpBasic(ServerHttpSecurity.HttpBasicSpec::disable)
            .authorizeExchange(exchanges -> exchanges
                    .pathMatchers("/auth/**").permitAll()
                    .pathMatchers("/public/**").permitAll()
                    .pathMatchers("/actuator/**").permitAll()
                    .anyExchange().permitAll()
            );

            

            

        return http.build();
    }
    







    @Bean
    public PasswordEncoder passwordEncoder(){
    	return new BCryptPasswordEncoder();
    }

  
    
}
  // You typically also define a PasswordEncoder bean and UserDetailsService bean here or in a separate config
    


