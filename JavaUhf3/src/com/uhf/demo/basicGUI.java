package com.uhf.demo;

import javax.swing.*;
import javax.swing.JPanel;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by hp on 2020/10/12.
 */
public class basicGUI {

    public static void main(String[] args){
        UhfDemo demo = new UhfDemo();
        demo.main(args);
        JFrame jf = new JFrame();
        JPanel basic =  new JPanel();
        JTextField position = new JTextField();
        JTextField length = new JTextField();
        JTextField data = new JTextField();
        length.setBounds(200,200,200,200);
        position.setBounds(400,400,200,200);
        data.setBounds(600,600,200,200);
        JButton jb1 = new JButton("EPC read");
        JButton jb2 = new JButton("epc w");
        JButton jb3 = new JButton("u r");
        JButton jb4 = new JButton("u w");
        JButton jb5 = new JButton("t r");
        jf.setVisible(true);
        jf.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        jf.add(basic);
        basic.add(position);
        basic.add(length);
        basic.add(data);
        basic.add(jb1);basic.add(jb2);basic.add(jb3);basic.add(jb4);basic.add(jb5);
        jb1.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int pos = Integer.parseInt(position.getText());
                int l = Integer.parseInt(length.getText());
            }
        });

    }

}
