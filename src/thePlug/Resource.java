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
    protected int removeRequests;
    protected float rate;

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

    /**
     * Return the name for that specific resource
     * @return name for that resource 
     */
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
     * Return the cost for that specific resource
     * @return cost for that resource 
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
    public ArrayList<String> getTags(){
        return this.tags;
    }

    /**
     * Return the last Active for that specific resource
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
    
    
    public void setName(String newName){
        this.name = newName;
    }
    /**
     * setting a cost by the user 
     */
    public void setCost(String newCost){
        this.cost = newCost;
    }
    /**
     * user sets the lastActive date
     */
    public void setLastActive(String newLastActive){
        this.lastActive = newLastActive;
    }

    /**
     * user sets
     */
    public void setContact(String newContact){
        this.contact = newContact;
    }

    
    public int comparebyName(Resource a, Resource b){
        return a.name.compareTo(b.name);
    }

    public int comparebyType(Resource a, Resource b){
        return a.type.compareTo(b.type);
    }

    public int comparebySchool(Resource a, Resource b){
        return a.school.compareTo(b.school);
    }
 
    
    public int comparebyTags(Resource a, Resource b){
        for (int i = 0; i < Math.min(a.tags.size(), b.tags.size()); i++) { 
            int comparison = a.tags.get(i).compareTo(b.tags.get(i)); 
            if (comparison != 0) { 
                return comparison; 
            }
        }
        return Integer.compare(a.tags.size(), b.tags.size());
    }
    

    // gives an error w/o this; can we get rid of it???
    @Override
    public int compareTo(Resource o) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'compareTo'");
    }
    

    




}
