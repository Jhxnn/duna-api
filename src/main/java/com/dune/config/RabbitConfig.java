    package com.dune.config;

    import org.springframework.amqp.rabbit.connection.ConnectionFactory;
    import org.springframework.amqp.core.Binding;
    import org.springframework.amqp.core.BindingBuilder;
    import org.springframework.amqp.core.DirectExchange;
    import org.springframework.amqp.core.Queue;
    import org.springframework.amqp.rabbit.core.RabbitTemplate;
    import org.springframework.amqp.support.converter.JacksonJsonMessageConverter;
    import org.springframework.context.annotation.Bean;
    import org.springframework.context.annotation.Configuration;
    import org.springframework.amqp.support.converter.MessageConverter;

    @Configuration
    public class RabbitConfig {

        @Bean
        public DirectExchange exchange() {
            return new DirectExchange("app.exchange");
        }

        @Bean
        public Queue taskSaveQueue() {
            return new Queue("task.save.queue", true);
        }

        @Bean
        public Queue taskUpdateQueue() {
            return new Queue("task.update.queue", true);
        }

        @Bean
        public Queue projectSaveQueue() {
            return new Queue("project.save.queue", true);
        }

        @Bean
        public Queue projectUpdateQueue() {
            return new Queue("project.update.queue", true);
        }

        @Bean
        public Binding taskSaveBinding() {
            return BindingBuilder
                    .bind(taskSaveQueue())
                    .to(exchange())
                    .with("task.save");
        }

        @Bean
        public Binding taskUpdateBinding() {
            return BindingBuilder
                    .bind(taskUpdateQueue())
                    .to(exchange())
                    .with("task.update");
        }

        @Bean
        public Binding projectSaveBinding() {
            return BindingBuilder
                    .bind(projectSaveQueue())
                    .to(exchange())
                    .with("project.save");
        }

        @Bean
        public Binding projectUpdateBinding() {
            return BindingBuilder
                    .bind(projectUpdateQueue())
                    .to(exchange())
                    .with("project.update");
        }
        @Bean
        public MessageConverter jsonMessageConverter() {
            return new JacksonJsonMessageConverter();
        }

        @Bean
        public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
            RabbitTemplate template = new RabbitTemplate(connectionFactory);
            template.setMessageConverter(jsonMessageConverter());
            return template;
        }


    }
