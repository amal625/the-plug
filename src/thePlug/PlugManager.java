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
                    return choice -1;
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
                    saveResources();
                    return true;
                }
            }
            
            // Update the count
            saveResources();
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
     * Saves resources to file
     */
    private void saveResources() {
        try {
            // Create a BufferedWriter to write to the file
            BufferedWriter writer = new BufferedWriter(new FileWriter(resourceFile));
            
            // Write header row
            writer.write("Name,School,Cost,Type,Genre,Contact,LastActive,Tags\n");
            
            // Collect all resources from BST
            ArrayList<Resource> allResources = new ArrayList<>();
            collectAllResources(resourcesByName.getRoot(), allResources);
            
            // Write each resource to the file
            for (Resource resource : allResources) {
                StringBuilder line = new StringBuilder();
                line.append(resource.getName()).append(",");
                line.append(resource.getSchool()).append(",");
                line.append(resource.getCost()).append(",");
                line.append(resource.getType()).append(",");
                line.append(resource.getGenre()).append(",");
                line.append(resource.getContact()).append(",");
                line.append(resource.getlastActive()).append(",");
                
                // Join tags with semicolons
                ArrayList<String> tags = resource.getTags();
                if (tags != null && !tags.isEmpty()) {
                    for (int i = 0; i < tags.size(); i++) {
                        line.append(tags.get(i));
                        if (i < tags.size() - 1) {
                            line.append(";");
                        }
                    }
                }
                
                writer.write(line.toString() + "\n");
            }
            
            writer.close();
        } catch (IOException e) {
            System.err.println("Error saving resources: " + e.getMessage());
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

            saveResources();//save updated state to file
        }
    }




    /////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////// FEATURE 3 ////////////////////////////////////////////////////////

    /**
     * feature 3: searching for resources
     */
    // TODO: maybe make it so that it can search by prefixes?
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
    public boolean rateResource (String name, float rating){
        if (rating <1.0 || rating > 5.0){
            return false;
        }
        Resource resource = resourcesByName.search(name);
        if (resource == null){
            return false;
        }
        resource.rate(rating);
        saveResources(); //saves the updated average rating to disk
        return true;
    }

    /**
    * get the top rated resources in a priority queue
    */
    public List<Resource> getTopRatedResources(int n){
        PriorityQueue<Resource> maxHeap = new PriorityQueue<>();
        (a,b) -> Double.compare(b.getRate(), a.getRate());
        for (Resource re1 : resourcesByName.inOrderTraversal()){
            if (re1.getRate() !=-1){
                maxHeap.offer(re1);
            }
        }
        List<Resource> topRated = new ArrayList<>();
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

        ArrayList<String> tags = new ArrayList<> (Arrays.asList("FGLI","health"));
        ArrayList<String> tags1 = new ArrayList<> (Arrays.asList("aamp", "health"));

        Resource r1 = new Resource("Health Center", "Pomona", "Free", "Service", "Healthcare and Wellness", tags, "None", "None");

        plugManager.addResource(r1);
        System.out.println("Testing resource removal");

        for(int i = 1; i <= 5; i++){
            System.out.println(r1.getRemoveRequests());
            System.out.println("request num" + i);
            System.out.println(plugManager.requestRemoval("Health Center"));
        }

        // System.out.println("removing the same resource again");
        // boolean secondRemove = plugManager.requestRemoval("Health Center");
        // System.out.println("re-remove result:" + (secondRemove));

        
        // Resource r2 = new Resource("STEM Internship Fund", "Harvey Mudd", "10-20", "Grant/Funding", "Career",tags1, "None", "None" );
        // Resource r3 = new Resource("7C Mindfulness Circle", "7C", "Free", "Event", "Healthcare and Wellness", tags, "None", "None");

        // plugManager.addResource(r1);
        // plugManager.addResource(r2);
        // plugManager.addResource(r3);

        // System.out.println("Searching by name");
        // ArrayList<Resource> nameResults = plugManager.searchByName("Health Center");
        // for(Resource res: nameResults){
        //     System.out.println(res.getName());
        // }
        
        // System.out.println("Searching by category");
        // ArrayList<Resource> categoryResults = plugManager.searchByCategory("Genre", "Healthcare and Wellness");
        // for(Resource res: categoryResults){
        //     System.out.println(res.getName() + ", Genre: " + res.getGenre());
        // }

        // System.out.println("Searching by tags");
        // ArrayList<Resource> tagsResults = plugManager.searchByTag("FGLI");
        // for(Resource res: tagsResults){
        //     System.out.println(res.getName() + " Tag: " + res.getTags());
        // }
        // plugManager.print();
        
        
        // to test upload user resource
        // plugManager.userAddResource();

        // // make sure something was added to bst???
        // System.out.println("Search for your resource by name to confirm:");
        // Scanner scanner = new Scanner(System.in);
        // String searchName = scanner.nextLine();
        // Resource found = plugManager.resourcesByName.search(searchName);

        // if (found != null) {
        //     System.out.println("Resource found: " + found.getName());
        //     System.out.println(found);
        // } else {
        //     System.out.println(" Resource not found.");
        // }

        
        

    }

