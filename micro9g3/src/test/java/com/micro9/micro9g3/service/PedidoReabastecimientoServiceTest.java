package com.micro9.micro9g3.service;

import com.micro9.micro9g3.model.*;
import com.micro9.micro9g3.repository.PedidoReabastecimientoRepository;
import com.micro9.micro9g3.repository.ProveedorRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

class PedidoReabastecimientoServiceTest {

    @Mock
    private PedidoReabastecimientoRepository pedidoRepository;

    @Mock
    private ProveedorRepository proveedorRepository;

    @InjectMocks
    private PedidoReabastecimientoService pedidoService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testObtenerTodosLosPedidos() {
        Proveedor proveedor = new Proveedor(2, "Proveedor Dos", "22.222.222-2", "Avenida Siempre Viva 742", "987654321", "proveedor2@correo.com", null);

        PedidoReabastecimiento pedido1 = new PedidoReabastecimiento(5, "Polera", 15, 300, proveedor, LocalDate.now(), EstadoPedido.PENDIENTE, null);
        PedidoReabastecimiento pedido2 = new PedidoReabastecimiento(6, "Cepillos", 8, 450, proveedor, LocalDate.now(), EstadoPedido.AUTORIZADO, "GERENTE");

        when(pedidoRepository.findById(5)).thenReturn(Optional.of(pedido1));
        when(pedidoRepository.findById(6)).thenReturn(Optional.of(pedido2));

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoReabastecimiento> resultado = pedidoService.obtenerTodosLosPedidos();

        assertThat(resultado).hasSize(2);
        assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Polera");
        assertThat(resultado.get(1).getEstadoPedidoReab()).isEqualTo(EstadoPedido.AUTORIZADO);
        verify(pedidoRepository).findAll();
    }

    @Test
    void testCrearPedido() {
        Proveedor proveedor = new Proveedor(2, "Proveedor Dos", "22.222.222-2", "Avenida Siempre Viva 742", "987654321", "proveedor2@correo.com", null);

        PedidoReabastecimiento pedido = new PedidoReabastecimiento(
            null,
            "Jabon",
            5,
            700,
            proveedor,
            null,
            null,
            null
        );

        PedidoReabastecimiento pedidoGuardado = new PedidoReabastecimiento(
            3,
            "Jabon",
            5,
            700,
            proveedor,
            LocalDate.now(),
            EstadoPedido.PENDIENTE,
            null
        );

        when(proveedorRepository.findById(2)).thenReturn(Optional.of(proveedor));
        when(pedidoRepository.save(any(PedidoReabastecimiento.class))).thenReturn(pedidoGuardado);

        PedidoReabastecimiento resultado = pedidoService.crearPedido(pedido);

        assertThat(resultado.getIdPedidoReab()).isEqualTo(3);
        assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.PENDIENTE);
        verify(pedidoRepository).save(any(PedidoReabastecimiento.class));
    }

    @Test
    void testActualizarEstadoPedido() {
        Proveedor proveedor = new Proveedor(2, "Proveedor Dos", "22.222.222-2", "Avenida Siempre Viva 742", "987654321", "proveedor2@correo.com", null);

        PedidoReabastecimiento pedidoExistente = new PedidoReabastecimiento(
            4,
            "Bolsa ecológica",
            12,
            1200,
            proveedor,
            LocalDate.now(),
            EstadoPedido.PENDIENTE,
            null
        );

        PedidoReabastecimiento pedidoActualizado = new PedidoReabastecimiento(
            4,
            "Bolsa ecológica",
            12,
            1200,
            proveedor,
            LocalDate.now(),
            EstadoPedido.AUTORIZADO,
            "GERENTE"
        );

        when(pedidoRepository.findById(4)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any(PedidoReabastecimiento.class))).thenReturn(pedidoActualizado);

        PedidoReabastecimiento resultado = pedidoService.actualizarEstadoPedido(4, EstadoPedido.AUTORIZADO, Autorizacion.GERENTE);

        assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.AUTORIZADO);
        assertThat(resultado.getAutorizadoPor()).isEqualTo("GERENTE");
        verify(pedidoRepository).save(any(PedidoReabastecimiento.class));
    }
}
