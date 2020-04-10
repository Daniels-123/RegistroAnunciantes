package com.digitalandroidyweb.registroanunciantes.Adaptor;

public class ExampleItem {

    private String Id_Nombre;
    private String Nombre;
    private String Direccion;
    private String Barrio;
    private String Categoria;
    private String SubCategoria;

    public ExampleItem() {
    }

    public ExampleItem(String id, String nombre, String direccion, String barrio, String categoria, String subcategoria) {

        Id_Nombre = id;
        Nombre = nombre;
        Direccion = direccion;
        Barrio = barrio;
        Categoria = categoria;
        SubCategoria = subcategoria;

    }


    public String getId_Nombre() {
        return Id_Nombre;
    }

    public String getNombre() {
        return Nombre;
    }

    public String getDireccion() {
        return Direccion;
    }

    public String getBarrio() {
        return Barrio;
    }

    public String getCategoria() {
        return Categoria;
    }

    public String getSubCategoria() {
        return SubCategoria;
    }
}
