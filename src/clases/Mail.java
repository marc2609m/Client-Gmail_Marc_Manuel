package clases;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Mail {
    
    private String contingut;
    private String remitente;
    private String asunto;
    private List<File> ListaArchivos;

    public Mail() {
    }
    
    

    public Mail(String contingut, String remitente, String asunto) {
        this.contingut = contingut;
        this.remitente = remitente;
        this.asunto = asunto;
        ListaArchivos = new ArrayList();
    }

    public String getContingut() {
        return contingut;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Mail other = (Mail) obj;
        if (!Objects.equals(this.contingut, other.contingut)) {
            return false;
        }
        if (!Objects.equals(this.remitente, other.remitente)) {
            return false;
        }
        if (!Objects.equals(this.asunto, other.asunto)) {
            return false;
        }
        return Objects.equals(this.ListaArchivos, other.ListaArchivos);
    }

    public String getRemitente() {
        return remitente;
    }

    public String getAsunto() {
        return asunto;
    }

    public List getListaArchivos() {
        return ListaArchivos;
    }

    public void setContingut(String contingut) {
        this.contingut = contingut;
    }

    public void setRemitente(String remitente) {
        this.remitente = remitente;
    }

    public void setAsunto(String asunto) {
        this.asunto = asunto;
    }

    public void setListaArchivos(List ListaArchivos) {
        this.ListaArchivos = ListaArchivos;
    }

    @Override
    public String toString() {
        return "Mail{" + "contingut=" + contingut + ", remitente=" + remitente + ", asunto=" + asunto + ", ListaArchivos=" + ListaArchivos + '}';
    }
    
    
    
    
    
    
    
}
