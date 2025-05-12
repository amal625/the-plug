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


    /**
     *Constructor initializes all data structures & loads resources
     */
    public PlugManager(){
        resourcesByName = new ResourceBST();
        resourcesByTag = new ResourceBST();

        categorizedResources = new ResourceArrayList<>(5); // intialize with categories: school, type, genre, ect.
        intializeCategories();
        loadResources();
    }

    /**
     *initialize cateogry lists  in the ResourceArrayList 
     */
    private void initializeCategories(){
        int schoolsIndex = categorizedResources.createNewList(5);// schools category (Pomona, CMC, Scripps,ect.)
        
        int typesIndex = categorizedResources.createNewList(5); //types of category (item,service,event,ect.)
        
        int genresIndex = categorizedResources.createNewList(5); //genres category (mutual aid, academic,ect.)
        
        int costIndex = categorizedResources.createNewList(5); //cost category(free,reduced fare,ect.)
        
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
