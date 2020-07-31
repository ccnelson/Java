/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

import javax.swing.JLabel;
import javax.swing.JPanel;
import org.junit.Test;
import static org.junit.Assert.*;


public class ITC_tuition_booking_systemTest {
    
    public ITC_tuition_booking_systemTest() {
    }
    
    @Test
    public void testStartup() 
    {
        System.out.println("Startup");
        ITC_tuition_booking_system instance = new ITC_tuition_booking_system();
        instance.Startup();
        
        // ### TEST WEEKARRAY POPULATED ###
        for (int i = 0; i < 8; i++)
        {
            assertNotNull(instance.weekArray[i]);
        }
        // ### CHECK LOGIN DETAILS NULL ###
        assertNull(instance.loggedInName);
        assertNull(instance.loggedInRole);
        assertNull(instance.userLoggedIn);
        
        // ## TEST LESSON CONFLICT ###
        // get a lesson
        LessonClass t = instance.weekArray[0].dayArray[1].dayMap.get("5A");
        // assign a user
        instance.userLoggedIn = "90013";
        // check conflict
        assertTrue(instance.DoesLessonConflict(t, 1));
        
    }
    
    @Test
    public void testResetClasses()
    {
        System.out.println("Reset classes");
        ITC_tuition_booking_system instance = new ITC_tuition_booking_system();
        String[][] testclasses = instance.ResetClasses();
        
        // ### TEST RESET PERFORMS PROPERLY ###
        for (int i = 0; i < 12; i++)
        {
            for (int j = 0; j < 5; j++)
            {
                assertNull(testclasses[i][j]);
            }
        } 
    }
    
    @Test
    public void testCreateTimeTablePanel()
    {
        System.out.println("Create timetablepanel");
        ITC_tuition_booking_system instance = new ITC_tuition_booking_system();
        JPanel x = instance.CreateTimeTablePanel("test", instance.classes);
        
        // ### TEST FUNCTION RETURNS CORRECT OBJECT ###
        assertTrue(x instanceof JPanel);
    }

    @Test
    public void testIncWeek()
    {
        System.out.println("Increment week");
        ITC_tuition_booking_system instance = new ITC_tuition_booking_system();
        instance.weekNo = 8;
        JLabel x = new JLabel("");
        instance.IncWeek(x);
        
        // ### TEST INC WEEK PREVENTS OVERFLOW ###
        assertEquals(instance.weekNo, 1, 0);
    }
    
    @Test
    public void testDecWeek()
    {
        System.out.println("Decrement week");
        ITC_tuition_booking_system instance = new ITC_tuition_booking_system();
        instance.weekNo = 1;
        JLabel x = new JLabel("");
        instance.DecWeek(x);
        
        // ### TEST DEC WEEK PREVENTS UNDERFLOW ###
        assertEquals(instance.weekNo, 8, 0);
    }
 
}
