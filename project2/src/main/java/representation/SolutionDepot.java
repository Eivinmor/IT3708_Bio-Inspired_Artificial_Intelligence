package representation;


import tools.Formulas;

import java.util.ArrayList;

public class SolutionDepot extends Depot{

    private ArrayList<Customer> customers;
    private ArrayList<SolutionRoute> solutionRoutes;

    public SolutionDepot(Depot depot) {
        super(depot.number, depot.getX(), depot.getY(), depot.maxRouteDuration, depot.maxLoadPerVehicle);
        this.customers = new ArrayList<>();
        this.solutionRoutes = new ArrayList<>();
        solutionRoutes.add(new SolutionRoute());
    }

    public void addCustomer(Customer customer) {
        customers.add(customer);
    }

    public ArrayList<Customer> getCustomers(){
        return customers;
    }

    public void addCustomerToCurrentRoute(Customer customer) {
        SolutionRoute curSolutionRoute = solutionRoutes.get(solutionRoutes.size()-1);
        double distance = Formulas.euclideanDistance(this, customer);
        if (curSolutionRoute.getLoad() + customer.demand > maxLoadPerVehicle) {
            solutionRoutes.add(new SolutionRoute());
            curSolutionRoute = solutionRoutes.get(solutionRoutes.size()-1);
        }
        else if (maxRouteDuration > 0 && curSolutionRoute.getDuration() + customer.serviceDuration + distance > maxRouteDuration) {
            solutionRoutes.add(new SolutionRoute());
            curSolutionRoute = solutionRoutes.get(solutionRoutes.size()-1);
        }
        curSolutionRoute.addCustomer(customer, distance);
    }

    public ArrayList<SolutionRoute> getSolutionRoutes() {
        return solutionRoutes;
    }
}
