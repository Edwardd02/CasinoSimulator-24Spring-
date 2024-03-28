import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class TempleAlertView extends JFrame {
    private TempleAlertModel templeAlertModel;

    private JLabel titleLabel;
    private JLabel postLabel;
    private JLabel postTitle;
    private JLabel postAuthor;
    private JTextArea postBody;
    private JLabel postTime;
    private JLabel spacerField;
    private JButton jButtonExit;

    public TempleAlertView(TempleAlertModel model) throws Exception {
        super("Temple's latest posts");
        this.templeAlertModel = model;
        model.startGame();
        model.fetchLatestTemplePost();

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(400, 300);
        getContentPane().setLayout(new BoxLayout(getContentPane(), BoxLayout.Y_AXIS));

        titleLabel = new JLabel("Temple Alerts");
        titleLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(titleLabel);

        postLabel = new JLabel("The latest current post from www.reddit.com/r/Temple:");
        postLabel.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(postLabel);

        spacerField = new JLabel("\n");
        add(spacerField);

        String title = model.getTitle();
        postTitle = new JLabel("Title: " + title);
        postTitle.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(postTitle);
        System.out.println(title);

        String author = model.getAuthor();
        postAuthor = new JLabel("Author: " + author);
        postAuthor.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(postAuthor);
        System.out.println(author);

        String time = model.getTime();
        postTime = new JLabel("Time Posted: " + time);
        postTime.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        add(postTime);
        System.out.println(time);

        String post = model.getBody();
        postBody = new JTextArea(post);
        postBody.setAlignmentX(Component.LEFT_ALIGNMENT); // Align to the left
        postBody.setLineWrap(true);
        postBody.setSize(300,1);
        add(postBody);
        System.out.println(post);

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
