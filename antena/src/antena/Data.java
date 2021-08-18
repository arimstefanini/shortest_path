package antena;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Data {

    public int p[][];
    public double c[];

    public int m;
    public int n;


    public void init(String arquivo){
        try{
            Scanner file = new Scanner(new File(arquivo));

            file.next();
            n = file.nextInt();

            file.next();
            m = file.nextInt();

            c = new double[m];
            p = new int[n][m];

            for (int i = 0; i < m; i++)
                file.next();

            for (int i = 0; i < m; i++)
                c[i] = file.nextDouble();

            file.next();
            file.next();
            for (int i = 0; i < m; i++)
                file.next();

            for (int i = 0; i < n; i++) {
                file.next();
                for (int j = 0; j < m; j++) {
                    p[i][j] = file.nextInt();
                }
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }


}
