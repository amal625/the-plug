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
    private static final int TYPE_INDEX = 2;
    private static final int GENRE_INDEX = 3;

    

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

        // categorizedResources.addToList(TYPE_INDEX, new ResourceArrayList<>(7)); //types of category (item,Service,Event,ect.)
        
        // categorizedResources.addToList(GENRE_INDEX, new ResourceArrayList<>(12)); //genres category (Mutual Aid, Academic,ect.)
        
        
        // initializeSchools(SCHOOL_INDEX); //add subcategories to each category
        // initilaizeTypes(TYPE_INDEX);
        // initializeGenres(GENRE_INDEX);
        // intializeCosts(COST_INDEX);
        /////////////////////// 

        categoriesResources.add(new ArrayList<ArrayList<Resource>>(6));// schools category (Pomona, CMC, Scripps,ect.)
        
        categoriesResources.add(new ArrayList<ArrayList<Resource>>(5)); //cost category(free,reduced fare,ect.)

        categoriesResources.add(new ArrayList<ArrayList<Resource>>(7)); //types of category (item,Service,Event,ect.)
        
        categoriesResources.add(new ArrayList<ArrayList<Resource>>(12)); //genres category (Mutual Aid, Academic,ect.)
        
        
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
                String[] resource = line.split(","); 

                // International Grant Fund,Pomona,Free,Grant,Healthcare and Wellness,tzocakkwhv@pomona.edu,,
                // [International Grant Fund, Pomona, Free, Grant, Healthcare and Wellness, tzocakkwhv@pomona.edu]
                String threeSubstr = line.substring(line.length()-3, line.length());
                String twoSubstr = line.substring(line.length()-2, line.length());
                String[] oldResource = resource;
                if (threeSubstr.length() == 3 && threeSubstr.equals(",,,")){
                    resource = new String[resource.length + 2];
                    for (int i = 0; i<oldResource.length; i++){
                        resource[i] = oldResource[i];
                    }
                    resource[oldResource.length] = "";
                    resource[oldResource.length + 1] = "";

                } else if (twoSubstr.length() == 2 && twoSubstr.equals(",,")){
                    resource = new String[resource.length + 1];
                    for (int i = 0; i<oldResource.length; i++){
                        resource[i] = oldResource[i];
                    }
                    resource[oldResource.length] = "";
                }

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

                // Book Room,Pomona,Free,Item,Academic,zztofpzgjv@pomona.edu,,
                // [Book Room, Pomona, Free, Item, Academic, zztofpzgjv@pomona.edu,,AAMP]

                String lastActive = resource[6];
                if (lastActive.equals("")){
                    lastActive = "None";
                }
                
                ArrayList<String> tags = new ArrayList<>();
                if (resource.length > 7 && resource[7] != null) { 
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
                ArrayList school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7c"));
                if (school_names.indexOf(school) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(SCHOOL_INDEX).get(school_names.indexOf(school)).add(resourceObject);

                // add to cost bucket
                ArrayList cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
                if (cost_names.indexOf(cost) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(COST_INDEX).get(cost_names.indexOf(cost)).add(resourceObject);


                // add to cost bucket
                ArrayList type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
                if (type_names.indexOf(type) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(TYPE_INDEX).get(type_names.indexOf(type)).add(resourceObject);

                // add to genre bucket
                ArrayList genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainability", "Transportation" ));
                if (genre_names.indexOf(genre) == -1){
                    throw new Error("invalid input");
                }
                categoriesResources.get(GENRE_INDEX).get(genre_names.indexOf(genre)).add(resourceObject);
                
            }

        }catch(IOException e){
            System.out.println("No existing resources file found. Starting with empty database.");

        }

    }

    // /**
    //  * feature 1: upload a resource
    //  */
        
    
    // /**
    //  * add a new resource 
    //  */
    // public boolean addResource(Resource resource){
    //     if (resourcesByName.search(resource.getName()) != null) {
    //         return false; // Already exists
    //     }
    //     else{
    //         // insert to main BST
    //         resourcesByName.insert(resource);

    //         for(String tag: resource.getTags()){
    //             resourcesByTag.insert(resource);
    //         }

    //         String school = resource.getSchool();
    //         String cost = resource.getCost();
    //         String type = resource.getType();
    //         String genre = resource.getGenre();

    //         // add to school bucket
           
    //         ArrayList school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", " Pitzer", "Pomona", "Scripps", "7C"));    
    //         categoriesResources.get(SCHOOL_INDEX).get(school_names.indexOf(school)).add(resource);
           

    //         // add to cost bucket
    //         ArrayList cost_names = new ArrayList<>(Arrays.asList("free", "1-10", "10-20", "20-30", "30+"));
    //         categoriesResources.get(COST_INDEX).get(cost_names.indexOf(cost)).add(resource);

    //         // add to type bucket
    //         ArrayList type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
    //         categoriesResources.get(TYPE_INDEX).get(type_names.indexOf(type)).add(resource);
            
    //         // add to genre bucket
    //         ArrayList genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" ));
    //         categoriesResources.get(GENRE_INDEX).get(genre_names.indexOf(genre)).add(resource);
    //     }
    //     return true;

    // }


    // /**
    //  * 
    //  */
    // public void userAddResource(Resource resource){
    //     Scanner scanner = new Scanner(System.in);

    //     // get users resource naame
    //     System.out.println("Enter the name of the resource: ");
    //     String name = scanner.nextLine();

    //     // outline list of school optios 
    //     String [] schools = {"Claremont Mckenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7C"};
    //     System.out.println("Seletct the school: ");
    //     for (int  i = 0; i < schools.length; i++){
    //          System.out.println((i + 1) + ". " + schools[i]);
    //     }
    //     int schoolIndex = getValidatedIndex(scanner,schools.length);
    //     String school = schools[schoolIndex];

    //     // outline list of cost options
    //     String[] costs = {"free", "1-10", "10-20", "20-30", "30+"};
    //     System.out.println("Select the cost");
    //     for (int  i = 0; i < costs.length; i++){
    //          System.out.println((i + 1) + ". " + costs[i]);
    //     }
    //     int costIndex = getValidatedIndex(scanner, costs.length);
    //     String cost = costs [costIndex];

    //     // outline list of type options
    //     String[] types = {"Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service" };
    //     System.out.println("Select the type: ");
    //     for (int i = 0; i < types.length; i++){
    //         System.out.println((i + 1) + ". " + types[i]);
    //     }
    //     int typeIndex = getValidatedIndex(scanner, types.length);
    //     String type = types[typeIndex];

    //     // outline list of genre options
    //     String[] genres = {"Academic", "Career", "Entertainment", "Food","Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" };
    //     System.out.println("Select the genre: ");
    //     for (int i = 0; i < genres.length; i++){
    //         System.out.println((i + 1) + ". " + genres[i]);
    //     }
    //     int genreIndex = getValidatedIndex(scanner, genres.length);
    //     String genre = genres[genreIndex];

    //     // get user to input tags seprated by commas
    //     System.out.println("Enter tags (comma separated, e.x., fli, AAMP, disability resources): ");
    //     String tagInput = scanner.nextLine();

    //     // create reosurce object w user collected input
    //     ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagInput.split(",")));
    //     Resource r2 = new Resource("Berry", "Claremont", "free","item", "Mutual Aid", new ArrayList<String>(Arrays.asList("a","b")), "None","None");
  
    //     // Try to add the resource to the system
    //     boolean success = addResource(newResource);

    //     // give user feedback of their upload attempt
    //     if (success){
    //         System.out.println("Resource added successfully!");
    //     } else {
    //         System.out.println("Resource not added :(");
    //     }
        
    // }
    
    // /**
    //  * Prompts the user to enter a number between 1 and max, and keeps asking until they do.
    //  * Returns the selected index as a 0-based value for use with arrays.
    //  */
    // private int getValidatedIndex(Scanner scanner, int max){
    //     int choice = -1;
    //     // loop until valid input is entered
    //     while(true){
    //         System.out.print("Enter a number (1 - " + max + "): ");
    //         // parse user input to integer
    //         try{
    //             choice = Integer.parseInt(scanner.nextLine());
    //             // make sure inout is valid
    //             if (choice >= 1 && choice <= max){
    //                 // convert to 0 based index for arrays
    //                 return choice -1;
    //             } else {
    //                 System.out.println("Invalid selection. Please choose a number from the list. ");
    //             }    
    //         } catch (NumberFormatException e){
    //             // when input is invalid
    //             System.out.println("Invalid input. Please enter a number.");
    //         }
    //     } 
    // }
    


    
    
    
/**
 * feature 2: Request removal of a resource
 * @param resourceName Name of a resource to remove
 * @return true if resource removed, false otherwise
 */
public boolean requestRemoval(String resourceName){
    //make sure there is an input parameter 
    if (resourceName == null || resourceName.trim().isEmpty()){
        System.err.println("Error: Resource name cannot be null");
        return false;
    }
    try{
        Resource resource = resourcesByName.search(resourceName);
        if (resource == null){
            System.err.println("Resource not found:" + resourceName);
            return false;
        }
    //increment removal requests & check threshold
    resource.setRemoveRequests(resource.getRemoveRequests()+1);
    if (resource.getRemoveRequests() >= removalCount);
        if (removeResourceFromDataStructures(resource)){
            System.out.println("Resource" + resourceName + "removed after reaching threshold")
            saveResource();
            return true;
        }
    //update the count
    saveResource();
    return false;
    }catch(Exception e){
        System.err.println("error processing removal request for:" + resourceName );
    }
} private boolean removeResourceFromDataStructures(Resource resource){
    if (resource == null) return false;

    //remove form BST by name
    resourcesbyName.delete(resource.getName());

    //remove from tag arrayList 
    for (String tag: resource.getTags()){
        resourcesByTag.delete(resource);
    }
    removeFromCategories(resource); //if we end up doing a hashtable
    return true;
}
private void removeFromCategories(Resource resource){
    ArrayList<String> categories = resource.getCategories();
    if (categories == null) return;

    for (String category : categories){
        ArrayList<Resource> categoryResources = categoriesResources.get(category);
        if (categoryResources != null){
            categoryResources.remove(resource);//remove resource from arraylist
            if (cateogryResources.isEmpty()){
                categoriesResources.remove(category); //clean up empty categories
            }
        }
    }
}private void saveResource(){
    try(ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("resources.new"))){
        out.writeObject(resourcesByName);
        out.writeObject(resourcesByTag);
        out.writeObject(categories);
    }catch (IOException e){
        System.err.println("Error saving resources.");
    }
}

        public static void main(String[] args){
        PlugManager plugManager = new PlugManager();
        plugManager.loadResources();
        System.out.println(plugManager.categorizedResources);


        // // to test upload user resource
        // plugManager.userAddResource();

        // // make sure something was added to bst???
        // System.out.println("Search for your resource by name to confirm:");
        // Scanner scanner = new Scanner(System.in);
        // String searchName = scanner.nextLine();
        // Resource found = plugManager.resourcesByName.search(searchName);

        // if (found != null) {
        //     System.out.println("✅ Resource found: " + found.getName());
        // } else {
        //     System.out.println("❌ Resource not found.");
        // }
    }

    
}
