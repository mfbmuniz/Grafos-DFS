package com.company;

public class teste {

    /**
     * Implementacao da Busca em profundidade Modificada para encontrar ciclos
     * @param grafo grafo a ser analisado
     * @param verticeInicialGrafoOriginal vertice onde a busca se iniciará
     * @return List com os ciclos detectados sem repeticoes a partir do vertice atual escolhido como parametro.
     */

    public static List dfs(Vertex[]grafo, Vertex verticeInicial_s, Vertex verticeFinal_t ){
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

            Vertex verticeInicial_s = grafoCopia[verticeInicial_s.getNumVertice()];
            Vertex verticeFinal_t = grafoCopia[verticeFinal_t.getNumVertice()];


            //Lista com o caminho atual;
            List<String> caminhoAtual = new ArrayList<Vertex>() ;

            if (verticeInicial.getCor() == 0) { //se a cor for branca
                timestamp = visitar(grafoCopia, timestamp, verticeInicial_s, verticeFinal_t,caminhoAtual); //lembrar que u e grafo copia estao sendo passados por referencia
            }

            for (Vertex u : grafoCopia) {
                if (u.getCor() == 0) { //se a cor for branca
                    timestamp = visitar(grafoCopia, timestamp, verticeInicial_s, verticeFinal_t,caminhoAtual); //lembrar que u e grafo copia estao sendo passados por referencia
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
    private static int visitar(Vertex[] grafoCopia, int timestamp, Vertex atual, Vertex verticeFinal_t,camin){
        try {

            timestamp = timestamp + 1;
            atual.setTempoDescoberta(timestamp);
            caminhoAtual.add(""+atual.getNumVertice);
            atual.setCor(1);

            if(atual.getNumVertice == verticeFinal_t.getNumVertice){
                atual.setCor(2);
                timestamp = timestamp + 1;
                atual.setTempoFinalizacao(timestamp);
                return timestamp;
            }
            for (Edge v : atual.arestas) {
                Vertex vizinhoAtual = grafoCopia[v.getNumVertice()];

                if (vizinhoAtual.getCor() == 0 ) { //verifica se o próximo vertice é branco( nao visitado)

                    vizinhoAtual.setPai(atual.getNumVertice());
                    visitar(grafoCopia,timestamp,vizinhoAtual,ciclos,caminhoAtual);

                }

            }

            caminhoAtual.add("|");
            atual.setCor(2);
            timestamp = timestamp + 1;
            atual.setTempoFinalizacao(timestamp);
            return timestamp;

        }catch (Exception e){e.printStackTrace();return -1;}
    }
}
