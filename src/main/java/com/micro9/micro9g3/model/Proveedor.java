package com.micro9.micro9g3.model;

import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "proveedor")
@JsonIgnoreProperties("pedidosReabastecimiento")
public class Proveedor {

    @Id
    @Column(name = "id")
    private int id;

    @Column(name = "nombre_proveedor", length = 100, nullable = false)
    private String nombreProveedor;

    @Column(name = "run_proveedor", length = 12, nullable = false, unique = true)
    private String runProveedor;

    @Column(name = "direccion_proveedor", length = 200, nullable = false)
    private String direccionProveedor;

    @Column(name = "telefono_proveedor", length = 15, nullable = false)
    private String telefonoProveedor;

    @Column(name = "email_proveedor", length = 100, nullable = false)
    private String emailProveedor;

    @OneToMany(mappedBy = "proveedor", fetch = FetchType.LAZY)
    private List<PedidoReabastecimiento> pedidosReabastecimiento;
}
