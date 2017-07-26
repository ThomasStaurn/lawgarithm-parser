package lawgaparser;

class Block {
    private int motif;
    private String prefix;
    private String contenu;
    private int niveau;
    private int indice;

    @Override
    public String toString() {
        return "{'motif':'" + motif + "', 'prefix':'"+prefix+", 'contenu':'" + contenu + "', 'niveau':'" + niveau + "', 'indice':'" + indice + "'}";
    }

    Block(int motif, String contenu, int indice) {
    	this(motif,contenu,-1,indice);
    }

    Block(int motif, String contenu, int niveau, int indice) {
        this.motif = motif;
        this.contenu = contenu;
        this.niveau = niveau;
        this.indice = indice;
    }
    
    Block(int motif, String prefix, String contenu, int indice) {
        this.motif = motif;
        this.prefix = prefix;
        this.contenu = contenu;
        this.niveau = -1;
        this.indice = indice;
    }

    public int getMotif() {
        return motif;
    }

    public void setMotif(int motif) {
        this.motif = motif;
    }
    
    public String getPrefix(){
    	return prefix;
    }

    public String getContenu() {
        return contenu;
    }

    public void setContenu(String contenu) {
        this.contenu = contenu;
    }

    public int getNiveau() {
        return niveau;
    }

    public void setNiveau(int niveau) {
        this.niveau = niveau;
    }

    public int getIndice() {
        return indice;
    }

    public void setIndice(int indice) {
        this.indice = indice;
    }

}
