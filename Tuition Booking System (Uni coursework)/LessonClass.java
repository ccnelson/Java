/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

import java.util.HashMap;
import javax.swing.JOptionPane;

public class LessonClass
{
    public final subjects subject;
    public final rooms room;
    public final String day;
    public final tutors tutorName;
    public final int capacity;
    public final int time;
    public int freeSpaces;
    // fixed size register reflecting capacity storing attending students IDs
    public String[] register;
    // attendancemap stores ID with booking status:
    // Key: ID - Values: B = booked, P = present, A = absent, C = cancelled
    public HashMap<String, String> attendanceMap = new HashMap<>();
    
    // enumerators providing fixed values for subjects, rooms, tutors.
    public enum subjects
    {
        MATHS,
        ENGLISH,
        FRENCH,
        SCIENCE,
        COMPUTERS,
        PARENT
    }
    
    public enum rooms
    {
        ROOM_A,
        ROOM_B,
        ROOM_C
    }
    
    public enum tutors
    {
        Dave_Smith,
        Sue_Willis,
        John_Jove,
        Bob_Bobson,
        Sally_Jones
    }
    
    public LessonClass (subjects subject, rooms room, String day, 
            int time, tutors tutorName, int capacity)
    {
        this.subject = subject;
        this.room = room;
        this.day = day;
        this.tutorName = tutorName;
        this.capacity = capacity;
        this.register = new String[capacity];
        this.time = time;
    }
    
    @Override
    public String toString () 
    {
        countAttendees();
        return subject + "            " +
                "\nRoom: " + room + 
                "\nDay: " + day +
                "\nTime: " + time + "pm" +
                "\nTutor: " + tutorName +
                "\nCapacity: " + capacity +
                "\nSpaces avaiable: " + freeSpaces +
                "\n";
    }
    
    public String toReport () 
    {
        countAttendees();
        return "Subject: " + subject +
                "\nTutor: " + tutorName +
                "\nAttendees: " + (capacity - freeSpaces) + " \\ " + capacity;
    }
    
        
    
    
    public void countAttendees()
    {
        // update freespaces value
        int count = 0;
        for (String s : register)
        {
            if (s != null)
            {
                count += 1;
            }
        }
        freeSpaces = capacity - count;
    }
    
    public void bookSpace(String id)
    {
        countAttendees();
        if (freeSpaces > 0)
        {
            for (int i = 0; i < register.length; i++)
            {
                if (register[i] == null)
                {
                    register[i] = id;
                    break;
                }
            }
            // B for booked
            attendanceMap.put(id, "B");
        }
        else
        {
            JOptionPane.showMessageDialog(null, "NO FREE SPACES", "LESSON FULL", JOptionPane.WARNING_MESSAGE);
        }   
    }
    
    public void cancelBooking(String id)
    {
        // better to start with invalid value - just in case
        int bookPos = 99;
        for (int i = 0; i < register.length; i++)
        {
            if (register[i] != null)
            {
                if (register[i].equals(id))
                {
                    bookPos = i;
                }
            }
        }
        register[bookPos] = null;
        // C for cancelled
        attendanceMap.put(id, "C");
    }
    
    public void attendLesson(String id)
    {
        // P for present
        attendanceMap.put(id, "P");
    }
    
    public void missLesson(String id)
    {
        // A for absent
        attendanceMap.put(id, "A");
    }
    
    
    public boolean isUserBooked(String id)
    {
        // has user already booked a place
        for (String r : register) {
            if (r != null) {
                if (r.equals(id)) {
                    return true;
                }
            }
        }
        return false;
    }
}
