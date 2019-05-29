package advancedaıp2;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class AdvancedAIP2{
    
    public static void main(String[] args) {
        
        initializePop("/CSE560Data/uruguay.txt");
        
    }
    
    public static void initializePop(String path){
        
        ArrayList<Point> points = new ArrayList<>();
        ArrayList<ArrayList<Point>> initialPop = new ArrayList<>();
        
        List<String> data = readData(path);
        
        for(String line : data){
            
            points.add(new Point(Double.parseDouble(line.split(" ")[1]), Double.parseDouble(line.split(" ")[2]), Integer.parseInt(line.split(" ")[0])));
            
        }
        
        for(Point start : points){
            
            ArrayList<Point> firstPath = new ArrayList<>();
            firstPath.add(start);
            
            ArrayList<Point> temp = new ArrayList<>();
            temp.addAll(points);
            temp.remove(firstPath.get(0));
            while(!temp.isEmpty()){
                
                firstPath.add(temp.remove((int)(Math.random() * temp.size())));
                
            }
            
            initialPop.add(firstPath);
            
        }
        
        passTime(initialPop);
        
    }
    
    public static void passTime(ArrayList<ArrayList<Point>> currentPop){
        
        double currentBestPathCost = fitness(currentPop.get(0));
        int popNum = currentPop.size();
        ArrayList<Point> currentBestPath = new ArrayList<>();
        ArrayList<Double> fitnesses = new ArrayList<>();
        while(true){
            
            currentPop = createNewGeneration(currentPop);
            for(ArrayList<Point> path : currentPop){
                fitnesses.add(fitness(path));
            }
            
            Collections.sort(fitnesses);
            if(currentBestPathCost > fitnesses.get(0)){
                currentBestPathCost = fitnesses.get(0);
                
                for(ArrayList<Point> path : currentPop){
                    if(fitness(path) == currentBestPathCost){
                        currentBestPath = path;
                    }
                }
                
            }
            
//            for(Point point : currentBestPath){
//                System.out.print(point.getIndex() + " -> ");
//            }
//            System.out.println("end");
            System.out.println(currentBestPathCost);
            System.out.println();
            
            ArrayList<ArrayList<Point>> removedOnes = new ArrayList<>();
            for(int x = 0; x < currentPop.size(); x++){
                
                if(fitness(currentPop.get(x)) > fitnesses.get(popNum - 1)){
                    removedOnes.add(currentPop.get(x));
                }
                
            }
            
            currentPop.removeAll(removedOnes);
            while(currentPop.size() != popNum){
                currentPop.remove(currentPop.size() - 1);
            }
            fitnesses.clear();
            
        }
        
    }
    
    public static ArrayList<ArrayList<Point>> createNewGeneration(ArrayList<ArrayList<Point>> currentPop){
        
        int popNum = currentPop.size();
        for(int x = 0; x < (popNum + 1) / 2; x++){
            
            int[] parents = selectParents(currentPop);
            currentPop.addAll(crossOver(currentPop.get(parents[0]), currentPop.get(parents[1])));
            
        }
        
        return currentPop;
        
    }
    
    public static ArrayList<ArrayList<Point>> crossOver(ArrayList<Point> p1, ArrayList<Point> p2){
        
        ArrayList<ArrayList<Point>> children = new ArrayList<>();
        
        ArrayList<Point> child1 = new ArrayList<>();
        ArrayList<Point> child2 = new ArrayList<>();
        
        int lowLim = (int)(Math.random() * p1.size() - 1);
        int upLim = (int)(Math.random() * p1.size());
        while(lowLim >= upLim){
            upLim = (int)(Math.random() * p1.size());
        }
        
        child1.addAll(p1.subList(lowLim, upLim));
        
        int index = 0;
        for(Point point : p2){
            
            if(child1.indexOf(point) < 0 && index < lowLim){
                child1.add(index ,point);
                index++;                
            }else if(child1.indexOf(point) < 0 && index == lowLim){
                child1.add(point);
            }
            
        }
        
        child2.addAll(p2.subList(lowLim, upLim));
        
        index = 0;
        for(Point point : p1){
            
            if(child2.indexOf(point) < 0 && index < lowLim){
                child2.add(index ,point);
                index++;
            }else if(child2.indexOf(point) < 0 && index == lowLim){
                child2.add(point);
            }
            
        }
        
        if(Math.random() < 0.01){
            Mutation(child1);
        }
        
        if(Math.random() < 0.01){
            Mutation(child2);
        }
        
        children.add(child1);
        children.add(child2);
        
        return children;
        
    }
    
    public static boolean contains(Point parentPoint, ArrayList<Point> child){
        
        for(Point point : child){
            
            if(point == null){
                continue;
            }
            
            if(parentPoint.equals(point)){
                return true;
            }
            
        }
        
        return false;
        
    }
    
    public static int[] selectParents(ArrayList<ArrayList<Point>> currentPop){
        
        int[] parents = new int[2];
        
        ArrayList<Double> probs = new ArrayList<>();
        double sum = 0;
        
        for(ArrayList<Point> path : currentPop){
            sum += fitness(path);
        }
        
        probs.add(0.0);
        for(int x = 0; x < currentPop.size(); x++){
            probs.add(probs.get(x) + (fitness(currentPop.get(x)) / sum));
        }
        
        double p1 = Math.random();
        
        for(int x = 0; x < probs.size() - 1; x++){
            
            if(p1 >= probs.get(x) && p1 <= probs.get(x + 1)){
                parents[0] = x;
            }
            
        }
        
        do{
            
            double p2 = Math.random();
            for(int x = 0; x < probs.size() - 1; x++){
                
                if(p2 >= probs.get(x) && p2 <= probs.get(x + 1)){
                    parents[1] = x;
                }
                
            }
            
        }while(parents[0] == parents[1]);
        
        return parents;
                
    }
    
    public static ArrayList<Point> Mutation(ArrayList<Point> path){
        
        int x = (int)(Math.random() * path.size());
        int y = (int)(Math.random() * path.size());
        while(x == y){
            y = (int)(Math.random() * path.size());
        }
        
        Collections.swap(path, x, y);
        
        return path;
        
    }
    
    public static double fitness(ArrayList<Point> path){
        
        double sum = 0;
        
        for(int x = 0; x < path.size() - 1; x++){
            
            sum += path.get(x).distance(path.get(x + 1));
            
        }
        
        return sum;
        
    }
    
    public static List<String> readData(String path){
        
        List<String> data = new ArrayList<>();
        try {
            
            Stream<String> stream = new BufferedReader(new InputStreamReader(AdvancedAIP2.class.getResourceAsStream(path))).lines();
            data = stream.collect(Collectors.toList());
            //Stream<String> stream = Files.lines(Paths.get(path));
            //stream.forEach(System.out::println);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return data;
        
    }
    
}
