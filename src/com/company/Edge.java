package com.company;

public class Edge implements Comparable<Edge>{

    private int numVertice;
    private int peso;
    private int distancia;
    private int pai ;
    private boolean isResidual;
    private String rotulo;

    public Edge() {
        init(-1,1,0,0,false,"");
    }
    public Edge(String rotulo) {
        init(-1,1,0,0,false,rotulo);
    }
    public Edge(int numVertice, int peso) {
        init(numVertice,peso,0,0,false,"");
    }
    public Edge(int numVertice, int peso, boolean isResidual,String rotulo) {
        init(numVertice,peso,0,0,isResidual,rotulo);
    }

    @Override
    public Edge clone() {
        Edge e = new Edge();
        e.numVertice = this.numVertice;
        e.peso = this.peso;
        e.distancia= this.distancia;
        e.pai= this.pai;
        e.isResidual = this.isResidual;

        return e;
    }

    private void init(int numVertice, int peso, int distancia, int pai,boolean isResidual,String rotulo){
        this.numVertice = numVertice;
        this.peso = peso;
        this.distancia = distancia;
        this.pai = pai;
        this.isResidual = isResidual;
        this.rotulo = rotulo;

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

    public boolean isResidual() {
        return isResidual;
    }

    public void setResidual(boolean residual) {
        isResidual = residual;
    }

    public String getRotulo() {
        return rotulo;
    }

    public void setRotulo(String rotulo) {
        this.rotulo = rotulo;
    }

    @Override
    public int compareTo(Edge e) {

        return ( this.getNumVertice()>e.getNumVertice() ? -1  : (this.getNumVertice() < e.getNumVertice() ? 1:0));
    }
    @Override
    public String toString(){
        return (""+numVertice );
    }


}
