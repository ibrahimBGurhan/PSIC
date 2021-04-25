package com.psic;

import java.util.ArrayList;
import java.util.List;

public class Physiotherapist extends Member {

    private List<String> expertiseList = new ArrayList<>();
    private List<String> treatmentList = new ArrayList<>();

    public Physiotherapist(String id, String fullName, String address, String telephoneNumber, String expertises, String treatmentList) {
        super(id, fullName, address, telephoneNumber);
        setExpertises(expertises);
        setTreatments(treatmentList);
    }

    private void setTreatments(String treatmentList) {
        String[] treatments = treatmentList.split(",");
        for (String treatment : treatments) {
            this.treatmentList.add(treatment);
        }
    }

    private void setExpertises(String expertises) {
        String[] expertisesArr = expertises.split(",");
        for (String experiment : expertisesArr) {
            this.expertiseList.add(experiment);
        }
    }

}