package thePlug;

import java.util.ArrayList;
import java.util.Comparator;

public class Resource implements Comparable<Resource>{
    protected String school;
    protected String cost;
    protected String type;
    protected String genre;
    protected ArrayList<String> tags;
    protected String lastActive;
    protected String contact;

    public Resource(String school, String cost, String type, String genre, ArrayList<String> tags, String lastActive ){
        this.school = school;
        this.cost = cost;
        this.type = type;
        this.genre = genre;
        
        if (!tags.equals("None")){

        }

        
    }

    

    public int compareTo(Resource that){
        return 0;
    }




    
}
