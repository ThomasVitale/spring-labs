package com.thomasvitale.tenantscope.note;

import com.thomasvitale.tenantscope.TenantScopeApplication;
import com.thomasvitale.tenantscope.identification.MultitenancyProperties;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(classes = TenantScopeApplication.class, webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class NoteRestControllerTest {

    @Autowired
    private MultitenancyProperties multitenancyProperties;

    @Autowired
    private TestRestTemplate restTemplate;

    @LocalServerPort
    private int port;

    @Test
    public void getNotesForTenantAcme() {
        // Expected data
        NoteDTO expectedNote1 = new NoteDTO("Acme Note 1", "Some funny note.");
        NoteDTO expectedNote2 = new NoteDTO("Acme Note 2", "Another funny note.");

        // Prepare HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.add(multitenancyProperties.getHttpHeader(), "TENANT_ACME");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Run HTTP request
        ResponseEntity<NoteDTO[]> response = restTemplate.exchange(
                buildNoteUrl(),
                HttpMethod.GET,
                entity,
                NoteDTO[].class
        );

        // Verify result
        Assertions.assertThat(response.getBody()).containsExactly(expectedNote1, expectedNote2);
    }

    @Test
    public void getNotesForTenantArgus() {
        // Expected data
        NoteDTO expectedNote1 = new NoteDTO("Argus Note 1", "Some secret note.");
        NoteDTO expectedNote2 = new NoteDTO("Argus Note 2", "Another secret note.");

        // Prepare HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.add(multitenancyProperties.getHttpHeader(), "TENANT_ARGUS");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Run HTTP request
        ResponseEntity<NoteDTO[]> response = restTemplate.exchange(
                buildNoteUrl(),
                HttpMethod.GET,
                entity,
                NoteDTO[].class
        );

        // Verify result
        Assertions.assertThat(response.getBody()).containsExactly(expectedNote1, expectedNote2);
    }

    @Test
    public void getTenantIdentifierForTenantAcme() {
        // Prepare HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.add(multitenancyProperties.getHttpHeader(), "TENANT_ACME");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Run HTTP request
        ResponseEntity<String> response = restTemplate.exchange(
                buildNoteUrl() + "/tenant",
                HttpMethod.GET,
                entity,
                String.class
        );

        // Verify result
        Assertions.assertThat(response.getBody()).isEqualTo("TENANT_ACME");
    }

    @Test
    public void getTenantIdentifierForTenantArgus() {
        // Prepare HTTP request
        HttpHeaders headers = new HttpHeaders();
        headers.add(multitenancyProperties.getHttpHeader(), "TENANT_ARGUS");
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        // Run HTTP request
        ResponseEntity<String> response = restTemplate.exchange(
                buildNoteUrl() + "/tenant",
                HttpMethod.GET,
                entity,
                String.class
        );

        // Verify result
        Assertions.assertThat(response.getBody()).isEqualTo("TENANT_ARGUS");
    }

    private String buildNoteUrl() {
        return "http://localhost:" + port + "/api/notes";
    }
}
