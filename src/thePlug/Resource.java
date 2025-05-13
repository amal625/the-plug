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
    protected double rate;
    private int numRatings = 0;

    /**
     * Costructor for Resource class, assigns all instance variables (for 3 optional attributes, checks if user entered something) upon creation of new resource
     */
    public Resource(String name, String school, String cost, String type, String genre, ArrayList<String> tags, String lastActive, String contact){
        this.name = name;
        this.school = school;
        this.cost = cost;
        this.type = type;
        this.genre = genre;
        this.contact = contact;
        this.removeRequests = removeRequests;
    

        // if (!tags.equals("None")){ //if the user does not add a tag before entering (null) or if the user does not want a tag == "none"
            this.tags = tags;
        // }
        // if (!lastActive.equals("None")){ //if the user does not add a last active before entering (null) or if the user does not want a last active date == "none"
            this.lastActive = lastActive;
        // }
        // if (!contact.equals("None")){ //if the user does not add a contact before entering (null) or if the user does not want a contact == "none"
            this.contact = contact;
        // }
        // if (removeRequests != 0){ //if the user does not add a contact before entering (null) or if the user does not want a contact == "none"
            this.removeRequests = removeRequests;
        // }
    }

    /**
     * Return the name for that specific resource
     * @return name for that resource 
     */
    public String getName(){
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
    public String getlastActive(){
        return this.lastActive;
    }

    /**
     * Return the contact for that specific resource
     * @return contact for that resource 
     */
    public String getContact(){
        return this.contact;
    }
    /**
     * will return the number of remove requests 
     * @return total removeRequests
     */
    public int getRemoveRequests(){
        return this.removeRequests;
    }
    /**
     * get the rating ( should we do an average here of all ratings)
     * @return rating
     */
    public double getRate(){
        if (numRatings == 0){
            return -1;
        }
        return rate/numRatings;
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

    /**
     * will allow the user to add a remove request
     * @param newRemoveRequests
     */
    public void setRemoveRequests(int newRemoveRequests){
        this.removeRequests = newRemoveRequests;
    }
    /**
     * will allow rating for the user
     * @param newRate
     */
    public void rate (float newRate){
        if (numRatings == 0){
            this.rate = newRate;
        }
        else{
            this.rate += newRate;
        }
        numRatings++;
    }
    
    public int compareTo(Resource a){
        return this.getName().compareTo(a.getName());
    }

    public int comparebyType(Resource a, Resource b){
        return a.type.compareTo(b.type);
    }

    public int comparebySchool(Resource a, Resource b){
        return a.school.compareTo(b.school);
    }
 
    
    public int comparebyTags(Resource a, Resource b){ //put a pin in it come back 
        for (int i = 0; i < Math.min(a.tags.size(), b.tags.size()); i++) { 
            int comparison = a.tags.get(i).compareTo(b.tags.get(i)); 
            if (comparison != 0) { 
                return comparison; 
            }
        }
        return Integer.compare(a.tags.size(), b.tags.size());
    }

    
    public String toString(){
        StringBuilder retString = new StringBuilder("");
        retString.append("Resource name: " + this.getName());
        retString.append("\nSchool : " + this.getSchool());
        retString.append("\nCost: " + this.getCost());
        retString.append("\nType: " + this.getType());
        retString.append("\nGenre: " + this.getGenre());
        
        if (this.getTags().size() != 0){
            retString.append("\nTags " + this.getTags());
        }
        if (!this.getlastActive().equals("None")){
            retString.append("\nLast Active: " + this.getTags());
        }
        if (this.getContact().equals("None")){
            retString.append("\nContact: " + this.getContact());
        }
        retString.append("\nRemove Requests: " + this.getRemoveRequests());
        
        if (this.getRate() == -1){
            retString.append("\nNo ratings collected for this resource");
        }
        else{
            retString.append("\nRate: " + this.getRate());
        }

        return retString.toString();

    }
}
