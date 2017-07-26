package lawgaparser;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class LawgaNoeud {

    private Block block = null;
    private LawgaNoeud parent = null;
    private List<LawgaNoeud> fils;

    public LawgaNoeud(Block block, LawgaNoeud parent, List<LawgaNoeud> fils) {
        this.block = block;
        this.parent = parent;
        this.fils = fils;
    }

    public void addFils(LawgaNoeud fils) {
        if (this.fils == null) {
            this.fils = new ArrayList<>();
        }
        this.fils.add(fils);
    }

    public static LawgaNoeud createRoot() {
        return new LawgaNoeud(null, null, new ArrayList<>());
    }

    public LawgaNoeud findParentAvecMotif(int motif) {
        return findParentAvecMotifDepuisNoeud(this, motif);
    }

    private LawgaNoeud findParentAvecMotifDepuisNoeud(LawgaNoeud noeud, int motif) {
        if (noeud.getParent() == null) return null;
        if (noeud.getBlock().getMotif() == motif) return noeud.getParent();
        else return findParentAvecMotifDepuisNoeud(noeud.getParent(), motif);
    }

    public int getProfondeur() {
        return getProfondeurDepuisNoeud(this, 0);
    }

    private int getProfondeurDepuisNoeud(LawgaNoeud noeud, int profondeur) {
        if (noeud.getParent() == null) return profondeur;
        else return getProfondeurDepuisNoeud(noeud.getParent(), profondeur + 1);
    }

    public Block getBlock() {
        return block;
    }

    public LawgaNoeud getParent() {
        return parent;
    }

    public List<LawgaNoeud> getFils() {
        return fils;
    }

    public String toString() {
        String strBlock = block == null ? "" : block.toString();
        String strFils = String.join(",", fils.stream().map(LawgaNoeud::toString).collect(Collectors.toList()));
        return "{'block':" + strBlock + ",'fils':[" + strFils + "]}";
    }


}
