import java.util.List;
import java.util.ArrayList;
/**
 *	Population - gives user options to see cities sorted by population or name,
	or finding cities based on name using file io and sorting methods
 *
 *	Requires FileUtils and Prompt classes.
 *
 *	@author	Melissa Qin
 *	@since	Jan 12 2023
 */
public class Population {
	
	// List of cities
	private List<City> cities;
	
	// US data file
	private final String DATA_FILE = "usPopData2017.txt";

	//number of results in the lists
	private final int NUM_RESULTS;

	//field so methods dont have to return it to be accessible
	private long timeElapsed;
	
	public Population() {
		cities = new ArrayList<City>();
		NUM_RESULTS = 50;
		timeElapsed = 0;
	}
	
	
	/**	Prints the introduction to Population */
	public void printIntroduction() {
		System.out.println("   ___                  _       _   _");
		System.out.println("  / _ \\___  _ __  _   _| | __ _| |_(_) ___  _ __ ");
		System.out.println(" / /_)/ _ \\| '_ \\| | | | |/ _` | __| |/ _ \\| '_ \\ ");
		System.out.println("/ ___/ (_) | |_) | |_| | | (_| | |_| | (_) | | | |");
		System.out.println("\\/    \\___/| .__/ \\__,_|_|\\__,_|\\__|_|\\___/|_| |_|");
		System.out.println("           |_|");
		System.out.println();
	}
	
	/**	Print out the choices for population sorting */
	public void printMenu() {
		System.out.println("1. Fifty least populous cities in USA (Selection Sort)");
		System.out.println("2. Fifty most populous cities in USA (Merge Sort)");
		System.out.println("3. First fifty cities sorted by name (Insertion Sort)");
		System.out.println("4. Last fifty cities sorted by name descending (Merge Sort)");
		System.out.println("5. Fifty most populous cities in named state");
		System.out.println("6. All cities matching a name sorted by population");
		System.out.println("9. Quit");
	}
	
	/**
	 * reads file and puts city info in array list
	 */
	public void readFile() {
		java.util.Scanner input = FileUtils.openToRead(DATA_FILE);
		input.useDelimiter("[\t\n]");
		while (input.hasNext()) {
			String state = input.next();
			String name = input.next();
			String type = input.next();
			int pop = input.nextInt();
			cities.add(new City(name, state, type, pop));
		}
	}
	
	/**
	 *	Swaps two City objects in array arr
	 *	@param arr		array of City objects
	 *	@param x		index of first object to swap
	 *	@param y		index of second object to swap
	 */
	private void swap(List<City> arr, int x, int y) {
		City temp = arr.get(x);
		arr.set(x, arr.get(y)); 
		arr.set(y, temp);
	}
	
	/**
	 *	Selection Sort algorithm 
	 *	@param arr		array of City objects to sort
	 *	@param ascending true if sorting in ascending order, false if descending
	 */
	public void selectionSort(List<City> arr, boolean ascending) {
		for (int o=arr.size()-1; o>0; o--) {
			int indexBiggest = 0;
			for (int i=0; i<=o; i++) {
				if (ascending){
					if (arr.get(i).compareTo(arr.get(indexBiggest)) > 0)
						indexBiggest = i;
				} else {
					if (arr.get(i).compareTo(arr.get(indexBiggest)) < 0) 
						indexBiggest = i;
				}
			}
			swap(arr, o, indexBiggest);
		}
	}
	
	/**
	 *	Insertion Sort algorithm by using City object's names
	 *	@param arr		array of City objects to sort
	 */
	public void insertionSortByCityName(List<City> arr) {
        /* o = index of number to insert */
		for (int o=1; o<arr.size(); o++) {
			City insert = arr.get(o);

			int i=o;
			while (i>0 && insert.getName().compareTo(arr.get(i-1).getName()) < 0) {
				arr.set(i, arr.get(i-1));
				i--;
			}
			arr.set(i, insert);
		}
	}
	
	
	/**
	 * merge sort 
	 * @param arr			List of City objects to sort
	 * @param ascending		true if sorting in ascending order, false if descending
	 * @param population	true if sorting by population false if by name
	 */
	public void mergeSort(List<City> arr, boolean population, boolean ascending) {
        if (arr.size() > 2) {

            /* split into 2 arrays */
            List<City> arr1, arr2;
            int split = (arr.size()-1)/2;
            arr1 = new ArrayList<City>();
            arr2 = new ArrayList<City>();
            for (int i=0; i<=split; i++) 
				arr1.add(arr.get(i));
            for (int i=split+1; i<arr.size(); i++) 
				arr2.add(arr.get(i));
            
            mergeSort(arr1, population, ascending);
            mergeSort(arr2, population, ascending);
            merge(arr, arr1, arr2, population, ascending);

        } else {
			if (population) {
				if (ascending) {
					if (arr.size() == 2 && arr.get(0).compareTo(arr.get(1)) > 0) swap(arr, 0, 1);
				} else {
					if (arr.size() == 2 && arr.get(0).compareTo(arr.get(1)) < 0) swap(arr, 0, 1);
				}
			} else { //sorting by name
				if (ascending) {
					if (arr.size() == 2 && arr.get(0).getName().
						compareTo(arr.get(1).getName()) > 0) 
						swap(arr, 0, 1);
				} else {
					if (arr.size() == 2 && arr.get(0).getName().
						compareTo(arr.get(1).getName()) < 0)
						swap(arr, 0, 1);
				}
			}
        }

	}

