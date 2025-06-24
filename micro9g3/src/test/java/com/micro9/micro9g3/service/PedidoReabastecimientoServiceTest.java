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
    Proveedor proveedor = new Proveedor(1, "Proveedor Uno", "11.111.111-1", "Calle 123", "912345678", "correo@ejemplo.com", null);

    PedidoReabastecimiento pedido1 = new PedidoReabastecimiento(1, "Producto A", 10, 500, proveedor, LocalDate.now(), EstadoPedido.PENDIENTE, null);
    PedidoReabastecimiento pedido2 = new PedidoReabastecimiento(2, "Producto B", 20, 800, proveedor, LocalDate.now(), EstadoPedido.AUTORIZADO, "GERENTE");

    when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

    List<PedidoReabastecimiento> resultado = pedidoService.obtenerTodosLosPedidos();

    assertThat(resultado).hasSize(2);
    assertThat(resultado.get(0).getNombreProducto()).isEqualTo("Producto A");
    assertThat(resultado.get(1).getEstadoPedidoReab()).isEqualTo(EstadoPedido.AUTORIZADO);
    verify(pedidoRepository).findAll();
}

    @Test
    void testCrearPedido() {
        Proveedor proveedor = new Proveedor(1, "Proveedor Uno", "11.111.111-1", "Calle 123", "912345678", "correo@ejemplo.com", null);

        PedidoReabastecimiento pedido = new PedidoReabastecimiento(
            null,
            "Producto A",
            10,
            500,
            proveedor,
            null,
            null,
            null
        );

        PedidoReabastecimiento pedidoGuardado = new PedidoReabastecimiento(
            1,
            "Producto A",
            10,
            500,
            proveedor,
            LocalDate.now(),
            EstadoPedido.PENDIENTE,
            null
        );

        when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
        when(pedidoRepository.save(any(PedidoReabastecimiento.class))).thenReturn(pedidoGuardado);

        PedidoReabastecimiento resultado = pedidoService.crearPedido(pedido);

        assertThat(resultado.getIdPedidoReab()).isEqualTo(1);
        assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.PENDIENTE);
        verify(pedidoRepository).save(any(PedidoReabastecimiento.class));
    }

    @Test
    void testActualizarEstadoPedido() {
        Proveedor proveedor = new Proveedor(1, "Proveedor Uno", "11.111.111-1", "Calle 123", "912345678", "correo@ejemplo.com", null);

        PedidoReabastecimiento pedidoExistente = new PedidoReabastecimiento(
            1,
            "Producto A",
            10,
            500,
            proveedor,
            LocalDate.now(),
            EstadoPedido.PENDIENTE,
            null
        );

        PedidoReabastecimiento pedidoActualizado = new PedidoReabastecimiento(
            1,
            "Producto A",
            10,
            500,
            proveedor,
            LocalDate.now(),
            EstadoPedido.AUTORIZADO,
            "GERENTE"
        );

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any(PedidoReabastecimiento.class))).thenReturn(pedidoActualizado);

        PedidoReabastecimiento resultado = pedidoService.actualizarEstadoPedido(1, EstadoPedido.AUTORIZADO, Autorizacion.GERENTE);

        assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.AUTORIZADO);
        assertThat(resultado.getAutorizadoPor()).isEqualTo("GERENTE");
        verify(pedidoRepository).save(any(PedidoReabastecimiento.class));
    }
}
