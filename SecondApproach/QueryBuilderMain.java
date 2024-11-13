package SecondApproach;

import FirstApproach.Filter;
import FirstApproach.FilterCriteria;
import FirstApproach.Operator;
import FirstApproach.*;

import java.util.List;

public class QueryBuilderMain {

    public static void main(String[] args) {
        FirstApproach.Filter filter = new FirstApproach.Filter(
            List.of(
                new FirstApproach.FilterCriteria("alertEntity", FirstApproach.Operator.EQ, "PATIENT_PLANNER"),
                new FirstApproach.FilterCriteria("createDateCrieria", FirstApproach.Operator.OR,
                    List.of(
                        new FirstApproach.FilterCriteria("createDate", FirstApproach.Operator.LTE, "31 Aug"),
                        new FilterCriteria("createDate", Operator.IS_NULL, null)
                    )
                )
            )
        );

        FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder();
        Filter customFilter = filterTreeBuilder.preprocessFilter(filter);

        if (customFilter != null) {
            FilterTreeNode root = filterTreeBuilder.buildParseTree(customFilter.getFilterCriterias().get(0));

            System.out.println("Root : " + root);

            // Print Tree Traversal
            System.out.println("Filter Parse Tree : ");
            root.printTree(0);

            System.out.println("Sql statement : " + root.getSqlStatement());
        } else {
            System.out.println("Error : Invalid Filter");
        }
    }
}