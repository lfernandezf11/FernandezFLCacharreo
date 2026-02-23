package es.cacharreo.beans;

import java.io.Serializable;

/**
 *
 * @author fdezf
 */
public class LineaPedido implements Serializable{
    private Short idLinea;
    private Pedido pedido;
    private Producto producto;
    private Integer cantidad;
    private Float importe; //??? Base imponible de la linea antes de la suma y el iva?

    public LineaPedido() {
    }

    public Short getIdLinea() {
        return idLinea;
    }

    public void setIdLinea(Short idLinea) {
        this.idLinea = idLinea;
    }

    public Pedido getPedido() {
        return pedido;
    }

    public void setPedido(Pedido pedido) {
        this.pedido = pedido;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }   

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }
    
}
