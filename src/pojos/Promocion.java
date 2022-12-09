package pojos;

public class Promocion {

    private Integer idPromocion;
    private Integer idSucursal;

    private String nombre;
    private String descripcion;
    private String fechaInicio;
    private String fechaTermino;
    private String restricciones;
    private Integer tipoPromocion;
    private String porcentaje;
    private float costoPromocion;
    private Integer categoriaPromocion;
    private Integer idEstatus;
    private String fotoPromocion;

    public Promocion(){
        
    }

    public Promocion(Integer idPromocion, Integer idSucursal, String nombre, String descripcion, String fechaInicio, String fechaTermino, String restricciones, Integer tipoPromocion, String porcentaje, float costoPromocion, Integer categoriaPromocion, Integer idEstatus,String fotoPromocion) {
        this.idPromocion = idPromocion;
        this.idSucursal = idSucursal;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaInicio = fechaInicio;
        this.fechaTermino = fechaTermino;
        this.restricciones = restricciones;
        this.tipoPromocion = tipoPromocion;
        this.porcentaje = porcentaje;
        this.costoPromocion = costoPromocion;
        this.categoriaPromocion = categoriaPromocion;
        this.idEstatus = idEstatus;
        this.fotoPromocion = fotoPromocion;
    }

    public Integer getIdPromocion() {
        return idPromocion;
    }

    public void setIdPromocion(Integer idPromocion) {
        this.idPromocion = idPromocion;
    }

    public Integer getIdSucursal() {
        return idSucursal;
    }

    public void setIdSucursal(Integer idSucursal) {
        this.idSucursal = idSucursal;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public void setFechaInicio(String fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    public String getFechaTermino() {
        return fechaTermino;
    }

    public void setFechaTermino(String fechaTermino) {
        this.fechaTermino = fechaTermino;
    }

    public String getRestricciones() {
        return restricciones;
    }

    public void setRestricciones(String restricciones) {
        this.restricciones = restricciones;
    }

    public Integer getTipoPromocion() {
        return tipoPromocion;
    }

    public void setTipoPromocion(Integer tipoPromocion) {
        this.tipoPromocion = tipoPromocion;
    }

    public String getPorcentaje() {
        return porcentaje;
    }

    public void setPorcentaje(String porcentaje) {
        this.porcentaje = porcentaje;
    }

    public float getCostoPromocion() {
        return costoPromocion;
    }

    public void setCostoPromocion(float costoPromocion) {
        this.costoPromocion = costoPromocion;
    }

    public Integer getCategoriaPromocion() {
        return categoriaPromocion;
    }

    public void setCategoriaPromocion(Integer categoriaPromocion) {
        this.categoriaPromocion = categoriaPromocion;
    }

    public Integer getIdEstatus() {
        return idEstatus;
    }

    public void setIdEstatus(Integer idEstatus) {
        this.idEstatus = idEstatus;
    }

    public String getFotoPromocion() {
        return fotoPromocion;
    }

    public void setFotoPromocion(String fotoPromocion) {
        this.fotoPromocion = fotoPromocion;
    }
                   
}
