import javax.swing.*;

public class MainWindow extends JFrame {
    public MainWindow() {
        setTitle("Змейка");
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        setSize(600, 600);
        setLocation(470, 125);
        add(new ProgramPanel());
        setVisible(true);
    }

    public static void main(String[] args) {
        MainWindow mw = new MainWindow();
    }
}