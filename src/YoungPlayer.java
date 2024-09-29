public class YoungPlayer extends Player {
    double growthFactor;
    public YoungPlayer(double boughtPrice, double growthFactor)
    {
        super(boughtPrice);
        this.growthFactor = growthFactor;
    }
    public String toString()
    {
        return "growthFactor: " + growthFactor;
    }
    //returns if the two growth factors are equal
    public boolean equals(YoungPlayer x)
    {
        return x.equals(x);
    }
}