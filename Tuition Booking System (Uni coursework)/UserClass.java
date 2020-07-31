/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

public class UserClass 
{
    public static int UID = 90000;
    
    public final String firstName;
    public final String surname;
    public final String telephone;
    public final String address;
    public final int ID;
    public roles role;
    
    public enum roles
    {
        TUTOR,
        STUDENT,
        PARENT
    }
    
    public UserClass (String firstName, String surname, String telephone, 
            String address)
    {
        this.firstName = firstName;
        this.surname = surname;
        this.telephone = telephone;
        this.address = address;
        this.ID = UID;
        this.role = null;
        
        UID += 1;
    }
    
    @Override
    public String toString () 
    {
    return "\nUser: " + firstName + " " + surname + 
            "\nTel: (" + telephone + ")" + 
            "\nAddress: " + address +
            "\nID: " + ID + 
            "\nRole: " + role + "\n";
    }
}
