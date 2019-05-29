package advancedaıp2;

public class Point{
    
    private final double x;
    private final double y;
    private final int index;
    
    public Point(double x, double y, int index){
        
        this.x = x;
        this.y = y;
        this.index = index;
        
    }
    
    public double distance(Point point){
        
        return Math.sqrt((Math.pow(x - point.getX(), 2)) + (Math.pow(y - point.getY(), 2)));
        
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public int getIndex() {
        return index;
    }
    
}
