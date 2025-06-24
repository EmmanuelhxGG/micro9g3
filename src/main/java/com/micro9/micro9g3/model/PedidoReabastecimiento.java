package com.micro9.micro9g3.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "pedido_reabastecimiento")
public class PedidoReabastecimiento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_pedido")
    private Integer idPedidoReab;

    @Column(name = "nombre_producto", length = 100, nullable = false)
    private String nombreProducto;

    @Column(name = "cantidad", nullable = false)
    private Integer cantidad;

    @Column(name = "precio_unitario", nullable = false)
    private Integer precioUnitario;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_proveedor", nullable = false)
    @JsonIgnoreProperties({"runProveedor", "direccionProveedor"})
    private Proveedor proveedor;

    @Column(name = "fecha_creacion")
    private LocalDate fechaCreacion;

    @Column(name = "estado_pedido_reab", length = 20)
    @Enumerated(EnumType.STRING)
    private EstadoPedido estadoPedidoReab;

    @Column(name = "autorizado_por", length = 50)
    private String autorizadoPor;
}
