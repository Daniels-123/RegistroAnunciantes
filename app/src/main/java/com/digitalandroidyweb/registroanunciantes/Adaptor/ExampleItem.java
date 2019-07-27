package com.digitalandroidyweb.registroanunciantes.Adaptor;

public class ExampleItem {

    private String Id;
    private String Nombre;
    private String Direccion;
    private String Barrio;
    private String Categoria;
    private String SubCategoria;

    public ExampleItem(String id,String nombre, String direccion, String barrio, String categoria, String subcategoria) {

        Id = id;
        Nombre = nombre;
        Direccion = direccion;
        Barrio = barrio;
        Categoria = categoria;
        SubCategoria = subcategoria;

    }


    public String getId() {
        return Id;
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