    /**
     * merges two sorted arrays into one
     * @param arr   merged array formed from arr1 and arr2
     * @param arr1  first array to merge
     * @param arr2  second array to merge
	 * @param population	true if sorting by population, false if by name
	 * @param ascending		true if sorting in ascending order, false if descending
     */
	private void merge(List<City> arr, List<City> arr1, List<City> arr2, 
						boolean population, boolean ascending) {
		int p1, p2;
		p1 = p2 = 0; //pointers for each array
		int count = 0;
		while (p1 < arr1.size() && p2 < arr2.size() && count<arr.size()) {
			if (population) {
				if (arr1.get(p1).compareTo(arr2.get(p2)) < 0) 
				{
					if (ascending) {
						arr.set(count, arr1.get(p1));
						p1++;
					} else {
						arr.set(count, arr2.get(p2));
						p2++;
					}
				} else {
					if (ascending) {
						arr.set(count, arr2.get(p2));
						p2++; 
					} else {
						arr.set(count, arr1.get(p1));
						p1++;
					}
				}
			} else {
				if (arr1.get(p1).getName().compareTo(arr2.get(p2).getName()) < 0)
				{
					if (ascending) {
						arr.set(count, arr1.get(p1));
						p1++;
					} else {
						arr.set(count, arr2.get(p2));
						p2++;
					}
				} else { 
					if (ascending) {
						arr.set(count, arr2.get(p2));
						p2++; 
					} else {
						arr.set(count, arr1.get(p1));
						p1++;
					}
				}
			}
			count++;
		}
		if (count < arr.size()) {
			if (p1 >= arr1.size()) { //fill rest of merged w arr2
				for (int i=count; i<arr.size(); i++) {
					arr.set(i, arr2.get(p2 + (i-count)));
				}
			} else if (p2 >= arr2.size()) {
				for (int i=count; i<arr.size(); i++) {
					arr.set(i, arr1.get(p1 + (i-count)));
				}
			}
		}
		
	}
	
	/**
	 * first option in menu, sorting least populated cities using selection sort
	 * @return	sorted list of ascending population of correct length
	 */
	public List<City> ascendingPopulation() {
		List<City> sorted = new ArrayList<City>();
		for (City ct : cities) sorted.add(ct);
		
		long startTime = System.currentTimeMillis();
		selectionSort(sorted, true);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return shortenList(sorted, NUM_RESULTS);
	}
	
	/**
	 * sorting most populated cities using merge sort
	 * @return	sorted list of descending population of correct length
	 */
	public List<City> descendingPopulation() {
		List<City> sorted = new ArrayList<City>();
		for (City ct : cities) sorted.add(ct);
		
		long startTime = System.currentTimeMillis();
		mergeSort(sorted, true, false);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return shortenList(sorted, NUM_RESULTS);
	}

	/**
	 * sorting cities alphabetically, ascending using insertion sort
	 * @return	sorted list of ascending name of correct length
	 */
	public List<City> ascendingName() {
		List<City> sorted = new ArrayList<City>();
		for (City ct : cities) sorted.add(ct);

		long startTime = System.currentTimeMillis();
		insertionSortByCityName(sorted);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return shortenList(sorted, NUM_RESULTS);
	}

	/**
	 * sorting cities alphabetically descending using merge sort
	 * @return	sorted list of descending name of correct length
	 */
	public List<City> descendingName() {
		List<City> sorted = new ArrayList<City>();
		for (City ct : cities) sorted.add(ct);

		long startTime = System.currentTimeMillis();
		mergeSort(sorted, false, false);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return shortenList(sorted, NUM_RESULTS);

	}

	/**
	 * sorting most populous cities in given state using merge sort
	 * @param state	name of state with correct capitalization
	 * @return		sorted list of correct length
	 */
	public List<City> mostPopulousInState(String state) {
		List<City> sorted = new ArrayList<City>();
		for (City ct : cities) {
			if (ct.getState().equals(state)) sorted.add(ct);
		}

		long startTime = System.currentTimeMillis();
		mergeSort(sorted, true, false);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return shortenList(sorted, NUM_RESULTS);
	}

