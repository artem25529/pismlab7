package com.es.servlets;

import javax.annotation.Resource;
import javax.jms.*;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@WebServlet("/mdb")
public class MdbServlet extends HttpServlet {
    @Resource(mappedName = "jms/queue")
    private ConnectionFactory connectionFactory;

    @Resource (mappedName = "jms/dest")
    private Queue queue;


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        try {
            sendJmsMessage(req.getParameter("msg"));
        } catch (JMSException e) {
            e.printStackTrace();
        }
        req.getRequestDispatcher("/index.jsp").forward(req, resp);
    }


    private Message createJmsMessageFromDest(Session session, Object msgData) throws JMSException {
        TextMessage textMessage = session.createTextMessage();
        textMessage.setText(msgData.toString());
        return textMessage;
    }


    private void sendJmsMessage(Object msgData) throws JMSException {
        try (Connection connection = connectionFactory.createConnection();
             Session session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE)) {
            MessageProducer producer = session.createProducer(queue);
            producer.send(createJmsMessageFromDest(session, msgData));
        }
    }
}
