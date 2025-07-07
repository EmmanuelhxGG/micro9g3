package com.micro9.micro9g3.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.assertj.core.api.Assertions.assertThat;

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
        Proveedor proveedor = new Proveedor(1, "Proveedor 1", "11.111.111-1", "Dirección 1", "123456789", "correo@proveedor.com", null);
        PedidoReabastecimiento pedido1 = new PedidoReabastecimiento(1, "Producto A", 10, 1000, proveedor, LocalDate.now(), EstadoPedido.PENDIENTE, null);
        PedidoReabastecimiento pedido2 = new PedidoReabastecimiento(2, "Producto B", 5, 500, proveedor, LocalDate.now(), EstadoPedido.AUTORIZADO, "GERENTE");

        when(pedidoRepository.findAll()).thenReturn(List.of(pedido1, pedido2));

        List<PedidoReabastecimiento> pedidos = pedidoService.ListarPedidos();

        assertThat(pedidos).hasSize(2);
        verify(pedidoRepository).findAll();
    }

        @Test
void testCrearPedidoValido() {
    Proveedor proveedor = new Proveedor(1, "Proveedor Valido", "11.111.111-1", "Calle Falsa 123", "123456789", "proveedor@correo.com", null);

    PedidoReabastecimiento pedido = new PedidoReabastecimiento(
        null,
        "Producto Valido",
        10,
        1000,
        proveedor,
        null,
        null,
        null
    );

    PedidoReabastecimiento pedidoGuardado = new PedidoReabastecimiento(
        1,
        "Producto Valido",
        10,
        1000,
        proveedor,
        LocalDate.now(),
        EstadoPedido.PENDIENTE,
        null
    );

    when(proveedorRepository.findById(1)).thenReturn(Optional.of(proveedor));
    when(pedidoRepository.save(any(PedidoReabastecimiento.class))).thenReturn(pedidoGuardado);

    PedidoReabastecimiento resultado = pedidoService.crearPedido(pedido);

    assertThat(resultado).isNotNull();
    assertThat(resultado.getIdPedidoReab()).isEqualTo(1);
    assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.PENDIENTE);
    verify(proveedorRepository).findById(1);
    verify(pedidoRepository).save(any(PedidoReabastecimiento.class));
}

    @Test
    void testCrearPedidoConPedidoNull() {
        PedidoReabastecimiento resultado = pedidoService.crearPedido(null);
        assertThat(resultado).isNull();
    }

    @Test
    void testCrearPedidoConProveedorNull() {
        PedidoReabastecimiento pedido = new PedidoReabastecimiento(
            null,
            "Producto X",
            1,
            100,
            null,
            null,
            null,
            null
        );
        PedidoReabastecimiento resultado = pedidoService.crearPedido(pedido);
        assertThat(resultado).isNull();
    }

    @Test
    void testActualizarEstadoPedido() {
        Proveedor proveedor = new Proveedor(1, "Proveedor 1", "11.111.111-1", "Dirección 1", "123456789", "correo@proveedor.com", null);
        PedidoReabastecimiento pedidoExistente = new PedidoReabastecimiento(1, "Producto A", 10, 1000, proveedor, LocalDate.now(), EstadoPedido.PENDIENTE, null);
        PedidoReabastecimiento pedidoActualizado = new PedidoReabastecimiento(1, "Producto A", 10, 1000, proveedor, LocalDate.now(), EstadoPedido.AUTORIZADO, "GERENTE");

        when(pedidoRepository.findById(1)).thenReturn(Optional.of(pedidoExistente));
        when(pedidoRepository.save(any())).thenReturn(pedidoActualizado);

        PedidoReabastecimiento resultado = pedidoService.actualizarEstadoPedido(1, EstadoPedido.AUTORIZADO, Autorizacion.GERENTE);

        assertThat(resultado.getEstadoPedidoReab()).isEqualTo(EstadoPedido.AUTORIZADO);
        assertThat(resultado.getAutorizadoPor()).isEqualTo("GERENTE");
        verify(pedidoRepository).findById(1);
        verify(pedidoRepository).save(any());
    }

}
