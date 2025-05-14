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
            ArrayList cost_names = new ArrayList<>(Arrays.asList("free", "1-10", "10-20", "20-30", "30+"));
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
    public void userAddResource(Resource resource){
        Scanner scanner = new Scanner(System.in);

        // get users resource naame
        System.out.println("Enter the name of the resource: ");
        String name = scanner.nextLine();

        // outline list of school optios 
        String [] schools = {"Claremont Mckenna", "Harvey Mudd", "Pitzer", "Pomona", "Scripps", "7C"};
        System.out.println("Seletct the school: ");
        for (int  i = 0; i < schools.length; i++){
             System.out.println((i + 1) + ". " + schools[i]);
        }
        int schoolIndex = get

        System.out.println("Enter the cost of the resource (free, 1-10, 10-20, 20-30, 30+)");
        String cost = scanner.nextLine();

        System.out.println("Enter the type of the resource (Activity, Event, Fare Reduction, Grant/Funding, Item, Organizational, Service): ");
        String type = scanner.nextLine();

        System.out.println("Enter the type of the resource (Academic, Career, Entertainment, Food, Healthcare and Wellness, Housing, Mutual Aid, Other, Religious, Supplies, Sustainabilty, Transportation): ");
        String genre = scanner.nextLine();

        // String.out.println("");
        
    }
    
    /**
     * 
     */
    private int getValidatedIndex(Scanner scanner, int max){
        int choice = -1;
        while(true){
            
            System.out.print("Enter a number (1 - " + max + "): ");
            try{
                choice = Integer.parseInt(scanner.nextLine());

                if (choice >= 1 && choice <= max){
                    return choice -1;
                } else {
                    System.out.println("Invalid selection. Please choose a number from the list. ");
                }
                
            } catch (NumberFormatException e){
                System.out.println("Invalid input. Please enter a number.");
            }
        } 
    }
    


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
    resource.setRemoveRequests(resource.getRemove()+1);
    if (resource.getRemoveRequests() >= REMOVAL_THRESHOLD);
        if (removeResourceFromDataStructures(resource)){
            saveResource();
            return true;
        }
    //update the count
    saveResources();
    return false;
    }catch(Exception e){
        System.err.println("error processing removal request for:" + resourceName );
        e.printStackTace    
    }



/**
 * 
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
 * 
 */
public ArrayList <Resource> searchByCategory(String category, String value){
    // list to hold search result
    ArrayList<Resource> results = new ArrayList<>();

    // get index of category and value within category
    int categoryIndex = getCategoryIndex(category);
    int valueIndex = getValueForCategory(categoryIndex, value);

    // if both indcies are valid
    if(categoryIndex != -1 && valueIndex != -1){
        //get resource list for the specific category and value
        ArrayList<Resource> matchedResources = categories.Resources.get(categoryIndex).get(valueIndex);

        // add the matching resources gto array list
        results.addAll(matchedResources);
    }

    return results;

}

/**
 * helppeerr!! returns the index of the category based on its name.
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
    switch (categoryIndex) {
        case 0: return findSchoolIndex(value);
        case 1: return findTypeIndex(value);
        case 2: return findGenreIndex(value);
        case 3: return findCostIndex(value);
        default: return -1;
    }
}


/**
 * @param tag The tag to search for (e.g., "fli", "AAMP")
 * @ return A list of resources that include this tag
 */
public ArrayList<Resource> searchByTag(String tag){
    ArrayList<Resource> results = new ArrayList<>();

    // get all resources in alphabetical order
    ArrayList<Resource> allResources = new ArrayList<>();
    resourcesByName.inOrderTraversal(resourcesByName.root, allResources);

    // filter resources that have the desired tag
    for(Resource resource: allResources){
        if (resource.getTags().contains(tag.trim())){
            results.add(resource);
        }
    }
    return results;
}
