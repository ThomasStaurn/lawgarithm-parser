package lawgaparser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.String.format;

public class Recup {

    private static final Pattern PATTERN_ARTICLE = Pattern.compile("ARTICLE|Article");
    private static final Pattern PATTERN_SECTION = Pattern.compile("Section|SECTION");
    private static final Pattern PATTERN_MINUSCULE = Pattern.compile("\\(*[a-z]\\)");
    private static final Pattern PATTERN_NIVEAU3_CHIFFRE = Pattern.compile("[0-9]\\.[0-9]\\.[0-9]");
    private static final Pattern PATTERN_NIVEAU2_CHIFFRE = Pattern.compile("([0-9]\\.[0-9])[\\s]|([0-9]\\.[0-9]\\.)[\\s]");
    private static final Pattern PATTERN_NIVEAU1_CHIFFRE_SPACE = Pattern.compile("([0-9]\\.)[\\s]");
    private static final Pattern PATTERN_NIVEAU1_CHIFFRE = Pattern.compile("([0-9]\\))");
    private static final Pattern PATTERN_CHIFFRE_ROMAIN = Pattern.compile("(II|III|IV|VI|VII|VIII|IX|XI|XII|XIII|XIV|XV|XVI|XVII|XVIII)\\)|(II|III|IV|VI|VII|VIII|IX|XI|XII|XIII|XIV|XV|XVI|XVII|XVIII)\\.");
    private static final Pattern PATTERN_MAJUSCULE = Pattern.compile("([A-Z]\\)|[A-Z]\\.)");

    private static final Pattern PATTERN_TOTO1 = Pattern.compile("\\((ii|iii|iv|vi|vii|viii|ix|xi|xii|xiii|xiv|xv|xvi|xvii|xviii)\\)");
    private static final Pattern PATTERN_TOTO2 = Pattern.compile("\\(([ivx])\\)");
    private static final Pattern PATTERN_TOTO3 = Pattern.compile("\\((ii|vi|xi)\\)");

