package SecondApproach;

import java.util.List;

public class Filter {
    List<FilterCriteria> filterCriterias;

    public Filter() {
        this.filterCriterias = null;
    }

    public Filter(List<FilterCriteria> filterCriteria) {
        this.filterCriterias = filterCriteria;
    }

    public List<FilterCriteria> getFilterCriterias() {
        return filterCriterias;
    }

    public void setFilterCriterias(List<FilterCriteria> filterCriteria) {
        this.filterCriterias = filterCriteria;
    }
}