	/**
	 * sorting all cities with given name by population using merge sort
	 * @param name	name of cities to search for
	 * @return		sorted list with all cities found
	 */
	public List<City> allMatchingName(String name) {
		List<City> sorted = new ArrayList<City>();
		for (City ct:cities) {
			if (ct.getName().equals(name)) sorted.add(ct);
		}

		long startTime = System.currentTimeMillis();
		mergeSort(sorted, true, false);
		long endTime = System.currentTimeMillis();
		timeElapsed = endTime-startTime;

		return sorted;
	}

	/**
	 * runs the program and asks for input
	 */
	public void run() {
		readFile();
		printIntroduction();
		System.out.println(cities.size() + " cities in database");
		System.out.println();

		int input = 0;
		do {
			printMenu();
			
			do {
				input = Prompt.getInt("Enter selection");
			} while ((input < 1 || input > 6) && input != 9);
			
			List<City> found = null;
			String title = "";
			if (input != 9) {
				if (input == 1) {
					found = ascendingPopulation();
					title = "Fifty least populous cities";
				} else if (input == 2) {
					found = descendingPopulation();
					title = "Fifty most populous cities";
				} else if (input == 3) {
					found = ascendingName();
					title = "Fifty cities sorted by name";
				} else if (input == 4) {
					found = descendingName();
					title = "Fifty cities sorted by name descending";

				} else if (input == 5) {
					String state = "";
					boolean first = true;
					System.out.println();
					do {
						if (!first) System.out.println("ERROR: " + state + " is not valid");
						state = Prompt.getString("Enter state name (ie. Alabama)");
						first = false;
					} while (! isStateName(state));
					found = mostPopulousInState(state);
					title = "Fifty most populous cities in " + state;
				} else {
					String name = "";
					System.out.println();
					name = Prompt.getString("Enter city name");
					found = allMatchingName(name);
					title = "City " + name + " by population";
				}

				System.out.println();
				printList(found, title);
				System.out.println();
				if (input < 5) {
					System.out.println("Elapsed Time " + timeElapsed + " milliseconds");
					System.out.println();
				}
			}

		} while (input != 9);

		System.out.println("\nThanks for using Population!\n");
	}

	/**
	 * prints list of cities
	 * @param list	list to print
	 * @param title	title of the list based on which option from menu picked
	 */
	public void printList(List<City> list, String title) {

		System.out.println(title);
		System.out.printf("%5s%-22s%-25s%-12s%12s\n", " ", "State", "Name", "Type", "Population");
		for (int i=0; i<list.size(); i++) {
			City c = list.get(i);
			System.out.printf("%3d: %-22s%-25s%-12s%12s\n", i+1, c.getState(), 
						c.getName(), c.getType(), formatNumber(c.getPopulation()));
		}
	}

	/**
	 * formats numbers into string with commas
	 * @param num	number to format
	 * @return		formatted string
	 */
	public String formatNumber(int num) {
		String str = "" + num;
		String formatted = "";

		/* start from end, every 3 numbers printed from end add a comma */
		int count = 0;
		for (int i=str.length()-1; i>=0; i--) {
			if (count%3 == 0 && count!=0) formatted = "," + formatted;
			formatted = "" + str.charAt(i) + formatted;
			count++;
		}

		return formatted;
	}

	/**
	 * shortens list to only the first specified number of elements
	 * @param list			list to shorten
	 * @param finalLength	desired length
	 * @return				shortened list
	 */
	public List<City> shortenList(List<City> list, int finalLength) {
		int initialLength = list.size();
		for (int i=0; i<initialLength-finalLength; i++) list.remove(finalLength);
		return list;
	}

	/**
	 * determines whether a string is the name of a state (case-sensitive)
	 * @param str	string to test
	 * @return		true if it's a state name, false if not
	 */
	public boolean isStateName(String str) {
		String[] states = {"Alaska", "Alabama", "Arkansas", 
		"Arizona", "California", "Colorado", "Connecticut", "District of Columbia",
		 "Delaware", "Florida", "Georgia", "Hawaii", "Iowa", "Idaho", 
		 "Illinois", "Indiana", "Kansas", "Kentucky", "Louisiana", "Massachusetts", 
		 "Maryland", "Maine", "Michigan", "Minnesota", "Missouri", "Mississippi", 
		 "Montana", "North Carolina", "North Dakota", "Nebraska", "New Hampshire", 
		 "New Jersey", "New Mexico", "Nevada", "New York", "Ohio", "Oklahoma", 
		 "Oregon", "Pennsylvania",  "Rhode Island", "South Carolina",
		  "South Dakota", "Tennessee", "Texas", "Utah", "Virginia", 
		   "Vermont", "Washington", "Wisconsin", "West Virginia", "Wyoming"};

		for (String s:states) {
			if (str.equals(s)) return true;
		}
		return false;
	}
	
	public static void main(String[] args) {
		Population p = new Population();
		p.run();
	}
	
}