    private static List<String> ouvrir(String chemin) throws IOException {
        List<String> lines = new ArrayList<>();
        try (BufferedReader br = Files.newBufferedReader(Paths.get(chemin))) {
            lines = br.lines().filter(line -> !line.trim().isEmpty()).collect(Collectors.toList());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lines;
    }


    //Etape 3: Recherche du motif identifier dans la fonction balisage si dessous, si elle troue alors retourne true -->
    private static boolean find(Pattern pattern, String chaine) {
        return pattern.matcher(chaine).find();
    }


    //Etape 3: Choix du motif � identifier ..... -->
    static List<Block> balisage(List<String> segments) {
        List<Block> blocsBalises = new ArrayList<>();
        int indice = 0;
        for (String segment : segments) {
            String prefix = segment.length() < 10 ? segment : segment.substring(0, 9);
            if (find(PATTERN_ARTICLE, prefix)) {
                blocsBalises.add(new Block(1, "", indice));
            } else if (find(PATTERN_SECTION, prefix)) {
                blocsBalises.add(new Block(2, "", indice));
            } else if (find(PATTERN_MINUSCULE, prefix)) {
                blocsBalises.add(new Block(3, prefix, indice));
            } else if (find(PATTERN_NIVEAU3_CHIFFRE, prefix)) {
                blocsBalises.add(new Block(4, "", indice));
            } else if (find(PATTERN_NIVEAU2_CHIFFRE, prefix)) {
                blocsBalises.add(new Block(5, "", indice));
            } else if (find(PATTERN_NIVEAU1_CHIFFRE_SPACE, prefix)) {
                blocsBalises.add(new Block(6, "", indice));
            } else if (find(PATTERN_NIVEAU1_CHIFFRE, prefix)) {
                blocsBalises.add(new Block(7, "", indice));
            } else if (find(PATTERN_CHIFFRE_ROMAIN, prefix)) {
                blocsBalises.add(new Block(8, "", indice));
            } else if (find(PATTERN_MAJUSCULE, prefix)) {
                blocsBalises.add(new Block(9, "", indice));
            }
            indice++;
        }
        return blocsBalises;
    }

    private static void algoLawgaTree(List<Block> blocsBalises) {
        LawgaNoeud root = LawgaNoeud.createRoot();

        boolean first = true;
        LawgaNoeud noeudCourant = null;
        for (Block block : blocsBalises) {
            LawgaNoeud noeudParent;
            if (first) noeudParent = root;
            else noeudParent = noeudCourant.findParentAvecMotif(block.getMotif());

            LawgaNoeud nouveauNoeud;
            if (first || motifExiste(noeudParent)) {
                nouveauNoeud = creerNoeudFrereOuPremierFils(block, noeudParent);
                noeudParent.addFils(nouveauNoeud);
            } else {
                nouveauNoeud = creerNoeudFils(noeudCourant, block);
                noeudCourant.addFils(nouveauNoeud);
            }
            noeudCourant = nouveauNoeud;
            first = false;
        }

        System.out.println(root.toString());
    }

    private static LawgaNoeud creerNoeudFils(LawgaNoeud noeudCourant, Block block) {
        int profondeur = noeudCourant.getProfondeur() + 1;
        block.setNiveau(profondeur);
        return new LawgaNoeud(block, noeudCourant, new ArrayList<>());
    }

    private static LawgaNoeud creerNoeudFrereOuPremierFils(Block block, LawgaNoeud noeudParent) {
        int profondeur = noeudParent.getProfondeur() + 1;
        block.setNiveau(profondeur);
        return new LawgaNoeud(block, noeudParent, new ArrayList<>());
    }

    private static boolean motifExiste(LawgaNoeud noeudParent) {
        return noeudParent != null;
    }

    private static List<Block> askniv2(List<Block> nivelage) {
        if (nivelage.isEmpty()) System.out.println("liste vide");
        int tmp = -1;
        int count = 0;

        // boucle termination du niveau 1
        for (Block block : nivelage) {
            if (block.getNiveau() == -1 && count == 0) {
                block.setNiveau(1);
                tmp = block.getMotif();
                count += 1;
            }
            if (count != 0 && tmp == block.getMotif()) {
                block.setNiveau(1);
            }
        }

        //intiation du tableau temp des niv
        List<Integer> listL = new ArrayList<>();
        for (Block block : nivelage) {
            if (block.getNiveau() == 1) {
                listL.clear();
                listL.add(block.getMotif());
                continue;
            }
            if (!listL.contains(block.getMotif())) {
                listL.add(block.getMotif());
            }
            block.setNiveau(listL.indexOf(block.getMotif()) + 1);
        }

        for (int i = 0; i < nivelage.size(); i++) {
            int niveau1 = nivelage.get(i).getNiveau();
            if (find(PATTERN_TOTO1, nivelage.get(i).getContenu())) {
                nivelage.get(i).setNiveau(niveau1 + 1);
            }
            if (find(PATTERN_TOTO2, nivelage.get(i).getContenu())) {
                if (find(PATTERN_TOTO3, nivelage.get(i + 1).getContenu())) {
                    nivelage.get(i).setNiveau(niveau1 + 1);
                }
            }
        }
        return nivelage;
    }

    private static List<Block> parser(List<String> segments, List<Block> blocks) {
        List<Block> resultBlock = new ArrayList<>(blocks);
        int j = 0, indiceD = 0, indiceF = 0;
        String contenu = "";
        for (j = 0; j < resultBlock.size(); j++) {
            indiceD = resultBlock.get(j).getIndice();
            if (j == resultBlock.size() - 1) {
                indiceF = segments.size() - 1;
            } else {
                indiceF = resultBlock.get(j + 1).getIndice();
            }
            for (int i = indiceD; i < indiceF; i++) {
                contenu += segments.get(i);
                resultBlock.get(j).setContenu(contenu);
            }
            contenu = "";
        }
        return resultBlock;

    }

    private static void export(List<Block> nivelage, String nomFichierResultat) {
        try (PrintWriter pw = new PrintWriter(nomFichierResultat, "UTF-8")) {
            nivelage.stream().map(block -> format("[@#$ %d] %s", block.getNiveau(), block.getContenu()))
                    .forEachOrdered(pw::println);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        try {
//            List<String> segments = ouvrir("D:\dev\projects\lawgarithm-parser\src\main\resources\contrat.txt");
            List<String> segments = ouvrir("D:\\dev\\projects\\lawgarithm-parser\\src\\main\\resources\\contrat-simple.txt");
            List<Block> blocks = balisage(segments);
//            blocks = askniv2(blocks);
            blocks = parser(segments, blocks);
            algoLawgaTree(blocks);
            export(blocks, "resultat.txt");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
