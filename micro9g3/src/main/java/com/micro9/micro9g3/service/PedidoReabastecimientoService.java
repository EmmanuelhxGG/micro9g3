package com.micro9.micro9g3.service;

import com.micro9.micro9g3.model.EstadoPedido;
import com.micro9.micro9g3.model.PedidoReabastecimiento;
import com.micro9.micro9g3.model.Proveedor;
import com.micro9.micro9g3.model.Autorizacion;
import com.micro9.micro9g3.repository.PedidoReabastecimientoRepository;
import com.micro9.micro9g3.repository.ProveedorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class PedidoReabastecimientoService {

    @Autowired
    private PedidoReabastecimientoRepository pedidoRepository;

    @Autowired
    private ProveedorRepository proveedorRepository;

    public List<PedidoReabastecimiento> obtenerTodosLosPedidos() {
        return pedidoRepository.findAll();
    }

    public PedidoReabastecimiento crearPedido(PedidoReabastecimiento pedido) {
        Optional<Proveedor> proveedorOpt = proveedorRepository.findById(pedido.getProveedor().getId());
        if (proveedorOpt.isEmpty()) {
            return null;
        }

        pedido.setProveedor(proveedorOpt.get());
        pedido.setFechaCreacion(LocalDate.now());
        pedido.setEstadoPedidoReab(EstadoPedido.PENDIENTE);
        pedido.setAutorizadoPor(null);

        return pedidoRepository.save(pedido);
    }

    public PedidoReabastecimiento actualizarEstadoPedido(int idPedido, EstadoPedido nuevoEstado, Autorizacion autorizadoPor) {
        Optional<PedidoReabastecimiento> pedidoOpt = pedidoRepository.findById(idPedido);
        if (pedidoOpt.isEmpty()) {
            return null;
        }

        PedidoReabastecimiento pedido = pedidoOpt.get();
        pedido.setEstadoPedidoReab(nuevoEstado);
        if (autorizadoPor != null && autorizadoPor == Autorizacion.GERENTE) {
            pedido.setAutorizadoPor("GERENTE");
        } else {
            pedido.setAutorizadoPor(null);
        }

        return pedidoRepository.save(pedido);
    }

}
