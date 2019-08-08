import java.util.HashMap;
import java.util.Map;

public class CommandTreeNode {
    boolean isEnd = false;
    String mParentName = "";
    public Map<Character, CommandTreeNode> mChilds = new HashMap<Character, CommandTreeNode>();

    public void add(char[] aCommand, String aParentName) {
        add(aCommand, aParentName, 0);
    }

    private void add(char[] aCommand, String aParentName, int aNumber) {
        char letter = aCommand[aNumber];

        if (!mChilds.containsKey(letter)) {
            mChilds.put(letter, new CommandTreeNode());
        }

        if (aCommand.length == aNumber + 1) {
            mChilds.get(letter).isEnd = true;
            mChilds.get(letter).mParentName = aParentName;
        } else {
            mChilds.get(letter).add(aCommand, aParentName, aNumber + 1);
        }
    }

    public String getParent(char[] aCommand) {
        return getParent(aCommand, 0);
    }

    private String getParent(char[] aCommand, int aNumber) {
        char letter = aCommand[aNumber];
        String result = "";
        if (mChilds.containsKey(letter)) {
            if (aCommand.length == aNumber + 1) {
                result = mChilds.get(letter).mParentName;
            } else {
                result = mChilds.get(letter).getParent(aCommand, aNumber + 1);
            }
        }
        return result;
    }
}