package edu.studyup.serviceImpl;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import edu.studyup.entity.Event;
import edu.studyup.entity.Location;
import edu.studyup.entity.Student;
import edu.studyup.util.DataStorage;
import edu.studyup.util.StudyUpException;

class EventServiceImplTest {
    EventServiceImpl eventServiceImpl;
    
    @BeforeAll
    static void setUpBeforeClass() throws Exception {
    	
    }
    
    @AfterAll
    static void tearDownAfterClass() throws Exception {
    	
    }
    
    @BeforeEach
    void setUp() throws Exception {
        eventServiceImpl = new EventServiceImpl();
        
        Event event = new Event();
        event.setEventID(1);
        event.setDate(new Date());
        event.setName("Event 1");
        Location location = new Location(-122, 37);
        event.setLocation(location);
       
        DataStorage.eventData.put(event.getEventID(), event);  
    }
    
    @AfterEach
    void tearDown() throws Exception {
        DataStorage.eventData.clear();
    }
    //------------------------------------------------------------  
    @Test
    void testUpdateEventName_GoodCase() throws StudyUpException {
        int eventID = 1;
        eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
        assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
    }
    
    @Test
    void testUpdateEvent_WrongEventID_badCase() {
        int eventID = 3;
        Assertions.assertThrows(StudyUpException.class, () -> {
            eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
          });
    }
    //------------------------------------------------------------
    @Test
    void testEventNameLength_GoodCase_EqualsTwenty() throws StudyUpException {
        int eventID = 1;
        String eventName = "This line is 20-char";
        eventServiceImpl.updateEventName(eventID, eventName);
        assertEquals(eventName, DataStorage.eventData.get(eventID).getName());
    }
    
    @Test
    void testEventNameLength_BadCase_EqualsTwentyOrMore() throws StudyUpException {
        int eventID = 1;
        String eventName = "This line is more than 20 chars";
        Assertions.assertThrows(StudyUpException.class, () -> {
            eventServiceImpl.updateEventName(eventID, eventName);
          });
    }
    
    @Test
    void testEventNameLength_BadCase_NoEvent() throws StudyUpException {
    	eventServiceImpl.deleteEvent(1);
        int eventID = 1;
        String eventName = "";
        Assertions.assertThrows(StudyUpException.class, () -> {
            eventServiceImpl.updateEventName(eventID, eventName);
          });
    }
    
    @Test
    void testEventNameLength_GoodCase_EqualsTen() throws StudyUpException{
        int eventID = 1;
        String eventName = "This line is 10";
        eventServiceImpl.updateEventName(eventID, eventName);
        assertEquals(eventName, DataStorage.eventData.get(eventID).getName());
    }
    //------------------------------------------------------------
    @Test
    void testAddStudentToEvent_GoodCase_OneStudent() throws StudyUpException{
        int eventID = 1;
        
        Student student1 = new Student();
        student1.setFirstName("Kevin");
        student1.setLastName("Benelli");
        student1.setEmail("kevin.e.benelli@email.com");
        student1.setId(1);
        
        eventServiceImpl.addStudentToEvent(student1, eventID);
        assertTrue(DataStorage.eventData.get(eventID).getStudents().contains(student1));
    }
    
    @Test
    void testAddStudentToEvent_GoodCase_TwoStudents() throws StudyUpException{      
        Event event = new Event();
        event.setEventID(2);
        event.setDate(new Date());
        event.setName("Event 2");
        Location location = new Location(-122, 37);
        event.setLocation(location);
        
        DataStorage.eventData.put(event.getEventID(), event);
        int eventID = event.getEventID();
                
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("JohnDoe@email.com");
        student1.setId(1);
        
        Student student2 = new Student();
        student2.setFirstName("Kevin");
        student2.setLastName("Benelli");
        student2.setEmail("kevin.e.benelli@email.com");
        student2.setId(2);
        
        eventServiceImpl.addStudentToEvent(student1, eventID);
        assertTrue(DataStorage.eventData.get(eventID).getStudents().contains(student1));
        eventServiceImpl.addStudentToEvent(student2, eventID);
        assertTrue(DataStorage.eventData.get(eventID).getStudents().contains(student2));
    }
    
