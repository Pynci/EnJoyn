package it.unimib.enjoyn;

import java.util.Date;

public class Event {

private int id;
private String titolo;

private String descrizione;

private Date data;

private boolean privata;

private String Luogo;

private String nomeLuogo;

private Category categoria;

private int numeroPersone;


    public Event(int id, String titolo, String descrizione, Date data, boolean privata, String luogo, String nomeLuogo, Category categoria, int numeroPersone) {
        setId(id);
        setTitolo(titolo);
        setDescrizione(descrizione);
        setData(data);
        setPrivata(privata);
        setLuogo(luogo);
        setNomeLuogo(nomeLuogo);
        setCategoria(categoria);
        setNumeroPersone(numeroPersone);
    }

    public void setData(Date data) {
        this.data = data;
    }

    public void setDescrizione(String descrizione) {
        this.descrizione = descrizione;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLuogo(String luogo) {
        Luogo = luogo;
    }

    public void setCategoria(Category categoria) {
        this.categoria = categoria;
    }

    public void setNomeLuogo(String nomeLuogo) {
        this.nomeLuogo = nomeLuogo;
    }

    public void setPrivata(boolean privata) {
        this.privata = privata;
    }

    public void setTitolo(String titolo) {
        this.titolo = titolo;
    }

    public void setNumeroPersone(int numeroPersone) {
        this.numeroPersone = numeroPersone;
    }

    public int getId() {
        return id;
    }

    public String getTitolo() {
        return titolo;
    }

    public String getDescrizione() {
        return descrizione;
    }

    public Date getData() {
        return data;
    }

    public boolean isPrivata() {
        return privata;
    }

    public String getLuogo() {
        return Luogo;
    }

    public String getNomeLuogo() {
        return nomeLuogo;
    }

    public Category getCategoria() {
        return categoria;
    }

    public int getNumeroPersone() {
        return numeroPersone;
    }
}
