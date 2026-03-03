package es.cacharreo.beans;

import java.io.Serializable;

/**
 * Representa una línea individual dentro de un pedido de compra.
 * <p>
 * Esta clase vincula un {@link Producto} específico con una cantidad
 * determinada, calculando el subtotal correspondiente. Actúa como el detalle
 * pormenorizado de la relación entre la cabecera del pedido y los productos del
 * catálogo.</p>
 *
 *
 *
 * @author Lucía Fernández Florencio
 * @version 1.0
 */
public class LineaPedido implements Serializable {

    /**
     * Identificador único de la línea de pedido en la base de datos
     */
    private Short idLinea;

    /**
     * Identificador del pedido (cabecera) al que pertenece esta línea
     */
    private Short idPedido;

    /**
     * Objeto producto asociado a esta línea, contiene datos como nombre y
     * precio unitario
     */
    private Producto producto;

    /**
     * Número de unidades solicitadas del producto
     */
    private Integer cantidad;

    /**
     * * Base imponible de la línea.
     * <p>
     * Calculado como: {@code producto.getPrecio() * cantidad}. Representa el
     * subtotal acumulado por este producto antes de aplicar el IVA global.</p>
     */
    private Float importe;

    /**
     * Constructor por defecto requerido para la especificación JavaBeans.
     */
    public LineaPedido() {
    }

    /**
     * Obtiene el identificador de la línea.
     *
     * @return El ID de la línea.
     */
    public Short getIdLinea() {
        return idLinea;
    }

    /**
     * Establece el identificador de la línea.
     *
     * @param idLinea El nuevo ID de la línea.
     */
    public void setIdLinea(Short idLinea) {
        this.idLinea = idLinea;
    }

    /**
     * Obtiene el identificador del pedido padre.
     *
     * @return El ID del pedido asociado.
     */
    public short getIdPedido() {
        return idPedido;
    }

    /**
     * Establece el identificador del pedido al que se vincula esta línea.
     *
     * @param idPedido El ID del pedido cabecera.
     */
    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * Obtiene el objeto Producto de la línea.
     *
     * @return Instancia de {@link Producto}.
     */
    public Producto getProducto() {
        return producto;
    }

    /**
     * Vincula un producto a la línea.
     *
     * @param producto El producto a añadir.
     */
    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    /**
     * Obtiene la cantidad de unidades de la línea.
     *
     * @return Número de unidades.
     */
    public Integer getCantidad() {
        return cantidad;
    }

    /**
     * Establece la cantidad de unidades para este producto.
     *
     * @param cantidad Cantidad total de artículos.
     */
    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    /**
     * Obtiene el importe total de la línea (Base imponible).
     *
     * @return El importe calculado (precio * cantidad).
     */
    public Float getImporte() {
        return importe;
    }

    /**
     * Establece el importe acumulado de la línea.
     *
     * @param importe El subtotal de la línea.
     */
    public void setImporte(Float importe) {
        this.importe = importe;
    }
}
