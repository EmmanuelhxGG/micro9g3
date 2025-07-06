package com.micro9.micro9g3.assembler;

import com.micro9.micro9g3.controller.ProveedorController;
import com.micro9.micro9g3.model.Proveedor;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.RepresentationModelAssembler;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.*;

@Component
public class ProveedorAssembler
        implements RepresentationModelAssembler<Proveedor, EntityModel<Proveedor>> {

    @Override
    @NonNull
    public EntityModel<Proveedor> toModel(@NonNull Proveedor proveedor) {
        return EntityModel.of(proveedor,
                linkTo(methodOn(ProveedorController.class)
                        .obtenerProveedores()).withRel("proveedores"),
                linkTo(methodOn(ProveedorController.class)
                        .registrarProveedor(proveedor)).withRel("registrar")
        );
    }
}