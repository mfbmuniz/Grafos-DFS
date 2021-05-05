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

    public static List detectCycles (Vertex[] grafo){
        List ciclosSomados = new ArrayList<List<Vertex>>();
        /*for (Vertex u : grafo) {
            //ciclosSomados.addAll( dfs(grafo,u) );
            List<List> listaTemp = new ArrayList<>();
            listaTemp.addAll( dfs(grafo,u) );

            for(List<Vertex> l :  listaTemp){
                inserirNaLista(l,ciclosSomados);
            }

        }*/
        ciclosSomados.addAll( dfs(grafo,(Vertex) grafo[5]) );
        int auxdebug2 =0;
        print(ciclosSomados);
        System.out.println("Total de ciclos: " +ciclosSomados.size());
        return ciclosSomados;
    }

    public static  List dfs(Vertex[]grafo, Vertex verticeInicialGrafoOriginal){
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

            Vertex verticeInicial = grafoCopia[verticeInicialGrafoOriginal.getNumVertice()];
            // Lista com as listas de ciclos
            List<List<Vertex>>ciclos  = new ArrayList<List<Vertex>>();
            //Lista com o caminho atual;
            List<Vertex> caminhoAtual = new ArrayList<Vertex>() ;

            if (verticeInicial.getCor() == 0) { //se a cor for branca
                timestamp = visitar(grafoCopia, timestamp,verticeInicial,ciclos,caminhoAtual); //lembrar que u e grafo copia estao sendo passados por referencia
            }

            for (Vertex u : grafoCopia) {
                if (u.getCor() == 0) { //se a cor for branca
                    timestamp = visitar(grafoCopia, timestamp,u,ciclos,caminhoAtual); //lembrar que u e grafo copia estao sendo passados por referencia
                }
            }

            System.out.println(" "+verticeInicial.getNumVertice());
            int h=0;
            print(ciclos);
            return ciclos;
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

                    adicionaCicloComCaminhamento(caminhoAtual,ciclos,vizinhoAtual,atual);

                }else if(vizinhoAtual.getCor() == 2){

                    if (vizinhoAtual.getPai() != atual.getNumVertice()){
                        adicionaCicloComCaminhamento(caminhoAtual,ciclos,vizinhoAtual,atual);
                    }
                    //if(atual.getPai() != vizinhoAtual.getNumVertice()){
                    for (Edge k : vizinhoAtual.arestas) {
                        Vertex vizinhoAtualDoVizinhoAtual = grafoCopia[k.getNumVertice()];
                        for (Edge l : vizinhoAtualDoVizinhoAtual.arestas ) {
                            Vertex arestaDoVizinhoDoVizinho = grafoCopia[l.getNumVertice()];
                            if (arestaDoVizinhoDoVizinho.getNumVertice() == atual.getNumVertice()) {
                                adicionaCicloPreto(caminhoAtual, ciclos, vizinhoAtual, atual, vizinhoAtualDoVizinhoAtual);
                            }
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

    private static void adicionaCicloComCaminhamento(List<Vertex> caminhoAtual, List<List<Vertex>> ciclos , Vertex vizinhoAtual,Vertex verticeAtual) {
        try {
            List cicloEncontrado = new ArrayList<Vertex>();
            cicloEncontrado.addAll(caminhoAtual);
            int indexVerticeAtual = 0;
            int indiceVizinhoAtual = 0;
            for (int i=0; i< cicloEncontrado.size(); i++) {
                Vertex v = (Vertex) cicloEncontrado.get(i);
                if (v.getNumVertice() == verticeAtual.getNumVertice()){
                    indexVerticeAtual = i;
                }
                if (v.getNumVertice() == vizinhoAtual.getNumVertice()){
                    indiceVizinhoAtual = i;
                }
            }
            List cicloEncontradoTratado = new ArrayList<Vertex>();
            if(vizinhoAtual.getCor() == 1) {
                cicloEncontradoTratado = cicloEncontrado.subList(indiceVizinhoAtual, indexVerticeAtual+1);
                cicloEncontradoTratado = ordenaInsercao(cicloEncontradoTratado);
                cicloEncontradoTratado.add(cicloEncontradoTratado.get(0)); //era o vizinho
                inserirNaLista(cicloEncontradoTratado,ciclos);   //ciclos.add(cicloEncontradoTratado);
            }else if (vizinhoAtual.getCor() == 2) {
                cicloEncontradoTratado = cicloEncontrado.subList(indexVerticeAtual,indiceVizinhoAtual+1);
                cicloEncontradoTratado = ordenaInsercao(cicloEncontradoTratado);
                cicloEncontradoTratado.add(cicloEncontradoTratado.get(0)); //era o atual
                inserirNaLista(cicloEncontradoTratado,ciclos);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }

    private static void inserirNaLista(List<Vertex> cicloEncontradoTratado, List<List<Vertex>> ciclos) {
        String ciclo_atual;
        String ciclo_insercao="";
        boolean flag=false;
        for (Vertex v: cicloEncontradoTratado){
            ciclo_insercao+=""+v.getNumVertice();
        }
        for (List<Vertex> vertexList: ciclos) {
            ciclo_atual="";
            for( Vertex v : vertexList  ){
                ciclo_atual+=""+v.getNumVertice();
            }
            if (ciclo_atual.equals(ciclo_insercao)){
                flag = true;
                return;
            }
        }
        ciclos.add(cicloEncontradoTratado);

    }

    private static ArrayList<Vertex> ordenaInsercao(List<Vertex> cicloEncontradoTratado) {

        int[]valor_posicao_DoMenorElemento = {MAX_VALUE,MAX_VALUE};
        for(int i=0 ; i<cicloEncontradoTratado.size() ;i++){
            if(cicloEncontradoTratado.get(i).getNumVertice() < valor_posicao_DoMenorElemento[0]){
                valor_posicao_DoMenorElemento[0] = cicloEncontradoTratado.get(i).getNumVertice();
                valor_posicao_DoMenorElemento[1] = i;
            }
        }
        List cicloOrdenado = new ArrayList<Vertex>();

        int posAnterior = valor_posicao_DoMenorElemento[1]-1 < 0 ? cicloEncontradoTratado.size()-1 : valor_posicao_DoMenorElemento[1]-1;
        int posPosterior = valor_posicao_DoMenorElemento[1]+1 > cicloEncontradoTratado.size()-1 ?0:valor_posicao_DoMenorElemento[1]+1;

        boolean flagFoward = cicloEncontradoTratado.get(posAnterior).getNumVertice()  > cicloEncontradoTratado.get(posPosterior).getNumVertice()?true:false;

        if(flagFoward) { //caminha pra frente
            int j = 0;
            for (int i = valor_posicao_DoMenorElemento[1]; j < cicloEncontradoTratado.size(); j++) {

                cicloOrdenado.add(cicloEncontradoTratado.get(i++));

                if (i == cicloEncontradoTratado.size()) {
                    i = 0;
                }
            }
        }else{ //caminha pra traz
            int j = 0;
            for (int i = valor_posicao_DoMenorElemento[1]; j < cicloEncontradoTratado.size(); j++) {

                cicloOrdenado.add(cicloEncontradoTratado.get(i--));

                if (i < 0  ) {
                    i = cicloEncontradoTratado.size()-1; //ou tamanho -1
                }
            }
        }
        return (ArrayList<Vertex>) cicloOrdenado;

    }

    private static void adicionaCicloPreto(List<Vertex> caminhoAtual, List<List<Vertex>> ciclos , Vertex vizinhoAtual,Vertex verticeAtual, Vertex vizinhoDoVizinho) {
        try {

            List cicloEncontrado = new ArrayList<Vertex>();
            cicloEncontrado.add(verticeAtual.clone());
            cicloEncontrado.add(vizinhoAtual.clone());
            cicloEncontrado.add(vizinhoDoVizinho.clone());
            cicloEncontrado = ordenaInsercao(cicloEncontrado);
            cicloEncontrado.add(cicloEncontrado.get(0));
            inserirNaLista(cicloEncontrado,ciclos);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }

    private static void print(List<List<Vertex>> ciclos) {
        int i = 0 ;
        int j=0;
        for ( List<Vertex> lista : ciclos) {
            System.out.print("Posição:"+(i++)+", Vertices: ");
            for (Vertex v: lista ) {
                System.out.print(""+v.getNumVertice()+(","));
            }
            System.out.println(" ");
        }
    }

    private static void retirarRepetidos(List<List<Vertex>> ciclos) {
        try {
            List<List<Vertex>> indicesDeRemocao ;

            for (int i = 0; i < ciclos.size(); i++) {
                if (ciclos.get(i) != null) {
                    List<Vertex> cicloAtual = (List<Vertex>) ciclos.get(i);
                    indicesDeRemocao = new ArrayList<List<Vertex>> ();
                    boolean flag = true;
                    for (int j = 0; j < ciclos.size(); j++) {
                        if (j != i && (ciclos.get(j) != null)) {
                            List<Vertex> temp_CicloRetiradoDeCiclos = (List<Vertex>) ciclos.get(j);
                            if (temp_CicloRetiradoDeCiclos.size() != cicloAtual.size()) {
                                continue; //doing nothing.
                            } else{
                                for (Vertex v : temp_CicloRetiradoDeCiclos) {
                                    if (!contemElemento(v, cicloAtual)) {
                                        flag = false;
                                    }
                                }
                                if (flag) {
                                    indicesDeRemocao.add( ciclos.get(j) );
                                }
                                flag = true;
                            }
                        }
                    }
                    removerIndices(indicesDeRemocao,ciclos);
                }

            }
            //removerIndices(indicesDeRemocao,ciclos);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void removerIndices(List<List<Vertex>> indicesDeRemocao, List<List<Vertex>> ciclos) {
        int auxdebug = 0;
        ciclos.removeAll(indicesDeRemocao);
    }

    private static boolean contemElemento(Vertex v, List<Vertex> cicloAtual) {
        boolean resp = false;
        for ( Vertex i: cicloAtual) {
            if(v.getNumVertice() == i.getNumVertice()){
                resp = true;
                return resp;
            }
        }
        return resp;
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

            List<List<Vertex>> ciclos = detectCycles(grafo);
            System.out.println("aa5 ");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