// // Search test
   // System.out.println("\nSearching for 'Apple':");
   // Resource found = bst.search("Apple");
   // System.out.println(found != null ? "Found: " + found.getName() : "Not found");


   // System.out.println("\nSearching for 'Berry':");
   // found = bst.search("Berry");
   // System.out.println(found != null ? "Still found (should be deleted)" : "Not found (correct)");


   // PlugManager manager = new PlugManager();
   // manager.loadResources();  // Make sure this is working


   // Scanner scanner = new Scanner(System.in);
   // System.out.println("1. Request removal of a resource");
   // System.out.println("2. Exit");
   // System.out.print("Choose an option: ");
  
   // int choice = scanner.nextInt();
   // scanner.nextLine(); // consume newline


   // // Create and insert some test resources
   // Resource r4 = new Resource("Apple", "Pomona", "Free", "Item", "Academic",
   //         new ArrayList<>(Arrays.asList("fli")), "None", "apple@pomona.edu");
   // Resource r5 = new Resource("Berry", "Scripps", "1-10", "Service", "Healthcare and Wellness",
   //         new ArrayList<>(Arrays.asList("aamp")), "None", "berry@scripps.edu");
   // Resource r6 = new Resource("Cake", "Harvey Mudd", "Free", "Event", "Entertainment",
   //         new ArrayList<>(Arrays.asList("fun")), "None", "cake@mudd.edu");


   // manager.resourcesByName.insert(r1);
   // manager.resourcesByName.insert(r2);
   // manager.resourcesByName.insert(r3);


   // // Show user what resources exist ~ i only did this bc i forgot resources it works!!
   // ArrayList<Resource> existing = new ArrayList<>();
   // manager.resourcesByName.inOrderTraversal(manager.resourcesByName.getRoot(), existing);


   // System.out.println("Available Resources:");
   // for (Resource res : existing) {
   //     System.out.println("- " + res.getName());
   // }


//     // Allow user to request removal
//     manager.userRequestResourceRemoval();


    // Test for getTopRatedResources with only one resource rated


    // public static void testGetTopRatedResourcesWithSingleRating(PlugManager plugManager) {
    //     plugManager.rateResource("Resource A", 5.0f);
    //     List<Resource> topRated = plugManager.getTopRatedResources(1);
    //     System.out.println("Top rated resources with one resource rated:");
    //     for (Resource resource : topRated) {
    //         System.out.println("  " + resource.getName() + " with rating " + resource.getRate());
    //     }
    // }


//     PlugManager plugManager = new PlugManager();
// // Add some test resources
//         plugManager.resourcesByName.insert(new Resource("Resource A", "School A", "Free", "Type A", "Genre A", new ArrayList<>(), "2025-05-01", "contact@a.com"));
//         plugManager.resourcesByName.insert(new Resource("Resource B", "School B", "Paid", "Type B", "Genre B", new ArrayList<>(), "2025-04-15", "contact@b.com"));

//         // Test the user-interactive rating method
//         Scanner scanner = new Scanner(System.in);
//         boolean continueRating = true;

//         while (continueRating) {
//             System.out.println("Enter the name of the resource you want to rate (or 'exit' to quit):");
//             String resourceName = scanner.nextLine();
//             if (resourceName.equalsIgnoreCase("exit")) {
//                 continueRating = false;
//                 System.out.println("Exiting rating system.");
//                 break;
//             }

//             System.out.println("Enter a rating for " + resourceName + " (between 1.0 and 5.0):");
//             float rating = scanner.nextFloat();
//             scanner.nextLine(); // Consume the newline character

//             boolean success = .rateResource(resourceName, rating);
//             if (success) {
//                 System.out.println("Successfully rated " + resourceName + " with a rating of " + rating);
//             } else {
//                 System.out.println("Failed to rate " + resourceName + ". Make sure the rating is between 1.0 and 5.0, and the resource exists.");
//             }

//             System.out.println("Would you like to rate another resource? (yes/no)");
//             String response = scanner.nextLine();
//             if (response.equalsIgnoreCase("no")) {
//                 continueRating = false;
//                 System.out.println("Exiting rating system.");
//             }
//         }
//         scanner.close();
//     }
    
}
