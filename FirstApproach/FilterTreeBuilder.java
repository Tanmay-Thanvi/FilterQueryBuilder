package FirstApproach;

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

    // Check if it's a logical operator
    if (LogicalOperators.operators.contains(filterCriteria.getOperator())) {
      if (filterCriteria.getKey().split(",").length == 1) {
        if (isListOfFilterCriterias(filterCriteria.getValue())) {
          root = new FilterTreeNode(filterCriteria.getOperator().name());

          List<?> tempList = (List<?>) filterCriteria.getValue();
          List<FilterCriteria> filterCriterias = (List<FilterCriteria>) tempList;

          for (FilterCriteria theFilterCriteria : filterCriterias) {
            root.addChild(buildParseTree(theFilterCriteria));
          }
        } else {
          throw new RuntimeException("Err: FilterCriteria with a logical operator must have List<FilterCriteria> as its value.");
        }
      } else {
        throw new RuntimeException("Err: FilterCriteria with a logical operator cannot have multiple keys separated by commas.");
      }
    } else {
      // Handle single key or comma-separated keys
      String[] keys = filterCriteria.getKey().split(",");
      if (keys.length == 1) {
        root = new FilterTreeNode(getFilterQuery(filterCriteria));
      } else {
        // Multiple keys, so treat this as an OR group
        root = new FilterTreeNode("OR");  // Create an OR root for these keys
        Operator operator = filterCriteria.getOperator();
        Object value = filterCriteria.getValue();

        for (String key : keys) {
          FilterCriteria singleKeyCriteria = new FilterCriteria(key, operator, value);
          FilterTreeNode childNode = buildParseTree(singleKeyCriteria);
          root.addChild(childNode);
        }
      }
    }

    return root;
  }
}