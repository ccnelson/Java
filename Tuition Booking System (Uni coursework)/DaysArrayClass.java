/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;


public class DaysArrayClass 
{
    public static int weekCount = 0;
    public DayObjectClass[] dayArray = new DayObjectClass[5];;
    public int weekNo;
    
    public DaysArrayClass()
    {
        // populate with days
        this.dayArray[0] = new DayObjectClass("Mon");
        this.dayArray[1] = new DayObjectClass("Tue");
        this.dayArray[2] = new DayObjectClass("Wed");
        this.dayArray[3] = new DayObjectClass("Thu");
        this.dayArray[4] = new DayObjectClass("Fri");
        
        // object identifier
        this.weekNo = weekCount;
        // increment count
        weekCount += 1;
    }
}
