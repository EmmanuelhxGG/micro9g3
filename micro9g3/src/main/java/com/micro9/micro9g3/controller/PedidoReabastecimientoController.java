package com.micro9.micro9g3.controller;

import com.micro9.micro9g3.model.EstadoPedido;
import com.micro9.micro9g3.model.Autorizacion;
import com.micro9.micro9g3.model.PedidoReabastecimiento;
import com.micro9.micro9g3.service.PedidoReabastecimientoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/pedidos")
public class PedidoReabastecimientoController {

    @Autowired
    private PedidoReabastecimientoService pedidoService;

    @GetMapping 
    public ResponseEntity<List<PedidoReabastecimiento>> obtenerTodosLosPedidos() {
        List<PedidoReabastecimiento> pedidos = pedidoService.obtenerTodosLosPedidos();
        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }

    @PostMapping("/crear")
    public ResponseEntity<PedidoReabastecimiento> crearPedido(@RequestBody PedidoReabastecimiento pedido) {
        PedidoReabastecimiento nuevoPedido = pedidoService.crearPedido(pedido);
        if (nuevoPedido == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoPedido);
    }

    @PutMapping("/autorizacion") 
    public ResponseEntity<PedidoReabastecimiento> actualizarAutorizacion(@RequestBody PedidoReabastecimiento pedido) {
        if (pedido.getAutorizadoPor() == null || !pedido.getAutorizadoPor().equals("GERENTE")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }

        EstadoPedido estado = pedido.getEstadoPedidoReab();
        Autorizacion autorizado = Autorizacion.GERENTE;

        PedidoReabastecimiento actualizado = pedidoService.actualizarEstadoPedido(
                pedido.getIdPedidoReab(),
                estado,
                autorizado
        );

        if (actualizado == null) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
        return ResponseEntity.status(HttpStatus.OK).body(actualizado);
    }

}
