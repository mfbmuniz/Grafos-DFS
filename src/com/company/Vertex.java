package com.company;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Vertex {

    private int numVertice;
    private int degree;
    private int inDegree;
    private int outDegree;
    private int tipoGrafo; //digrafo ou não digrafo
    private int isPonderado;
    private int cor; // 0= branco 1= cinza  2=preto
    List<Edge> arestas;
    private int pai;
    private int tamGrafo; //numero total de vertices
    List<Integer> caminhos;
    List<List<Vertex>>ciclos;


    private int tempoDescoberta; // tempo de descoberta para busca em largura
    private int tempoFinalizacao; // tempo de descoberta para busca em largura

    public Vertex(int numVertice, int degree, int tipoGrafo, int cor, List<Edge> arestas, int pai, int tamGrafo, List<Integer> caminhos,
                  int isPonderado, int inDegree, int outDegree,int tempoDescoberta,int tempoFinalizacao) {
        this.isPonderado = isPonderado;
        init(numVertice,degree,tipoGrafo,cor,arestas,pai,tamGrafo,caminhos,isPonderado,inDegree,outDegree,tempoDescoberta,tempoFinalizacao,new ArrayList<List<Vertex>>());
    }
    public Vertex(){
        init( -1,0,0,0,new ArrayList<Edge>(),0,0,new ArrayList<Integer>(),0,0,0,0,0,new ArrayList<List<Vertex>>() );
    }

    private void init(int numVertice, int degree, int tipoGrafo, int cor, List<Edge> arestas, int pai, int tamGrafo, List<Integer> caminhos,int isPonderado,int inDegree, int outDegree,int tempoDescoberta, int tempoFinalizacao,List<List<Vertex> >ciclos ){
        this.numVertice = numVertice;
        this.degree = degree;
        this.tipoGrafo = tipoGrafo;
        this.cor = cor;
        this.arestas = arestas;
        this.caminhos = caminhos;
        this.pai = pai;
        this.tamGrafo = tamGrafo;
        this.isPonderado = isPonderado;
        this.inDegree = inDegree;
        this.outDegree = outDegree;
        this.tempoFinalizacao = tempoFinalizacao;
        this.tempoDescoberta = tempoDescoberta;
        this.ciclos = ciclos;
    }

    @Override
    public String toString() {
        return "Vertex{" +
                "numVertice=" + numVertice +
                ", grau=" + degree +
                ", tipoGrafo=" + tipoGrafo +
                ", cor=" + cor +
                ", arestas=" + arestas +
                ", pai=" + pai +
                ", tamGrafo=" + tamGrafo +
                ", caminhos=" + caminhos +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        Vertex vertex = (Vertex) o;
        return getNumVertice() == vertex.getNumVertice() && getDegree() == vertex.getDegree() && getTipoGrafo() == vertex.getTipoGrafo() && getCor() == vertex.getCor() && getPai() == vertex.getPai() && getTamGrafo() == vertex.getTamGrafo() && getArestas().equals(vertex.getArestas()) && getCaminhos().equals(vertex.getCaminhos());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getNumVertice(), getDegree(), getTipoGrafo(), getCor(), getArestas(), getPai(), getTamGrafo(), getCaminhos());
    }

    @Override
    public Vertex clone() throws CloneNotSupportedException {
        Vertex v = new Vertex();

        v.degree = this.degree;
        v.tamGrafo = this.tamGrafo;
        v.numVertice = this.numVertice;
        v.tipoGrafo = this.tipoGrafo;
        v.cor=this.cor;
        v.pai=this.pai;
        v.arestas = (new ArrayList<Edge>());
        v.caminhos = (new ArrayList<Integer>());
        Edge temp;
        if (this.arestas != null) {
            for (Edge vet : this.arestas) {
                temp = new Edge();
                temp.setNumVertice(vet.getNumVertice());
                temp.setPeso(vet.getPeso());
                v.arestas.add(temp);
            }
        }else {
            v.arestas = null;
        }
        if (this.caminhos != null) {
            for (int vet : this.caminhos) {
                v.caminhos.add(vet);
            }
        }else {
            v.caminhos = null;
        }
        return v;
    }

    public void addInDegree(){
        inDegree++;
    }
    public void subInDegree(){
        inDegree--;
    }
    public void addOutDegree(){
        outDegree++;
    }
    public void subOutDegree(){
        outDegree--;
    }
    public void addDegree(){
        degree++;
    }
    public void subDegree(){
        degree--;
    }

    public int getIsPonderado() {
        return isPonderado;
    }
    public void setIsPonderado(int isPonderado) {
        this.isPonderado = isPonderado;
    }
    public int getNumVertice() {
        return numVertice;
    }
    public void setNumVertice(int numVertice) {
        this.numVertice = numVertice;
    }
    public int getDegree() {
        return degree;
    }
    public void setDegree(int degree) {
        this.degree = degree;
    }
    public int getTipoGrafo() {
        return tipoGrafo;
    }
    public void setTipoGrafo(int tipoGrafo) {
        this.tipoGrafo = tipoGrafo;
    }
    public int getCor() {
        return cor;
    }
    public void setCor(int cor) {
        this.cor = cor;
    }
    public List<Edge> getArestas() {
        return arestas;
    }
    public void setArestas(List<Edge> arestas) {
        this.arestas = arestas;
    }
    public int getPai() {
        return pai;
    }
    public void setPai(Integer pai) {
        this.pai = pai;
    }
    public int getTamGrafo() {
        return tamGrafo;
    }
    public void setTamGrafo(int tamGrafo) {
        this.tamGrafo = tamGrafo;
    }
    public List<Integer> getCaminhos() {
        return caminhos;
    }
    public void setCaminhos(List<Integer> caminhos) {
        this.caminhos = caminhos;
    }
    public int getInDegree() {
        return inDegree;
    }
    public void setInDegree(int inDegree) {
        this.inDegree = inDegree;
    }
    public int getOutDegree() {
        return outDegree;
    }
    public void setOutDegree(int outDegree) {
        this.outDegree = outDegree;
    }

    public void setPai(int pai) {
        this.pai = pai;
    }

    public int getTempoDescoberta() {
        return tempoDescoberta;
    }

    public void setTempoDescoberta(int tempoDescoberta) {
        this.tempoDescoberta = tempoDescoberta;
    }

    public int getTempoFinalizacao() {
        return tempoFinalizacao;
    }

    public void setTempoFinalizacao(int tempoFinalizacao) {
        this.tempoFinalizacao = tempoFinalizacao;
    }

   // @Override

}
