package com.gymbuddy.auth.config;

import com.gymbuddy.auth.security.CustomMethodSecurityExpressionHandler;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDecisionVoter;
import org.springframework.security.access.expression.method.ExpressionBasedPreInvocationAdvice;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.access.prepost.PreInvocationAuthorizationAdviceVoter;
import org.springframework.security.access.vote.AffirmativeBased;
import org.springframework.security.access.vote.AuthenticatedVoter;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableMethodSecurity(securedEnabled = true, jsr250Enabled = true)
public class MethodSecurityConfig {
    @Autowired
    private ApplicationContext applicationContext;

    @PostConstruct
    public MethodSecurityExpressionHandler createExpressionHandler() {
        var expressionHandler = new CustomMethodSecurityExpressionHandler();
        expressionHandler.setApplicationContext(applicationContext);
        return expressionHandler;
    }

    @Bean
    public AccessDecisionManager accessDecisionManager() {

        List<AccessDecisionVoter<? extends Object>> decisionVoters = new ArrayList<>();

        var expresionAdvice = new ExpressionBasedPreInvocationAdvice();
        expresionAdvice.setExpressionHandler(createExpressionHandler());

        decisionVoters.add(new PreInvocationAuthorizationAdviceVoter(expresionAdvice));
        decisionVoters.add(new AuthenticatedVoter()); //It is necessary to add this one when we override the default AccessDecisionManager
        return new AffirmativeBased(decisionVoters);
    }
}
