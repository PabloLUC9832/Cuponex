package pojos;

public class Catalogo {
    
    private Integer idCatalogo;
    private String nombre;

    public Catalogo() {
    }        

    public Catalogo(Integer idCatalogo, String nombre) {
        this.idCatalogo = idCatalogo;
        this.nombre = nombre;
    }

    public Integer getIdCatalogo() {
        return idCatalogo;
    }

    public void setIdCatalogo(Integer idCatalogo) {
        this.idCatalogo = idCatalogo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        //String idCatalogoStr = Integer.toString(idCatalogo);
        return idCatalogo+"-"+nombre;
    }
    
}
