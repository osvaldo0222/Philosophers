package logical;

public class InitConfig {
    private int NPhil;
    private int SEating;
    private int SHungry;
    private int SThinking;

    public InitConfig(int NPhil, int SEating, int SHungry, int SThinking) {
        this.NPhil = NPhil;
        this.SEating = SEating;
        this.SHungry = SHungry;
        this.SThinking = SThinking;
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
}
