package com.micro9.micro9g3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.micro9.micro9g3.model.EstadoPedido;
import com.micro9.micro9g3.model.PedidoReabastecimiento;
import com.micro9.micro9g3.model.Proveedor;
import com.micro9.micro9g3.service.PedidoReabastecimientoService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(PedidoReabastecimientoController.class)
class PedidoReabastecimientoControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PedidoReabastecimientoService pedidoService;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testObtenerTodosPedidos_conResultados() throws Exception {
        PedidoReabastecimiento p1 = crearPedidoEjemplo(1);
        PedidoReabastecimiento p2 = crearPedidoEjemplo(2);

        Mockito.when(pedidoService.ListarPedidos()).thenReturn(List.of(p1, p2));

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].nombreProducto").value("Producto 1"))
                .andExpect(jsonPath("$[1].nombreProducto").value("Producto 2"));
    }

    @Test
    void testObtenerTodosPedidos_sinResultados() throws Exception {
        Mockito.when(pedidoService.ListarPedidos()).thenReturn(List.of());

        mockMvc.perform(get("/api/pedidos"))
                .andExpect(status().isNoContent());
    }

    @Test
    void testCrearPedido_ok() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(0);
        PedidoReabastecimiento pedidoGuardado = crearPedidoEjemplo(10);

        Mockito.when(pedidoService.crearPedido(any(PedidoReabastecimiento.class)))
                .thenReturn(pedidoGuardado);

        mockMvc.perform(post("/api/pedidos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.idPedidoReab").value(10));
    }

    @Test
    void testCrearPedido_error() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(0);

        Mockito.when(pedidoService.crearPedido(any(PedidoReabastecimiento.class)))
                .thenReturn(null);

        mockMvc.perform(post("/api/pedidos/crear")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarAutorizacion_ok_conGerente() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(1);
        pedidoEntrada.setEstadoPedidoReab(EstadoPedido.AUTORIZADO);
        pedidoEntrada.setAutorizadoPor("GERENTE");

        PedidoReabastecimiento actualizado = crearPedidoEjemplo(1);
        actualizado.setEstadoPedidoReab(EstadoPedido.AUTORIZADO);
        actualizado.setAutorizadoPor("GERENTE");

        Mockito.when(pedidoService.actualizarEstadoPedido(
                eq(1),
                eq(EstadoPedido.AUTORIZADO),
                eq(com.micro9.micro9g3.model.Autorizacion.GERENTE)))
                .thenReturn(actualizado);

        mockMvc.perform(put("/api/pedidos/autorizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estadoPedidoReab").value("AUTORIZADO"))
                .andExpect(jsonPath("$.autorizadoPor").value("GERENTE"));
    }

    @Test
    void testActualizarAutorizacion_conAutorizadoPorNulo() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(1);
        pedidoEntrada.setEstadoPedidoReab(EstadoPedido.AUTORIZADO);
        pedidoEntrada.setAutorizadoPor(null);

        mockMvc.perform(put("/api/pedidos/autorizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarAutorizacion_conAutorizadoPorOtroRol() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(1);
        pedidoEntrada.setEstadoPedidoReab(EstadoPedido.AUTORIZADO);
        pedidoEntrada.setAutorizadoPor("SUPERVISOR");

        mockMvc.perform(put("/api/pedidos/autorizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void testActualizarAutorizacion_errorAlActualizar() throws Exception {
        PedidoReabastecimiento pedidoEntrada = crearPedidoEjemplo(1);
        pedidoEntrada.setEstadoPedidoReab(EstadoPedido.AUTORIZADO);
        pedidoEntrada.setAutorizadoPor("GERENTE");

        Mockito.when(pedidoService.actualizarEstadoPedido(
                eq(1),
                eq(EstadoPedido.AUTORIZADO),
                eq(com.micro9.micro9g3.model.Autorizacion.GERENTE)))
                .thenReturn(null);

        mockMvc.perform(put("/api/pedidos/autorizacion")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(pedidoEntrada)))
                .andExpect(status().isBadRequest());
    }

    private PedidoReabastecimiento crearPedidoEjemplo(int id) {
        Proveedor prov = new Proveedor();
        prov.setId(1);
        prov.setNombreProveedor("Proveedor X");

        PedidoReabastecimiento pedido = new PedidoReabastecimiento();
        pedido.setIdPedidoReab(id);
        pedido.setNombreProducto("Producto " + id);
        pedido.setCantidad(10);
        pedido.setPrecioUnitario(1000);
        pedido.setProveedor(prov);
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstadoPedidoReab(EstadoPedido.PENDIENTE);
        pedido.setAutorizadoPor(null);

        return pedido;
    }
}
