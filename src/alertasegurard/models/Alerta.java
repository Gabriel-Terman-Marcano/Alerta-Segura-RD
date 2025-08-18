package alertasegurard.models;

import java.sql.Timestamp;

/**
 * Clase que representa una alerta en el sistema.
 *
 * <p>Esta clase es un POJO (Plain Old Java Object) que encapsula los datos
 * de una alerta, como su título, descripción, tipo y estado, para ser
 * transferida entre las diferentes capas de la aplicación.</p>
 *
 * @author G1
 */
public class Alerta {
    
    // Atributos de la clase
    private int id;
    private String titulo;
    private String descripcion;
    private String tipoAlerta;
    private String nivelAlerta;
    private String sectorAfectado;
    private String instrucciones;
    private Timestamp fechaCreacion;
    private String imagenURL;

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getTipoAlerta() { return tipoAlerta; }
    public void setTipoAlerta(String tipoAlerta) { this.tipoAlerta = tipoAlerta; }
    
    public String getNivelAlerta() { return nivelAlerta; }
    public void setNivelAlerta(String nivelAlerta) { this.nivelAlerta = nivelAlerta; }
    
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getSectorAfectado() { return sectorAfectado; }
    public void setSectorAfectado(String sectorAfectado) { this.sectorAfectado = sectorAfectado; }

    public String getInstrucciones() { return instrucciones; }
    public void setInstrucciones(String instrucciones) { this.instrucciones = instrucciones; }

    public Timestamp getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(Timestamp fechaCreacion) { this.fechaCreacion = fechaCreacion; }
    
    public String getImagenUrl() { return imagenURL; }
    public void setImagenUrl(String imagenUrl) { this.imagenURL = imagenUrl; }
}
