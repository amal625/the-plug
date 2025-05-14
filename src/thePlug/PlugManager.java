package thePlug;
import java.util.*;
import java.util.ArrayList;
import java.io.*;



/**
 * Plug Manager class manages all resource data and implements the four main features 
 * upload,remove,search,and rate resources.
 */
public class PlugManager {
    protected ResourceBST resourcesByName; // BST w/ resource names as keys
    protected ResourceBST resourcesByTag; // BST to search by tags
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

        ArrayList<ArrayList> categoriesResources = new ArrayList<ArrayList>(4);
        initializeCategories();
    }

    private ArrayList<ArrayList<ArrayList<Resource>>> getCategoriesResources(){
        return this.categoriesResources;
    }

    private void print(){
        ArrayList<ArrayList<ArrayList<Resource>>> categorizedResources = this.getCategoriesResources();
        String[] cats = {"School", "Cost", "Type", "Genre"};
        ArrayList<String> school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7c"));
        ArrayList<String> cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
        ArrayList<String> type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
        ArrayList<String> genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainability", "Transportation" ));
        
        ArrayList<ArrayList<String>> subCats = new ArrayList<>(Arrays.asList(school_names, cost_names, type_names, genre_names));


        for (int i = 0; i < 4; i++){
            System.out.println(cats[i] + " subcategories: \n____________________________________\n");
            for (int j = 0; j<categorizedResources.get(i).size(); j++){
                System.out.println("\n\n************" + subCats.get(i).get(j) + " arrayList: \n");
                for (int k = 0; k < categorizedResources.get(i).get(j).size(); k++){
                    if (k == 136){
                        System.out.println("at 136");
                    }
                    System.out.println("-------------" + k + "----------------------");
                    System.out.println(categorizedResources.get(i).get(j).get(k));
                }
            }
        }
    }

    /**
     *initialize cateogry lists in the ResourceArrayList 
     */
    private void initializeCategories(){
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
    // TODO: Kalyani add comments
    private void loadResources() {
        try{
            BufferedReader reader = new BufferedReader (new FileReader (resourceFile));
            String line; 
            reader.readLine(); // skips first line w/titles of columns

            while((line = reader.readLine()) != null){
                String[] resource = line.split(","); 

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

                String contact = resource[5];
                  if (contact.equals("")){
                    contact = "None";
                }

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


                // add to type bucket
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

    // /////////////////////////////////////////////////////////////////////////////////////////////////////////
    // ////////////////////////////////////// FEATURE 1 ////////////////////////////////////////////////////////
    /**
     * feature 1: upload a resource
     */
        
    
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
            ArrayList cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
            categoriesResources.get(COST_INDEX).get(cost_names.indexOf(cost)).add(resource);

            // add to type bucket
            ArrayList type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
            categoriesResources.get(TYPE_INDEX).get(type_names.indexOf(type)).add(resource);
            
            // add to genre bucket
            ArrayList genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" ));
            categoriesResources.get(GENRE_INDEX).get(genre_names.indexOf(genre)).add(resource);
        }
        return true;
    }

    /**
     * 
     */
    public void userAddResource(){
        Scanner scanner = new Scanner(System.in);

        // get users resource naame
        System.out.println("Enter the name of the resource: ");
        String name = scanner.nextLine();

        // outline list of school optios 
        String [] schools = {"Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7C"};
        System.out.println("Select the school: ");
        for (int  i = 0; i < schools.length; i++){
             System.out.println((i + 1) + ". " + schools[i]);
        }
        int schoolIndex = getValidatedIndex(scanner,schools.length);
        String school = schools[schoolIndex];

        // outline list of cost options
        String[] costs = {"Free", "1-10", "10-20", "20-30", "30+"};
        System.out.println("Select the cost");
        for (int  i = 0; i < costs.length; i++){
             System.out.println((i + 1) + ". " + costs[i]);
        }
        int costIndex = getValidatedIndex(scanner, costs.length);
        String cost = costs [costIndex];

        // outline list of type options
        String[] types = {"Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service" };
        System.out.println("Select the type: ");
        for (int i = 0; i < types.length; i++){
            System.out.println((i + 1) + ". " + types[i]);
        }
        int typeIndex = getValidatedIndex(scanner, types.length);
        String type = types[typeIndex];

        // outline list of genre options
        String[] genres = {"Academic", "Career", "Entertainment", "Food","Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainabilty", "Transportation" };
        System.out.println("Select the genre: ");
        for (int i = 0; i < genres.length; i++){
            System.out.println((i + 1) + ". " + genres[i]);
        }
        int genreIndex = getValidatedIndex(scanner, genres.length);
        String genre = genres[genreIndex];

        // get user to input tags seprated by commas
        System.out.println("Enter tags (comma separated, e.x., fli, AAMP, disability resources): ");
        String tagInput = scanner.nextLine();

        // get user to input contact
        System.out.println("Enter a contact for this resource (first and last name, a website, a phone number, etc.), if you don't have a contact, enter \"None\": ");
        String contactInput = scanner.nextLine();

        // get user to input lastActive
        System.out.println("Enter when this resource was last active/used, (ex. \"current\", 2025, 5/5/2025, etc.), if you don't have a last active date, enter \"None\": ");
        String lastActive = scanner.nextLine();


        // create reosurce object w user collected input
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(tagInput.split(",")));
        Resource r2 = new Resource(name, school, cost,type, genre, tags, lastActive, contactInput);
  
        // Try to add the resource to the system
        boolean success = addResource(r2);

        // give user feedback of their upload attempt
        if (success){
            System.out.println("Resource added successfully!");
        } else {
            System.out.println("Resource not added :(");
        }
        
    }
    
    /**
     * Helper for userAddResource()
     * Prompts the user to enter a number between 1 and max, and keeps asking until they do.
     * Returns the selected index as a 0-based value for use with arrays.
     */
    private int getValidatedIndex(Scanner scanner, int max){
        int choice = -1;
        // loop until valid input is entered
        while(true){
            System.out.print("Enter a number (1 - " + max + "): ");
            // parse user input to integer
            try{
                choice = Integer.parseInt(scanner.nextLine());
                // make sure inout is valid
                if (choice >= 1 && choice <= max){
                    // convert to 0 based index for arrays
                    return choice - 1;
                } else {
                    System.out.println("Invalid selection. Please choose a number from the list. ");
                }    
            } catch (NumberFormatException e){
                // when input is invalid
                System.out.println("Invalid input. Please enter a number.");
            }
        } 
    }
    
    


    ///////////////////////////////////////////////////////////////////////////////////////////////////////////
    //////////////////////////////////////// FEATURE 2 ////////////////////////////////////////////////////////
  
    /**
     * feature 2: Request removal of a resource
     * @param resourceName Name of a resource to remove
     * @return true if resource removed, false otherwise
     */
    public boolean requestRemoval(String resourceName) {
        // Make sure there is an input parameter 
        if (resourceName == null || resourceName.trim().isEmpty()) {
            System.err.println("Error: Resource name cannot be null");
            return false;
        }
        
        try {
            Resource resource = resourcesByName.search(resourceName);
            if (resource == null) {
                System.err.println("Resource not found: " + resourceName);
                return false;
            }
            
            // Increment removal requests & check threshold
            resource.setRemoveRequests(resource.getRemoveRequests() + 1);
            if (resource.getRemoveRequests() >= removalCount) {
                if (removeResourceFromDataStructures(resource)) {
                    System.out.println("Resource " + resourceName + " removed after reaching threshold");
                    return true;
                }
            }
            return false;

        } catch (Exception e) {
            System.err.println("Error processing removal request for: " + resourceName);
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Removes a resource from all data structures
     * @param resource The resource to remove
     * @return true if successful, false otherwise
     */
    private boolean removeResourceFromDataStructures(Resource resource) {
        if (resource == null) return false;

        // Remove from BST by name
        resourcesByName.delete(resource.getName());

        // Remove from tag BST
        // Note: Assuming ResourceBST has a method to delete by resource object
        // If it doesn't, you'll need to modify the ResourceBST class
        for (String tag : resource.getTags()) {
            // delete method in ResourceBST that takes a Resource
            resourcesByTag.deleteResource(resource);
        }
        
        // Remove from categorized resources
        removeFromCategories(resource);
        
        return true;
    }

    /**
     * Removes resource from category lists ! so far it works but may need to go over it to see if its working the way we want it too
     * @param resource The resource to remove
     */
    private void removeFromCategories(Resource resource) {
        // Get resource attributes
        String school = resource.getSchool();
        String cost = resource.getCost();
        String type = resource.getType();
        String genre = resource.getGenre();
        
        // Lists to look up indices
        ArrayList<String> school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7c"));
        ArrayList<String> cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
        ArrayList<String> type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
        ArrayList<String> genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainability", "Transportation"));
        
        // Remove from school category
        int schoolIndex = school_names.indexOf(school);
        if (schoolIndex != -1) {
            categoriesResources.get(SCHOOL_INDEX).get(schoolIndex).remove(resource);
        }
        
        // Remove from cost category
        int costIndex = cost_names.indexOf(cost);
        if (costIndex != -1) {
            categoriesResources.get(COST_INDEX).get(costIndex).remove(resource);
        }
        
        // Remove from type category
        int typeIndex = type_names.indexOf(type);
        if (typeIndex != -1) {
            categoriesResources.get(TYPE_INDEX).get(typeIndex).remove(resource);
        }
        
        // Remove from genre category
        int genreIndex = genre_names.indexOf(genre);
        if (genreIndex != -1) {
            categoriesResources.get(GENRE_INDEX).get(genreIndex).remove(resource);
        }
    }

    /**
     * Helper method to collect all resources from the BST
     * @param node Current node
     * @param resources List to store resources
     */
    private void collectAllResources(ResourceBST.Node node, ArrayList<Resource> resources) {
        if (node == null) return;
        
        // In-order traversal
        ArrayList<Resource> allResources = new ArrayList<>(); // i don't know if we want to do it in a arrayList
        resourcesByName.inOrderTraversal(resourcesByName.getRoot(), allResources);
    }

    public void userRequestResourceRemoval(){
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter the name of the resource you want to request for removal");
        String resourceName = scanner.nextLine().trim();

        if(resourceName.isEmpty()){
            System.out.println("Error: Resource name cannot be empty");
            return;
        }
        Resource resource = resourcesByName.search(resourceName);
        if (resource == null){
            System.out.println("Resource not found" + resourceName);
            return;
        }
        //increment remove request count
        int currentCount = resource.getRemoveRequests();
        resource.setRemoveRequests(currentCount + 1);
        System.out.println("Remove request submitted. Current count:" + resource.getRemoveRequests());

        if (resource.getRemoveRequests() >= removalCount){
            System.out.println("Maximum removal requests reached.Removing resource from system..");
            if (removeResourceFromDataStructures(resource)){
                System.out.println("Resource successfully removed.");
            }else{
                System.out.println("Error: Could not remove resource.");
            }

        }
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// FEATURE 3 ////////////////////////////////////////////////////////

    /**
     * feature 3: searching for resources
     */
    public ArrayList <Resource> searchByName(String name){
        // list to hold search result
        ArrayList<Resource> results = new ArrayList<>();

        // use bst search to find resource by name
        Resource found = resourcesByName.search(name);

        // add found resource to list
        if(found != null){
            results.add(found);
        }
        
        // return result list
        return results;
    }

    /** 
     * Searches for resourceds by specific category and value(subcategory)
     * @param category The category to search by category and its value
     * @param value The specific value in that category(e.x. "Pomona")
     * @return A list of resources with matching category and value
     */
    public ArrayList <Resource> searchByCategory(String category, String value){
        // list to hold search result
        ArrayList<Resource> results = new ArrayList<>();

        // get index of category and value within category
        int categoryIndex = getCategoryIndex(category);
        int valueIndex = getValueIndexForCategory(categoryIndex, value);

        // if both indices are valid
        if(categoryIndex != -1 && valueIndex != -1){
            //get resource list for the specific category and value
            ArrayList<Resource> matchedResources = categoriesResources.get(categoryIndex).get(valueIndex);

            // add the matching resources gto array list
            results.addAll(matchedResources);
        }

        return results;

    }

    public ArrayList<Resource> searchByAnotherCategory(String category, String value, ArrayList<Resource> currentResources){
        // list to hold search result
        ArrayList<Resource> updatedResults = new ArrayList<>();

        // get index of category and value within category
        int categoryIndex = getCategoryIndex(category);
        int valueIndex = getValueIndexForCategory(categoryIndex, value);

        // if both indices are valid
        if(categoryIndex != -1 && valueIndex != -1){
            //get resource list for the specific category and value
            ArrayList<Resource> matchedResources = categoriesResources.get(categoryIndex).get(valueIndex);

            for (Resource resource: currentResources){
                if (matchedResources.indexOf(resource) != (-1)){
                    updatedResults.add(resource);
                }
            }
        }

        return updatedResults;
}




    /**
     * helper for searchByCategory()! returns the index of the category based on its name.
     * @param: category the category to search by category and its value
     */
    private int getCategoryIndex(String category) {
        switch (category.toLowerCase()) {
            case "school": return SCHOOL_INDEX;
            case "type": return TYPE_INDEX;
            case "genre": return GENRE_INDEX;
            case "cost": return COST_INDEX;
            default: return -1;
        }
    }


    /**
     * helperrr! returns the index of a value within a category.
     */
    private int getValueIndexForCategory(int categoryIndex, String value) {
        ArrayList<String> school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7c"));
        ArrayList<String> cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
        ArrayList<String> type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
        ArrayList<String> genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainability", "Transportation"));
        ArrayList<ArrayList<String>> subCats = new ArrayList<>(Arrays.asList(school_names, cost_names, type_names, genre_names));

        return subCats.get(categoryIndex).indexOf(value);
    }


    /**
     * @param tag The tag to search for (e.g., "fli", "AAMP")
     * @ return A list of resources that include this tag
     */
    public ArrayList<Resource> searchByTag(String tag){
        ArrayList<Resource> results = new ArrayList<>();

        // get all resources in alphabetical order
        ArrayList<Resource> allResources = new ArrayList<>();
        resourcesByName.inOrderTraversal(resourcesByName.getRoot(), allResources);

        // filter resources that have the desired tag
        for(Resource resource: allResources){
            if (resource.getTags().contains(tag.trim())){
                results.add(resource);
            }
        }
        return results;
    }


    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// FEATURE 4 ////////////////////////////////////////////////////////
    /**
    * feature 4: Rate a resource
    // */
    public boolean rateResource (String name, double rating){
        if (rating <1.0 || rating > 5.0){
            return false;
        }
        Resource resource = resourcesByName.search(name);
        if (resource == null){
            return false;
        }
        resource.rate(rating);
        System.out.println("The new rating for this resource is " + resource.getRate());
        return true;
    }

    /**
    * get the top rated resources in a priority queue
    */
    public ArrayList<Resource> getTopRatedResources(int n){
        // PriorityQueue<Resource> maxHeap = new PriorityQueue<>((a,b) -> Double.compare(b.getRate(), a.getRate()));
        PriorityQueue<Resource> maxHeap = new PriorityQueue<>(resourcesByName.size(resourcesByName.getRoot()), Resource.compareByRating());  
        
        ArrayList<Resource> resources = new ArrayList<>();
        resourcesByName.inOrderTraversal(resourcesByName.getRoot(), resources);
        for (Resource r : resources){
            if (r.getRate() !=-1){
                maxHeap.offer(r);
            }
        }
        ArrayList<Resource> topRated = new ArrayList<>();
        for (int i =0 ; i <n && !maxHeap.isEmpty();i++){
            topRated.add(maxHeap.poll());
        }
        return topRated;
     }



    /*
     * Main method to run the plug
     * 
     */
    public static void main(String[] args){
        PlugManager plugManager = new PlugManager();
        plugManager.loadResources();

        Scanner user = new Scanner(System.in);
        System.out.println("Hi! Welcome to The Plug. What would you like to do first?\n");


        while (true){
            System.out.println("Main Menu-- Enter the number corresponding to what action you'd like to complete: ");
            String [] options = {"Upload a Resource", "Search for a Resource", "Request Removal of a Resource", "Rate a Resource", "Exit The Plug"};
            for (int  i = 0; i < options.length; i++){
                System.out.println((i + 1) + ". " + options[i]);
            }
            int menuIndex = plugManager.getValidatedIndex(user, options.length);
            String option = options[menuIndex];
            
            if (menuIndex == 0){ // user wants to upload
                plugManager.userAddResource();
        
            } else if (menuIndex == 1){ // user wants to search for a resource
                // Scanner scanner = new Scanner(System.in);
                String [] searchOptions = {"Search by name of a Resource", "Search by categorization", "Search by a tag", "Search for the top rated resources"};
                System.out.println("Please enter the number corresponding to which type of search you'd like to do: ");
                for (int  i = 0; i < searchOptions.length; i++){
                    System.out.println((i + 1) + ". " + searchOptions[i]);
                }
                
                int searchIndex = plugManager.getValidatedIndex(user, searchOptions.length);
                String searchOption = options[searchIndex];

                    if (searchIndex == 0){ //Search by name of a Resource
                        System.out.println("You're searching by name, please enter the name of the resource you'd like to find: ");
                        String userName = user.nextLine();                    
                        ArrayList<Resource> nameResults = plugManager.searchByName(userName);
                        System.out.println("\n\nHere are the search results: ");
                        for(Resource res: nameResults){
                            System.out.println(res);
                        }
                        
                    } else if(searchIndex == 1){ //Search by categorization
                    
                        ArrayList<String> school_names = new ArrayList<>(Arrays.asList("Claremont McKenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7c"));
                        ArrayList<String> cost_names = new ArrayList<>(Arrays.asList("Free", "1-10", "10-20", "20-30", "30+"));
                        ArrayList<String> type_names = new ArrayList<>(Arrays.asList("Activity", "Event", "Fare Reduction", "Grant/Funding", "Item", "Organizational", "Service"));
                        ArrayList<String> genre_names = new ArrayList<>(Arrays.asList("Academic", "Career", "Entertainment", "Food", "Healthcare and Wellness", "Housing", "Mutual Aid", "Other", "Religious", "Supplies", "Sustainability", "Transportation"));
                        
                        ArrayList<ArrayList<String>> subCats = new ArrayList<>(Arrays.asList(school_names, cost_names, type_names, genre_names));
                        
                
                        // return subCats.get(categoryIndex).indexOf(value);
                        
                        System.out.println("You're searching by category, please choose from one of the 4 options by typing the corresponding number: \n");
                        String [] bigCategories = {"school", "cost", "type", "genre"};
                        
                        for (int  i = 0; i < bigCategories.length; i++){
                            System.out.println((i + 1) + ". " + bigCategories[i]);
                        }
                        int bigCatIndex = plugManager.getValidatedIndex(user, bigCategories.length);
                        String bigCatString = bigCategories[bigCatIndex];
                        ArrayList<String> innerCategory = subCats.get(bigCatIndex);

                        
                        System.out.println("Please choose a subcategory by its corresponding number to see resources: \n");
                        for (int j = 0; j < innerCategory.size(); j++){
                            System.out.println((j + 1) + ". " + innerCategory.get(j));
                        }
                        int innerCatIndex = plugManager.getValidatedIndex(user, bigCategories.length);
                        String subCatFinal = innerCategory.get(innerCatIndex);
                        ArrayList<Resource> categoryResults = plugManager.searchByCategory(bigCatString, subCatFinal);
                        
                        for(Resource res: categoryResults){
                            System.out.println("------------------------------------");
                            System.out.println(res);
                        }
                        
                    } else if(searchIndex == 2){ //Search by a tag
                        System.out.println("Searching by tags. Please enter a tag to see all resouces with that tag: ");
                        String userTag = user.nextLine();
                        ArrayList<Resource> tagsResults = plugManager.searchByTag(userTag);

                        for(Resource res: tagsResults){
                            System.out.println("------------------------------------");
                            System.out.println(res);
                        }
                        
                    }
                    else{ // Search for the top rated resources
                        System.out.println("You're now searching for top rated resources... Type in how many of the top rated resources you'd like to see: ");
                        int numResources = Integer.parseInt(user.nextLine());
                        System.out.println("Thanks! Here are the " + numResources + " top rated resources:\n");
                        ArrayList<Resource> topRated = plugManager.getTopRatedResources(numResources);
                        
                        for (int x = 0;  x < topRated.size(); x++){
                            System.out.println(x + 1 + ". " + topRated.get(x));
                        }

                    }

                
            } else if(menuIndex == 2){ // user wants to request removal of a resource
                plugManager.userRequestResourceRemoval();  // Allow user to request removal
                
            } else if(menuIndex == 3){ // user wants to rate a resource
                System.out.println("~~~~Welcome to the Plug Rate System~~~~~"); 
                while(true){
                    System.out.println("Choose an option: \n");
                    System.out.println("1. Rate a Resource \n");
                    System.out.println("2. Exit \n");
                    
                    String choice = user.nextLine();
                
                    if (choice.equals("1")){ // rating the resource is if the user chooses 1
                        System.out.println("Enter the name of the resource:");
                        String name = user.nextLine();
                        System.out.print("Enter your rating (1.0-5.0): ");
                        double rating;
                        try{
                            rating = Double.parseDouble(user.nextLine());
                        }catch(NumberFormatException e){
                            System.out.println("Invalid input. Rating must be a numebr!!");
                            user.close();
                            return;
                        }


                        if(plugManager.rateResource(name,rating)){
                            System.out.println("Yipee!! Rating has been recorded.");
                        } else{
                            System.out.println("Failed to rate. Make sure the resource exists and rating is between 1.0 and 5.0. ");
                        }  
                    }
                    else if(choice.equals("2")){ // user chooses to exit the rating 
                        System.out.println("\nThank you for using the Plug");
                        break;
                    }
                    else {
                        System.out.println("Invalid option. Try again");
                    }        
    
                }
            }
            else if (menuIndex == 4){
                break;
            }
        }
        user.close();   

    }

}
