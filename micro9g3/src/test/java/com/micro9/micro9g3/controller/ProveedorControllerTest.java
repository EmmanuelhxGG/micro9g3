package com.micro9.micro9g3.controller;

import com.micro9.micro9g3.model.Proveedor;
import com.micro9.micro9g3.repository.ProveedorRepository;
import com.micro9.micro9g3.service.ProveedorService;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.ArrayList;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.fasterxml.jackson.databind.ObjectMapper;

@WebMvcTest(ProveedorController.class)
class ProveedorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProveedorRepository proveedorRepository;

    @MockBean
    private ProveedorService proveedorService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerProveedores_conDatos() throws Exception {
        List<Proveedor> proveedores = new ArrayList<>();
        proveedores.add(new Proveedor(1, "Prov 1", "11.111.111-1", "Dir 1", "123456", "prov1@mail.com", null));
        proveedores.add(new Proveedor(2, "Prov 2", "22.222.222-2", "Dir 2", "654321", "prov2@mail.com", null));

        when(proveedorRepository.findAll()).thenReturn(proveedores);

        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.length()").value(2))
            .andExpect(jsonPath("$[0].nombreProveedor").value("Prov 1"))
            .andExpect(jsonPath("$[1].runProveedor").value("22.222.222-2"));
    }

    @Test
    void testObtenerProveedores_sinDatos() throws Exception {
        when(proveedorRepository.findAll()).thenReturn(List.of());

        mockMvc.perform(get("/api/proveedores"))
            .andExpect(status().isNoContent());
    }

    @Test
    void testRegistrarProveedor() throws Exception {
        Proveedor nuevo = new Proveedor(10, "Nuevo Prov", "99.999.999-9", "Dir Nuevo", "999999", "nuevo@mail.com", null);

        when(proveedorService.guardarProveedor(any(Proveedor.class))).thenReturn(nuevo);

        mockMvc.perform(post("/api/proveedores/crear")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(nuevo)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").value(10))
            .andExpect(jsonPath("$.nombreProveedor").value("Nuevo Prov"));
    }
}
