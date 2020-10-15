package br.unifil.dc.sisop;

        import java.io.*;
        import java.util.ArrayList;
        import java.util.Scanner;

/**
 * Write a description of class Jsh here.
 *
 * @author Ricardo Inacio Alvares e Silva
 * @version 180823
 */
public final class Jsh {

    ComandosInternos comandosInternos;



    /**
     * Funcao principal do Jsh.
     */
    public static void promptTerminal() throws Exception {

        while (true) {
            exibirPrompt();
            ComandoPrompt comandoEntrado = lerComando();
            executarComando(comandoEntrado);
        }
    }

    /**
     * Escreve o prompt na saida padrao para o usuário reconhecê-lo e saber que o
     * terminal está pronto para receber o próximo comando como entrada.
     */
    public static void exibirPrompt() throws IOException, InterruptedException {
        String nomeUsuario = System.getProperty("user.name");
        String pwd = System.getProperty("user.dir");
        System.out.print(nomeUsuario + "#" + recuperarIdUsuario(nomeUsuario) + ": " + pwd + " ");
    }

    /**
     * Preenche as strings comando e parametros com a entrada do usuario do terminal.
     * A primeira palavra digitada eh sempre o nome do comando desejado. Quaisquer
     * outras palavras subsequentes sao parametros para o comando. A palavras sao
     * separadas pelo caractere de espaco ' '. A leitura de um comando eh feita ate
     * que o usuario pressione a tecla <ENTER>, ou seja, ate que seja lido o caractere
     * EOL (End Of Line).
     *
     * @return
     */
    public static ComandoPrompt lerComando() throws Exception {
        ArrayList<String> comandosShell = new ArrayList<String>();
        comandosShell.add("la");
        comandosShell.add("encerrar");
        comandosShell.add("mesg_do_dia");
        comandosShell.add("relogio");
        comandosShell.add("falha_arbitraria");
        Scanner scanner = new Scanner(System.in);
        String comando = scanner.nextLine();
        ComandoPrompt comandoPrompt = new ComandoPrompt(comando);
        boolean isValid = false;
        int contador = 0;
        File file = new File(System.getProperty("user.dir"));
        File afile[] = file.listFiles();
        if (!comandosShell.contains(comando)) {
            if (comando.contains("cd") || comando.contains("mdt") || comando.contains("ad")) {
                isValid = true;
            } else {
                for (int i = 0; i < afile.length; i++) {
                    if (comando.equals(afile[i].getName())) {
                        if (afile[i].canExecute()) {
                            try {
                                BufferedReader br = new BufferedReader(
                                        new FileReader(file.getAbsolutePath() + "/" + comando));
                                while (br.ready()) {
                                    String linha = br.readLine();
                                    System.out.println(linha);
                                    br.close();
                                }
                            } catch (Exception e) {
                                System.out.print(" ");
                            }
                        } else {
                            System.out.println("Não possui permissão para executar esse arquivo");
                        }
                        contador++;
                    }
                }
            }
        } else {
            isValid = true;
        }
        if (!isValid) {
            System.out.println("Não existe esse comando");
        }

        if (contador > 0) {
            System.out.println("Existe um arquivo com esse nome");
        } else if (!isValid) {
            System.out.println("Não existe nenhum arquivo com esse nome");
        }

        return comandoPrompt;
    }


    /**
     * Recebe o comando lido e os parametros, verifica se eh um comando interno e,
     * se for, o executa.
     * <p>
     * Se nao for, verifica se é o nome de um programa terceiro localizado no atual
     * diretorio de trabalho. Se for, cria um novo processo e o executa. Enquanto
     * esse processo executa, o processo do uniterm deve permanecer em espera.
     * <p>
     * Se nao for nenhuma das situacoes anteriores, exibe uma mensagem de comando ou
     * programa desconhecido.
     */
    public static void executarComando(ComandoPrompt comando) throws Exception {
        boolean isValid = false;
        File file = new File(System.getProperty("user.dir"));
        File afile[] = file.listFiles();

        if (comando.getNome().equals("la")) {
            ComandosInternos.escreverListaArquivos(java.util.Optional.ofNullable(comando.getNome()));
        } else

        if (comando.getNome().contains("mdt")) {
            ComandosInternos.mudarDiretorioTrabalho(comando.getNome());
        } else

        if (comando.getNome().contains("cd")) {
            ComandosInternos.criarNovoDiretorio(comando.getNome());
        } else

        if (comando.getNome().equals("relogio")) {
            ComandosInternos.exibirRelogio();
        } else

        if (comando.getNome().contains("ad")) {
            ComandosInternos.apagarDiretorio(comando.getNome());
        } else

        if(comando.getNome().equals("encerrar")) {
            System.exit(0);
        } else

        if(comando.getNome().equals("msg_do_dia")) {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("./" + comando.getNome());
        } else

        if(comando.getNome().equals("falha_arbitraria")) {
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec("./" + comando.getNome());
        } else
        if (ComandosInternos.escreverListaArquivo(java.util.Optional.ofNullable(comando.getNome())).contains(comando.getNome())) {
            File comandFile = new File(comando.getNome());
            if(comandFile.canExecute()) {
                Runtime rt = Runtime.getRuntime();
                Process pr = rt.exec("./" + comando.getNome());
            } else {
                System.out.println("Esse arquivo não possui permissão para leitura");
            }
        } else {
            String command= "/usr/bin/xterm";
            Runtime rt = Runtime.getRuntime();
            Process pr = rt.exec(command);

        }
    }

    public static int executarPrograma(ComandoPrompt comando) {
        throw new RuntimeException("Método ainda não implementado.");
    }

    public static Integer getUserId(String username) throws IOException, InterruptedException {
        Integer id = null;
        Process process;
        try {
            process = Runtime.getRuntime().exec("id -u" + username);
            process.waitFor();
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
            String line;

            while ((line = reader.readLine()) != null) {
                id = Integer.valueOf(line);
            }

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static Integer recuperarIdUsuario(String userName) {
        Integer id = null;
        Process process;
        try {
            process = Runtime.getRuntime().exec("id -u " + userName);
            process.waitFor();
            id = recuperarCodigoRetornoPrograma(process.getInputStream());
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return id;
    }

    private static int recuperarCodigoRetornoPrograma(InputStream is) {
        int codigoRetorno = 0;
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            String line;

            while ((line = reader.readLine()) != null) {
                codigoRetorno = Integer.valueOf(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return codigoRetorno;
    }

    /**
     * Entrada do programa. Provavelmente você não precisará modificar esse método.
     */
    public static void main(String[] args) throws Exception {
        promptTerminal();
    }


    /**
     * Essa classe não deve ser instanciada.
     */
    private Jsh() {
    }
}
