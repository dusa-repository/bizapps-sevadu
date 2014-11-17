package com.dusa.thermometer.service.bf;

import com.dusa.thermometer.service.to.ActivationTo;
import com.dusa.thermometer.service.to.TotalTo;
import com.dusa.thermometer.service.transformation.ClientData;
import com.dusa.thermometer.service.transformation.ThermometerData;

import java.util.List;

public class ProcessDataBf {

    public SuperTotal calculateTotals(List<ThermometerData> thermometerDataList) {
        if (thermometerDataList != null && thermometerDataList.size() != 0) {
            if (thermometerDataList.get(0) instanceof ClientData) {
                ClientData clientData = (ClientData) thermometerDataList.get(0);
                List<ActivationTo> activations = clientData.getActivations();
                for (int i = 0; i < activations.size(); i++) {
                    ActivationTo activation = activations.get(i);
                    TotalTo total = new TotalTo();
                    total.setClients(1);
                    if (activation.isActivated() && activation.isObjective()) {
                        total.addActivatedAndPlanned();
                        clientData.getFinalTotals().addActivatedAndPlanned();
                        total.addPlanned();
                        clientData.getFinalTotals().addPlanned();
                    }
                    else if (activation.isActivated()) {
                        total.addActivatedAndNotPlanned();
                        clientData.getFinalTotals().addActivatedAndNotPlanned();
                    }
                    else if (activation.isObjective()) {
                        total.addPlanned();
                        clientData.getFinalTotals().addPlanned();
                    }
                    clientData.getFinalTotals().setClients(1);
                    clientData.getLabelsTotals().add(total);
                }
                SuperTotal megaTotal = calculateTotals(thermometerDataList.subList(1, thermometerDataList.size()));
                return SuperTotal.sumSuperTotals(new SuperTotal(clientData.getLabelsTotals(), clientData.getFinalTotals()), megaTotal);
            } else {
                ThermometerData data = thermometerDataList.get(0);
                SuperTotal superTotal = calculateTotals(data.getChildren());
                data.setFinalTotals(superTotal.getFinalTotals());
                data.setLabelsTotals(superTotal.getLabelTotals());
                SuperTotal megaTotal = calculateTotals(thermometerDataList.subList(1, thermometerDataList.size()));
                return SuperTotal.sumSuperTotals(superTotal, megaTotal);
            }
        } else {
            return null;
        }
    }

    private static class SuperTotal {
        private List<TotalTo> labelTotals;
        private TotalTo finalTotals;
        private int clients;

        private SuperTotal(List<TotalTo> labelTotals, TotalTo finalTotals) {
            this.labelTotals = labelTotals;
            this.finalTotals = finalTotals;
        }

        public List<TotalTo> getLabelTotals() {
            return labelTotals;
        }

        public void setLabelTotals(List<TotalTo> labelTotals) {
            this.labelTotals = labelTotals;
        }

        public TotalTo getFinalTotals() {
            return finalTotals;
        }

        public void setFinalTotals(TotalTo finalTotals) {
            this.finalTotals = finalTotals;
        }

        public SuperTotal duplicate() {

            return new SuperTotal(TotalTo.duplicateList(labelTotals), this.finalTotals.duplicate());
        }

        public static SuperTotal sumSuperTotals(SuperTotal totals1, SuperTotal totals2) {
            if (totals1 == null) {
                return totals2.duplicate();
            } else if (totals2 == null) {
                return totals1.duplicate();
            } else {
                return new SuperTotal(
                        TotalTo.sumTotalsList(totals1.getLabelTotals(), totals2.getLabelTotals()),
                        TotalTo.sumTotal(totals1.getFinalTotals(), totals2.getFinalTotals()));
            }
        }

        public int getClients() {
            return clients;
        }

        public void setClients(int clients) {
            this.clients = clients;
        }
    }

}
