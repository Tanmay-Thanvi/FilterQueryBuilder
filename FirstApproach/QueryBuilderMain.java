package FirstApproach;

import java.util.List;

public class QueryBuilderMain {

    public static void main(String[] args) {
        Filter filter = new Filter(
            List.of(
                new FilterCriteria("startDate1", Operator.GT, "1 Aug"),
                new FilterCriteria("endDate2", Operator.LT, "31 Aug"),
                new FilterCriteria("endDate3", Operator.LT, "1 Oct"),
                new FilterCriteria("endDate4", Operator.LT, "31 Oct"),
                new FilterCriteria("CrieriaList5", Operator.OR,
                    List.of(
                        new FilterCriteria("OrCrieriaList1", Operator.EQ, "VALUE1"),
                        new FilterCriteria("OrCrieriaList2", Operator.EQ, "VALUE2")
                    )
                ),
                new FilterCriteria("NotCriteriaList", Operator.NOT, List.of(new FilterCriteria("endDate6", Operator.LT, "31 Oct"))),
                new FilterCriteria("endDate", Operator.IS_NULL, null)
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