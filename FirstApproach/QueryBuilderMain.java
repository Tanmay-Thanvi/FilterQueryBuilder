package FirstApproach;

import java.util.List;

public class QueryBuilderMain {

    public static void main(String[] args) {
        Filter filter = new Filter(
            List.of(
                new FilterCriteria("startDate1", Operator.GT, "Value 1"),
                new FilterCriteria("endDate2", Operator.LT, "Value 2"),
                new FilterCriteria("endDate3", Operator.LT, "Value 3"),
                new FilterCriteria("endDate4", Operator.LT, "Value 4"),
                new FilterCriteria("CrieriaList5", Operator.OR,
                    List.of(
                        new FilterCriteria("OrCrieriaList1", Operator.EQ, "VALUE5-1"),
                        new FilterCriteria("OrCrieriaList2", Operator.EQ, "VALUE5-2")
                    )
                ),
                new FilterCriteria("NotCriteriaList6", Operator.NOT, List.of(new FilterCriteria("endDate6", Operator.LT, "Value 6"))),
                new FilterCriteria("endDate7, endDate8", Operator.IS_NULL, "null")
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