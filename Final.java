import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.*;
import java.lang.StringBuilder;
import java.io.*;


public class Final {

  private static JTextArea textArea = new JTextArea(10,10);
  private static JTextArea colorTA = new JTextArea(400,300);//

  public static void main(String[] args){

    JFrame window = new JFrame("Color Finder");
    JPanel left = new JPanel();
    JPanel right = new JPanel();
    JSplitPane splitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT,
                           left,right);
    splitPane.setOneTouchExpandable(true);
    splitPane.setDividerLocation(400);

    Dimension minimumSize = new Dimension(100, 50);
    left.setMinimumSize(minimumSize);
    right.setMinimumSize(minimumSize);



    left.setLayout(new GridLayout(2,0));
    JPanel leftbar = new JPanel();
    leftbar.setLayout(new GridLayout(6,2));
    leftbar.add(new JLabel("Color Finding Machine")); leftbar.add(new JLabel(""));
    leftbar.add(new JLabel("Color Name: "));
    JTextField name = new JTextField("ColorName");leftbar.add(name);
    leftbar.add(new JLabel("Hex Code: "));
    JTextField code = new JTextField("Code (Enter in #FFFFFF format)");leftbar.add(code);
    JButton save = new JButton("Save");leftbar.add(save);
    JButton find = new JButton("Find");leftbar.add(find);
    //Ask for feedback
    leftbar.add(new JLabel("Is this app helpful?"));
    leftbar.add(new JLabel(""));
    //Jradio button and dialog
    ButtonGroup feedback=new ButtonGroup();
    JRadioButton yes=new JRadioButton("Yes");
    leftbar.add(yes);
    feedback.add(yes);
    JRadioButton no=new JRadioButton("No");
    leftbar.add(no);
    feedback.add(no);
    yes.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent a){
        JOptionPane.showMessageDialog(splitPane, "Thank you!");
      }
    });
    no.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent b){
        JOptionPane.showMessageDialog(splitPane, "We'll improve!");
      }
    });
    left.add(leftbar);

    //JTextArea textArea = new JTextArea(10,10);
    JScrollPane scrollPane = new JScrollPane(textArea);
    scrollPane.setPreferredSize(new Dimension(450, 110));
    left.add(scrollPane);

    //For displaying color
    right.setLayout(new GridLayout(0,1));
    colorTA.setBackground(Color.WHITE);//
    right.add(colorTA);//


    window.setContentPane(splitPane);
    window.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    window.setLocation(120,70);
    window.setSize(800,300);
    window.setVisible(true);

    //Create 2 hashmap
    Map<String,String> colorBook;
    colorBook = new HashMap<String,String>();

    Map<String,String> reverseColorBook;
    reverseColorBook = new HashMap<String,String>();
    //When save button is pressed,information should be stored to two files
    save.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String namet = name.getText();
        String codet = code.getText();
        colorBook.put(namet,codet);
        reverseColorBook.put(codet,namet);
        printColorBook(colorBook);
        writeMapToFile(colorBook,"name-code.txt");
        writeMapToFile(reverseColorBook,"code-name.txt");

      }
    });
    //When find button is pressed,it should return what user is trying to find
    //also show the color on right panel
    find.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent e){
        String code1 = code.getText();
        if(code1.equals("")){
          String namelook = name.getText();
          String codelookup = colorBook.get(namelook);
          if(codelookup==null){
            code.setText("unknown");
            colorTA.setBackground(Color.WHITE);
          }else{
            code.setText(""+codelookup);
            readMapFromFile("name-code.txt");
            Color colorcode = Color.decode(codelookup);
            colorTA.setBackground(colorcode);
          }
        }else{
          String codelook = code.getText();
          String colorlookup = reverseColorBook.get(codelook);
          Color color = Color.decode(codelook);
          colorTA.setBackground(color);
          if(colorlookup==null){
            name.setText("unknown");
            colorTA.setBackground(Color.WHITE);
          }else{
            name.setText(""+colorlookup);
            readMapFromFile("code-name.txt");
          }
        }
      }
    });

  }


  public static void printColorBook(Map<String,String>colorBook){
    StringBuilder str = new StringBuilder();
    Set<String> keys = colorBook.keySet();
    for(String color: keys){
      str.append("Color: ")
         .append(color)
         .append("; HexCode: ")
         .append(colorBook.get(color))
         .append("\n");
       }
    textArea.setText(str.toString());
  }

  //method to write information to a txt file
  public static void writeMapToFile(Map<String,String>colorBook,String filename){
    try {
      PrintWriter writer = new PrintWriter(filename, "UTF-8");
      Set<String> keys = colorBook.keySet();
      for(String color: keys){
        writer.println(color+":"+colorBook.get(color));
      }
      writer.close();
    } catch (Exception e){
      System.out.println("Problem writing to file: "+e);
    }
  }
  //method to read the file
  public static Map<String,String> readMapFromFile(String filename){
    Map<String,String> colorBook = new HashMap<String,String>();
    try{
      File file = new File(filename);
      Scanner scanner = new Scanner(file);
      while (scanner.hasNext()){
        String line = scanner.nextLine();
        int nomeaning = line.indexOf(":");
        String key = line.substring(0,nomeaning);
        String value = line.substring(nomeaning+1);
        colorBook.put(key,value);
      }
      scanner.close();
    } catch (Exception e){
      System.out.println("Problem reading map from file "+e);
    }
    return colorBook;
  }




}
