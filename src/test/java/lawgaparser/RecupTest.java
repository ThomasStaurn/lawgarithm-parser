package lawgaparser;


import junit.framework.TestCase;
import org.assertj.core.api.Assertions;
import org.junit.Test;


import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


public class RecupTest {

    private Recup recup = new Recup();
//
//    @Test
//    void test_lecture_fichier() throws IOException {
//
//        String oldResultat = recup.oldOuvrir("D:\\dev\\projects\\lawgarithmParser\\src\\contrat.txt");
//        String newResultat = String.join("\n",recup.ouvrir("D:\\dev\\projects\\lawgarithmParser\\src\\contrat.txt"));
//
//        Assertions.assertEquals(oldResultat, newResultat);
//    }
//
//    @Test
//    void test_segmentation_fichier() throws IOException {
//        System.out.println("Old");
//        String oldResultat = recup.oldOuvrir("D:\\dev\\projects\\lawgarithmParser\\src\\contrat.txt");
//        String[] oldSegmentation = recup.segmentation(oldResultat);
//        export("oldSegmentation.txt", Arrays.asList(oldSegmentation));
//        System.out.println("New");
//        List<String> newResultats = recup.ouvrir("D:\\dev\\projects\\lawgarithmParser\\src\\contrat.txt");
//        String[] newSegmentation = recup.segmentation(String.join("\n",newResultats));
//        export("newSegmentation.txt", Arrays.asList(newSegmentation));
//
//    }

    @Test
    public void test_balisage() throws IOException {
        List<Block> blocks = recup.balisage(Arrays.asList(
                "ARTICLE 1 tata",
                "SECTION 1.0 tata",
                "1.1 tata",
                "(a) tata",
                "(i) tata",
                "(ii) tata",
                "SECTION 2.0 tata",
                "A. tata"
        ));
        List<Block> expectedBlocks = Arrays.asList(
                new Block(1, "ARTCILE 1 tata", 1, 1),
                new Block(5, "SECTION 1.0 tata", 2, 2),
                new Block(3, "1.1 tata", 3, 3),
                new Block(3, "(a) tata", 4, 4),
                new Block(3, "(i) tata", 5, 5),
                new Block(3, "(ii) tata", 5, 5),
                new Block(2, "SECTION 2.0 tata", 2, 6)
        );
//        blocks.forEach(block -> System.out.println(block.toString()));
        Assertions.assertThat(blocks).containsAll(expectedBlocks);
    }


    @Test
    public void test_LawgaNoeud_toString() throws IOException {
        Block article = new Block(1, "ARTCILE 1 tata", 1, 1);
        Block section1 = new Block(5, "SECTION 1.0 tata", 2, 2);
        Block chiffre =  new Block(3, "1.1 tata", 3, 3);
        Block section2 = new Block(5, "SECTION 2.0 tata", 2, 4);

        LawgaNoeud root = new LawgaNoeud(null,null, new ArrayList<>());
        LawgaNoeud noeud_article = new LawgaNoeud(article,root, new ArrayList<>());
        LawgaNoeud noeud_section1 = new LawgaNoeud(section1,noeud_article, new ArrayList<>());
        LawgaNoeud noeud_chiffre = new LawgaNoeud(chiffre,noeud_section1, new ArrayList<>());
        LawgaNoeud noeud_section2 = new LawgaNoeud(section2,noeud_article, new ArrayList<>());

        root.addFils(noeud_article);
        noeud_article.addFils(noeud_section1);
        noeud_article.addFils(noeud_section2);
        noeud_section1.addFils(noeud_chiffre);

        System.out.println(root.toString());
    }

    private void export(String nomFichier, List<String> lines) {
        try (PrintWriter pw = new PrintWriter(nomFichier, "UTF-8")) {
            lines.forEach(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}