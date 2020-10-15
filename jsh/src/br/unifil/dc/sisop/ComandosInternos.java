package br.unifil.dc.sisop;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * Write a description of class ComandosInternos here.
 *
 * @author Ricardo Inacio Alvares e Silva
 * @version 180823
 */
public class ComandosInternos {

    public static int exibirRelogio() {  //TODO FEITOOO
        Timestamp dataDeHoje = new Timestamp(System.currentTimeMillis());
        System.out.println("Hoje é: " + dataDeHoje);
        return 0;
    }

    public static int escreverListaArquivos(Optional<String> nomeDir) { //TODO FEITOOO
        File file = new File(System.getProperty("user.dir"));
        File afile[] = file.listFiles();
        int i = 0;
        if(afile.length == 0) {
            System.out.println("Esse diretório não possuí arquivos");
        }
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            System.out.println(arquivos.getName());
        }
        return 1;
    }

    public static List<String> escreverListaArquivo(Optional<String> nomeDir) { //TODO FEITOOO
        File file = new File(System.getProperty("user.dir"));
        File afile[] = file.listFiles();
        List<String> arquivosList = new ArrayList<>();
        int i = 0;
        if(afile.length == 0) {
            System.out.println("Esse diretório não possuí arquivos");
        }
        for (int j = afile.length; i < j; i++) {
            File arquivos = afile[i];
            arquivosList.add(arquivos.getName());
        }
        return arquivosList;
    }

    public static int criarNovoDiretorio(String nomeDire) throws Exception { //TODO FEITOOO
        int nomeDir = nomeDire.indexOf(" ");
        String nomeDiretorio = nomeDire.substring(nomeDir + 1, nomeDire.length());
        boolean isCriado = false;
        try {
            String diretorioAtual = System.getProperty("user.dir");
            File file = new File(diretorioAtual + "/" + nomeDiretorio);
            isCriado = file.mkdir();

            if(!isCriado) {
                System.out.println("Esse diretório não foi possível ser criado");
            }
            return  1;
        } catch (Exception e) {
            throw new Exception("Não foi possivel criar o diretório");
        }


    }

    public static int apagarDiretorio(String nomeDire) { //TODO FEITOOO
        int nomeDir = nomeDire.indexOf(" ");
        String nomeDiretorio = nomeDire.substring(nomeDir + 1, nomeDire.length());
        try {
            File file = new File(nomeDiretorio);
            if ((file.exists()) && (file.isDirectory())) {
                file.delete();
            } else {
                System.out.println("O diretório não pode ser excluído");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }


        return 1;
    }

    public static int mudarDiretorioTrabalho(String nomeDire){ //TODO FEITOOO
        int nomeDir = nomeDire.indexOf(" ");
        String nomeDiretorio = nomeDire.substring(nomeDir + 1, nomeDire.length());
        File folder = new File(nomeDiretorio);
        if(folder.exists()) {
            System.setProperty("user.dir", folder.getAbsolutePath());
        } else {
            System.out.println("O diretório informado não existe");
        }
        return 1;
    }

    /**
     * Essa classe não deve ser instanciada.
     */
    public ComandosInternos() {}
}