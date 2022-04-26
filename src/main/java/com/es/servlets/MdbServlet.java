package com.es.servlets;

import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.TimeUnit;

@WebServlet("/mdb")
public class MdbServlet extends HttpServlet {
    @Resource(mappedName = "jms/queue")
    private ConnectionFactory connectionFactory;

    @Resource (mappedName = "jms/dest")
    private Queue queue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        /*try {
            sendJmsMessage(req.getParameter("msg"));
            TimeUnit.SECONDS.sleep(1);
            System.out.println("Hello world");
            List<String> strings = Files.readAllLines(Paths.get("E:\\Programs\\glassfish5\\file.txt"));
            String s = strings.get(0);
            if (s.length() >= 4) {
                System.out.println(s);
            }
        } catch (JMSException | InterruptedException e) {
            e.printStackTrace();
        }*/
        sendJmsMessage(req.getParameter("msg"));
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }


    private Message createJmsMessageFromDest(Session session, Object msgData) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(msgData.toString());
        return textMessage;
    }


    private void sendJmsMessage(Object msgData) {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageProducer producer = session.createProducer(queue);
            producer.send(createJmsMessageFromDest(session, msgData));
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}
