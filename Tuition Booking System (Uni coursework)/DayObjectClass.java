/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

import java.util.HashMap;


public class DayObjectClass 
{
    // unique key pair values holding nulls for empty periods
    public HashMap<String, LessonClass> dayMap = new HashMap<>();
    // store which day
    public String day;
    
    public DayObjectClass(String day)
    {
        this.day = day;
        // fill hashmap with nulls to indicate empty periods
        // number-letter indicates hour-room
        dayMap.put("5A", null);
        dayMap.put("6A", null);
        dayMap.put("7A", null);
        dayMap.put("8A", null);
        
        dayMap.put("5B", null);
        dayMap.put("6B", null);
        dayMap.put("7B", null);
        dayMap.put("8B", null);
        
        dayMap.put("5C", null);
        dayMap.put("6C", null);
        dayMap.put("7C", null);
        dayMap.put("8C", null);
  
    }
}
