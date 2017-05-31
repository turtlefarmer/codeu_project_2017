package codeu.chat.client.simplegui2;

import codeu.chat.client.ClientContext;
import codeu.chat.common.ConversationSummary;
import codeu.chat.common.Message;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by nora on 4/6/17.
 */
public class ChatMenu {



    private BorderPane mainWindow;
    private Scene scene;
    private Stage stage;
    private final ClientContext clientContext;
    private final DefaultListModel<String> messageListModel = new DefaultListModel<>();


    //ChatMenu constructor
    public ChatMenu(ClientContext clientContext){
        this.clientContext=clientContext;

        //initializes the different panels within the grid layout
        mainWindow=new BorderPane();
        mainWindow.setMinHeight(700);
        mainWindow.setMinWidth(700);

        //organize main Pane
        mainWindow.setTop(topMenuBar());
        mainWindow.setLeft(sideBar());
        mainWindow.setBottom(textBar());
        mainWindow.setCenter(centralTextBox());

        stage = new Stage();
        scene= new Scene(mainWindow);
        stage.setScene(scene);

        getAllMessages(clientContext.conversation.getCurrent());

    }

    //Public method to call in order to display the main chat stage
    public void display(){
        stage.show();
    }


    private HBox topMenuBar(){

        //create menu at top
        HBox topbar= new HBox(10);
        topbar.setMaxHeight(100);
        topbar.setPadding(new Insets(15, 12, 15, 12));
        topbar.setStyle("-fx-background-color: #336699;");

        //add buttons for different options
        final Button signOut = new Button("Sign Out");
        signOut.setPrefSize(100, 20);

        final Button addUser = new Button("Add User");
        addUser.setPrefSize(100, 20);

        final Button newConvo = new Button("New Conversation");
        newConvo.setPrefSize(100, 20);

        signOut.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (clientContext.user.hasCurrent()) {

                    boolean successfulSignOut= clientContext.user.signOutUser();

                } else {
                    AlertBox alertNoUser = new AlertBox("You are not signed in.", "You cannot sign out without being logged in.");
                    alertNoUser.display();
                }
            }
        });

        addUser.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {


                if (clientContext.user.hasCurrent()) {

                    AlertBox alertNoUser = new AlertBox("A user is signed in.", "Please sign out first.");
                    alertNoUser.display();

                } else {
                    NewUser addNewUser=new NewUser(clientContext);
                    final String s=addNewUser.getInput();
                    if (s != null && s.length() > 0) {
                        clientContext.user.addUser(s);
                        clientContext.user.signInUser(s);

                    }

                }
            }
        });

        newConvo.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                if (clientContext.user.hasCurrent()) {

                    NewConversation addConversationBox = new NewConversation(clientContext, "Add Conversation", "Enter new conversation name:" );
                    addConversationBox.enterNewConversation();
                    String s = addConversationBox.conversationInput();

                    if (s != null && s.length() > 0) {
                        clientContext.conversation.startConversation(s, clientContext.user.getCurrent().id);

                    }
                } else {
                    AlertBox alertNoUser = new AlertBox("You are not signed in.", "Please sign in before joining a conversation.");
                    alertNoUser.display();
                }
            }
        });


        //add buttons to menu
        topbar.getChildren().addAll(signOut, addUser, newConvo);

        return topbar;
    }

    private VBox sideBar(){

        //create sidebar
        VBox sidebar= new VBox(5);

        sidebar.setPadding(new Insets(3));
        sidebar.setAlignment(Pos.TOP_LEFT);
        sidebar.setMinWidth(200);

        //Label for box
        Label sideLabel=new Label("Conversations");

        //border for box
        sidebar.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4;"  +
                        "-fx-border-insets: 0;"+
                "-fx-border-radius: 5;" +
                "-fx-border-color: #336699;");

        sidebar.getChildren().addAll(sideLabel);

        return sidebar;

    }
    private HBox textBar(){

        //create menu at bottom
        HBox bottombar=new HBox(10);
        bottombar.setMinHeight(200);

        TextArea textBox=new TextArea();
        textBox.setWrapText(true);

        //border for box
        bottombar.setStyle("-fx-padding: 10;" +
                "-fx-border-style: solid inside;" +
                "-fx-border-width: 4;"  +
                "-fx-border-radius: 5;" +
                "-fx-border-color: #336699;");

        //Button to send messages.
        Button send = new Button("Send");
        send.setPrefSize(100, 20);
        send.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent actionEvent) {

                //if no user or current conversation, alert box
                if (!clientContext.user.hasCurrent()) {
                    AlertBox alertNoInput= new AlertBox("Cannot send message","You are not signed in");
                    alertNoInput.display();
                } else if (!clientContext.conversation.hasCurrent()) {
                    AlertBox alertNoInput= new AlertBox("Cannot send message","No current conversations");
                    alertNoInput.display();
                }
                else if(containsBadLanguage(textBox.getText())){
                    AlertBox alertNoInput= new AlertBox("Cannot send message","Cannot send an omitted word");
                    alertNoInput.display();
                }
                else {
                    final String messageText = textBox.getText();
                    if (messageText != null && messageText.length() > 0) {
                        clientContext.message.addMessage(
                                clientContext.user.getCurrent().id,
                                clientContext.conversation.getCurrentId(),
                                messageText);
                        ChatMenu.this.getAllMessages(clientContext.conversation.getCurrent());
                    }
                }

            }
        });

        //will be place to input text
        bottombar.getChildren().addAll(textBox, send);

        return bottombar;

    }
    private static VBox centralTextBox(){

        VBox centralBox=new VBox();

        TextArea messages=new TextArea();
        messages.setPrefSize(500,500);
        messages.setEditable(false);
        messages.setWrapText(true);

        centralBox.getChildren().addAll(messages);

        return centralBox;
    }

    private void getAllMessages(ConversationSummary conversation) {
        messageListModel.clear();

        for (final Message m : clientContext.message.getConversationContents(conversation)) {
            // Display author name if available.  Otherwise display the author UUID.
            final String authorName = clientContext.user.getName(m.author);

            final String displayString = String.format("%s: [%s]: %s",
                    ((authorName == null) ? m.author : authorName), m.creation, m.content);

            messageListModel.addElement(displayString);
        }
    }

    //pulls each word out of line and checks against list of omitted words
    private boolean containsBadLanguage(String userLine){

        boolean containsBadWord=false;
        int size= userLine.length();
        int position=0;

        ArrayList<String> separators = new ArrayList<>();
        commonSeparators(separators);


        String[] omittedWords= {"fuck", "bitch", "ass", "shit","damnit", "dammit", "hoe", "faggot"};

        while(position <size && !containsBadWord){

            String singleWord = nextWordOrSeparator(userLine, position,  separators);
            singleWord = singleWord.toLowerCase();
            for (String omitted: omittedWords) {
                if(singleWord.contains(omitted)){
                    containsBadWord=true;
                }
            }
            position+= singleWord.length();
        }

        return containsBadWord;



    }

    //returns a single word or a group of separators from starting position
    private static String nextWordOrSeparator(String text, int position,
                                              ArrayList<String> separators) {
        assert text != null : "Violation of: text is not null";
        assert separators != null : "Violation of: separators is not null";
        assert 0 <= position : "Violation of: 0 <= position";
        assert position < text.length() : "Violation of: position < |text|";

        boolean wordOrSep = separators
                .contains(text.substring(position, position + 1));
        int length = 0;

        //while characters match type of first found, increment length
        while (position + length < text.length()
                && separators.contains(text.substring(position + length,
                position + length + 1)) == wordOrSep) {

            length++;

        }

        //return the substring of given word or sep
        return text.substring(position, position + length);
    }

    private static void commonSeparators(ArrayList<String> sep) {

        sep.clear();

        sep.add("\t");
        sep.add("\n");
        sep.add("\r");
        sep.add(".");
        sep.add(",");
        sep.add(" ");
        sep.add("?");
        sep.add("\'");
        sep.add("\"");
        sep.add("-");
        sep.add("!");
        sep.add(";");
        sep.add(":");
        sep.add("/");
        sep.add("\\");
        sep.add("(");
        sep.add(")");
        sep.add("[");
        sep.add("]");
        sep.add("{");
        sep.add("}");
        sep.add("*");
        sep.add("`");

    }
}
