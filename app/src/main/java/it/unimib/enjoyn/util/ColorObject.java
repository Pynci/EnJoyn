package it.unimib.enjoyn.util;

public class ColorObject {
    private String name;
    private String hex;
    private String contrastHex;

    public ColorObject(String name, String hex, String contrastHex) {
        this.name = name;
        this.hex = hex;
        this.contrastHex = contrastHex;
    }

    public String getHexHash() {
        return "#" + hex;
    }

    public String getContrastHexHash() {
        return "#" + contrastHex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getHex() {
        return hex;
    }

    public void setHex(String hex) {
        this.hex = hex;
    }

    public String getContrastHex() {
        return contrastHex;
    }

    public void setContrastHex(String contrastHex) {
        this.contrastHex = contrastHex;
    }
}