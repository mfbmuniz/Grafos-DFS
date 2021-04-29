package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class Main {
    public static int MAX_VALUE=999999999;

    public static String lerGrafoArquivo(String name){

        String resp ="";
        try{
            BufferedReader arquivo = new BufferedReader(new FileReader(name));
            do{
                String tipoGrafo = arquivo.readLine(); // digrafo = 1  ou não direcionado = 0
                String isPonderado = arquivo.readLine(); // se é ponderado = 1 não ponderado =0
                String numVertices = arquivo.readLine();

                String vertices="";
                String linha;
                do{
                    linha=arquivo.readLine();
                    vertices+=linha+(";");

                }while(linha.equals("FIM") == false);

                resp += tipoGrafo+(";")+isPonderado+(";")+numVertices+(";")+vertices+(";||");
            }while(arquivo.ready());

        }catch(Exception e){e.printStackTrace();}

        return resp;

    }

    /**
     * Metodo que monta o grafo a partir de uma string padronizada //{"0" ,"0", "6" , "2,3,1" , "0,1,1" , "3,5,1" , "0,5,1" , "1,3,1" , "0,4,1" , "3,4,1" , "1,4,1" , "FIM" , "||"};
     * @param lidoArquivo String montada da leitura
     * @return Grafo montado em lista
     */
    public static Vertex[] montarGrafoLista(String lidoArquivo) {

        try{
            String[] arrayLidoArquivo = lidoArquivo.split(";");
            int tamGrafo= Integer.parseInt(arrayLidoArquivo[2]); //numero de vertices
            int isPonderado = Integer.parseInt(arrayLidoArquivo[1]); // se é ponderado = 1 não ponderado =0
            int tipoGrafo = Integer.parseInt(arrayLidoArquivo[0]);

            Vertex grafo[] = new Vertex[tamGrafo];

            //inicializando as pos do grafo
            for(int i=0 ; i<tamGrafo ; i++){
                grafo[i] = new Vertex();
                grafo[i].setNumVertice(i);
                grafo[i].setTipoGrafo(tipoGrafo);
                grafo[i].setTamGrafo(tamGrafo);
                grafo[i].setIsPonderado(isPonderado);
            }
            //colocando sempre na pos 0 . as informacoes basicas
            grafo[0].setTipoGrafo(tipoGrafo);
            grafo[0].setTamGrafo(tamGrafo );

            boolean finalizador = false; // limitador para terminar as iteracoes ao ler FIM
            for(int i = 0 ; finalizador==false ; i++) {

                String comando[] = arrayLidoArquivo[i + 3].split(",");
                finalizador = (arrayLidoArquivo[i + 3].equals("FIM") ? true : false);
                if(!finalizador){

                    Edge arestaOrigem = new Edge();
                    arestaOrigem.setNumVertice( Integer.parseInt(comando[0]) ) ;
                    arestaOrigem.setPeso(Integer.parseInt(comando[2]));

                    Edge arestaDestino = new Edge();
                    arestaDestino.setNumVertice(Integer.parseInt(comando[1]));
                    arestaDestino.setPeso(Integer.parseInt(comando[2]));

                    int origem = Integer.parseInt (comando[0]) ;
                    int destino =  Integer.parseInt (comando[1]) ;

                    if(tipoGrafo==0){
                        grafo[origem].arestas.add(arestaDestino);
                        grafo[destino].arestas.add(arestaOrigem);
                        grafo[origem].addDegree();
                        grafo[destino].addDegree();
                    }else{

                        grafo[origem].arestas.add(arestaDestino);
                        grafo[origem].addOutDegree();
                        grafo[destino].addInDegree();
                    }
                }
            }
            return grafo;
        }catch(Exception e){e.printStackTrace();return null;}
    }

    /**
     * Metodo que retorna uma String com as arestas do grafo formatada conforme o padrao do enunciado
     * @param grafo1 grafo a ser analizado
     * @return string com as arestas do grafo
     */
    public static String printArestasPadrao (Vertex [] grafo1){
        int tipoGrafo = 0 ;

        try {
            Vertex[] grafoCopia = new Vertex[grafo1.length];

            for (int i = 0; i < grafo1.length; i++) {

                grafoCopia[i] = grafo1[i].clone();
            }

            String resp = "";
            if (tipoGrafo == 0) {

                for (int i = 0; i < grafoCopia.length; i++) {

                    for (int j = 0; j < grafoCopia[i].arestas.size(); j++) {


                        Edge temp = grafoCopia[i].arestas.get(j);

                        resp += (grafoCopia[i].getNumVertice()) + "," + (temp.getNumVertice()) + "," + (temp.getPeso());
                        resp += ";";


                        //anula o oposto
                        for (int k = 0; k < grafoCopia[temp.getNumVertice()].arestas.size(); k++) {
                            Edge vet = new Edge();

                            vet.setNumVertice(grafoCopia[i].getNumVertice());

                            if (vet.getNumVertice() == grafoCopia[temp.getNumVertice()].arestas.get(k).getNumVertice()) {
                                grafoCopia[temp.getNumVertice()].arestas.remove(k);
                                grafoCopia[temp.getNumVertice()].subDegree();
                            }
                        }

                    }
                }


            } else {

            }


            return resp;
        }catch(Exception e){e.printStackTrace();return null;}
    }

    public static  Vertex[] dfs(Vertex[]grafo){
        try {
            // Vamos trabalhar com a copia do grafo original , este passo copia o grafo
            Vertex[] grafoCopia = new Vertex[grafo.length];
            for (int i = 0; i < grafo.length; i++) {
                grafoCopia[i] = grafo[i].clone();
            }

            for (Vertex u : grafoCopia) {
                u.setCor(0);
                u.setPai(-1);
            }
            int timestamp = 0;

            // Lista com as listas de ciclos
            List<List<Vertex>>ciclos  = new ArrayList<List<Vertex>>();
            //Lista com o caminho atual;
            List<Vertex> caminhoAtual = new ArrayList<Vertex>() ;


            for (Vertex u : grafoCopia) {
                if (u.getCor() == 0) { //se a cor for branca
                    timestamp = visitar(grafoCopia, timestamp,u,ciclos,caminhoAtual); //lembrar que u e grafo copia estao sendo passados por referencia
                }
            }
            grafoCopia[0].ciclos.addAll(ciclos);
            return grafoCopia;
        }catch (Exception e){e.printStackTrace();return null;}

    }


    private static int visitar(Vertex[] grafoCopia, int timestamp, Vertex atual, List<List<Vertex>> ciclos,List<Vertex> caminhoAtual){
        try {

            timestamp = timestamp + 1;
            atual.setTempoDescoberta(timestamp);
            caminhoAtual.add(atual.clone());
            atual.setCor(1);
            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];
                if (vizinhoAtual.getCor() == 0) {

                    vizinhoAtual.setPai(atual.getNumVertice());
                    visitar(grafoCopia,timestamp,vizinhoAtual,ciclos,caminhoAtual);

                }
            }

            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];
                if(vizinhoAtual.getCor() == 1 && vizinhoAtual.getNumVertice() != atual.getPai() ){

                    adicionaCiclo(caminhoAtual,ciclos,vizinhoAtual,atual);

                }else if(vizinhoAtual.getCor() == 2){

                    for (Edge k : vizinhoAtual.arestas) {
                        Vertex vizinhoAtualDoVizinhoAtual = grafoCopia[k.getNumVertice()];
                        if(vizinhoAtualDoVizinhoAtual.arestas.contains(atual.getNumVertice())) {
                            adicionaCiclo(caminhoAtual,ciclos,vizinhoAtual,atual);
                        }
                    }
                }
            }

            atual.setCor(2);
            timestamp = timestamp + 1;
            atual.setTempoFinalizacao(timestamp);
            return timestamp;

        }catch (Exception e){e.printStackTrace();return -1;}
    }

    private static void adicionaCiclo(List<Vertex> caminhoAtual, List<List<Vertex>> ciclos , Vertex vizinhoAtual,Vertex verticeAtual) {
        try {
            List cicloEncontrado = new ArrayList<Vertex>();
            cicloEncontrado.addAll(caminhoAtual);
            cicloEncontrado.add(vizinhoAtual.clone());
            int index = 0;
            for (int i=0; i< cicloEncontrado.size()-1; i++) {
                Vertex v = (Vertex) cicloEncontrado.get(i);
                if (v.getNumVertice() == vizinhoAtual.getNumVertice()){
                    index = i;
                }
            }
            List cicloEncontradoTratado = new ArrayList<Vertex>();
            cicloEncontradoTratado = cicloEncontrado.subList(index,cicloEncontrado.size());
            ciclos.add(cicloEncontradoTratado);
        }catch (Exception e){e.printStackTrace();}

    }


    public static void main(String[] args) {
        // write your code here

        try {

            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            String lidoArquivo = lerGrafoArquivo(input.readLine());
            System.out.println(lidoArquivo);

            String[] arrayLidoArquivo = lidoArquivo.split(";");
            Vertex[] grafo = montarGrafoLista(lidoArquivo);
            String printGrafo = printArestasPadrao(grafo);
            System.out.println(printGrafo);
            Vertex[] grafoDFS = dfs(grafo);
            System.out.println("aa5 ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
