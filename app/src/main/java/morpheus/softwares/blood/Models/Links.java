package morpheus.softwares.blood.Models;

public class Links {
    String[] bloodGroups = {"A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-", "Blood Bank"};
    String[] genotypes = {"AA", "AS", "AC", "SS", "SC", "aa", "Blood Bank"};
    String[] roles = {"Donor", "Recipient", "Blood Bank"};

    public Links() {
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
}