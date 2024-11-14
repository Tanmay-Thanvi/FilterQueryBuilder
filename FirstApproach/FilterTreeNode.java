package FirstApproach;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FilterTreeNode {
    private String NodeValue;
    private List<FilterTreeNode> children;

    // Constructors
    public FilterTreeNode(String NodeValue) {
        this.NodeValue = NodeValue;
        this.children = new ArrayList<>();
    }

    public FilterTreeNode(String NodeValue, List<FilterTreeNode> children) {
        this.NodeValue = NodeValue;
        this.children = children;
    }

    // ToString
    @Override
    public String toString() {
        return "FilterTreeNode [nodeValue=" + NodeValue.toString() + ",\nchildren=" +
            children.stream()
                .filter(Objects::nonNull)  // Skip any null children
                .map(Object::toString)  // Convert each child to a String
                .collect(Collectors.joining(", ")) + "]";
    }

    // Getters and Setters
    public String getNodeValue() {
        return this.NodeValue;
    }

    public void setNodeValue(String nodeValue) {
        this.NodeValue = nodeValue;
    }

    public List<FilterTreeNode> getChildren() {
        return this.children;
    }

    public void setChildren(List<FilterTreeNode> children) {
        this.children = children;
    }

    public void addChild(FilterTreeNode child) {
        children.add(child);
    }

    // Traversal to get sql string
    public void printTree(int level) {
        FilterTreeNode node = this;
        if (node == null) return;

        // Indent based on level
        System.out.println(" ".repeat(level * 2) + "- " + node.NodeValue.toString());
        for (FilterTreeNode child : node.children) {
            child.printTree(level + 1);
        }
    }

    private static boolean isLogicalOperator(String value) {
        return LogicalOperators.operators.stream().map(Enum::name).anyMatch(value::equals);
    }

    public static String generateSqlDFS(FilterTreeNode root) {
        if (root == null) {
            return "";
        }

        // If root is a logical operator, process children with the operator
        if (isLogicalOperator(root.getNodeValue())) {
            StringBuilder sqlString = new StringBuilder("(");

            if (root.NodeValue == "NOT"){
                sqlString.append("NOT (");
            }

            for (int i = 0; i < root.getChildren().size(); i++) {
                FilterTreeNode child = root.getChildren().get(i);

                // Recursively generate SQL for each child
                sqlString.append(generateSqlDFS(child));

                if (root.NodeValue != "NOT"){
                    // Add the logical operator between children, except after the last child
                    if (i < root.getChildren().size() - 1) {
                        sqlString.append(" ").append(root.getNodeValue()).append(" ");
                    }
                }
            }

            if (root.NodeValue == "NOT"){
                sqlString.append(")");
            }
            sqlString.append(")");
            return sqlString.toString();
        } else {
            // If it's a condition, just return the condition string
            return root.getNodeValue();
        }
    }

    public String getSqlStatement() {
        String sqlString = "";

        // DFS from root
        FilterTreeNode node = this;
        return generateSqlDFS(this);
    }
}