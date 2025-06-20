package org.eclipse.om2m.ipe.sample.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.eclipse.om2m.ipe.sample.model.SampleModel;
import org.eclipse.om2m.ipe.sample.monitor.SampleMonitor;
import org.osgi.framework.FrameworkUtil;

public class GUI extends JFrame {
    static Log LOGGER = LogFactory.getLog(GUI.class);
    private static final long serialVersionUID = 1L;

    private JPanel contentPanel;

    static ImageIcon iconLampON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_ON.png"));
    static ImageIcon iconLampOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Lamp_OFF.png"));
    static ImageIcon iconButtonON = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_ON.png"));
    static ImageIcon iconButtonOFF = new ImageIcon(FrameworkUtil.getBundle(GUI.class).getResource("images/Btn_OFF.png"));

    static GUI frame;

    static JLabel LABEL_LAMP_0 = new JLabel("");
    static JLabel LABEL_LAMP_1 = new JLabel("");
    static JLabel LABEL_LAMP_2 = new JLabel("");

    static String LAMP_0 = "LAMP_0";
    static String LAMP_1 = "LAMP_1";
    static String LAMP_2 = "LAMP_2";

    static SampleModel.LampObserver lampObserver;

    public static void init() {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    frame = new GUI();
                    frame.setVisible(true);
                } catch (Exception e) {
                    LOGGER.error("GUI init Error", e);
                }
            }
        });
    }

    public static void stop() {
        SampleModel.deleteObserver(lampObserver);
        frame.setVisible(false);
        frame.dispose();
    }

    public GUI() {
        setLocationByPlatform(true);
        setVisible(false);
        setResizable(false);
        setTitle("Sample Simulated IPE");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setBounds((screenSize.width - 500) / 2, (screenSize.height - 820) / 2, 497, 820);

        contentPanel = new JPanel();
        contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPanel);
        contentPanel.setLayout(null);

        // 建立 LAMP_0 面板
        createLampPanel(10, 5, LABEL_LAMP_0, LAMP_0, "Switch LAMP_0");

        // 建立 LAMP_1 面板
        createLampPanel(10, 271, LABEL_LAMP_1, LAMP_1, "Switch LAMP_1");

        // 建立 LAMP_2 面板
        createLampPanel(10, 537, LABEL_LAMP_2, LAMP_2, "Switch LAMP_2");

        // 已刪除 switch all 按鈕與標籤
        lampObserver = new SampleModel.LampObserver() {
	    @Override
	    public void onLampStateChange(String lampId, boolean state) {
		setLabel(lampId, state);
	}
};
        SampleModel.addObserver(lampObserver);
    }

    private void createLampPanel(int x, int y, JLabel label, final String lampId, String labelText) {
        JPanel panel = new JPanel();
        panel.setBounds(x, y, 319, 260);
        panel.setBorder(new LineBorder(new Color(0, 0, 0), 1, true));
        panel.setLayout(null);
        contentPanel.add(panel);

        label.setIcon(iconLampOFF);
        label.setHorizontalTextPosition(SwingConstants.CENTER);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setBounds(10, 9, 154, 240);
        panel.add(label);

        JButton button = new JButton();
        button.setOpaque(false);
        button.setPressedIcon(iconButtonON);
        button.setIcon(iconButtonOFF);
        button.setBounds(187, 44, 122, 156);
        panel.add(button);

        JLabel switchLabel = new JLabel(labelText);
        switchLabel.setFont(new Font("Vani", Font.BOLD | Font.ITALIC, 14));
        switchLabel.setBounds(187, 199, 118, 29);
        panel.add(switchLabel);

        button.addActionListener(new java.awt.event.ActionListener() {
            @Override
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        SampleMonitor.switchLamp(lampId);
                    }
                }).start();
            }
        });
    }

    public static void setLabel(String appId, boolean newState) {
        JLabel label = null;
        if (appId.endsWith("LAMP_0")) {
            label = LABEL_LAMP_0;
        } else if (appId.endsWith("LAMP_1")) {
            label = LABEL_LAMP_1;
        } else if (appId.endsWith("LAMP_2")) {
            label = LABEL_LAMP_2;
        }
        if (label != null) {
            label.setIcon(newState ? iconLampON : iconLampOFF);
        }
    }
}

