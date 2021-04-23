package ISTB_19_2_Pervykh.menu;

import ISTB_19_2_Pervykh.DBWorker;
import ISTB_19_2_Pervykh.people.Group;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.sql.SQLException;
import javax.swing.*;
import javax.swing.table.*;

public class Menu_GUI extends JFrame {
    private MyTableModel tableModel;
    private DefaultTableModel tableModelSearch;
    private JTable table1;
    private JTable tableSearch;
    private JDialog popupNew = new JDialog(this, "Новый сотрудник", true);
    private JDialog popupSearch = new JDialog(this, "Поиск сотрудника", true);
    private JDialog popupDelete = new JDialog(this, "Удалить сотрудника", true);
    private JDialog popupTableSearch = new JDialog(this, "Таблица поиска", true);

    private JPanel buttonsPanel = new JPanel();
    private JButton newStaff = new JButton("Новый сотрудник");
    private JButton deleteStaff = new JButton("Удалить сотрудника");
    private JButton searchStaff = new JButton("Найти сотрудника");
    private JButton workStaff = new JButton("Заставить работать");

    private Object[] columnsHeader = new String[] {"Должность", "Имя",
            "Возраст"};

    public Menu_GUI() {
        super("Отдел кадров");
        this.setBounds(300, 500, 550, 600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        DBWorker.initDB();
        this.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                try {
                    DBWorker.closeConnection();
                } catch (SQLException ex) {
                    ex.printStackTrace();
                }
            }
        });

        tableModel = new MyTableModel(new Group());
        table1 = new JTable(tableModel);


        // Создание кнопки добавления сотрудника
        newStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    AddButtonEventListener addButtonEventListener=new AddButtonEventListener();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        // Создание кнопки удаления сотрудника
        deleteStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                DeleteButtonEventListener DeleteButtonEventListener=new DeleteButtonEventListener();
            }
        });

        // Поиск сотрудника
        searchStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try {
                    SearchButtonEventListener SearchButtonEventListener=new SearchButtonEventListener();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        });

        // Заставить сотрудника работать
        workStaff.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                try{
                    String staffWork[] = Group.getWorkStaff(Group.getStaff(table1.getSelectedRow()));
                    JOptionPane.showMessageDialog(null, "<html><h2>Обязанность: "+staffWork[0]+"</h2><h3>Ставка:"+staffWork[1]+"</h3>");
                }
                catch (IndexOutOfBoundsException | SQLException ex){
                    infoBox("Выберите сотрудника");
                }
            }
        });

        // Формирование интерфейса
        buttonsPanel.setLayout(new GridLayout(4,1));
        buttonsPanel.add(newStaff);
        buttonsPanel.add(deleteStaff);
        buttonsPanel.add(searchStaff);
        buttonsPanel.add(workStaff);
        getContentPane().add(buttonsPanel, BorderLayout.WEST);
        Box contents = new Box(BoxLayout.Y_AXIS);
        contents.add(new JScrollPane(table1));
        getContentPane().add(contents);
        this.pack();
    }

    // Создание кнопки добавления работника
    private class AddButtonEventListener extends JDialog {
        private JPanel staffPanel = new JPanel();
        private JPanel addPanel = new JPanel();
        private JPanel profPanel = new JPanel();
        private JPanel workPanel = new JPanel();
        private JLabel labelName = new JLabel("Имя:");
        private JLabel labelWork = new JLabel("Обязанность:");
        private JLabel labelProf = new JLabel("Профессия:");
        private JLabel labelAge = new JLabel("Возраст:");

        private JTextField textName = new JTextField(5);
        private JTextField textAge = new JTextField(5);

        private String profSelect, workSelect, name, age;

        public AddButtonEventListener() throws SQLException {
            JButton subButton = new JButton("Добавить");
            popupNew.setPreferredSize(new Dimension(400,350));

            //Панель выбора профессии
            ButtonGroup groupProf = new ButtonGroup();
            profPanel.setLayout(new GridLayout(2,1));
            for (String data : DBWorker.getAllProfession()){
                System.out.println(data);
                final JRadioButton button1 = new JRadioButton(data);
                button1.setActionCommand(data);
                groupProf.add(button1);
                profPanel.add(button1);
            }
            //Панель выбора обязанности
            ButtonGroup groupWork = new ButtonGroup();
            workPanel.setLayout(new GridLayout(2,1));
            for (String data : DBWorker.getAllDuty()){
                System.out.println(data);
                final JRadioButton button1 = new JRadioButton(data);
                button1.setActionCommand(data);
                groupWork.add(button1);
                workPanel.add(button1);
            }
            addPanel.setLayout(new GridLayout(4,1));
            addPanel.add(labelProf);
            addPanel.add(profPanel);
            addPanel.add(labelWork);
            addPanel.add(workPanel);

            staffPanel.setLayout(new GridLayout(2,2));
            staffPanel.add(labelName);
            staffPanel.add(textName);
            staffPanel.add(labelAge);
            staffPanel.add(textAge);

            popupNew.add(new JScrollPane(addPanel), BorderLayout.NORTH);
            popupNew.add(staffPanel, BorderLayout.CENTER);
            popupNew.add(subButton, BorderLayout.SOUTH);

            // Добавление нового сотрудника при нажатии на кнопку
            subButton.addActionListener(new ActionListener() {
               @Override
               public void actionPerformed(ActionEvent e) {
                   name = textName.getText();
                   age = textAge.getText();

                   //Проверка имени
                   boolean nameTrue = Menu_control.staffName(name);

                   //Проверка возраста
                   boolean ageTrue = Menu_control.staffAge(age);


                   try{
                       profSelect = groupProf.getSelection().getActionCommand();
                       workSelect = groupWork.getSelection().getActionCommand();
                   }
                   catch (NullPointerException ex){
                       infoBox("Выберите профессия и/или обязанность");
                   }

                   if (nameTrue && ageTrue && (profSelect != null) && (workSelect != null)) {
                       String[] staff = new String[]{profSelect, name, age, workSelect};
                       // Добавление нового сотрудника
                       try {
                           Group.add(staff);
                       } catch (SQLException ex) {
                           ex.printStackTrace();
                       }
                       tableModel.fireTableDataChanged();
                       textName.setText("");
                       textAge.setText("");
                       name = "";
                       age = "";
                   }
                }
            }
            );

            popupNew.setLocationRelativeTo(null);
            popupNew.setResizable(false);
            popupNew.pack();
            popupNew.setVisible(true);
            popupNew.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);

        }

    }

    // Удаление сотрудника
    private class DeleteButtonEventListener extends JDialog {
        private JPanel deletePanel = new JPanel();
        private JLabel labelDelete = new JLabel("Удалить по имени:");
        private JTextField textName = new JTextField(5);
        private JButton deleteButton = new JButton("Удалить");

        public DeleteButtonEventListener() {
            popupDelete.setPreferredSize(new Dimension(200,150));
            deletePanel.setLayout(new GridLayout(3,1));
            deletePanel.add(labelDelete);
            deletePanel.add(textName);
            deletePanel.add(deleteButton);
            popupDelete.add(deletePanel, BorderLayout.CENTER);

            deleteButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    //Удаление сотрудника по имени
                    try {
                        Group.delete(textName.getText());
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                    tableModel.fireTableDataChanged();
                    textName.setText("");
                }
            });

            popupDelete.setLocationRelativeTo(null);
            popupDelete.setResizable(false);
            popupDelete.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
            popupDelete.pack();
            popupDelete.setVisible(true);
        }
    }

    // Создание кнопки поиска работника
    private class SearchButtonEventListener extends JDialog {
        private JPanel staffPanel = new JPanel();
        private JPanel namePanel = new JPanel();
        private JLabel labelWork = new JLabel("Поиск по должности:");
        private JLabel labelName = new JLabel("Поиск по имени:");
        private JTextField textName = new JTextField(5);
        private JButton workButton = new JButton("Поиск по должности");
        private JButton nameButton = new JButton("Поиск по имени");
        private String radioButtonSelect;

        public SearchButtonEventListener() throws SQLException {
            popupTableSearch.setPreferredSize(new Dimension(300,250));
            tableModelSearch = new DefaultTableModel(columnsHeader, 0);
            tableModelSearch.setColumnIdentifiers(columnsHeader);

            popupSearch.setPreferredSize(new Dimension(300,250));

            ButtonGroup groupProf = new ButtonGroup();
            staffPanel.setLayout(new GridLayout(2,1));

            for (String data : DBWorker.getAllProfession()){
                System.out.println(data);
                final JRadioButton button1 = new JRadioButton(data);
                button1.setActionCommand(data);
                groupProf.add(button1);
                staffPanel.add(button1);
            }
            popupSearch.add(labelWork, BorderLayout.NORTH);
            popupSearch.add(new JScrollPane(staffPanel), BorderLayout.CENTER);

            namePanel.setLayout(new GridLayout(4,1));
            namePanel.add(workButton);
            namePanel.add(labelName);
            namePanel.add(textName);
            namePanel.add(nameButton);
            popupSearch.add(namePanel, BorderLayout.SOUTH);

            SearchPopupButtonEventListener SearchPopupButtonEventListener=new SearchPopupButtonEventListener();
            workButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    try{
                        radioButtonSelect = groupProf.getSelection().getActionCommand();
                    }
                    catch (NullPointerException ex){
                        infoBox("Выберите профессия и/или обязанность");
                    }

                    if(Group.searchProff(radioButtonSelect) != null){
                        updateTableSearch();
                    }

                }
            });

            nameButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    if(Group.searchName(textName.getText()) != null){
                        updateTableSearch();
                    }
                    textName.setText("");
                }
            });
            popupSearch.setLocationRelativeTo(null);
            popupSearch.setResizable(false);
            popupSearch.pack();
            popupSearch.setVisible(true);
            popupSearch.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        }
    }

    private class SearchPopupButtonEventListener extends JDialog {

        public SearchPopupButtonEventListener() {
            tableSearch = new JTable(tableModelSearch);
            popupTableSearch.add(new JScrollPane(tableSearch));
            popupTableSearch.setLocationRelativeTo(null);
            popupTableSearch.setResizable(false);
            popupTableSearch.pack();
            popupTableSearch.setDefaultCloseOperation(popupTableSearch.DISPOSE_ON_CLOSE);
        }
    }

    // Вывод сообщения
    public static void infoBox(String infoMessage) {
        JOptionPane.showMessageDialog(null, "<html><h2>Ошибка!</h2>" + infoMessage);
    }

    // Обновление таблицы поиска
    private void updateTableSearch(){
        tableModelSearch.setRowCount(0);
        for (int i = 0; i < Group.staffSearch.size(); i++){
            String staff = Group.staffSearch.get(i).getProfession();
            String name = Group.staffSearch.get(i).getName();
            int age = Group.staffSearch.get(i).getAge();
            tableModelSearch.addRow(new Object[]{staff, name, age});
        }
        popupTableSearch.setVisible(true);
    }
}