import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class BWButton extends JButton implements ActionListener{
    ImageIcon X,O,Pink;//Black and White pieces images
    int value=0;
	/*
	0:never pressed
	1:pressed
	2:already pressed
	*/
    /**returns value*/
    public int getValue(){
        return this.value;
    }
    /**sets value*/
    public void setValue(int num){
        this.value = num;
    }
    /**removes listener for this button*/
    public void removeButton(){ // make the button unpressable
        this.removeActionListener(this);
    }
    /**adds listener for button*/
    public void addButton(){ // make the button pressable
        this.addActionListener(this);
        this.value = 0;
    }
    /**default construction*/
    public BWButton(){ // setting the button to be pressable
        X=new ImageIcon(this.getClass().getResource("3.jpg"));//red
        O=new ImageIcon(this.getClass().getResource("4.jpg"));//bitcoin
        Pink=new ImageIcon(this.getClass().getResource("Pink.png"));
        this.addActionListener(this);
    }
    /**if pressed changes value*/
    public void actionPerformed(ActionEvent e){//if a button is pressed
        value = 1;
    }
}