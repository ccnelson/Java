/**
 * @author Chris Nelson UoH 2020
 * PSEP CW1
 */

package itc_tuition_booking_system;

import java.util.ArrayList;


public class TutorClass extends UserClass
{    
    public ArrayList<LessonClass.subjects> tutorSubjects;

    public TutorClass (String firstName, String surname, String telephone, 
            String address, ArrayList<LessonClass.subjects> subj)
    {
        super(firstName, surname, telephone, address);
        this.role = roles.TUTOR;
        this.tutorSubjects = subj;
    }    
    
    @Override
    public String toString () 
    {
    return "\nUser: " + firstName + " " + surname + 
            "\nTel: (" + telephone + ")" + 
            "\nAddress: " + address +
            "\nID: " + ID + 
            "\nRole: " + role + 
            "\nSubjects: " + tutorSubjects + "\n";
    }
}
