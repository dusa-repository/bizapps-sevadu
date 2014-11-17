package com.dusa.thermometer.service.to;

import java.util.ArrayList;
import java.util.List;

public class TotalTo {

    private int activatedAndPlanned;
    private int activatedAndNotPlanned;
    private int planned;
    private int clients;

    public TotalTo() {
    }

    private TotalTo(int activatedAndPlanned, int activatedAndNotPlanned, int planned, int clients) {
        this.activatedAndNotPlanned = activatedAndNotPlanned;
        this.activatedAndPlanned = activatedAndPlanned;
        this.planned = planned;
        this.clients = clients;
    }

    public void addActivatedAndPlanned() {
        activatedAndPlanned++;
    }

    public void addActivatedAndNotPlanned() {
        activatedAndNotPlanned++;
    }

    public void addPlanned() {
        planned++;
    }

    public void addActivatedAndPlanned(int value) {
        activatedAndPlanned += value;
    }

    public void addActivatedAndNotPlanned(int value) {
        activatedAndNotPlanned += value;
    }

    public void addPlanned(int value) {
        planned += value;
    }

    public int getActivatedAndPlanned() {
        return activatedAndPlanned;
    }

    public int getActivatedAndNotPlanned() {
        return activatedAndNotPlanned;
    }

    public int getPlanned() {
        return planned;
    }

    public String getActivationPerLabel() {
        return (String.valueOf(activatedAndPlanned / activatedAndNotPlanned * 100) + "%");
    }

    public String getActivationObjective(int clients) {
        return (String.valueOf(planned / clients * 100) + "%");
    }

    public void setActivatedAndPlanned(int activatedAndPlanned) {
        this.activatedAndPlanned = activatedAndPlanned;
    }

    public void setActivatedAndNotPlanned(int activatedAndNotPlanned) {
        this.activatedAndNotPlanned = activatedAndNotPlanned;
    }

    public void setPlanned(int planned) {
        this.planned = planned;
    }

    public TotalTo duplicate() {
        return new TotalTo(this.activatedAndPlanned, this.activatedAndNotPlanned, this.planned, clients);
    }

    public static List<TotalTo> duplicateList(List<TotalTo> list) {
        List<TotalTo> resultList = new ArrayList<TotalTo>();
        for (TotalTo total : list) {
            resultList.add(total.duplicate());
        }
        return resultList;
    }

    public static TotalTo sumTotal(TotalTo total1, TotalTo total2) {
        if (total1 == null) {
            return total2.duplicate();
        } else if (total2 == null) {
            return total1.duplicate();
        } else {
            return new TotalTo(total1.getActivatedAndPlanned() + total2.getActivatedAndPlanned(),
                    total1.getActivatedAndNotPlanned() + total2.getActivatedAndNotPlanned(),
                    total1.getPlanned() + total2.getPlanned(), 
                    total1.getClients() + total2.getClients());
        }
    }

    public static List<TotalTo> sumTotalsList(List<TotalTo> totals1, List<TotalTo> totals2) {
        List<TotalTo> result = new ArrayList<TotalTo>();
        for (int i = 0; i < totals1.size(); i++) {
            result.add(new TotalTo(totals1.get(i).activatedAndPlanned + totals2.get(i).activatedAndPlanned,
                    totals1.get(i).getActivatedAndNotPlanned() + totals2.get(i).getActivatedAndNotPlanned(),
                    totals1.get(i).getPlanned() + totals2.get(i).getPlanned(), 
                    totals1.get(i).getClients() + totals2.get(i).getClients()));
        }
        return result;
    }

    public int getClients() {
        return clients;
    }

    public void setClients(int clients) {
        this.clients = clients;
    }
    
}
