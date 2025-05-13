package thePlug;
import java.util.*;
import java.util.ArrayList;
import java.io.*;



/**
 * Plug Manager class manages all resource data and implements the four main features 
 * upload,remove,search,and rate resources.
 */
public class PlugManager {
    private ResourceBST resourcesByName; // BST w/ resource names as keys
    private ResourceBST resourcesByTag; // BST to search by tags
    private ResourceArrayList <Resource> categorizedResources; // nested arraylists for organization by category
    private ArrayList<ArrayList<ArrayList<Resource>>> categoriesResources = new ArrayList<>(4);
    private static final int removalCount = 5; // Number of removal requests before resource is removed
    private static final String resourceFile = "src/thePlug/Plug_Data_CSV.csv"; // file storing all resources
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
        ArrayList<ArrayList> categoriesResources = new ArrayList<ArrayList>(4);
        initializeCategories();
        // loadResources();

    }

    /**
     *initialize cateogry lists in the ResourceArrayList 
     */
    private void initializeCategories(){
        // categorizedResources.addToList(SCHOOL_INDEX,new ResourceArrayList<>(6));// schools category (Pomona, CMC, Scripps,ect.)
        
        // categorizedResources.addToList(COST_INDEX,new ResourceArrayList<>(5));; //cost category(free,reduced fare,ect.)

        // categorizedResources.addToList(TYPE_INDEX, new ResourceArrayList<>(7)); //types of category (item,service,event,ect.)
        
        // categorizedResources.addToList(GENRE_INDEX, new ResourceArrayList<>(12)); //genres category (mutual aid, academic,ect.)
        
        
        // initializeSchools(SCHOOL_INDEX); //add subcategories to each category
        // initilaizeTypes(TYPE_INDEX);
        // initializeGenres(GENRE_INDEX);
        // intializeCosts(COST_INDEX);
        /////////////////////// 

        categoriesResources.add(new ArrayList<ArrayList<Resource>>(6));// schools category (Pomona, CMC, Scripps,ect.)
        
        categoriesResources.add(new ArrayList<ArrayList<Resource>>(5)); //cost category(free,reduced fare,ect.)

        categoriesResources.add(new ArrayList<ArrayList<Resource>>(7)); //types of category (item,service,event,ect.)
        
        categoriesResources.add(new ArrayList<ArrayList<Resource>>(12)); //genres category (mutual aid, academic,ect.)
        
        
        initializeEverything(); //adds empty arraylists to each subcategory

    }
    /**
     * helper methods to initalize subcategories    
     * 
     */
    public void initializeEverything(){
        // String[] schools = {"Pomona", "Claremont Mckenna", "Harvey Mudd", "Scripps", "Pitzer", "7C"};
        // for (String school : schools){
        //     ResourceArrayList<Resource> schoolResourceList = new ResourceArrayList <>(10);
        //     categorizedResources.getList(index).add(categorizedResources.getList(index).size(), schoolResourceList);
        // }
        // /////// 

        int[] numsWithin = {6, 5, 7, 12};
        for (int i = 0; i<4; i++){
            for (int j = 0; j < numsWithin[i]; j++){
                categoriesResources.get(i).add(new ArrayList<Resource>()); // when j = 0, added empty aL for Pomona
            }
        }
        
    }
    
    /**
     * Load resources from CSV file
     */
    private void loadResources() {
        try{
            BufferedReader reader = new BufferedReader (new FileReader (resourceFile));
            String line; 
            reader.readLine(); // skips first line w/titles of columns

            while((line = reader.readLine()) != null){
                System.out.println(line);
                String[] resource = line.split(","); 
                String name = resource[0];
                String school = resource[1];
                String cost = resource[2];
                String type = resource[3];
                String genre = resource[4];

                // ArrayList<String> tag = Arrays.asList(resource[5].split(";"));
                // List<String> tags = Arrays.asList(resource[5].split(";"));

                // String[] tags = resource[7].split(",");
                // genderinclusive, AAMP,
                //lastActive
                // contact,,fli

                 String contact = resource[5];
                  if (contact.equals("")){
                    contact = "None";
                }

                String lastActive = resource[6];
                if (lastActive.equals("")){
                    lastActive = "None";
                }
                
                ArrayList<String> tags = new ArrayList<>();
                if (resource.length >= 7 && resource[7] != null) { 
                    for (String tag : resource[7].split(";")) {
                        if (!tag.trim().isEmpty()) {
                            tags.add(tag.trim());
                        }    
                    }
                }

                Resource resourceObject = new Resource(name, school, cost, type, genre, tags, lastActive, contact);
                //add to BST :) 
                resourcesByName.insert(resourceObject);
                
                for (String tag : tags){ //comeback!!!
                    resourcesByTag.insert(resourceObject);
                }


                // add to school bucket
                ArrayList school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", " Pitzer", "Pomona", "Scripps", "7C"));
                if (school_names.indexOf(name) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(SCHOOL_INDEX).get(school_names.indexOf(name)).add(resourceObject);

                // add to cost bucket
                ArrayList cost_names = new ArrayList<>(Arrays.asList("free", "1-10", "10-20", "20-30", "30+"));
                if (cost_names.indexOf(cost) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(COST_INDEX).get(cost_names.indexOf(cost)).add(resourceObject);


                // add to cost bucket
                ArrayList type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
                if (type_names.indexOf(cost) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(TYPE_INDEX).get(type_names.indexOf(type)).add(resourceObject);

                // add to genre bucket
                ArrayList genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" ));
                if (genre_names.indexOf(cost) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(GENRE_INDEX).get(genre_names.indexOf(genre)).add(resourceObject);

                
            }

        }catch(IOException e){
            System.out.println("No existing resources file found. Starting with empty database.");

        }

    }

    /**
     * add a new resource 
     */
    public boolean addResource(Resource resource){
        if (resourcesByName.search(resource.getName()) != null) {
            return false; // Already exists
        }
        else{
            // insert to main BST
            resourcesByName.insert(resource);

            for(String tag: resource.getTags()){
                resourcesByTag.insert(resource);
            }

            String school = resource.getSchool();
            String cost = resource.getCost();
            String type = resource.getType();
            String genre = resource.getGenre();

            // add to school bucket
            ArrayList school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", " Pitzer", "Pomona", "Scripps", "7C"));
            categoriesResources.get(SCHOOL_INDEX).get(school_names.indexOf(school)).add(resource);

            // add to cost bucket
            ArrayList cost_names = new ArrayList<>(Arrays.asList("free", "1-10", "10-20", "20-30", "30+"));
            categoriesResources.get(COST_INDEX).get(cost_names.indexOf(cost)).add(resource);

            // add to type bucket
            ArrayList type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
            categoriesResources.get(TYPE_INDEX).get(type_names.indexOf(type)).add(resource);
            
            // add to genre bucket
            ArrayList genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" ));
            categoriesResources.get(GENRE_INDEX).get(genre_names.indexOf(genre)).add(resource);
        }
        return true;

    }

    /**
     * 
     */
    public void userAddResource(Resource resource){
        Scanner scanner = new Scanner(System.in);
    }


    public static void main(String[] args){
        PlugManager plugManager = new PlugManager();
        plugManager.loadResources();
        System.out.println(plugManager.categorizedResources);
    }








    
}
