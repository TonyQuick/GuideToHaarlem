package com.example.tonyquick.thequicklawsonguidetohaarlem.utilities;

import com.example.tonyquick.thequicklawsonguidetohaarlem.fragments.ShowCategory;
import com.example.tonyquick.thequicklawsonguidetohaarlem.models.Attraction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Created by Tony Quick on 26/10/2016.
 */

public class Sorters {

    public static ArrayList<Attraction> sort(ArrayList<Attraction> attractions, String sortBy){

        if (sortBy.equals(ShowCategory.STATE_ALPHABETICALLY)){

            Collections.sort(attractions, new Comparator<Attraction>() {
                @Override
                public int compare(Attraction o1, Attraction o2) {
                    return o1.getTitle().compareTo(o2.getTitle());
                }
            });

            return attractions;
        }
        if (sortBy.equals(ShowCategory.STATE_DISTANCE)){

            Collections.sort(attractions, new Comparator<Attraction>() {
                @Override
                public int compare(Attraction o1, Attraction o2) {
                    return Double.compare(o1.getDistanceAway(),o2.getDistanceAway());
                }
            });


            return attractions;
        }



        return attractions;
    }


}
