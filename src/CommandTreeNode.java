import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class CommandTreeNode {
    boolean isEnd;
    String mParentName;
    public Map<Character, CommandTreeNode> mChilds;

    public CommandTreeNode() {
        isEnd = false;
        mParentName = "";
        mChilds = new HashMap<Character, CommandTreeNode>();
    }

    public void add(String aCommand, String aParentName) {
        if (Objects.equals(aCommand, "")) {
            isEnd = true;
            mParentName = aParentName;
        } else {
            char letter = aCommand.charAt(0);
            if (!mChilds.containsKey(letter)) {
                mChilds.put(letter, new CommandTreeNode());
            }
            mChilds.get(letter).add(aCommand.substring(1), aParentName);
        }
    }

    public String getParent(String aCommand) {
        String result = "";
        if (Objects.equals(aCommand, "")) {
            result = mParentName;
        } else {
            char letter = aCommand.charAt(0);
            if (mChilds.containsKey(letter)) {
                result = mChilds.get(letter).getParent(aCommand.substring(1));
            }
        }
        return result;
    }
}