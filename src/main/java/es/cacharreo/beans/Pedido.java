package es.cacharreo.beans;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 *
 * @author fdezf
 */
public class Pedido implements Serializable {

    private Short idPedido;
    private Date fecha;
    private Character estado; // default 'C'
    private Usuario usuario; // A través de fk idUsuario
    private Float importe; // 2 decimales
    private Float iva; // 2 decimales
    private List<LineaPedido> lineas; // Almacena las líneas con sus Productos, cantidades e importes correspondientes

    public Pedido() {
    }

    public Short getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    
    /**
     * Calcula y actualiza los totales del pedido basándose en sus líneas. Este
     * método sincroniza el importe de cada línea y los totales de la cabecera,
     * asegurando que el objeto en memoria coincide con lo que la bd espera
     * recibir.
     */
    public void calcularTotales() {
        float acumuladoBase = 0.0f;

        if (this.lineas != null && !this.lineas.isEmpty()) {
            for (LineaPedido lp : this.lineas) {
                // Importe de la línea (subtotal = precio x cantidad)
                float precioUnidad = lp.getProducto().getPrecio();
                float subtotalLinea = precioUnidad * lp.getCantidad();

                lp.setImporte(subtotalLinea);

                acumuladoBase += subtotalLinea;
            }
        }

        // Actualizamos la cabecera 
        this.importe = acumuladoBase; // Base Imponible
        this.iva = acumuladoBase * 0.21f; // IVA calculado al 21%
    }

}
