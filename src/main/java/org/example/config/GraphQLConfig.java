package org.example.config;

import org.example.service.CustomerService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.graphql.execution.RuntimeWiringConfigurer;

/**
 * Run and go to <a href="http://localhost:8080/graphiql?path=/graphql">page</a>
 * and try these examples:
 * <p>
 * {
 *   customers {
 *     name
 *   }
 * }
 * </p>
 * <p>
 * {
 *   customerById(id:2) {
 *     id
 *     name
 *   }
 * }
 * </p>
 */
@Configuration
public class GraphQLConfig {

    /**
     * We bind the service (here 'CustomerService') to our GraphQL schema (here the file 'mySchema.graphqls')
     */
    @Bean
    public RuntimeWiringConfigurer runtimeWiringConfigurer(CustomerService customerService) {
        return builder -> {
            builder.type("Query", wiring -> wiring
                    .dataFetcher("customers", environment -> customerService.getAll())
                    .dataFetcher("customerById", environment -> customerService.getById(environment.getArgument("id"))));
        };
    }
}
