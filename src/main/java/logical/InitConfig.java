package logical;

public class InitConfig {
    private int NPhil;
    private String SEating;
    private String SHungry;
    private String SThinking;
    private int TThinkig;
    private int TEating;

    public InitConfig(int NPhil, String SEating, String SHungry, String SThinking, int TThinkig, int TEating) {
        this.NPhil = NPhil;
        this.SEating = SEating;
        this.SHungry = SHungry;
        this.SThinking = SThinking;
        this.TThinkig = TThinkig;
        this.TEating = TEating;
    }

    public int getNPhil() {
        return NPhil;
    }

    public void setNPhil(int NPhil) {
        this.NPhil = NPhil;
    }

    public String getSEating() {
        return SEating;
    }

    public void setSEating(String SEating) {
        this.SEating = SEating;
    }

    public String getSHungry() {
        return SHungry;
    }

    public void setSHungry(String SHungry) {
        this.SHungry = SHungry;
    }

    public String getSThinking() {
        return SThinking;
    }

    public void setSThinking(String SThinking) {
        this.SThinking = SThinking;
    }

    public int getTThinkig() {
        return TThinkig;
    }

    public void setTThinkig(int TThinkig) {
        this.TThinkig = TThinkig;
    }

    public int getTEating() {
        return TEating;
    }

    public void setTEating(int TEating) {
        this.TEating = TEating;
    }
}
