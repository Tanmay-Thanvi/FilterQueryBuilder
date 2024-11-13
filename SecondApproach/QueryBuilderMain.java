package SecondApproach;

import java.util.List;

public class QueryBuilderMain {

    public static void main(String[] args) {
        Filter filter = new Filter(
            List.of(
                new FilterCriteria("alertEntity", Operator.EQ, "PATIENT_PLANNER"),
                new FilterCriteria("createDateCrieria", Operator.OR,
                    List.of(
                        new FilterCriteria("createDate", Operator.LTE, "31 Aug"),
                        new FilterCriteria("createDate", Operator.IS_NULL, null)
                    )
                )
            )
        );

        //        FilterTreeBuilder filterTreeBuilder = new FilterTreeBuilder();
        //        Filter customFilter = filterTreeBuilder.preprocessFilter(filter);
        //
        //        if (customFilter != null) {
        //            FilterTreeNode root = filterTreeBuilder.buildParseTree(customFilter.getFilterCriterias().get(0));
        //
        //            System.out.println("Root : " + root);
        //
        //            // Print Tree Traversal
        //            System.out.println("Filter Parse Tree : ");
        //            root.printTree(0);
        //
        //            System.out.println("Sql statement : " + root.getSqlStatement());
        //        } else {
        //            System.out.println("Error : Invalid Filter");
        //        }
    }
}