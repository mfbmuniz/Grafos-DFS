package com.company;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStreamReader;
import java.util.*;


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
                    int peso = Integer.parseInt(comando[2]) < 1 ? 1 : Integer.parseInt(comando[2]) ;
                    arestaOrigem.setPeso(peso);
                    String rotuloOrigem = ""+Integer.parseInt(comando[1]);
                    arestaOrigem.setRotulo (rotuloOrigem );

                    Edge arestaDestino = new Edge();
                    arestaDestino.setNumVertice(Integer.parseInt(comando[1]));
                    peso = Integer.parseInt(comando[2]) < 1 ? 1 : Integer.parseInt(comando[2]) ;
                    arestaDestino.setPeso(peso);
                    String rotuloDestino = ""+Integer.parseInt(comando[1]);
                    arestaDestino.setRotulo( rotuloDestino );

                    int origem = Integer.parseInt (comando[0]) ;
                    int destino =  Integer.parseInt (comando[1]) ;

                    if(tipoGrafo==0){
                        grafo[origem].arestas.add(arestaDestino);
                        grafo[destino].arestas.add(arestaOrigem);
                        grafo[origem].addDegree();
                        grafo[origem].addOutDegree();
                        grafo[origem].addInDegree();
                        grafo[destino].addDegree();
                        grafo[destino].addOutDegree();
                        grafo[destino].addInDegree();

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
     * @param grafo1 grafo a ser analisado
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


    /**
     * Metodo que detecta ciclos em um grafo usando busca em profundidade
     * @param grafo grafo a ser analisado
     * @return List com TODOS os ciclos detectados sem repeticoes
     */
    public static List detectCycles (Vertex[] grafo){
        List ciclosSomados = new ArrayList<List<Vertex>>();
        for (Vertex u : grafo) {
            //ciclosSomados.addAll( dfs(grafo,u) );
            List<List> listaTemp = new ArrayList<>();
            listaTemp.addAll( dfs(grafo,u) );

            for(List<Vertex> l :  listaTemp){
                inserirNaLista(l,ciclosSomados);
            }

        }
        System.out.println("Enumeração dos ciclos: ");
        print(ciclosSomados);
        System.out.println("Total de ciclos: " +ciclosSomados.size());
        return ciclosSomados;
    }

    /**
     * Implementacao da Busca em profundidade Modificada para encontrar ciclos
     * @param grafo grafo a ser analisado
     * @param verticeInicialGrafoOriginal vertice onde a busca se iniciará
     * @return List com os ciclos detectados sem repeticoes a partir do vertice atual escolhido como parametro.
     */

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

            System.out.println("Ciclos Para o Vertice: "+verticeInicial.getNumVertice());
            print(ciclos);
            System.out.println("-------------------------------------------------------");
            return ciclos;
        }catch (Exception e){e.printStackTrace();return null;}

    }
    /**
     * Implementacao do metodo visita, da busca em profundidade (recursivo)
     * @param grafoCopia grafo a ser analisado (copia do grafo original)
     * @param atual vertice atual, iniciando no vertice passado como parametro no dfs e alterando conforme a recursividade é chamada.
     * @param caminhoAtual caminho percorrido
     * @param ciclos Lista geral de ciclos a partir do vertice inicial, passada por referencia para o preenchimento correto no metodo de visita.
     * @param timestamp tempo de descoberta da busca em profundidade.
     * @return int com o valor do timestamp.
     */
    private static int visitar(Vertex[] grafoCopia, int timestamp, Vertex atual, List<List<Vertex>> ciclos,List<Vertex> caminhoAtual){
        try {

            timestamp = timestamp + 1;
            atual.setTempoDescoberta(timestamp);
            caminhoAtual.add(atual.clone());
            atual.setCor(1);
            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];

                if (vizinhoAtual.getCor() == 0) { //verifica se o próximo vertice é branco( nao visitado)

                    vizinhoAtual.setPai(atual.getNumVertice());
                    visitar(grafoCopia,timestamp,vizinhoAtual,ciclos,caminhoAtual);

                }
            }

            //após a busca, na volta da recursividade adicionamos as logicas abaixo para deteccao de ciclos

            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];
                if(vizinhoAtual.getCor() == 1 && vizinhoAtual.getNumVertice() != atual.getPai() ){ //se o vertice ja foi visitado e não finalizado , não é meu pai, e é meu vizinho.

                    // tenho um ciclo que percorre o caminho atual até mim, pegando a sublista a partir deste vizinho até mim + o vizinho(aresta até entao desconhecida).
                    adicionaCicloComCaminhamento(caminhoAtual,ciclos,vizinhoAtual,atual);

                }else if(vizinhoAtual.getCor() == 2){ //se o vertice já foi finalizado e é meu viznho

                    //se ja foi finalizado e não é meu filho, só pode ser um ciclo
                    if (vizinhoAtual.getPai() != atual.getNumVertice()){
                        adicionaCicloComCaminhamento(caminhoAtual,ciclos,vizinhoAtual,atual);
                    }
                    //se o vizinho do meu vizinho(não contando comigo) tem aresta comigo é um ciclo, pois caracteriza um caminho novo
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

    /**
     * Implementacao do metodo que adiciona o ciclo detectado a partir do caminhamento na lista de ciclos permanentes sem repeticoes
     * @param verticeAtual verticeAtual da busca em largura
     * @param vizinhoAtual vizinho com qual detectei ciclo
     * @param caminhoAtual caminho percorrido
     * @param ciclos Lista geral de ciclos a partir do vertice inicial, passada por referencia para o preenchimento correto no metodo de visita.
     * @return void
     */
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
                boolean cicloValido = validaCiclo(cicloEncontradoTratado);
                cicloEncontradoTratado.add(cicloEncontradoTratado.get(0)); //era o vizinho
                if(cicloValido) inserirNaLista(cicloEncontradoTratado,ciclos);   //ciclos.add(cicloEncontradoTratado);
            }else if (vizinhoAtual.getCor() == 2) {
                cicloEncontradoTratado = cicloEncontrado.subList(indexVerticeAtual,indiceVizinhoAtual+1);
                cicloEncontradoTratado = ordenaInsercao(cicloEncontradoTratado);
                boolean cicloValido = validaCiclo(cicloEncontradoTratado);
                cicloEncontradoTratado.add(cicloEncontradoTratado.get(0)); //era o atual
                if(cicloValido)inserirNaLista(cicloEncontradoTratado,ciclos);
            }
        }catch (Exception e){
            e.printStackTrace();
            System.exit(0);
        }

    }

    /**
     * Implementacao do metodo que valida se o ciclo emcontrado é mesmo um ciclo quando comparado com o grafo original, atravez da conferencia das arestas
     * @param cicloEncontradoTratado Lista com o ciclo a ser conferido.
     * @return boolean com true para valido e false para ciclo invalido.
     */

    private static boolean validaCiclo(List<Vertex> cicloEncontradoTratado) {
        boolean cicloValido =false;
        int i=0;
        for (Vertex v : cicloEncontradoTratado){
            int vizinho = (i == cicloEncontradoTratado.size()-1 ? 0:++i);
            if(v.temAresta(cicloEncontradoTratado.get(vizinho))){
                cicloValido = true;
            }else{
                return false;
            }
        }
        return cicloValido;
    }

    /**
     * Implementacao do metodo que insere na lista definitiva sem repeticoes
     * @param cicloEncontradoTratado Lista com o ciclo a ser inserido.
     * @param ciclos lista de ciclos
     * @return boolean com true para valido e false para ciclo invalido.
     */

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
    /**
     * Implementacao do metodo que ordena o ciclo a partir do menor (colocando o menor na posicao 0 e girando para esquerda ou para direita)
     * dependendo de onde está o segundo menor valor, sem desordenar a sequencia de vertices que forma o ciclo
     * @param cicloEncontradoTratado Lista com o ciclo a ser conferido.
     * @return ArrayList<Vertex> ccom o ciclo ordenado
     */

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

    /**
     * Implementacao do metodo que adiciona o ciclo detectado a partir vertice finalizado na lista de ciclos permanentes sem repeticoes
     * @param verticeAtual verticeAtual da busca em largura
     * @param vizinhoAtual vizinho com qual detectei ciclo
     * @param caminhoAtual caminho percorrido
     * @param ciclos Lista geral de ciclos a partir do vertice inicial, passada por referencia para o preenchimento correto no metodo de visita.
     * @return void
     */
    private static void adicionaCicloPreto(List<Vertex> caminhoAtual, List<List<Vertex>> ciclos , Vertex vizinhoAtual,Vertex verticeAtual, Vertex vizinhoDoVizinho) {
        try {

            List cicloEncontrado = new ArrayList<Vertex>();
            cicloEncontrado.add(verticeAtual.clone());
            cicloEncontrado.add(vizinhoAtual.clone());
            cicloEncontrado.add(vizinhoDoVizinho.clone());
            cicloEncontrado = ordenaInsercao(cicloEncontrado);
            boolean cicloValido = validaCiclo(cicloEncontrado);
            cicloEncontrado.add(cicloEncontrado.get(0));
            if(cicloValido) inserirNaLista(cicloEncontrado,ciclos);

        } catch (Exception e) {
            e.printStackTrace();
            System.exit(0);
        }
    }
    /**
     * Implementacao do metodo que recebe uma Lista de ciclos e printa na saida padrão
     * @param ciclos Lista geral de ciclos a partir do vertice inicial, passada por referencia.
     * @return void
     */
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

    /**
     * Implementacao do metodo recursivo que gera todas os subconjuntos com 'R' elementos de uma determinada sequencia
     * no nosso caso estamos gerando todos os subconjuntos do grafo para permutar posteriormente.
     * @param arr GrafoAtual
     * @param data array de tamanho 'R' que contem o numero 'r' de elementos do subconjuntos atuais.
     * @param end marcador posicionado no fim do grafo iniciamente que será movimentado com a recursão
     * @param index marcador posicionado inicialmente no inicio do grafo que será movimentado com a recursão
     * @param listaDeSubconjuntos lista de subconjuntos encontrados.
     * @param start  marcador posicionado inicialmente no inicio do array que será movimentado com a recursão
     * @param tamSubconjunto tamanho 'R' do subconjunto atual.
     * @return void
     */

    static void combinationUtil(Vertex[] arr, Vertex[] data, int start, int end, int index, int tamSubconjunto, List<List<Vertex>> listaDeSubconjuntos) {
        try {
            // Current combination is ready to be stored, store.
            if (index == tamSubconjunto) {

                List<Vertex> subconjuntoEncontrado = new ArrayList<Vertex>();
                subconjuntoEncontrado.addAll(Arrays.asList(data));
                subconjuntoEncontrado = ordenaInsercao(subconjuntoEncontrado);
                inserirNaLista(subconjuntoEncontrado, listaDeSubconjuntos);   //ciclos.add(cicloEncontradoTratado);
                return;
            }

            // replace index with all possible elements. The condition
            // "end-i+1 >= r-index" makes sure that including one element
            // at index will make a combination with remaining elements
            // at remaining positions
            for (int i = start; i <= end && end - i + 1 >= tamSubconjunto - index; i++) {
                data[index] = arr[i].clone();
                combinationUtil(arr, data, i + 1, end, index + 1, tamSubconjunto, listaDeSubconjuntos);
            }
        }catch (Exception  e){
            e.printStackTrace();
        }
    }

    /**
     * Implementacao do metodo que recebe um grafo para salvar os seus subconjuntos em uma lista
     * @param grafo grafo a ser analisado
     * @return Lista de todos os subconjuntos deste grafo
     */
    static List<List<Vertex>> saveSubsets(Vertex grafo[]) {
        // A temporary array 'subconjuntoAtual' to store all subsets one by one
        List<List<Vertex>> listaDeSubconjuntos  = new ArrayList<List<Vertex>>();
        for (int i = 3; i <= grafo.length; i++) {
            Vertex subconjuntoAtual[]=new Vertex[i];
            combinationUtil(grafo, subconjuntoAtual, 0, grafo.length-1, 0, i,listaDeSubconjuntos);
        }
        return listaDeSubconjuntos;
    }

    /**
     * Implementacao do metodo que recebe uma sequencia de vertices em formato de string
     * encontra todas as permutações para este determinado subconjunto e salva na lista
     * @param str String com a sequencia inicial dos vertices
     * @param ans resposta que está sendo construida com a recursividade
     * @param permutations lista de permutacoes onde será salvo as permutacoes encontradas
     * @return Lista de todos os subconjuntos deste grafo
     */

    static void searchPermutations(String str, String ans ,List<String> permutations) {
    try{
        // If string is empty
        if (str.length() == 0) {
            permutations.add(ans);
            return;
        }

        for (int i = 0; i < str.length(); i++) {

            // ith character of str
            char ch = str.charAt(i);

            // Rest of the string after excluding
            // the ith character
            String ros = str.substring(0, i) +
                    str.substring(i + 1);

            // Recurvise call
            searchPermutations(ros, ans + ch , permutations);
        }

    }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Implementacao do metodo que procura e imprime ciclos em um grafo baseado em permutações de seus vertices
     * @param grafo grafo a ser analisado
     * @return void
     */
    public static void searchCyclesWithPermutations(Vertex []grafo){
        try {
            List<List<Vertex>> subSets = saveSubsets(grafo);
            List<List<Vertex>> ciclos = new ArrayList<List<Vertex>>();
            int numberOfPermutations =0;

            for (List<Vertex> actualSubset: subSets) {
                String actualSubsetParsedString = "";
                for (Vertex v : actualSubset) {
                    actualSubsetParsedString+= v.getNumVertice();
                }
                List<String> permutations = new LinkedList<String>();

                searchPermutations(actualSubsetParsedString, "", permutations);
                numberOfPermutations += permutations.size();
                addValidCycles(permutations,grafo,ciclos);

            }
            //imprime o numero de permutações na tela
            System.out.println("Number of Pertmurations: "+numberOfPermutations);

            //imprime a lista de ciclos
            System.out.println("Ciclos encontrados: ");
            print(ciclos);


        }catch (Exception e){
            e.printStackTrace();
        }
    }

    /**
     * Implementacao do metodo que recebe uma lista das permutações da sequencia atual de vertices , verifica no
     * grafo se é um ciclo válido e insere na lista se verdadeiro
     * @param grafo grafo a ser analisado
     * @param ciclos lista definitiva de ciclos
     * @param permutations permutações do subconjunto atual
     * @return void
     */
    private static void addValidCycles(List<String> permutations, Vertex[] grafo, List<List<Vertex>> ciclos) {
        try {
            for (String s : permutations) {
                List<Vertex> cicloAtual = new ArrayList<Vertex>();
                for (int i = 0; i < s.length(); i++) {
                    String s1 = "";
                    s1 += s.charAt(i);
                    int numNerticeAtual = Integer.parseInt(s1);
                    int pos = posicaoVertice(grafo, numNerticeAtual);
                    cicloAtual.add(grafo[pos].clone());
                }
                cicloAtual = ordenaInsercao(cicloAtual);
                boolean cicloValido = validaCiclo(cicloAtual);
                cicloAtual.add(cicloAtual.get(0));
                if(cicloValido) inserirNaLista(cicloAtual,ciclos);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }
    /**
     * Implementacao do metodo que um rotulo de vertice e procura sua posição no grafo
     * @param grafo grafo a ser analisado
     * @param numVertice numero do vertice a ser analisado
     * @return Lista de todos os subconjuntos deste grafo
     */
    public static int posicaoVertice(Vertex [] grafo , int numVertice ){
        for (int i = 0; i < grafo.length; i++) {
            if(grafo[i].getNumVertice() == numVertice ){
                return i;
            }
        }
        return -1;
    }

    public static List<String[]> fluxoFordFulkerson(Vertex[] grafo, int s_Source, int t_Sink ){

        //como o algoritmo de fluxo é somente para digrafos ponderados
        //se o grafo passado como parametro não for digrafo, o algorimo não executa.
        if(grafo[0].getTipoGrafo() < 1 ){
            return null;
        }

        try {
            // Vamos trabalhar com a copia do grafo original , este passo copia o grafo original
            // Vamos também criar o grafo que irá conter a rede residual, que neste passo é igual ao original

            Vertex[] grafoCopia = new Vertex[grafo.length];
            Vertex[] grafoResidual = new Vertex[grafo.length];
            for (int i = 0; i < grafo.length; i++) {
                grafoCopia[i] = grafo[i].clone();
                grafoResidual[i] = grafo[i].clone();

            }
            // vamos colocar as arestas reversas da rede residual, que por ser o inicio todas terão uma aresta de retorno com peso nulo.
            // o metodo de verificar se um vertice u tem aresta com um vertice v do grafo E , será se a aresta em questão
            // tem peso maior que 0, pois após este passo sempre haverá uma aresta de ida e uma de volta
            insereArestasReversasDigrafo(grafoResidual);

            List<String[]> caminhos = new LinkedList<String[]>();

            String[] caminho = existeCaminho(grafoResidual, s_Source, t_Sink); //caminho através do rótulo

            while(caminho.length > 0 && (caminho.equals("VAZIO")) ==false ){

                int fluxoMaximoAtual = encontraFluxoMaximo(caminho,grafoResidual);

                for(int i= 0; i< caminho.length-1 ; i++){

                    String s1= caminho[i].contains("R") ? ""+caminho[i].charAt(0) : caminho[i];
                    int index = Integer.parseInt(s1);
                    Vertex verticeAtual = grafoResidual[index];

                    String s2= caminho[i+1].contains("R") ? ""+caminho[i+1].charAt(0) : caminho[i+1];
                    int arestaAtual = Integer.parseInt(s2);

                    Edge e = removeByName(verticeAtual.arestas,arestaAtual,caminho[i+1]);

                    if(!e.isResidual()){ // e é um arco original */
                        /* arestaAtual(e) = arestaAtual(e) - fluxoMaximoAtual;
                        atualizar reverso;	*/

                        e.setPeso( e.getPeso() - fluxoMaximoAtual);
                        Edge residuo = removeByName(grafoResidual[arestaAtual].arestas,verticeAtual.getNumVertice(),"R");
                        residuo.setPeso(fluxoMaximoAtual);

                        grafoResidual[arestaAtual].arestas.add(residuo.clone());
                        verticeAtual.arestas.add(e.clone());

                    }else{ //* e é um arco reverso */
                        /* arestaAtual(e) = arestaAtual(e) + fluxoMaximoAtual;
                        atualizar reverso;*/

                        e.setPeso( e.getPeso() + fluxoMaximoAtual);
                        Edge originalEdge = removeByName(grafoResidual[arestaAtual].arestas,verticeAtual.getNumVertice(),"");
                        originalEdge.setPeso(originalEdge.getPeso() - fluxoMaximoAtual);

                        grafoResidual[arestaAtual].arestas.add(originalEdge.clone());
                        verticeAtual.arestas.add(e.clone());
                    }
                }
                caminhos.add(caminho);
                caminho = existeCaminho(grafoResidual, s_Source, t_Sink);
            }
            return caminhos;

        }catch( Exception e){
            e.printStackTrace();
        }

        return null;
    }
    public static void insereArestasReversasDigrafo( Vertex[] grafoResidual){
        for (Vertex u : grafoResidual) {
            for(Edge e : u.arestas){
                if( e.getPeso() > 0){
                    Edge residuo  = new Edge ( u.getNumVertice(),0,true,(""+u.getNumVertice()+'R'));
                    grafoResidual[e.getNumVertice()].arestas.add(residuo);
                }
            }
        }
    }

    private static String[] existeCaminho(Vertex[] grafoResidual, int s_source, int t_sink) {
        try {
            String caminho = dfsFordFulkerson(grafoResidual, grafoResidual[s_source], grafoResidual[t_sink]);
            String[]caminhoRetorno;
            if(!caminho.equals("VAZIO")){
                caminhoRetorno = caminho.split(",");
            }else{
                caminhoRetorno = new String[0];
            }
            return caminhoRetorno;

        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    private static int encontraFluxoMaximo(String []caminho, Vertex[] grafoResidual) {
        int menorPeso = MAX_VALUE;
        int menorPesoAtual;

        for (int i = 0; i < caminho.length-1; i++) {
            boolean isResidual = caminho[i+1].contains("R");
            String s1= caminho[i].contains("R") ? ""+caminho[i].charAt(0) : caminho[i];
            int indexVerticeAtual = Integer.parseInt(s1);

            String s2= caminho[i+1].contains("R") ? ""+caminho[i+1].charAt(0) : caminho[i+1];
            int indexArestaAtual = Integer.parseInt(s2);

            menorPesoAtual= localizaPeso(grafoResidual[indexVerticeAtual].arestas, indexArestaAtual,isResidual);
            menorPeso =  menorPesoAtual< menorPeso ? menorPesoAtual : menorPeso;

        }
        return menorPeso;
    }
    private static int localizaPeso(List<Edge> arestas, int indexArestaAtual, boolean isResidual) {

        for (Edge e: arestas) {
            String rotulo = ""+indexArestaAtual+"R";
            if(e.getNumVertice() == indexArestaAtual && !isResidual){
                return e.getPeso();
            }else if(e.getNumVertice() == indexArestaAtual && e.getRotulo().equals(rotulo) && isResidual){
                return  e.getPeso();
            }
        }
        return -1;
    }
    private static Edge removeByName(List<Edge> arestas, int arestaAtual, String s) {
        Edge arestaEncontrada =  new Edge();
        boolean isResidual = s.contains("R");
        for (Edge e: arestas) {
            String rotulo = ""+arestaAtual+"R";
            if(e.getNumVertice() == arestaAtual && !isResidual){
                arestaEncontrada = e.clone();
                arestas.remove(e);
                return arestaEncontrada;
            }else if(e.getNumVertice() == arestaAtual && e.getRotulo().equals(rotulo)  && isResidual){
                arestaEncontrada = e.clone();
                arestas.remove(e);
                return arestaEncontrada;
            }
        }
        return arestaEncontrada;
    }
    public static Vertex[] simplesToDigrafo (Vertex [] grafo1){
        try {
            int tipoGrafo = grafo1[0].getTipoGrafo();
            Vertex[] grafoCopia = new Vertex[grafo1.length];
            for (int i = 0; i < grafo1.length; i++) {
                grafoCopia[i] = grafo1[i].clone();
            }

            if (tipoGrafo == 0) {
                for (int i = 0; i < grafoCopia.length; i++) {
                    for (int j = 0; j < grafoCopia[i].arestas.size(); j++) {

                        int arestaAtual = grafoCopia[i].arestas.get(j).getNumVertice();
                        int verticeAtual = grafoCopia[i].getNumVertice();
                        grafoCopia[arestaAtual].setDegree(0);
                        grafoCopia[verticeAtual].setDegree(0);

                        for (int k = 0; k < grafoCopia[arestaAtual].arestas.size(); k++) {
                            if ( verticeAtual == grafoCopia[arestaAtual].arestas.get(k).getNumVertice()) {
                                grafoCopia[arestaAtual].arestas.remove(k);
                                grafoCopia[arestaAtual].subOutDegree();
                            }
                        }

                        grafoCopia[i].setTipoGrafo(1);
                        grafoCopia[i].setIsPonderado(1);
                    }
                }


            }
            return grafoCopia;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public static List<String> arrayToString (List<String[]>arrayCaminhos){
        List<String> caminhos = new LinkedList<String>();
        for (String []s : arrayCaminhos) {
            String caminho ="";
            for (int i = 0; i < s.length; i++) {
                caminho+=s[i];
            }
            caminhos.add(caminho);
        }
        return caminhos;
    }
    public static void caminhosDisjuntos (List<String[]> arrayCaminhos ){

        List<String> caminhos = arrayToString(arrayCaminhos);

        System.out.println("----------caminhos-----------------------");
        for (String s: caminhos ) {
            System.out.println(s);
        }
        System.out.println("---------------------------------");

        //List<String> caminhosDisjuntos = new LinkedList<String>();
        List<String> listaDeRemocao = new LinkedList<String>();
        for (String s: caminhos ) {
            for (int i = 0; i < s.length()-1; i++) {
                String chaveDeBuscaAtual = ""+s.charAt(i)+s.charAt(i+1);
                for (String s2: caminhos ) {
                    if(!s2.equals(s)){
                        if(s2.contains(chaveDeBuscaAtual)){
                            listaDeRemocao.add(s2);
                        }
                    }
                }

            }
        }
        System.out.println("----------------lista de remocao-----------------");
        for (String s: listaDeRemocao ) {
            System.out.println(s);
        }
        System.out.println("---------------------------------");
        caminhos.removeAll(listaDeRemocao);
        System.out.println("------------caminhos disjuntos de arestas---------------------");
        for (String s: caminhos ) {
            System.out.println(s);
        }
        System.out.println("---------------------------------");

    }


    /**
     * Implementacao da Busca em profundidade Modificada para encontrar ciclos
     * @param grafo grafo a ser analisado
     * @param  verticeInicial_s1 vertice onde a busca se iniciará
     * @return List com os ciclos detectados sem repeticoes a partir do vertice atual escolhido como parametro.
     */

    public static String dfsFordFulkerson(Vertex[]grafo, Vertex verticeInicial_s1, Vertex verticeFinal_t1 ){
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

            Vertex verticeInicial_s = grafoCopia[verticeInicial_s1.getNumVertice()];
            Vertex verticeFinal_t = grafoCopia[verticeFinal_t1.getNumVertice()];


            //Lista com o caminho atual;
            List<String> caminhoAtual = new ArrayList<String>() ;

            if (verticeInicial_s.getCor() == 0) { //se a cor for branca
                timestamp = visitarFordFulkerson(grafoCopia, timestamp, verticeInicial_s, verticeFinal_t,caminhoAtual,false); //lembrar que u e grafo copia estao sendo passados por referencia
            }

            for (Vertex u : grafoCopia) {
                if (u.getCor() == 0) { //se a cor for branca
                    timestamp = visitarFordFulkerson(grafoCopia, timestamp, verticeInicial_s, verticeFinal_t,caminhoAtual,false); //lembrar que u e grafo copia estao sendo passados por referencia
                }
            }
            String caminhoTratado = formataCaminho (caminhoAtual);

            //System.out.println(" Caminho Atual Encontrado: "+caminhoTratado);

            return caminhoTratado;
        }catch (Exception e){e.printStackTrace();return null;}

    }

    private static String formataCaminho(List<String> caminhoAtual) {

        String caminho = "";
        for (String s: caminhoAtual) {
            caminho+=s;
        }
        if(!caminho.contains("F")){
            return "VAZIO";
        }else{
            int cut = caminho.indexOf('F');
            caminho = caminho.substring(0, cut);
            String[] caminhoSplit = caminho.split(",");

            for (int i = 0; i < caminhoSplit.length; i++) {
                if (caminhoSplit[i].contains("|")) {
                    String quantidadeRemocaoString = caminhoSplit[i].substring(0, caminhoSplit[i].length() - 1);
                    int quantRemocao = quantidadeRemocaoString.length();
                    for (int j = i - quantRemocao; j < i; j++) {
                        caminhoSplit[j] = "NULO";
                    }
                    caminhoSplit[i] = "" + caminhoSplit[i].charAt(caminhoSplit[i].length() - 1);
                }
            }
            String caminhoTratado = "";

            for (int i = 0; i < caminhoSplit.length; i++) {
                if (!caminhoSplit[i].contains("NULO")) {
                    caminhoTratado += caminhoSplit[i]+",";
                }
            }
            return caminhoTratado;
        }
    }

    /**
     * Implementacao do metodo visita, da busca em profundidade (recursivo)
     * @param caminhoAtual Lista geral de ciclos a partir do vertice inicial, passada por referencia para o preenchimento correto no metodo de visita.
     * @param grafoCopia grafo a ser analisado (copia do grafo original)
     * @param timestamp tempo de descoberta da busca em profundidade.
     * @param atual vertice atual, iniciando no vertice passado como parametro no dfs e alterando conforme a recursividade é chamada.
     * @param verticeFinal_t vertice final, iniciando no vertice passado como parametro no dfs e alterando conforme a recursividade é chamada.
     * @param caminhoAtual caminho percorrido
     * @return int com o valor do timestamp.
     */
    private static int visitarFordFulkerson(Vertex[] grafoCopia, int timestamp, Vertex atual, Vertex verticeFinal_t,List<String> caminhoAtual, boolean arcoReverso)throws Exception{
        try {

            timestamp = timestamp + 1;
            atual.setTempoDescoberta(timestamp);
            if (!arcoReverso){
                caminhoAtual.add("" + atual.getNumVertice() + ",");
            }else{
                caminhoAtual.add("" + atual.getNumVertice() +"R"+ ",");
            }
            atual.setCor(1);
            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];
                if (vizinhoAtual.getCor() == 0 && v.getPeso()>0) { //verifica se o próximo vertice é branco( nao visitado)

                    vizinhoAtual.setPai(atual.getNumVertice());
                    if (atual.getNumVertice() == verticeFinal_t.getNumVertice()) {
                        caminhoAtual.add("F");
                    }
                    arcoReverso = v.getRotulo().contains("R");
                    visitarFordFulkerson(grafoCopia, timestamp, vizinhoAtual, verticeFinal_t, caminhoAtual,arcoReverso);

                }

            }
            if (atual.getNumVertice() == verticeFinal_t.getNumVertice()) {
                caminhoAtual.add("F");
            }else{
                caminhoAtual.add("|");
            }

            atual.setCor(2);
            timestamp = timestamp + 1;
            atual.setTempoFinalizacao(timestamp);
            return timestamp;
        }catch (Exception e){e.printStackTrace();return -1;}
    }



    public static void main(String[] args) {
        try {

            System.out.println("Digite o nome e extesão do arquivo padrão do grafo:   (exemplo: pub3.in) ");
            BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

            String lidoArquivo = lerGrafoArquivo(input.readLine());
            System.out.print("Lido do Arquivo: ");
            System.out.println(lidoArquivo);
            Vertex[] grafo = montarGrafoLista(lidoArquivo);
            List<String[]> arrayCaminhos = fluxoFordFulkerson(grafo,grafo[0].getNumVertice(),grafo[5].getNumVertice());
            System.out.println("_________");
            /*for (String s: dfsresult) {
                System.out.println(" "+s);
            }*/
            /*System.out.print("Arestas padrão após grafo montado: ");
            String printGrafo = printArestasPadrao(grafo);
            System.out.println(printGrafo);
            System.out.println("------------------------------------------------------- ");
            System.out.println("Ciclos a partir da busca em profundidade: ");
            long startTime = System.currentTimeMillis();
            List<List<Vertex>> ciclos = detectCycles(grafo);
            long totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Tempo total: "+totalTime+" ms.");
            System.out.println(" ");
            System.out.println("Ciclos a partir das permutações: ");
            startTime = System.currentTimeMillis();
            searchCyclesWithPermutations(grafo);
            totalTime = System.currentTimeMillis() - startTime;
            System.out.println("Tempo total: "+totalTime+" ms. ");
            System.out.println(" ");*/
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
