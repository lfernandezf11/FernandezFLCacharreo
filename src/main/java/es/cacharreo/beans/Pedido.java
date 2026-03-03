package es.cacharreo.beans;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Representa la cabecera de un pedido de compra en el sistema.
 * <p>
 * Esta clase centraliza la información general de una transacción, vinculando a
 * un {@link Usuario} con una lista de {@link LineaPedido}. Gestiona el estado
 * del ciclo de vida del pedido (Carrito, Pendiente, Finalizado) y realiza los
 * cálculos económicos globales (Base imponible e IVA).</p>
 *
 *
 *
 * @author fdezf
 * @version 1.0
 */
public class Pedido implements Serializable {

    /**
     * Identificador único del pedido en la base de datos
     */
    private Short idPedido;

    /**
     * Fecha de creación o finalización del pedido
     */
    private Date fecha;

    /**
     * Estado actual del pedido
     */
    private Character estado; // default 'c'

    /**
     * Usuario propietario del pedido
     */
    private Usuario usuario; // A través de fk idUsuario

    /**
     * Base imponible total (suma de los importes de todas las líneas)
     */
    private Float importe; // 2 decimales

    /**
     * Cuota de IVA total calculada sobre la base imponible
     */
    private Float iva; // 2 decimales

    /**
     * Colección de líneas que componen el detalle del pedido
     */
    private List<LineaPedido> lineas; // Almacena las líneas con sus Productos, cantidades e importes correspondientes

    /**
     * Constructor por defecto.
     * <p>
     * Inicializa la lista de líneas como un {@link ArrayList} vacío y establece
     * los valores económicos iniciales a cero para evitar excepciones de
     * puntero nulo durante la renderización en las vistas (JSP).</p>
     */
    public Pedido() {
        this.lineas = new ArrayList<>();
        this.importe = 0.0f;
        this.iva = 0.0f;
    }

    /**
     * @return El ID único del pedido
     */
    public Short getIdPedido() {
        return idPedido;
    }

    public void setIdPedido(Short idPedido) {
        this.idPedido = idPedido;
    }

    /**
     * @return Fecha asociada al registro
     */
    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    /**
     * @return Carácter representativo del estado (default 'C')
     */
    public char getEstado() {
        return estado;
    }

    public void setEstado(char estado) {
        this.estado = estado;
    }

    /**
     * @return El objeto {@link Usuario} vinculado
     */
    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * @return La base imponible acumulada
     */
    public Float getImporte() {
        return importe;
    }

    public void setImporte(Float importe) {
        this.importe = importe;
    }

    /**
     * @return El IVA total calculado
     */
    public Float getIva() {
        return iva;
    }

    public void setIva(Float iva) {
        this.iva = iva;
    }

    /**
     * @return Lista de {@link LineaPedido} del pedido
     */
    public List<LineaPedido> getLineas() {
        return lineas;
    }

    public void setLineas(List<LineaPedido> lineas) {
        this.lineas = lineas;
    }

    /**
     * Calcula y actualiza los totales económicos del pedido.
     * <p>
     * Este método recorre cada {@link LineaPedido}, actualiza su importe
     * individual (precio * cantidad) y suma los resultados para actualizar la
     * base imponible y el IVA (21%) en la cabecera.</p>
     *
     * <p>
     * Es fundamental invocar este método tras cualquier modificación en las
     * líneas para garantizar la consistencia de los datos antes de enviarlos a
     * la BD.</p>
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

    /**
     * Obtiene el conteo total de unidades físicas presentes en el pedido.
     * <p>
     * Útil para representar el número de artículos en los indicadores (badges)
     * del carrito en la interfaz de usuario.</p>
     *
     * @return Suma total de las cantidades de todas las líneas.
     */
    public int getCantidadTotal() {
        int total = 0;
        if (this.lineas != null) {
            for (LineaPedido lp : this.lineas) {
                total += lp.getCantidad(); // Sumamos las unidades de cada producto 
            }
        }
        return total;
    }
}
