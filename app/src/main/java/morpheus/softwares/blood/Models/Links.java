package morpheus.softwares.blood.Models;

public class Links {
    String databaseReference = "Users";
    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Blood Bank"};
    String[] genotypes = {"AA", "AS", "AC", "Blood Bank"};
    String[] roles = {"Donor", "Recipient", "Blood Bank"};
    String[] states = {"Abia", "Adamawa", "Akwa Ibom", "Anambra", "Bauchi", "Bayelsa", "Benue",
            "Borno", "Cross River", "Delta", "Ebonyi", "Edo", "Ekiti", "Enugu", "Gombe", "Imo",
            "Jigawa", "Kaduna", "Kano", "Katsina", "Kebbi", "Kogi", "Kwara", "Lagos", "Nasarawa",
            "Niger", "Ogun", "Ondo", "Osun", "Oyo", "Plateau", "Rivers", "Sokoto", "Taraba",
            "Yobe", "Zamfara", "FCT"};

    public Links() {
    }

    /**
     * Returns the database's immediate child reference
     */
    public String getDatabaseReference() {
        return databaseReference;
    }

    /**
     * Returns the list of predefined blood groups
     */
    public String[] getBloodGroups() {
        return bloodGroups;
    }

    /**
     * Returns the list of predefined genotypes
     */
    public String[] getGenotypes() {
        return genotypes;
    }

    /**
     * Returns the list of predefined roles
     */
    public String[] getRoles() {
        return roles;
    }

    /**
     * Returns the list of predefined states
     */
    public String[] getStates() {
        return states;
    }
}