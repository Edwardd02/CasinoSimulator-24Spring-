import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TempleAlertView extends JFrame {
    private TempleAlertModel templeAlertModel;

    private JLabel titleLabel;
    private JLabel postLabel;
    private JTextField betField;
    private JTextField guessField;
    private JButton jButtonExit;

    public TempleAlertView(TempleAlertModel model) {
        super("Temple's latest posts");
        this.templeAlertModel = model;

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Temple Alerts");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(titleLabel);

        postLabel = new JLabel("The latest current post from www.reddit.com/r/Temple:");
        postLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(postLabel);

        betField = new JTextField(10);
        add(betField);

        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left

        jButtonExit = new JButton("Exit");
        jButtonExit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                templeAlertModel.exit();
            }
        });
        add(jButtonExit);
        setLocationRelativeTo(null);
        setVisible(true);
    }


}
