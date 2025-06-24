package com.micro9.micro9g3.service;

import com.micro9.micro9g3.model.Proveedor;
import com.micro9.micro9g3.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class ProveedorServiceTest {

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private ProveedorService proveedorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGuardarProveedor() {
        Proveedor proveedor = new Proveedor(
            0,
            "Proveedor Uno",
            "11.111.111-1",
            "Av. Siempre Viva 123",
            "912345678",
            "proveedor@correo.com",
            null
        );

        Proveedor proveedorGuardado = new Proveedor(
            1,
            "Proveedor Uno",
            "11.111.111-1",
            "Av. Siempre Viva 123",
            "912345678",
            "proveedor@correo.com",
            null
        );

        when(proveedorRepository.save(proveedor)).thenReturn(proveedorGuardado);

        Proveedor resultado = proveedorService.guardarProveedor(proveedor);

        assertThat(resultado.getId()).isEqualTo(1);
        assertThat(resultado.getNombreProveedor()).isEqualTo("Proveedor Uno");
        assertThat(resultado.getRunProveedor()).isEqualTo("11.111.111-1");

        verify(proveedorRepository).save(proveedor);
    }
}
