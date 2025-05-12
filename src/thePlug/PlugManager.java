package thePlug;
import java.util.*;
import java.io.*;


/**
 * Plug Manager class manages all resource data and implements the four main features 
 * upload,remove,search,and rate resources.
 */
public class PlugManager {
    private ResourceBST resourcesByName; // BST w/ resource names as keys
    private ResourceBST resourcesByTag; // BST to search by tags
    private ResourceArrayList <Resource> categorizedResources; // nested arraylists for organization by category
    private static final int removalCount = 5; // Number of removal requests before resource is removed
    private static final String resourceFile = "iforgotwhatwecalledit.csv"; // file storing all resources
    private static final int SCHOOL_INDEX = 0;
    private static final int COST_INDEX = 1;
    private static final int GENRE_INDEX = 2;
    private static final int TYPE_INDEX = 3;



    /**
     *Constructor initializes all data structures & loads resources
     */
    public PlugManager(){
        resourcesByName = new ResourceBST();
        resourcesByTag = new ResourceBST();

        categorizedResources = new ResourceArrayList<>(4); // intialize with categories: school, type, genre, ect.
        intializeCategories();
        loadResources();
    }

    /**
     *initialize cateogry lists in the ResourceArrayList 
     */
    private void initializeCategories(){
        categorizedResources.addToList(SCHOOL_INDEX,new ResourceArrayList<>(6));// schools category (Pomona, CMC, Scripps,ect.)
        
        categorizedResources.addToList(COST_INDEX,new ResourceArrayList<>(5));; //cost category(free,reduced fare,ect.)

        categorizedResources.addToList(TYPE_INDEX, new ResourceArrayList<>(7)); //types of category (item,service,event,ect.)
        
        categorizedResources.addToList(GENRE_INDEX, new ResourceArrayList<>(12)); //genres category (mutual aid, academic,ect.)
        
        
        initializeSchools(schoolsIndex); //add subcategories to each category
        initilaizeTypes(typesIndex);
        initializeGenres(genresIndex);
        intializeCosts(costIndex);


    }
    /**
     * helper methods to initalize subcategories    
     * 
     */
    public void initializeSchools(int index){
        String[] schools = {"Pomona", "Claremont Mckenna", "Harvey Mudd", "Scripps", "Pitzer"};
        for (String school : schools){
            ResourceArrayList<Resource> schoolResourceList = new ResourceArrayList <>(10);
            categorizedResources.getList(index).add(categorizedResources.getList(index).size(), schoolResourceList);
        }
    }


    
}
