public class ExpertPlayer extends Player {
    double growthFactor;
    public ExpertPlayer(double boughtPrice, double growthFactor)
    {
        super(boughtPrice);
        this.growthFactor = growthFactor;
    }
    public String toString()
    {
        return "growthFactor: " + growthFactor;
    }
    //returns if the two growth factors are equal
    public boolean equals(ExpertPlayer x)
    {
        return x.equals(x);
    }
}