    @Test
    void testAddStudentToEvent_BadCase_ThreeStudents() throws StudyUpException{
    	int eventID = 1;

        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("JohnDoe@email.com");
        student1.setId(1);
        
        
        Student student2 = new Student();
        student2.setFirstName("Kevin");
        student2.setLastName("Benelli");
        student2.setEmail("kevin.e.benelli@email.com");
        student2.setId(2);
        
        
        Student student3 = new Student();
        student1.setFirstName("Jane");
        student1.setLastName("Doe");
        student1.setEmail("JohnDoe@email.com");
        student1.setId(3);
    	
        eventServiceImpl.addStudentToEvent(student1, eventID);
        eventServiceImpl.addStudentToEvent(student2, eventID);
        eventServiceImpl.addStudentToEvent(student3, eventID);   
        
        assertFalse(DataStorage.eventData.get(eventID).getStudents().contains(student3));
    }

    @Test
    void testAddStudentToEvent_BadCase_ZeroEvent() throws StudyUpException{
        int eventID = 1;
    	eventServiceImpl.deleteEvent(1);
            
        Student student1 = new Student();
        student1.setFirstName("John");
        student1.setLastName("Doe");
        student1.setEmail("JohnDoe@email.com");
        student1.setId(1);
        
        Assertions.assertThrows(StudyUpException.class, () -> {
            eventServiceImpl.addStudentToEvent(student1, eventID);
          });
    }
    //------------------------------------------------------------
    @Test
    void testGetActiveEvents_BadCase_AllPast() throws StudyUpException{ 
        Calendar past = Calendar.getInstance();
        past.add(Calendar.DATE, -60);

        Event event2 = new Event();
        event2.setEventID(3);
        event2.setDate(past.getTime());
        event2.setName("Event 2");
        
        Event event3 = new Event();
        event3.setEventID(5);
        event3.setDate(past.getTime());
        event3.setName("Event 3");
        
        DataStorage.eventData.put(event2.getEventID(), event2);
        DataStorage.eventData.put(event3.getEventID(), event3);
       
        assertFalse(eventServiceImpl.getActiveEvents().containsAll(eventServiceImpl.getPastEvents()));
    }
    
    @Test
    void testPastActiveEvents_GoodCase_Allfutureure() throws StudyUpException{ 
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DATE, 60);
       
        Event event1 = new Event();
        event1.setEventID(2);
        event1.setDate(future.getTime());
        event1.setName("Event 2");
        
        Event event2 = new Event();
        event2.setEventID(4);
        event2.setDate(future.getTime());
        event2.setName("Event 3");
        
        DataStorage.eventData.put(event1.getEventID(), event1);
        DataStorage.eventData.put(event2.getEventID(), event2);
        
        assertFalse(eventServiceImpl.getPastEvents().containsAll(eventServiceImpl.getActiveEvents()));
    }
    
    @Test
    void testgetActiveEvents_GoodCase_NotEqual() throws StudyUpException{  
    	
        Calendar past = Calendar.getInstance();
        past.add(Calendar.DATE, -60);
        
        Calendar future = Calendar.getInstance();
        future.add(Calendar.DATE, 60);
       
        Event event1 = new Event();
        event1.setEventID(2);
        event1.setDate(future.getTime());
        event1.setName("Event 2");

        Event event2 = new Event();
        event2.setEventID(3);
        event2.setDate(past.getTime());
        event2.setName("Event 3");

        Event event3 = new Event();
        event3.setEventID(4);
        event3.setDate(future.getTime());
        event3.setName("Event 4");
        
        Event event4 = new Event();
        event4.setEventID(5);
        event4.setDate(past.getTime());
        event4.setName("Event 5");
        
        DataStorage.eventData.put(event1.getEventID(), event1);
        DataStorage.eventData.put(event2.getEventID(), event2);
        DataStorage.eventData.put(event3.getEventID(), event3);
        DataStorage.eventData.put(event4.getEventID(), event4);

        assertNotEquals(eventServiceImpl.getActiveEvents(),(eventServiceImpl.getPastEvents()));
    }
}