package logical;

public class InitConfig {
    private int NPhil;
    private int SEating;
    private int SHungry;
    private int SThinking;
    private int TThinkig;
    private int TEating;

    public InitConfig(int NPhil, int SEating, int SHungry, int SThinking, int TThinkig, int TEating) {
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

    public int getSEating() {
        return SEating;
    }

    public void setSEating(int SEating) {
        this.SEating = SEating;
    }

    public int getSHungry() {
        return SHungry;
    }

    public void setSHungry(int SHungry) {
        this.SHungry = SHungry;
    }

    public int getSThinking() {
        return SThinking;
    }

    public void setSThinking(int SThinking) {
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
