/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.Arrays;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import itc_tuition_booking_system.LessonClass.subjects;
import itc_tuition_booking_system.LessonClass.tutors;
import itc_tuition_booking_system.UserClass.roles;


public class ITC_tuition_booking_system 
{ 
    // #### time and space values 
    private final int numberOfWeeks = 8;
    private final int daysPerWeek = 5;
    private final int totalSlots = 12; // rooms * hours

    // #### CREATE DATA MODEL ##################################################
    protected final DaysArrayClass[] weekArray = new DaysArrayClass[numberOfWeeks];
    // #### list to store all users
    private final ArrayList<UserClass> userList = new ArrayList<>();
    
    // variables used in GUI
    protected int weekNo = 1;
    protected String userLoggedIn = null;
    protected String loggedInName = null;
    protected roles loggedInRole = null;
    private int idToTest = 0;    
    private boolean validUser = false;
    private int selectedTab = 0;
    private boolean byTutorFlag = false;
    private boolean bySubjectFlag = false;
    private boolean byAllFlag = true;
    // to store value selected in subject combo
    private subjects subComb;
    // to store value selected in tutor combo
    private tutors tutComb;
    // store table model initialised in createtimetablepanel method
    private DefaultTableModel main_dtm;
    // store temporary table selections
    private int rowSelected = 0;
    private int colSelected = 0;
        
