package thePlug;

import java.util.ArrayList;
import java.util.Comparator;


/**
 * Resource class defines a new Resource consisting of a name, schooll, cost, type, genre, tags, lastActive, and contact
 */
public class Resource implements Comparable<Resource>{
    protected String name;
    protected String school;
    protected String cost;
    protected String type;
    protected String genre;
    protected ArrayList<String> tags;
    protected String lastActive;
    protected String contact;

    /**
     * Costructor for Resource class, assigns all instance variables (for 3 optional attributes, checks if user entered something) upon creation of new resource
     */
    public Resource(String name, String school, String cost, String type, String genre, ArrayList<String> tags, String lastActive, String contact){
        this.name = name;
        this.school = school;
        this.cost = cost;
        this.type = type;
        this.genre = genre;
        
        if (!tags.equals("None")){ //if the user does not add a tag before entering (null) or if the user does not want a tag == "none"
            this.tags = tags;
        }
        if (!lastActive.equals("None")){ //if the user does not add a last active before entering (null) or if the user does not want a last active date == "none"
            this.lastActive = lastActive;
        }
        if (!contact.equals("None")){ //if the user does not add a contact before entering (null) or if the user does not want a contact == "none"
            this.contact = contact;
        }
    }
    
    public String name(){
        return this.name;
    }
    
    /**
     * Return the school for that specific resource
     * @return school for that resource 
     */
    public String getSchool(){
        return this.school;
    }
    
    /**
     * 
     * 
     */
    public String getCost(){
        return this.cost;
        
    }
    
    /**
     * Return the name for that specific resource
     * @return name for that resource 
     */
    public String getType(){
        return this.type;
 
    }
    /**
     * Return the genre for that specific resource
     * @return genre for that resource 
     */
    public String getGenre(){
        return this.genre;
    }
    /**
     * Return the tags for that specific resource
     * @return tags for that resource 
     */
    public String getTags(){
        return this.tags;
    }
    /**
     * Return the last Active  for that specific resource
     * @return last Active for that resource 
     */
    public String getlastActve(){
        return this.lastActive;
    
    }
    /**
     * Return the contact for that specific resource
     * @return contact for that resource 
     */
    public String getContact(){
        return this.contact;
    
    }
    



    


    

    public int compareTo(Resource that){
        return 0;
    }




    
}
