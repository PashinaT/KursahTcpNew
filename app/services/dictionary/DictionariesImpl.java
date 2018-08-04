package services.dictionary;



import models.workDatabase;

import javax.jws.WebService;
import java.util.ArrayList;

/**
 * Implementation of webservice interface.
 * Methods return structure or values of table in xml format
 * */
@WebService(endpointInterface = "services.dictionary.Dictionaries")
public class DictionariesImpl implements Dictionaries {
    @Override
    public ArrayList<ArrayList<String>> getTableValues(String tablename) {
        return workDatabase.getTableValues(tablename.toLowerCase());
    }

}
