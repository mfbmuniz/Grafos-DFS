package com.company;

public class Edge {

    private int numVertice;
    private int peso;
    private int distancia=0;
    private int pai =0 ;

    public Edge() {
        init(-1,0,0,0);
    }

    @Override
    public Edge clone() {
        Edge e = new Edge();
        e.numVertice = this.numVertice;
        e.peso = this.peso;
        e.distancia= this.distancia;
        e.pai= this.pai;

        return e;
    }

    private void init(int numVertice, int peso, int distancia, int pai){
        this.numVertice = numVertice;
        this.peso = peso;
        this.distancia = distancia;
        this.pai = pai;

    }

    public int getNumVertice() {
        return numVertice;
    }

    public void setNumVertice(int numVertice) {
        this.numVertice = numVertice;
    }

    public int getPeso() {
        return peso;
    }

    public void setPeso(int peso) {
        this.peso = peso;
    }

    public int getDistancia() {
        return distancia;
    }

    public void setDistancia(int distancia) {
        this.distancia = distancia;
    }

    public int getPai() {
        return pai;
    }

    public void setPai(int pai) {
        this.pai = pai;
    }
}
