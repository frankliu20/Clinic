package by.gp.clinic.configuration;

import by.gp.clinic.enumerated.UserRole;
import by.gp.clinic.filter.AuthenticationFilter;
import by.gp.clinic.filter.LoginFilter;
import by.gp.clinic.service.TokenAuthenticationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.sql.DataSource;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final DataSource dataSource;
    private final TokenAuthenticationService tokenAuthenticationService;
    private final Jackson2ObjectMapperBuilder objectMapperBuilder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.cors(withDefaults())
            .csrf(csrf -> csrf.disable())
            .authorizeRequests(requests -> requests
                .requestMatchers(HttpMethod.POST, "/login").permitAll()
                .requestMatchers("/development/**").permitAll()
                .requestMatchers("/public/**").permitAll()
                .requestMatchers("/actuator/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-ui.html").permitAll()
                .requestMatchers(HttpMethod.GET, "/v2/api-docs").permitAll()
                .requestMatchers(HttpMethod.GET, "/webjars/springfox-swagger-ui/**").permitAll()
                .requestMatchers(HttpMethod.GET, "/swagger-resources/**").permitAll()
                .requestMatchers("/admin/**").hasAuthority(UserRole.ADMIN.name())
                .requestMatchers("/**").hasAnyAuthority(UserRole.USER.name(), UserRole.DOCTOR.name(), UserRole.ADMIN.name())
                .anyRequest().authenticated())
            .addFilterBefore(new LoginFilter(tokenAuthenticationService, authenticationManager(http), objectMapperBuilder),
                UsernamePasswordAuthenticationFilter.class)
            .addFilterBefore(new AuthenticationFilter(tokenAuthenticationService),
                UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
        AuthenticationManagerBuilder auth = http.getSharedObject(AuthenticationManagerBuilder.class);
        auth.jdbcAuthentication()
            .dataSource(dataSource)
            .usersByUsernameQuery("select alias, password, enabled from user where alias=?")
            .authoritiesByUsernameQuery("select alias, role from user where alias=?");
        return auth.build();
    }
}
