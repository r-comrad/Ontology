import java.util.HashMap;
import java.util.Map;

public class CommandTreeNode {
    boolean isEnd = false;
    public Map<Character, CommandTreeNode> mChilds = new HashMap<Character, CommandTreeNode>();

    public void add(char[] aCommand, int aNumber) {
        char letter = aCommand[aNumber];

        if (!mChilds.containsKey(letter)) {
            mChilds.put(letter, new CommandTreeNode());
        }

        if (aCommand.length == aNumber + 1) {
            mChilds.get(letter).isEnd = true;
        } else {
            mChilds.get(letter).add(aCommand, aNumber + 1);
        }
    }
}