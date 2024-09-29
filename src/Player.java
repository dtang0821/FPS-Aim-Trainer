public class Player
{
    private double boughtPrice;
    public Player(double boughtPrice)
    {
        this.boughtPrice = boughtPrice;
    }
    public Player(){}

    public double getBoughtPrice()
    {
        return boughtPrice;
    }
    public void setBoughtPrice(int x)
    {
        boughtPrice = x;
    }
    public String toString()
    {
        return "boughtPrice: " + boughtPrice;
    }
    public boolean equals(Player x)
    {
        return x.getBoughtPrice() == boughtPrice;
    }

}