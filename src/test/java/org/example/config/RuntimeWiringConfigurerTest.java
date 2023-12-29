package org.example.config;

import org.example.model.Customer;
import org.example.model.Profile;
import org.example.service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationContext;
import org.springframework.graphql.test.tester.HttpGraphQlTester;
import org.springframework.test.web.reactive.server.WebTestClient;

import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureWebTestClient
class RuntimeWiringConfigurerTest {

    @MockBean
    private CustomerService customerService;

    private HttpGraphQlTester graphQLTester;

    private final List<Customer> customers = List.of(
            new Customer(1, "Bob", new Profile(3, 1)),
            new Customer(2, "Steve", new Profile(1, 2)),
            new Customer(3, "Maria", new Profile(2, 3))
    );

    @BeforeEach
    void setUp(@Autowired ApplicationContext applicationContext) {

        if (graphQLTester == null) {

            var webTestClient = WebTestClient.bindToApplicationContext(applicationContext)
                    .configureClient()
                    .baseUrl("/graphql")
                    .build();

            graphQLTester = HttpGraphQlTester.create(webTestClient);
        }

        // Mocking
        when(customerService.getAll()).thenReturn(customers);

        for (var customer : customers) {
            when(customerService.getById(customer.getId())).thenReturn(customer);
            when(customerService.getProfileFor(customer)).thenReturn(customer.getProfile());
        }
    }

    @Test
    void testQueryingCustomers() {

        String query = """
                {
                    customers {
                      id
                      name
                      profile {
                        id
                        customerId
                      }
                    }
                }
                """;

        var customersReceived = graphQLTester.document(query)
                .execute()
                .path("data.customers")
                .hasValue()
                .entityList(Customer.class)
                .get();

        assertEquals(customers.size(), customersReceived.size());
        assertEquals(new HashSet<>(customers), new HashSet<>(customersReceived));

        verify(customerService, times(1)).getAll();
    }

    @Test
    void testQueryingCustomerById() {

        for (var customer : customers) {

            String query = """
                    {
                        customerById(id: %s) {
                          id
                          name
                          profile {
                            id
                            customerId
                          }
                        }
                    }
                    """;

            query = String.format(query, customer.getId());

            var customerReceived = graphQLTester.document(query)
                    .execute()
                    .path("data.customerById")
                    .hasValue()
                    .entity(Customer.class)
                    .get();

            assertEquals(customer, customerReceived);

            verify(customerService, times(1)).getById(customer.getId());
        }
    }

    @Test
    void testQueryingCustomerByIdNotFound() {

        int id = -1;

        String query = """
                {
                    customerById(id: %s) {
                      id
                      name
                      profile {
                        id
                        customerId
                      }
                    }
                }
                """;

        query = String.format(query, id);

        graphQLTester.document(query)
                .execute()
                .path("data.customerById")
                .valueIsNull();

        verify(customerService, times(1)).getById(id);
    }
}