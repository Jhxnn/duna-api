package com.dune.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange("app.exchange");
    }

    @Bean
    public Queue taskQueue() {
        return new Queue("task.save.queue", true);
    }

    @Bean
    public Queue projectQueue() {
        return new Queue("project.save.queue", true);
    }

    @Bean
    public Binding taskBinding() {
        return BindingBuilder
                .bind(taskQueue())
                .to(exchange())
                .with("task.save");
    }

    @Bean
    public Binding projectBinding() {
        return BindingBuilder
                .bind(projectQueue())
                .to(exchange())
                .with("project.save");
    }
}
