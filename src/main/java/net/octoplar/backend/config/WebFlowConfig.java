package net.octoplar.backend.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.servlet.i18n.LocaleChangeInterceptor;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.webflow.config.AbstractFlowConfiguration;
import org.springframework.webflow.definition.registry.FlowDefinitionRegistry;
import org.springframework.webflow.engine.builder.support.FlowBuilderServices;
import org.springframework.webflow.executor.FlowExecutor;
import org.springframework.webflow.mvc.builder.MvcViewFactoryCreator;
import org.springframework.webflow.mvc.servlet.FlowHandlerAdapter;
import org.springframework.webflow.mvc.servlet.FlowHandlerMapping;

import java.util.Collections;

/**
 * Created by Octoplar.
 */
@Configuration
@EnableAutoConfiguration
public class WebFlowConfig extends AbstractFlowConfiguration {

    @Autowired
    private InternalResourceViewResolver resolver;

    @Autowired
    private LocaleChangeInterceptor localeChangeInterceptor;

    @Autowired
    private LocalValidatorFactoryBean localValidatorFactoryBean;

    @Bean
    public FlowExecutor flowExecutor() {
        return getFlowExecutorBuilder(flowRegistry()).build();
    }

    @Bean
    public FlowBuilderServices flowBuilderServices() {
        return getFlowBuilderServicesBuilder()
                .setDevelopmentMode ( true )
                .setViewFactoryCreator(mvcViewFactoryCreator())
                .setValidator(localValidatorFactoryBean)
                .build();
    }

    @Bean
    public MvcViewFactoryCreator mvcViewFactoryCreator(){
        MvcViewFactoryCreator mvcViewFactoryCreator=new MvcViewFactoryCreator();
        mvcViewFactoryCreator.setViewResolvers(Collections.singletonList(resolver));
        return mvcViewFactoryCreator;
    }

    @Bean
    public FlowDefinitionRegistry flowRegistry() {
        return getFlowDefinitionRegistryBuilder()
                .setFlowBuilderServices(flowBuilderServices())
                .addFlowLocation("/WEB-INF/flows/coffee-flow.xml", "order_webflow")
                .build();
    }

    @Bean
    public FlowHandlerMapping flowHandlerMapping() {
        FlowHandlerMapping mapping = new FlowHandlerMapping();
        mapping.setOrder(1);
        mapping.setFlowRegistry(flowRegistry());
        mapping.setInterceptors(localeChangeInterceptor);
        return mapping;
    }

    @Bean
    public FlowHandlerAdapter flowHandlerAdapter() {
        FlowHandlerAdapter adapter = new FlowHandlerAdapter();
        adapter.setFlowExecutor(flowExecutor());
        return adapter;
    }






}

