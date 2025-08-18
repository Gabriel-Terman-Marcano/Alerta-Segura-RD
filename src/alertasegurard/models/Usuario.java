package alertasegurard.models;

/**
 * Clase que representa un usuario en el sistema.
 *
 * <p>Esta clase es un POJO (Plain Old Java Object) que encapsula los datos
 * de un usuario, como su información personal y estado, para ser transferida
 * entre las diferentes capas de la aplicación.</p>
 *
 * @author G1
 */
public class Usuario {
    
    // Atributos de la clase
    private int id;
    private String nombre;
    private String usuario; // Este campo suele representar la cédula en este contexto
    private String telefono;
    private String email;
    private String direccion;
    private String password;
    private String estado;

    /**
     * Constructor vacío.
     * <p>Inicializa el estado del usuario por defecto como "activo".</p>
     */
    public Usuario() {
        this.estado = "activo";
    }

    // Getters y Setters
    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    
    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}