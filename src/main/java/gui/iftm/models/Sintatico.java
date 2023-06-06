package gui.iftm.models;

import java.util.List;

public class Sintatico {
    private Lexico lexico;
    private Token token;

    public Sintatico(String filePath) {
        this.lexico = new Lexico(filePath);
    }

    public void readNextToken() {
        token = lexico.getToken();
        System.out.println(token);
    }

    public void ExecutarAnalise() {
        readNextToken();
        programa();
    }

    public void programa() {
        if ((token.getClasse() == Classe.cPalRes)
                && (token.getValor().getValorIdentificador().equalsIgnoreCase("program"))) {
            readNextToken();
            if (token.getClasse() == Classe.cId) {
                readNextToken();
                corpo();
                if (token.getClasse() == Classe.cPonto) {
                    readNextToken();
                } else showErrorMessage(ErrorConstants.pontoFinal.getDescription());
            } else showErrorMessage(ErrorConstants.nomePrograma.getDescription());
        } else showErrorMessage(ErrorConstants.declaracaoPrograma.getDescription());
    }

    public void corpo() {
        declara();
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))) {
            readNextToken();
            sentencas();
            if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))) {
                readNextToken();
            } else showErrorMessage(ErrorConstants.declaracaoFim.getDescription() + ErrorConstants.rotinaPrograma.getDescription());
        } else showErrorMessage(ErrorConstants.declaracaoInicio.getDescription() + ErrorConstants.rotinaPrograma.getDescription());
    }

    public void declara() {
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("var"))) {
            readNextToken();
            dvar();
            mais_dc();
        }
    }

    public void mais_dc() {
        if (token.getClasse() == Classe.cPontoVirgula) {
            readNextToken();
            cont_dc();
        } else showErrorMessage(ErrorConstants.pontoVirgula.getDescription());
    }

    public void cont_dc() {
        if (token.getClasse() == Classe.cId) {
            dvar();
            mais_dc();
        }
    }

    public void dvar() {
        variaveis();
        if (token.getClasse() == Classe.cDoisPontos) {
            readNextToken();
            tipo_var();
        } else showErrorMessage(ErrorConstants.doisPontos.getDescription());
    }

    public void tipo_var() {
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("integer"))) {
            readNextToken();
        } else if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("real"))) {
            readNextToken();
        } else showErrorMessage(ErrorConstants.declaracaoTipoVariavel.getDescription());
    }

    public void variaveis() {
        if (token.getClasse() == Classe.cId) {
            readNextToken();
            mais_var();
        } else showErrorMessage(ErrorConstants.declaracaoIdentificadorVariavel.getDescription());
    }

    public void mais_var() {
        if (token.getClasse() == Classe.cVirgula) {
            readNextToken();
            variaveis();
        }
    }

    public void sentencas() {
        comando();
        mais_sentencas();
    }

    public void mais_sentencas() {
        if (token.getClasse() == Classe.cPontoVirgula) {
            readNextToken();
            cont_sentencas();
        } else showErrorMessage(ErrorConstants.pontoVirgula.getDescription());
    }

    public void cont_sentencas() {
        if(token.getClasse() == Classe.cPalRes) {
            String valIdentificador = token.getValor().getValorIdentificador();

            if((valIdentificador.equalsIgnoreCase("read")) ||
                    (valIdentificador.equalsIgnoreCase("write")) ||
                    (valIdentificador.equalsIgnoreCase("for")) ||
                    (valIdentificador.equalsIgnoreCase("repeat")) ||
                    (valIdentificador.equalsIgnoreCase("while")) ||
                    (valIdentificador.equalsIgnoreCase("if"))){
                sentencas();
            }
        } else if (token.getClasse() == Classe.cId){
            sentencas();
        }
    }

    public void var_read() {
        if (token.getClasse() == Classe.cId) {
            readNextToken();
            mais_var_read();
        } else showErrorMessage(ErrorConstants.declaracaoIdentificadorVariavel.getDescription());
    }

    public void mais_var_read() {
        if (token.getClasse() == Classe.cVirgula) {
            readNextToken();
            var_read();
        }
    }

    public void var_write() {
        if (token.getClasse() == Classe.cId) {
            readNextToken();
            mais_var_write();
        } else showErrorMessage(ErrorConstants.declaracaoIdentificadorVariavel.getDescription());
    }

    public void mais_var_write() {
        if (token.getClasse() == Classe.cVirgula) {
            readNextToken();
            var_write();
        }
    }

    public void comando() {
        if(token.getClasse() == Classe.cPalRes){
            String valIdentificador = token.getValor().getValorIdentificador();

            if (valIdentificador.equalsIgnoreCase("read")){
                comandoRead();
            } else if (valIdentificador.equalsIgnoreCase("write")){
                comandoWrite();
            } else if (valIdentificador.equalsIgnoreCase("for")){
                comandoFor();
            } else if (valIdentificador.equalsIgnoreCase("repeat")){
                comandoRepeat();
            } else if (valIdentificador.equalsIgnoreCase("while")){
                comandoWhile();
            } else if (valIdentificador.equalsIgnoreCase("if")){
                comandoIf();
            }
        } else if (token.getClasse() == Classe.cId){
            readNextToken();
            if (token.getClasse() == Classe.cAtribuicao){
                readNextToken();
                expressao();
            } else showErrorMessage(ErrorConstants.operadorAtribuicao.getDescription());
        }
    }

    public void comandoRead() {
        readNextToken();
        if (token.getClasse() == Classe.cParEsq) {
            readNextToken();
            var_read();
            if (token.getClasse() == Classe.cParDir) {
                readNextToken();
            } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription());
        } else showErrorMessage(ErrorConstants.parenteseAbertura.getDescription());
    }

    public void comandoWrite() {
        readNextToken();
        if (token.getClasse() == Classe.cParEsq) {
            readNextToken();
            var_write();
            if (token.getClasse() == Classe.cParDir) {
                readNextToken();
            } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription());
        } else showErrorMessage(ErrorConstants.parenteseAbertura.getDescription());
    }

    public void comandoFor() {
        String rotinaDescription = ErrorConstants.rotinaFor.getDescription();

        readNextToken();
        if (token.getClasse() == Classe.cId) {
            readNextToken();
            if (token.getClasse() == Classe.cAtribuicao){
                readNextToken();
                expressao();
                if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("to"))){
                    readNextToken();
                    expressao();
                    if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("do"))){
                        readNextToken();
                        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
                            readNextToken();
                            sentencas();
                            if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
                                readNextToken();
                            } else showErrorMessage(ErrorConstants.declaracaoFim.getDescription() + rotinaDescription);
                        } else showErrorMessage(ErrorConstants.declaracaoInicio.getDescription() + rotinaDescription);
                    } else showErrorMessage(ErrorConstants.declaracaoDo.getDescription() + rotinaDescription);
                } else showErrorMessage(ErrorConstants.declaracaoTo.getDescription() + rotinaDescription);
            } else showErrorMessage(ErrorConstants.operadorAtribuicao.getDescription() + rotinaDescription);
        } else showErrorMessage(ErrorConstants.declaracaoIdentificadorVariavel.getDescription() + rotinaDescription);
    }

    public void comandoRepeat() {
        String rotinaDescription = ErrorConstants.rotinaRepeat.getDescription();

        readNextToken();
        sentencas();
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("until"))){
            readNextToken();
            if (token.getClasse() == Classe.cParEsq){
                readNextToken();
                condicao();
                if (token.getClasse() == Classe.cParDir){
                    readNextToken();
                } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription() + rotinaDescription);
            } else showErrorMessage(ErrorConstants.parenteseAbertura.getDescription() + rotinaDescription);
        } else showErrorMessage(ErrorConstants.declaracaoUntil.getDescription() + rotinaDescription);
    }

    public void comandoWhile() {
        String rotinaDescription = ErrorConstants.rotinaWhile.getDescription();

        readNextToken();
        if (token.getClasse() == Classe.cParEsq){
            readNextToken();
            condicao();
            if (token.getClasse() == Classe.cParDir){
                readNextToken();
                if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("do"))){
                    readNextToken();
                    if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
                        readNextToken();
                        sentencas();
                        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
                            readNextToken();
                        } else showErrorMessage(ErrorConstants.declaracaoFim.getDescription() + rotinaDescription);
                    } else showErrorMessage(ErrorConstants.declaracaoInicio.getDescription() + rotinaDescription);
                } else showErrorMessage(ErrorConstants.declaracaoDo.getDescription() + rotinaDescription);
            } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription() + rotinaDescription);
        } else showErrorMessage(ErrorConstants.parenteseAbertura.getDescription() + rotinaDescription);
    }

    public void comandoIf() {
        String rotinaDescription = ErrorConstants.rotinaIf.getDescription();

        readNextToken();
        if (token.getClasse() == Classe.cParEsq){
            readNextToken();
            condicao();
            if (token.getClasse() == Classe.cParDir){
                readNextToken();
                if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("then"))){
                    readNextToken();
                    if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
                        readNextToken();
                        sentencas();
                        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
                            readNextToken();
                            pfalsa();
                        } else showErrorMessage(ErrorConstants.declaracaoFim.getDescription() + rotinaDescription);
                    } else showErrorMessage(ErrorConstants.declaracaoInicio.getDescription() + rotinaDescription);
                } else showErrorMessage(ErrorConstants.declaracaoDo.getDescription() + rotinaDescription);
            } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription() + rotinaDescription);
        } else showErrorMessage(ErrorConstants.parenteseAbertura.getDescription() + rotinaDescription);
    }

    public void condicao() {
        expressao();
        relacao();
        expressao();
    }


    public void pfalsa() {
        if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("else"))){
            readNextToken();
            if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("begin"))){
                readNextToken();
                sentencas();
                if ((token.getClasse() == Classe.cPalRes) && (token.getValor().getValorIdentificador().equalsIgnoreCase("end"))){
                    readNextToken();
                } else showErrorMessage(ErrorConstants.declaracaoFim.getDescription());
            } else showErrorMessage(ErrorConstants.declaracaoInicio.getDescription());
        }
    }

    public void relacao() {
        List<Classe> reservedWords = List.of(Classe.cIgual, Classe.cMaior, Classe.cMenor, Classe.cMaiorIgual,
                Classe.cMenorIgual, Classe.cDiferente);

        if (reservedWords.contains(token.getClasse())) {
            readNextToken();
        } else showErrorMessage(ErrorConstants.operadorComparacao.getDescription());
    }

    public void expressao() {
        termo();
        outros_termos();
    }

    public void outros_termos() {
        if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
            op_ad();
            termo();
            outros_termos();
        }
    }

    public void op_ad() {
        if (token.getClasse() == Classe.cMais || token.getClasse() == Classe.cMenos) {
            readNextToken();
        } else showErrorMessage(ErrorConstants.operadorSoma.getDescription());
    }

    public void termo() {
        fator();
        mais_fatores();
    }


    public void mais_fatores() {
        if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
            op_mul();
            fator();
            mais_fatores();
        }
    }

    public void op_mul() {
        if (token.getClasse() == Classe.cMultiplicacao || token.getClasse() == Classe.cDivisao) {
            readNextToken();
        } else showErrorMessage(ErrorConstants.operadorMultiplicacao.getDescription());
    }


    public void fator() {
        if (token.getClasse() == Classe.cId) {
            readNextToken();
        } else if (token.getClasse() == Classe.cInt || token.getClasse() == Classe.cReal) {
            readNextToken();
        } else if (token.getClasse() == Classe.cParEsq){
            readNextToken();
            expressao();
            if (token.getClasse() == Classe.cParDir){
                readNextToken();
            } else showErrorMessage(ErrorConstants.parenteseFechamento.getDescription());
        } else showErrorMessage(ErrorConstants.declaracaoExpoente.getDescription());
    }

    public void showErrorMessage(String error) {
        System.err.println("Problema sintático encontrado -> " + error + " (Linha: "+ token.getLinha() + ", Coluna: " + token.getColuna() + ")");
    }
}
