package com.example.shoppingmaill.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing //를 지정하여 Auditing 기능 활성화
public class AuditingConfig {
    // "auditorProvider" 이름으로 AuditorAware 구현체 Bean 등록
    @Bean
    public AuditorAware<String> auditorProvide(){
        return new AuditorAwareImpl();
    }
}
