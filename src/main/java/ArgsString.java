import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

public class ArgsString {
    private String schema;
    private String[] args;
    private boolean valid;
    private Set<Character> unexpectedArguments = new TreeSet<Character>();
    private Map<Character, String> stringArgs =
            new HashMap<>();
    private int numberOfArguments = 0;

    public ArgsString(String schema, String[] args) {
        this.schema = schema;
        this.args = args;
        valid = parse();
    }

    public boolean isValid() {
        return valid;
    }

    private boolean parse() {
        if (schema.length() == 0 && args.length == 0)
            return true;
        parseSchema();
        parseArguments();
        return unexpectedArguments.size() == 0;
    }

    private boolean parseSchema() {
        for (String element : schema.split(",")) {
            parseSchemaElement(element);
        }
        return true;
    }

    private void parseSchemaElement(String element) {
        if (element.length() == 1) {
            parseStringSchemaElement(element);
        }else if(element.contains("*")) {
            parseStringSchemaElement(element);
        }
    }

    private void parseStringSchemaElement(String element) {
        char c = element.charAt(0);
        if (Character.isLetter(c)) {
            stringArgs.put(c, null);
        }
    }

    private boolean parseArguments() {
        for (String arg : args)
            parseArgument(arg);
        return true;
    }

    private void parseArgument(String arg) {
        if (arg.startsWith("-"))
            parseElements(arg);
    }

    private void parseElements(String arg) {
        for (int i = 1; i < arg.length(); i++)
            parseElement(arg.charAt(i));
    }

    private void parseElement(char argChar) {
        if (isString(argChar)) {
            numberOfArguments++;
            setStringArg(argChar, "");
        } else
            unexpectedArguments.add(argChar);
    }

    private void setStringArg(char argChar, String value) {
        stringArgs.put(argChar, value);
    }

    private boolean isString(char argChar) {
        return stringArgs.containsKey(argChar);
    }

    public int cardinality() {
        return numberOfArguments;
    }

    public String usage() {
        if (schema.length() > 0)
            return "-[" + schema + "]";
        else
            return "";
    }

    public String errorMessage() {
        if (unexpectedArguments.size() > 0) {
            return unexpectedArgumentMessage();
        } else
            return "";
    }

    private String unexpectedArgumentMessage() {
        StringBuffer message = new StringBuffer("Argument(s) -");
        for (char c : unexpectedArguments) {
            message.append(c);
        }
        message.append(" unexpected.");
        return message.toString();
    }

    public String getString(char arg) {
        return stringArgs.get(arg);
    }
}

