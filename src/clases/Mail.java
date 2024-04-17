package clases;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
