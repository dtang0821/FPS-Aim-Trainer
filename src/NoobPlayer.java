public class NoobPlayer extends Player {
    double growthFactor;
    public NoobPlayer(double boughtPrice, double growthFactor)
    {
        super(boughtPrice);
        this.growthFactor = growthFactor;
    }
    @Override
    public double getBoughtPrice()
    {
        return super.getBoughtPrice();
    }
    public String toString()
    {
        return "growthFactor: " + growthFactor;
    }
    //returns if the two growth factors are equal
    public boolean equals(NoobPlayer x)
    {
        return x.equals(x);
    }
}