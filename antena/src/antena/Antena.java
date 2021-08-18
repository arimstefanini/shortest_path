package antena;

import ilog.concert.IloException;
import ilog.concert.IloLinearNumExpr;
import ilog.concert.IloNumVar;
import ilog.cplex.IloCplex;

public class Antena {

    public static IloCplex model;
    public static IloNumVar[][] s;
    public static IloNumVar[] v;

    public static Data data = new Data();

    public static void main(String[] args){

        data.init("src/antena/antena_instancia_008.input");

        try {

            model = new IloCplex();

            s = new IloNumVar[data.n][data.m];
            v = new IloNumVar[data.m];

            // Alocando matriz s
            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s[i].length; j++) {
                    s[i][j] = model.boolVar();
                }
            }

            // alocando vetor v
            for (int j = 0; j < v.length; j++) {
                v[j] = model.boolVar();
            }

            // função objetivo
            IloLinearNumExpr fo = model.linearNumExpr();
            for (int i = 0; i < v.length; i++) {
                fo.addTerm(data.c[i], v[i]);
            }
            model.addMinimize(fo);

            //restrição de possibilidade de atendimento
            for (int i = 0; i < s.length; i++) {
                for (int j = 0; j < s[i].length; j++) {
                    model.addLe(s[i][j], data.p[i][j]);
                }
            }

            //restrição de obrigação de atendimento
            for (int i = 0; i < data.n; i++) {
                IloLinearNumExpr ex = model.linearNumExpr();
                for (int j = 0; j < data.m; j++) {
                    ex.addTerm(1, s[i][j]);
                }
                model.addEq(ex, 1);
            }

            //matriz condicional
            for (int j = 0; j < data.m; j++) {
                IloLinearNumExpr ex = model.linearNumExpr();
                for (int i = 0; i < data.n; i++) {
                    ex.addTerm(1, s[i][j]);
                }

                IloLinearNumExpr if1 = model.linearNumExpr();
                IloLinearNumExpr if2 = model.linearNumExpr();

                if1.addTerm(data.m + 1, v[j]);
                if2.addTerm(0.0000002, v[j]);

                model.addLe(ex, if1);
                model.addLe(if2, ex);

            }

            /*
            //parte 2 do relatorio
            for (int i = 0; i < data.m; i++) {
                IloLinearNumExpr ex = model.linearNumExpr();
                for (int j = 0; j < data.n; j++) {
                    ex.addTerm(0.7 * data.lc[j], s[i][j]);
                }
                model.le(ex, data.la[i]);
            }
             */
            
            if(model.solve()){
                System.out.println("---------------- Instancia ");
                System.out.println("Deu certo ");
                System.out.println(model.getStatus());
                System.out.println(model.getObjValue());
                print();
            }else{
                System.out.println("---------------- Instancia ");
                System.out.println("Deu merda ");
                System.out.println(model.getStatus());
                System.out.println("----------------");
            }

        } catch (IloException ex) {
            System.out.println("Deu Erro !!");
            System.out.println(ex.getMessage());
        }

    }

    public static void print() throws IloException{
        System.out.println("------------------------------------------");
        for (int i = 0; i < data.m; i++) {
            System.out.print(makeLine(i));
        }
        System.out.println("------------------------------------------");
        System.out.println();
    }

    private static String makeLine(int j) throws IloException{
        boolean controle = false;
        String r = "Antena "+j+"\t|\t ->";
        int conta = 0;
        for (int i = 0; i < data.n; i++) {
            if(model.getValue(s[i][j]) != 0){
                controle = true;
                conta++;
                r +=  i + "  \t||";
            }
            if(conta == 5){
                conta = 0;
                r+="\n\t\t\t|\t ->";
            }
        }
        r+="\n";
        return (controle) ? r : "";
    }

}
