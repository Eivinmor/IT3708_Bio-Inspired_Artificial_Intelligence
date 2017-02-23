package representation;


import java.util.ArrayList;

public class SolutionRoute {

    private double duration, load;
    private ArrayList<Customer> customers;


    public SolutionRoute() {
        customers = new ArrayList<>();
        duration = 0;
        load = 0;
    }

    public void addCustomer(Customer customer, double distance) {
        duration += customer.serviceDuration + distance;
        load += customer.demand;
        customers.add(customer);
    }

    public double getDuration() {return duration;}

    public double getLoad() {return load;}

    public ArrayList<Customer> getCustomers() {
        return customers;
    }


}
