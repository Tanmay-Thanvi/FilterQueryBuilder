import java.util.List;

public class FilterTreeBuilder {

  public Filter preprocessFilter(Filter filter) {
    if (filter.getFilterCriterias().size() > 1) {
      FilterCriteria filterCriteria = new FilterCriteria("filteringCriteria", Operator.AND, filter.getFilterCriterias());
      filter.setFilterCriterias(List.of(filterCriteria));
    } else if (filter.getFilterCriterias().size() != 1) {
      return null;
    }

    return filter;
  }

  public static String getFilterQuery(FilterCriteria filterCriteria) {
      String FilterQuery = "";

//            RANGE,
//            NOT_IN_RANGE,
//            CONTAINS,
//            NOT_CONTAINS,
//            STARTS_WITH,
//            ENDS_WITH,

      switch(filterCriteria.getOperator()){
        case EQ:
          FilterQuery = filterCriteria.getKey() + " = " + filterCriteria.getValue();
          break;
        case NEQ:
          FilterQuery = filterCriteria.getKey() + " != " + filterCriteria.getValue();
          break;
        case GT:
          FilterQuery = filterCriteria.getKey() + " > " + filterCriteria.getValue();
          break;
        case GTE:
          FilterQuery = filterCriteria.getKey() + " >= " + filterCriteria.getValue();
          break;
        case LT:
          FilterQuery = filterCriteria.getKey() + " < " + filterCriteria.getValue();
          break;
        case LTE:
          FilterQuery = filterCriteria.getKey() + " <= " + filterCriteria.getValue();
          break;
        case IN:
          FilterQuery = filterCriteria.getKey() + " in (" + (List) filterCriteria.getValue() + ")";
          break;
        case NIN:
          FilterQuery = filterCriteria.getKey() + " not in (" + (List) filterCriteria.getValue() + ")";
          break;
        case IS_NULL:
          FilterQuery = filterCriteria.getKey() + " is null";
          break;
        case IS_NOT_NULL:
          FilterQuery = filterCriteria.getKey() + " is not null";
          break;
      }
      return FilterQuery;
  }

  private boolean isListOfFilterCriterias(Object value) {
    if (value instanceof List<?>) {
      List<?> list = (List<?>) value;

      boolean allFilterCriteria = list.stream().allMatch(item -> item instanceof FilterCriteria);

      if (!allFilterCriteria) {
        System.out.println("Value is a List, but not all elements are of type FilterCriteria.");
      }
      return allFilterCriteria;
    } else {
      System.out.println("Value is not a List of FilterCriteria.");
      return false;
    }
  }

  public FilterTreeNode buildParseTree(FilterCriteria filterCriteria) {
    FilterTreeNode root = null;

    if (LogicalOperators.operators.contains(filterCriteria.getOperator())){
      if (isListOfFilterCriterias(filterCriteria.getValue())){
        root = new FilterTreeNode(filterCriteria.getOperator().name());

        List<?> tempList = (List<?>) filterCriteria.getValue();
        List<FilterCriteria> filterCriterias = (List<FilterCriteria>) tempList;

        for (FilterCriteria theFilterCriteria : filterCriterias) {
          root.addChild(buildParseTree(theFilterCriteria));
        }

      } else {
        throw new RuntimeException("Err : FilterCriteria having Logical Operator always require List<FilterCriteria> as value");
      }
    } else {
      root = new FilterTreeNode(getFilterQuery(filterCriteria));
    }

    return root;
  }
}