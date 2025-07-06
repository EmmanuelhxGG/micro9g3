package com.micro9.micro9g3.assembler;

import com.micro9.micro9g3.controller.PedidoReabastecimientoController;
import com.micro9.micro9g3.model.PedidoReabastecimiento;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

import org.springframework.lang.NonNull;

@Component
public class PedidoReabastecimientoAssembler
        implements RepresentationModelAssembler<PedidoReabastecimiento, EntityModel<PedidoReabastecimiento>> {

        @Override
        @NonNull
        public EntityModel<PedidoReabastecimiento> toModel(@NonNull PedidoReabastecimiento pedido) {
            return EntityModel.of(pedido,
                    linkTo(methodOn(PedidoReabastecimientoController.class)
                            .obtenerTodosLosPedidos()).withRel("pedidos"),
                    linkTo(methodOn(PedidoReabastecimientoController.class)
                            .crearPedido(pedido)).withRel("crear"),
                    linkTo(methodOn(PedidoReabastecimientoController.class)
                            .actualizarAutorizacion(pedido)).withRel("autorizar")
            );
        }
}
