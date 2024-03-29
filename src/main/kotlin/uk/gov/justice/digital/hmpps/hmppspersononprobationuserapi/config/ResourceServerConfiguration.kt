package uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.invoke
import org.springframework.security.web.SecurityFilterChain

@Configuration
@EnableWebSecurity
@EnableMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@EnableJpaRepositories("uk.gov.justice.digital.hmpps.hmppspersononprobationuserapi.jpa.repository")
class ResourceServerConfiguration {

  @Bean
  fun filterChain(http: HttpSecurity): SecurityFilterChain {
    http {
      csrf { disable() }
      authorizeHttpRequests {
        listOf(
          "/webjars/**", "/favicon.ico", "/csrf",
          "/health/**", "/info", "/prometheus", "/h2-console/**", "/prototype/**",
          "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html",
        ).forEach { authorize(it, permitAll) }
        authorize(anyRequest, authenticated)
      }
      oauth2ResourceServer { jwt { jwtAuthenticationConverter = AuthAwareTokenConverter() } }
    }
    return http.build()
  }
}
