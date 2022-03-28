package com.es.service;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.ejb.Singleton;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

@MessageDriven(mappedName = "jms/dest", activationConfig = {
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge"),
        @ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue")
})
public class MyMdb implements MessageListener {
    public static final Path PATH = Paths.get("E:\\Programs\\glassfish5\\file.txt");
    public static final Path DIGITS = Paths.get("E:\\Programs\\glassfish5\\Digits.txt");
    public static final Path LETTERS = Paths.get("E:\\Programs\\glassfish5\\Letters.txt");
    private List<String> messages = new ArrayList<>();

    @Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            byte[] buffer = (text + '\n').getBytes();
            try {
                Integer.parseInt(text);
            } catch (NumberFormatException e) {
                Files.write(LETTERS, buffer, StandardOpenOption.APPEND);
                return;
            }
            Files.write(DIGITS, buffer, StandardOpenOption.APPEND);

        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }

    }

    /*@Override
    public void onMessage(Message message) {
        TextMessage textMessage = (TextMessage) message;
        try {
            String text = textMessage.getText();
            int i = messages.indexOf(text);
            if (i >= 0) {
                String s = String.format("Same messages: %d, %d", i, messages.size());
                Files.write(PATH, s.getBytes());
                messages.clear();
            } else {
                messages.add(text);
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }*/

    /*@Override
    public void onMessage(Message msg) {
        TextMessage textMessage = (TextMessage) msg;
        try {
            String text = textMessage.getText();
            System.out.println(text);
            if (text.endsWith("!")) {
                byte[] buffer = (text + '\n').getBytes();
                Files.write(PATH, buffer, StandardOpenOption.APPEND);
            }
        } catch (JMSException | IOException e) {
            e.printStackTrace();
        }
    }*/
}
