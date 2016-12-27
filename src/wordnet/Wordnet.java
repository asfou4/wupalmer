/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package wordnet;

/**
 *
 * @author Firana
 */
import edu.cmu.lti.lexical_db.ILexicalDatabase;
import edu.cmu.lti.lexical_db.NictWordNet;
import edu.cmu.lti.ws4j.RelatednessCalculator;
import edu.cmu.lti.ws4j.impl.HirstStOnge;
import edu.cmu.lti.ws4j.impl.JiangConrath;
import edu.cmu.lti.ws4j.impl.LeacockChodorow;
import edu.cmu.lti.ws4j.impl.Lesk;
import edu.cmu.lti.ws4j.impl.Lin;
import edu.cmu.lti.ws4j.impl.Path;
import edu.cmu.lti.ws4j.impl.Resnik;
import edu.cmu.lti.ws4j.impl.WuPalmer;
import edu.cmu.lti.ws4j.util.WS4JConfiguration;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Vector;
import javax.swing.text.Document;
import org.jsoup.Jsoup;
import org.jsoup.select.Elements;

public class Wordnet {

    /**
     * @param args the command line arguments
     */
    static Connection kon;
    private static ILexicalDatabase db = new NictWordNet();
    /*
     //available options of metrics
     private static RelatednessCalculator[] rcs = { new HirstStOnge(db),
     new LeacockChodorow(db), new Lesk(db), new WuPalmer(db),
     new Resnik(db), new JiangConrath(db), new Lin(db), new Path(db) };
     */

    private static double compute(String word1, String word2) {
        WS4JConfiguration.getInstance().setMFS(true);
        double s = new WuPalmer(db).calcRelatednessOfWords(word1, word2);
        return s;
    }
    
    private static void konek() throws SQLException {
        String dbHost = "jdbc:mysql://localhost:3306/uninform";
        String dbUser = "root";
        String dbPass = "";

        try {
            kon = (Connection) DriverManager.getConnection(dbHost, dbUser, dbPass);
            //System.out.println("SUKSES !!!");

        } catch (SQLException e) {
            System.err.println(e);
        }
    }
    
    private static String add_doc() throws IOException, SQLException{
        String url = "http://www.uinsby.ac.id";
        org.jsoup.nodes.Document doc = Jsoup.connect(url).timeout(0).get();
        Elements text = doc.select("body");
        String teks = text.text();
        teks = teks.replaceAll("[(-+^:,'|&?!)]", "").replaceAll("yang", "").replaceAll("dengan", "").replaceAll("dan", "").replaceAll("dari", "");
        String dokumen = teks;
        
        return dokumen;
    }
    public static void main(String[] args) throws IOException, SQLException {
       
        BufferedReader x = new BufferedReader(new InputStreamReader(System.in));
      
        System.out.println("Masukkan kalimat :");
        String kalimat = x.readLine();
        kalimat = kalimat.replaceAll("[(-+^:,'|&?!)]", "").replaceAll("yang", "").replaceAll("dengan", "").replaceAll("dan", "").replaceAll("dari", "");
        String dok = kalimat;
        String[] index = dok.split(" ");
        for (int i = 0; i < index.length; i++) {
            System.out.println(index[i]);
        }
//       String dok = add_doc();
//         Vector<String> index = new <String>Vector();
//        Vector<String> dokumen = new <String>Vector();
//          
//        String[] kalimat = dok.split("\\.");
//        for (int i = 0; i < kalimat.length; i++) {
//            dokumen.add(kalimat[i]);
//        }
//        //mencari index katanya
//        for (int i = 0; i < dokumen.size(); i++) {
//            String[] kata = dokumen.get(i).split(" ");
//            for (int j = 0; j < kata.length; j++) {
//                if (kata[j].isEmpty()||kata[j].contains(" ")||kata[j].matches("\\S")) {
//                }else{
//                   //proses add index
//                    int hitung = 0;
//                    for (int k = 0; k < index.size(); k++) {
//                        if (index.size() == 0) {
//                            index.add(kata[j]);
//                        }else{
//                            if(!kata[j].equalsIgnoreCase(index.get(k))){
//                                hitung++;
//                            }
//                        }
//                    }
//                    if (hitung == index.size()) {
//                        index.add(kata[j]);
//                    }
//                    
//                }
//                
//            }
//        }
        //menghitung similiarity
        for (int i = 0; i < index.length; i++) {
//            
            for (int j = 0; j < index.length; j++) {
                //if (index[i].equalsIgnoreCase(index[j])) {

                //} else {
                    double distance = compute(index[i], index[j]);
                    System.out.println(index[i] + " -  " + index[j] + " = " + distance);
                //}
            }
        }
        //=============================================================
        

    }

}