    // 2 dimensional string array passed to timetable
    // 5 days * 12 slots
    protected String[][] classes = {
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null }};
        
    private final String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri"};
    
    // list of subjects for combobox
    private final subjects[] subjectsL = { LessonClass.subjects.ENGLISH, LessonClass.subjects.FRENCH, LessonClass.subjects.MATHS,
                                LessonClass.subjects.SCIENCE, LessonClass.subjects.COMPUTERS, LessonClass.subjects.PARENT};
    
    // list of tutors for combo box
    private final tutors[] tutorsL = { tutors.Dave_Smith, tutors.Sue_Willis, tutors.John_Jove, tutors.Bob_Bobson, tutors.Sally_Jones };
    

    // array representing daily lesson slots - matches Keys in dayMap from DayObjectClass
    private final String[] roomNtime = { "5A", "5B", "5C", "6A", "6B", "6C", "7A", "7B", "7C", "8A", "8B", "8C" };

    private final String[] attendance = { "Unmodified", "Present", "Absent" };

    
    // #### USE MAIN TO CALL STARTUP FROM EVENT DISPATCH THREAD#################
    
    public static void main(String[] args) 
    {
        SwingUtilities.invokeLater(() -> new ITC_tuition_booking_system().Startup() );
    }
    
    public void Startup()
    {
        
        // ##### POPULATE DATA MODEL ###########################################
        
        for (int i = 0; i < weekArray.length; i++)
        {
            weekArray[i] = new DaysArrayClass();
        }
        
        // #### POPULATE USER LIST WITH STUDENTS
        
        userList.add(new StudentClass("Bob", "Jones", "01544987654", "2 High Street, Wonderland"));
        userList.add(new StudentClass("Sam", "Williams", "01454111222", "3 Maple Grove, Mars"));
        userList.add(new StudentClass("Simon", "Says", "01898699669", "4 Grove Lane, Buxton"));
        userList.add(new StudentClass("Hilary", "Sanders", "01123 875547", "32 Long Shot, Lambert"));
        userList.add(new StudentClass("Tom", "Strange", "01420 204420", "52 York Road, Itchyfoot"));
        userList.add(new StudentClass("Geoff", "Jazz", "01741321661", "80 Throwback Street, Barnaby"));
        userList.add(new StudentClass("El", "Producto", "01234554612", "71 Tickerton, Madison"));
        userList.add(new StudentClass("Mike", "Asesino", "01120 774331", "15 Square Garden, Grange Hill"));
        userList.add(new StudentClass("Anu", "Utari", "01937 332211", "10 Sidthe, Eriu"));
        userList.add(new StudentClass("Francis", "Wellington", "0800 710723", "13 The Channel, Wilmsly"));
        userList.add(new StudentClass("Joe", "Frasier", "01342 779886", "99 The Rise, Edgeton"));
        userList.add(new StudentClass("Fred", "Bloggs", "01843 721546", "18 The Lark, Ingpop"));
        userList.add(new StudentClass("Susan", "Smith", "01224 414444", "33 Hilltop Road, Flipington"));
        userList.add(new StudentClass("Omar", "Little", "01597 664338", "23 Riprun, Omardontscare"));
        userList.add(new StudentClass("Bunk", "Moreland", "02000 0002000", "4 Traintracks, Patrolcar"));
        
        // #### POPULATE USER LIST WITH TUTORS
        
        userList.add(new TutorClass("Dave", "Smith", "01234123456", "1 The Road, Kimton", 
            new ArrayList<>(Arrays.asList(LessonClass.subjects.ENGLISH, LessonClass.subjects.MATHS, LessonClass.subjects.COMPUTERS))));
        userList.add(new TutorClass("Sue", "Willis", "01432987654", "1 The Street, Barnton", 
            new ArrayList<>(Arrays.asList(LessonClass.subjects.FRENCH, LessonClass.subjects.MATHS))));
        userList.add(new TutorClass("John", "Jove", "01652777555", "2 High street, Wislop", 
            new ArrayList<>(Arrays.asList(LessonClass.subjects.MATHS, LessonClass.subjects.SCIENCE))));
        userList.add(new TutorClass("Bob", "Bobson", "01414222333", "32 The Road, Kimsly", 
            new ArrayList<>(Arrays.asList(LessonClass.subjects.ENGLISH, LessonClass.subjects.COMPUTERS, LessonClass.subjects.FRENCH))));
        userList.add(new TutorClass("Sally", "Jones", "01348635638", "5 Long Drive, Upton", 
            new ArrayList<>(Arrays.asList(LessonClass.subjects.SCIENCE))));
        
        
        
        // POPULATE FULL TERM OF LESSONS INTO DATA MODEL 
        // (new instances ensure unique instances throughout term)
        
        for (int i = 0; i < numberOfWeeks; i++)
        {
            // ### LESSONS ###
            weekArray[i].dayArray[1].dayMap.put("5A", new LessonClass(LessonClass.subjects.MATHS, 
                    LessonClass.rooms.ROOM_A, "Tuesday", 5, tutors.Dave_Smith, 5));
            
            weekArray[i].dayArray[2].dayMap.put("5A", new LessonClass(LessonClass.subjects.ENGLISH, 
                    LessonClass.rooms.ROOM_A, "Wednesday", 5, tutors.Dave_Smith, 5));
            
            weekArray[i].dayArray[2].dayMap.put("6B", new LessonClass(LessonClass.subjects.MATHS, 
                    LessonClass.rooms.ROOM_B, "Wednesday", 6, tutors.Sue_Willis, 5));
            
            weekArray[i].dayArray[2].dayMap.put("5C", new LessonClass(LessonClass.subjects.FRENCH, 
                    LessonClass.rooms.ROOM_C, "Wednesday", 5, tutors.Sue_Willis, 5));
            
            weekArray[i].dayArray[0].dayMap.put("5A", new LessonClass(LessonClass.subjects.MATHS, 
                    LessonClass.rooms.ROOM_A, "Monday", 5, tutors.John_Jove, 5));
            
            weekArray[i].dayArray[3].dayMap.put("7B", new LessonClass(LessonClass.subjects.SCIENCE, 
                    LessonClass.rooms.ROOM_B, "Thursday", 7, tutors.John_Jove, 5));
            
            weekArray[i].dayArray[4].dayMap.put("5C", new LessonClass(LessonClass.subjects.FRENCH, 
                    LessonClass.rooms.ROOM_C, "Friday", 5, tutors.Bob_Bobson, 5));
            
            weekArray[i].dayArray[1].dayMap.put("6A", new LessonClass(LessonClass.subjects.COMPUTERS, 
                    LessonClass.rooms.ROOM_A, "Tuesday", 6, tutors.Bob_Bobson, 5));
            
            weekArray[i].dayArray[0].dayMap.put("6B", new LessonClass(LessonClass.subjects.SCIENCE, 
                    LessonClass.rooms.ROOM_B, "Monday", 6, tutors.Sally_Jones, 5));
            
            weekArray[i].dayArray[0].dayMap.put("8C", new LessonClass(LessonClass.subjects.SCIENCE, 
                    LessonClass.rooms.ROOM_C, "Monday", 8, tutors.Sally_Jones, 5));
            
            
            // ### APPOINTMENTS FOR PARENTS ###
            weekArray[i].dayArray[0].dayMap.put("8A", new LessonClass(LessonClass.subjects.PARENT, 
                    LessonClass.rooms.ROOM_A, "Monday", 8, tutors.Dave_Smith, 3));
            
            weekArray[i].dayArray[1].dayMap.put("8A", new LessonClass(LessonClass.subjects.PARENT, 
                    LessonClass.rooms.ROOM_A, "Tuesday", 8, tutors.Sue_Willis, 3));
            
            weekArray[i].dayArray[2].dayMap.put("8A", new LessonClass(LessonClass.subjects.PARENT, 
                    LessonClass.rooms.ROOM_A, "Wednesday", 8, tutors.John_Jove, 3));
            
            weekArray[i].dayArray[3].dayMap.put("8A", new LessonClass(LessonClass.subjects.PARENT, 
                    LessonClass.rooms.ROOM_A, "Thursday", 8, tutors.Bob_Bobson, 3));
            
            weekArray[i].dayArray[4].dayMap.put("8A", new LessonClass(LessonClass.subjects.PARENT, 
                    LessonClass.rooms.ROOM_A, "Friday", 8, tutors.Sally_Jones, 3));
        }
        
        // add a test booking
        // first week, tuesday, room A, 5pm = maths
        LessonClass t = weekArray[0].dayArray[1].dayMap.get("5A");
        t.bookSpace("90013");
        
        // populate table data from data model
        MakeTableData();
        
        // #### label showing logged in user info ###################################
        JLabel userInfo = new JLabel("Not logged in", JLabel.CENTER);
        userInfo.setFont(new Font("serif", Font.PLAIN, 32));
        
        
        // #### SETUP MENUS ####################################################
        
        // #### STUDENT / TUTOR LOGIN ##########################################
        
        JMenuItem sLogin = new JMenuItem("Student / Tutor login");
        sLogin.addActionListener(e -> 
        {
            if (userLoggedIn == null)
            {
                JLabel message = new JLabel("Enter ID:");
                JTextField who = new JTextField();
                Object[] m = { message, who };
                JOptionPane.showMessageDialog(null, m, "PLEASE LOGIN", JOptionPane.INFORMATION_MESSAGE);
                
                try
                {
                    idToTest = Integer.parseInt(who.getText());
                }
                catch (NumberFormatException pe)
                {
                    JOptionPane.showMessageDialog(null, "ENTER ID AS NUMBER", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
                
                userList.forEach((s) -> 
                {
                    if (s.ID == idToTest)
                    {
                        validUser = true;
                        loggedInName = (s.firstName + " " + s.surname + " " + s.ID);
                        loggedInRole = s.role;
                    }    
                });
                
                if (validUser == true)
                {
                    userLoggedIn = who.getText();
                    System.out.println("User: " + userLoggedIn);
                    System.out.println("Role: " + loggedInRole);
                    userInfo.setText(loggedInName + " logged in");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "INVALID USER", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "USER ALREADY LOGGED IN", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // #### PARENT LOGIN ###################################################

        JMenuItem pLogin = new JMenuItem("Parent login");
        pLogin.addActionListener(e -> 
        {
            if (userLoggedIn == null)
            {
                JLabel message = new JLabel("Enter offspring's ID:");
                JTextField who = new JTextField();
                Object[] m = { message, who };
                JOptionPane.showMessageDialog(null, m, "PLEASE LOGIN", JOptionPane.INFORMATION_MESSAGE);
                
                try
                {
                    idToTest = Integer.parseInt(who.getText());
                }
                catch (NumberFormatException pe)
                {
                    JOptionPane.showMessageDialog(null, "ENTER ID AS NUMBER", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
                
                userList.forEach((s) -> 
                {
                    if (s.ID == idToTest)
                    {
                        if (s.role != roles.TUTOR)
                        {
                            validUser = true;
                            loggedInName = ("Parent Of: " + s.firstName + " " + s.surname + " " + s.ID);
                            loggedInRole = roles.PARENT;
                        }
                        
                    }    
                });
                
                if (validUser == true)
                {
                    userLoggedIn = who.getText();
                    System.out.println("User: " + userLoggedIn);
                    System.out.println("Role: " + loggedInRole);
                    userInfo.setText(loggedInName + " logged in");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "INVALID USER", "ERROR", JOptionPane.WARNING_MESSAGE);
                }
            }
            else
            {
                JOptionPane.showMessageDialog(null, "USER ALREADY LOGGED IN", "ERROR", JOptionPane.WARNING_MESSAGE);
            }
        });
        
        // #### LOGOUT #########################################################
        
        JMenuItem logout = new JMenuItem("Logout");
        logout.addActionListener(e -> 
        {
            userLoggedIn = null;
            userInfo.setText("Not logged in");
            validUser = false;
            loggedInName = null;
            idToTest = 0;
            loggedInRole = null;
        });
        
        // #### REPORT MENU ####
        
        JMenuItem userReport = new JMenuItem("Show All Users");
        userReport.addActionListener( e -> 
        {
            ShowAllUsers();
        });
        
        JMenuItem fourWeekLessonReport = new JMenuItem("Four Week Lesson Report");
        fourWeekLessonReport.addActionListener( e -> 
        {
            System.out.println("4 week report");
            AllWeekLessonReport(4);
        });
        
        JMenuItem eightWeekLessonReport = new JMenuItem("Eight Week Lesson Report");
        eightWeekLessonReport.addActionListener( e -> 
        {
            System.out.println("8 week report");
            AllWeekLessonReport(8);
        });
        
        JMenuItem fourWeekAppointmentReport = new JMenuItem("Four Week Parent Appointment Report");
        fourWeekAppointmentReport.addActionListener( e -> 
        {
            AllWeekAppointmentReport(4);
        });
        
        JMenuItem eightWeekAppointmentReport = new JMenuItem("Eight Week Parent Appointment Report");
        eightWeekAppointmentReport.addActionListener( e -> 
        {
            AllWeekAppointmentReport(8);
        });
        
        JMenuItem allStudentAttendanceReport = new JMenuItem("All Student Attendance Report");
        allStudentAttendanceReport.addActionListener( e -> 
        {
            //System.out.println("All student atttendance report");
            AllStudentAttendanceReport();
        });
        
        
        // ### ADD NEW USER MENU ####
        JMenuItem addNewUser = new JMenuItem("Add New Student");
        addNewUser.addActionListener( e -> 
        {
            //System.out.println("add new user code here");
            
            JLabel firstNameMessage = new JLabel("Enter first name:");
            JTextField firstName = new JTextField();
            
            JLabel surnameMessage = new JLabel("Enter surname:");
            JTextField surname = new JTextField();
            
            JLabel telephoneMessage = new JLabel("Enter telephone:");
            JTextField telephone = new JTextField();
            
            JLabel addressMessage = new JLabel("Enter address:");
            JTextField address = new JTextField();
            
            
            Object[] m = { firstNameMessage, firstName, surnameMessage, surname, telephoneMessage, telephone, addressMessage, address };
            JOptionPane.showMessageDialog(null, m, "NEW USER CREDENTIALS", JOptionPane.INFORMATION_MESSAGE);
            
            userList.add(new StudentClass(firstName.getText(), surname.getText(), telephone.getText(), address.getText()));

        });
        
        
        
        // #### SETUP MENUS ####################################################
        
        JMenu mainLogin = new JMenu("User Login");
        mainLogin.add(sLogin);
        mainLogin.add(pLogin);
        mainLogin.addSeparator();
        mainLogin.add(logout);
        
        JMenu reportMenu = new JMenu("Reports");
        reportMenu.add(userReport);
        reportMenu.add(fourWeekLessonReport);
        reportMenu.add(eightWeekLessonReport);
        reportMenu.add(fourWeekAppointmentReport);
        reportMenu.add(eightWeekAppointmentReport);
        reportMenu.add(allStudentAttendanceReport);
        
        JMenu newUser = new JMenu("Add User");
        newUser.add(addNewUser);
        
        JMenuBar menuBar = new JMenuBar();
        menuBar.add(mainLogin);
        menuBar.add(reportMenu);
        menuBar.add(newUser);
        

        // ##### CREATE SWING TIMETABEL PANELS #################################
        
        JPanel ttpanel1 = CreateTimeTablePanel(" WHAT'S ON ", classes);
        JPanel ttpanel2 = CreateTimeTablePanel(" MY DIARY ", classes);
        
        // #### CREATE TABBED PANES ############################################
        
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.add("WHAT'S ON", ttpanel1);
        tabbedPane.add("MY DIARY", ttpanel2);
        
        // listen for tabbed pane change
        tabbedPane.addChangeListener( e -> 
        {
            selectedTab = tabbedPane.getSelectedIndex();
            MakeTableData();
            main_dtm.setDataVector(classes, days);
        });
        
        // #### CREATE & POPULATE MAIN PANEL ###################################
        
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 20, 20, 20));
        mainPanel.add(userInfo, BorderLayout.NORTH);
        mainPanel.add(tabbedPane);
        
        // #### CREATE FRAME & SET CONTENT #####################################
        
        // frame stuff
        JFrame frame = new JFrame("ITC Tuition Booking System - Chris Nelson UoH 2020");
        // special add menu function
        frame.setJMenuBar(menuBar);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(mainPanel);
        frame.setLocationRelativeTo(null);
        frame.setSize(970, 696);
        frame.setResizable(false);
        frame.setVisible(true);
    }
    
    // loop weeks, update label
    public void IncWeek(JLabel weeknoLabel)
    {
        weekNo += 1;
        if (weekNo > numberOfWeeks) { weekNo = 1; }
        weeknoLabel.setText(" WEEK: " + weekNo);
    }
    
    public void DecWeek(JLabel weeknoLabel)
    {
        weekNo -= 1;
        if (weekNo < 1) { weekNo = numberOfWeeks; }
        weeknoLabel.setText(" WEEK: " + weekNo);
    }
    
    
    public JPanel CreateTimeTablePanel(String title, String[][] classesData)
    {
        // ############# PANELS ################################################
        
        // main panel
        JPanel timetablePanel = new JPanel(new BorderLayout());
        timetablePanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // north panel
        JPanel northPanel = new JPanel(new BorderLayout());
        //northPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        // center panel
        JPanel centerPanel = new JPanel(new BorderLayout());
        
        // east panel
        JPanel eastPanel = new JPanel(new GridLayout(4, 2));
        
        // refresh sub panel
        JPanel refreshSubPanel = new JPanel(new BorderLayout());
        refreshSubPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        
        
        // ############### NORTH PANEL #########################################
        
        // WHAT'S on LABEL
        JLabel whatsonLabel = new JLabel(title, JLabel.CENTER);
        whatsonLabel.setFont(new Font("serif", Font.PLAIN, 32));
        northPanel.add(whatsonLabel, BorderLayout.NORTH);
        
        // WEEK # LABEL
        JLabel weeknoLabel = new JLabel(" WEEK: " + weekNo, JLabel.CENTER);
        weeknoLabel.setFont(new Font("serif", Font.BOLD, 32));
        northPanel.add(weeknoLabel, BorderLayout.CENTER);
        
        // buffer label - aesthetic only
        JLabel buffy = new JLabel(" ");
        northPanel.add(buffy, BorderLayout.SOUTH);
        
        // ############### CENTER PANEL ########################################
        
        // timetable
        JTable table = new JTable()
        {
            @Override
            public boolean isCellEditable(int row, int column)
            {
                return false;
            }
        };
        DefaultTableModel dtm = new DefaultTableModel(classesData, days);
        table.setModel(dtm);
        main_dtm = dtm;
        
        // table properties
        Font font = new Font("Verdana", Font.PLAIN, 12);
        table.getTableHeader().setReorderingAllowed(false);
        table.getTableHeader().setResizingAllowed(false);
        table.setSelectionBackground(Color.YELLOW);
        table.setCellSelectionEnabled(true);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setFont(font);
        table.setRowHeight(30);
        centerPanel.add(new JScrollPane(table));
                
        
        // ##### buttons to be used in following mouselistener #################
        
        JButton bookButton = new JButton("Book");
        bookButton.addActionListener(e ->
        {
            LessonClass t = weekArray[weekNo -1].dayArray[colSelected].dayMap.get(roomNtime[rowSelected]);
            if (!t.isUserBooked(userLoggedIn))
            {
                if (!DoesLessonConflict(t, colSelected))
                {
                    t.bookSpace(userLoggedIn);
                    //System.out.println("BOOKED");
                }
                else
                {
                    JOptionPane.showMessageDialog(null, "Lesson Time Conflict", "CONFLICT", JOptionPane.WARNING_MESSAGE);
                }
            } 
        });
        
        JButton cancelBookingButton = new JButton("Cancel Booking");
        cancelBookingButton.addActionListener(e ->
        {
            LessonClass t = weekArray[weekNo -1].dayArray[colSelected].dayMap.get(roomNtime[rowSelected]);
            t.cancelBooking(userLoggedIn);
            //System.out.println("CANCELLED");
        });
        
        //######################################################################
        
        
        // table listener for mouse clicks
        table.addMouseListener(new MouseAdapter()
        {
            @Override
            public void mouseClicked(MouseEvent e)
            {
                int viewRow = table.rowAtPoint(e.getPoint());
                int viewCol = table.columnAtPoint(e.getPoint());
                int modelRow = table.convertRowIndexToModel(viewRow);
                int modelCol = table.convertColumnIndexToModel(viewCol);
                
                //System.out.println("clicked: " + table.getValueAt(modelRow, modelCol));
                rowSelected = modelRow;
                colSelected = modelCol;
                
                LessonClass t = weekArray[weekNo -1].dayArray[colSelected].dayMap.get(roomNtime[rowSelected]);
                                
                Object lesson = table.getValueAt(modelRow, modelCol);
                
                // m is an object to hold various button and info, determined below.
                Object[] m = { null, null, null, null, null, null, null, null, null, null, null };
                
                // lists to hold labels and combo boxes
                JLabel[] labelList = new JLabel[5];
                JComboBox[] comboList = new JComboBox[5];
                
                // ### FIRST - BUILD DATA TO DISPLAY ###########################
                
                // if user logged in is student, and lesson is not a parent appointment
                if (  lesson != null && userLoggedIn != null && loggedInRole == roles.STUDENT && t.subject != subjects.PARENT )
                {
                    // if user isnt already booked onto lesson
                    if ( !t.isUserBooked(userLoggedIn) )
                    {
                        // show lesson and booking button
                        m[0] = lesson;
                        m[1] = bookButton;
                    }

                    // if user already booked onto lesson
                    else if ( t.isUserBooked(userLoggedIn) )
                    {
                        // show lesson and cancel button
                        m[0] = lesson;
                        m[1] = cancelBookingButton;
                    }
                }
                // otherwise if user logged in is parent, and lesson is parent appointment
                else if ( userLoggedIn != null && loggedInRole == roles.PARENT && t.subject == subjects.PARENT )
                {
                    // if user isnt already booked onto lesson
                    if ( !t.isUserBooked(userLoggedIn) )
                    {
                        // show lesson info and booking button
                        m[0] = lesson;
                        m[1] = bookButton;
                    }

                    // if user is already booked
                    else if ( t.isUserBooked(userLoggedIn) )
                    {
                        // show lesson and cancel button
                        m[0] = lesson;
                        m[1] = cancelBookingButton;
                    }
                }
                
                // if user is tutor, allow marking of attendance
                else if ( userLoggedIn != null && loggedInRole == roles.TUTOR )
                {
                    for (int i = 0; i < t.register.length; i++)
                    {
                        if (t.register[i] != null)
                        {
                            labelList[i] = new JLabel(t.register[i]);
                            comboList[i] = new JComboBox<>(attendance);
                        }  
                    }
                    // sorry - i know this could be neater but not sure how to do
                    
                    m[0] = new JLabel("  MARK ATTENDANCE  ");
                    
                    m[1] = labelList[0]; m[2] = comboList[0];
                    m[3] = labelList[1]; m[4] = comboList[1];
                    m[5] = labelList[2]; m[6] = comboList[2];
                    m[7] = labelList[3]; m[8] = comboList[3];
                    m[9] = labelList[4]; m[10] = comboList[4];
                }
                
                // otherwise if nobody logged in just provide info
                else
                {
                    m[0] = lesson ;
                } 
                
                // #### SECOND - DISPLAY RELEVANT DATA AND DEAL WITH INPUT #####
                
                // if lesson is empty, do nothing, otherwise diplay the info
                // and buttons determined in preceding conditions
                if ( lesson != null)
                {
                    JOptionPane.showMessageDialog(null, m, "LESSON", JOptionPane.INFORMATION_MESSAGE);
                    
                    // finally react to attendance selections made by tutor
                    // LessonClass attendanceMap maps ID to attendance values
                    if (loggedInRole == roles.TUTOR)
                    {
                        for (int i = 0; i < labelList.length; i++)
                        {
                            if (comboList[i] != null)
                            {
                                if (comboList[i].getSelectedItem().equals("Unmodified"))
                                {
                                    // do nothing
                                }
                                
                                else if (comboList[i].getSelectedItem().equals("Present"))
                                {
                                    // mark as present 'P' value in hashmap
                                    t.attendLesson(labelList[i].getText());
                                }
                                
                                else if (comboList[i].getSelectedItem().equals("Absent"))
                                {
                                    // mark as absent 'A' value in hashmap
                                    t.missLesson(labelList[i].getText());
                                }   
                            }    
                        }
                    }         
                }
            }
        });

        
        // #### WEEK SELECTION BUTTONS ####
        // come last as refering to table model (dtm)
        // LEFT BUTTON
        JButton leftweekButton = new JButton("<");
        northPanel.add(leftweekButton, BorderLayout.WEST);
        leftweekButton.addActionListener( e -> 
        {
            DecWeek(weeknoLabel);
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
        
        // RIGHT BUTTON
        JButton rightweekButton = new JButton(">");
        northPanel.add(rightweekButton, BorderLayout.EAST);
        rightweekButton.addActionListener( e -> 
        {
            IncWeek(weeknoLabel); 
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
    
        
        // ############# EAST PANEL ############################################
        
        // top label
        JLabel displayLabel = new JLabel(" DISPLAY", JLabel.RIGHT);
        JLabel optionsLabel = new JLabel(" OPTIONS ", JLabel.LEFT);
        displayLabel.setFont(new Font("serif", Font.PLAIN, 20));
        optionsLabel.setFont(new Font("serif", Font.PLAIN, 20));
        
        // radio buttons
        JRadioButton bySubject = new JRadioButton("by Subect");
        JRadioButton byTutor = new JRadioButton("by Tutor");
        JRadioButton byAll = new JRadioButton("by All", true);
        
        // refresh button
        JButton refreshButton = new JButton("REFRESH DATA");
        refreshButton.setFont(new Font("serif", Font.BOLD, 13));
        refreshButton.setForeground(Color.red);
        
        // radio buttons grouped
        ButtonGroup buttonGroupA = new ButtonGroup();
        buttonGroupA.add(bySubject);
        buttonGroupA.add(byTutor);
        buttonGroupA.add(byAll);
        
        // combo boxes
        JComboBox<subjects> subjectCombo = new JComboBox<>(subjectsL);
        JComboBox<tutors> tutorCombo = new JComboBox<>(tutorsL);
        // disabled tutor & subject combo on start (default selected is byAll)
        subjectCombo.setEnabled(false);
        tutorCombo.setEnabled(false);
        
        // disable combo boxes depending on selection
        // BY SUBJECT
        bySubject.addActionListener(e -> 
        {
            tutorCombo.setEnabled(false);
            subjectCombo.setEnabled(true);
            bySubjectFlag = true;
            byTutorFlag = false;
            byAllFlag = false;
            subComb = (subjects) subjectCombo.getSelectedItem();
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
        
        // BY TUTOR
        byTutor.addActionListener(e -> 
        {
            subjectCombo.setEnabled(false);
            tutorCombo.setEnabled(true);
            bySubjectFlag = false;
            byTutorFlag = true;
            byAllFlag = false;
            tutComb = (tutors) tutorCombo.getSelectedItem();
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
        
        // BY ALL
        byAll.addActionListener( e ->
        {
            subjectCombo.setEnabled(false);
            tutorCombo.setEnabled(false);
            bySubjectFlag = false;
            byTutorFlag = false;
            byAllFlag = true;
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
        
        // refresh button updates data after booking / cancellation changes
        refreshButton.addActionListener( e -> 
        {
            MakeTableData();
            dtm.setDataVector(classes, days);
        });
        
        // #### ADD GUI ELEMENTS ####
        
        // add to eastpanel
        eastPanel.add(displayLabel);
        eastPanel.add(optionsLabel);
        eastPanel.add(bySubject);
        eastPanel.add(subjectCombo);
        eastPanel.add(byTutor);
        eastPanel.add(tutorCombo);
        eastPanel.add(byAll);
        eastPanel.add(refreshSubPanel);
        refreshSubPanel.add(refreshButton);
        
        // add to timetablepanel
        timetablePanel.add(northPanel, BorderLayout.NORTH);
        timetablePanel.add(centerPanel, BorderLayout.CENTER);
        timetablePanel.add(eastPanel, BorderLayout.EAST);
        
        return timetablePanel;
    }
 
    public void MakeTableData()
    {
        // zero out formatted data
        classes = ResetClasses();
        
        // 'WHATS ON' TAB SELECTED
        if (selectedTab == 0)
        {
            // iterate through days
            for (int i = 0; i < daysPerWeek; i++)
            {
                // iterate through times and rooms
                for (int j = 0; j < totalSlots; j++)
                {
                    LessonClass a = weekArray[(weekNo -1)].dayArray[i].dayMap.get(roomNtime[j]);
                    if (a != null)
                    {
                        if (byAllFlag == true)
                        {
                            classes[j][i] = a.toString();
                        }
                        else if (bySubjectFlag == true)
                        {
                            if (a.subject == subComb)
                            {
                                classes[j][i] = a.toString();
                            }
                        }
                        else if (byTutorFlag == true)
                        {
                            if (a.tutorName == tutComb)
                            {
                                classes[j][i] = a.toString();
                            }
                        }
                    }
                }
            }
        }
        // 'DIARY' TAB SELECTED
        else if (selectedTab == 1)
        {
            if (userLoggedIn != null)
            {                
                // iterate through days
                for (int i = 0; i < daysPerWeek; i++)
                {
                    // iterate through times and rooms
                    for (int j = 0; j < totalSlots; j++)
                    {
                        // pertinent tracks if ID is in register
                        boolean pertinent = false;
                        LessonClass a = weekArray[(weekNo -1)].dayArray[i].dayMap.get(roomNtime[j]);
                        if (a != null)
                        {
                            for (String s : a.register)
                            {
                                if (userLoggedIn.equals(s))
                                {
                                    pertinent = true;
                                }
                            }                            
                            
                            if (byAllFlag == true && pertinent == true)
                            {
                                classes[j][i] = a.toString();
                            }
                            else if (bySubjectFlag == true && pertinent == true)
                            {
                                if (a.subject == subComb)
                                {
                                    classes[j][i] = a.toString();
                                }
                            }
                            else if (byTutorFlag == true && pertinent == true)
                            {
                                if (a.tutorName == tutComb)
                                {
                                    classes[j][i] = a.toString();
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    
    // method to zero out formatted data displayed in timetable table
    public String[][] ResetClasses()
    {
        String[][] classesR = {
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null },
        { null, null, null, null, null }};
        
        return classesR;
    }
    
    // check if user is already attending a lesson at this time on this day
    public boolean DoesLessonConflict(LessonClass t, int day)
    {
        for (int j = 0; j < totalSlots; j++)
        {
            LessonClass a = weekArray[(weekNo -1)].dayArray[day].dayMap.get(roomNtime[j]);
            if (a != null)
            {
                if (a.isUserBooked(userLoggedIn))
                {
                    if (t.time == a.time)
                    {   
                        return true;
                    }
                }
            }
        }
        return false;
    }
    

    
    // view all registered users
    public void ShowAllUsers()
    {
        userList.forEach((s) -> { System.out.println(s); });
    }

    // show all lessons available with info
    public void AllWeekLessonReport(int weeks)
    {
        for (int i = 0; i < weeks; i++)
        {
            for (int j = 0; j < daysPerWeek; j++)
            {
                for (int k = 0; k < totalSlots; k++)
                {
                    LessonClass l = weekArray[(i)].dayArray[j].dayMap.get(roomNtime[k]);
                    
                    // if lesson exists, and isnt a parent appointment
                    if (l != null && l.subject != subjects.PARENT)
                    {
                        System.out.println("\n| WEEK: " + (i+1) + " | " + "DAY: " + days[j] 
                                + " | " + "TIME: " + l.time + "pm | " + l.room + " |");
                        System.out.println(l.toReport());
                    }
                }
            }
        }
    }
    
    // show all parent appointments with info
    public void AllWeekAppointmentReport(int weeks)
    {
        for (int i = 0; i < weeks; i++)
        {
            for (int j = 0; j < daysPerWeek; j++)
            {
                for (int k = 0; k < totalSlots; k++)
                {
                    LessonClass a = weekArray[(i)].dayArray[j].dayMap.get(roomNtime[k]);
                    
                    // if appointment exists and is parent appointment
                    if (a != null && a.subject == subjects.PARENT)
                    {
                        System.out.println("\n| WEEK: " + (i+1) + " | " + "DAY: " + days[j] 
                                + " | " + "TIME: " + a.time + "pm | " + a.room + " |");
                        System.out.println(a.toReport());
                        System.out.println("Visitor offspring ID: ");
                        
                        // track if anybody has booked this slot
                        boolean visitor = false;
                        
                        // for each entry in register
                        for (String s : a.register)
                        {
                            if (s != null)
                            {
                                System.out.print(s + "\n");
                                visitor = true;
                            }
                        }
                        // feedback for nobody booked this slot
                        if (visitor == false)
                        {
                            System.out.println("No visits booked");
                        }
                    }
                }
            }
        }
    }
    
    public void AllStudentAttendanceReport()
    {
        // iterate users
        for (UserClass s : userList)
        {
            // track individual stats
            int booked = 0;
            int attended = 0;
            int missed = 0;
            int cancelled = 0;
            
            // get users ID as string to compare to register
            String tid = String.valueOf(s.ID);
            
            // only interested in students
            if (s.role == roles.STUDENT)
            {
                // iterate through data model
                for (int i = 0; i < numberOfWeeks; i++)
                {
                    for (int j = 0; j < daysPerWeek; j++)
                    {
                        for (int k = 0; k < totalSlots; k++)
                        {
                            LessonClass a = weekArray[(i)].dayArray[j].dayMap.get(roomNtime[k]);
                            
                            // if the attendancemap contains id as key
                            if (a != null && a.attendanceMap.containsKey(tid))
                            {
                                // get regsiter status as x
                                String x = a.attendanceMap.get(tid);
                                if (x != null)
                                {
                                    switch (x) {
                                        case "B": // booked
                                            booked += 1;
                                            break;
                                        case "P":  // present
                                            attended += 1;
                                            // must have booked to attend
                                            booked += 1;
                                            break;
                                        case "A": // absent
                                            missed += 1;
                                            // must have booked to have missed
                                            booked += 1;
                                            break;
                                        case "C": // cancelled
                                            cancelled += 1;
                                            // must have booked to have cancelled
                                            booked += 1;
                                            break;
                                        default:
                                            break;
                                    }
                                }
                            }
                        }
                    }
                }
                // output info
                System.out.println("Student: " + s.firstName + " " + s.surname);
                System.out.println("ID: " + s.ID);
                System.out.println("Booked lessons: " + booked);
                System.out.println("Attended lessons: " + attended);
                System.out.println("Missed lessons: " + missed);
                System.out.println("Cancelled lessons: " + cancelled);
                System.out.println("\n");
            }
        }
    }
}
