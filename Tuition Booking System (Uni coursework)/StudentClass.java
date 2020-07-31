/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;


public class StudentClass extends UserClass
{
    public StudentClass(String firstName, String surname, String telephone, String address)
    {
        super(firstName, surname, telephone, address);
        this.role = roles.STUDENT;
    }
    
}
