package edu.studyup.serviceImpl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

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
		//Create Student
		Student student = new Student();
		student.setFirstName("John");
		student.setLastName("Doe");
		student.setEmail("JohnDoe@email.com");
		student.setId(1);
		
		//Create Event1
		Event event = new Event();
		event.setEventID(1);
		event.setDate(new Date());
		event.setName("Event 1");
		Location location = new Location(-122, 37);
		event.setLocation(location);
		List<Student> eventStudents = new ArrayList<>();
		eventStudents.add(student);
		event.setStudents(eventStudents);
		
		
		DataStorage.eventData.put(event.getEventID(), event);
	}

	@AfterEach
	void tearDown() throws Exception {
		DataStorage.eventData.clear();
	}

	//Test 1
	@Test
	void testUpdateEventName_GoodCase() throws StudyUpException {
		int eventID = 1;
		eventServiceImpl.updateEventName(eventID, "Renamed Event 1");
		assertEquals("Renamed Event 1", DataStorage.eventData.get(eventID).getName());
	}
	
	//Test 2
	@Test
	void testUpdateEvent_WrongEventID_badCase() {
		int eventID = 3;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "Renamed Event 3");
		  });
	}
	
	//Test 3
	@Test
	void testUpdateEventName_LongName_badCase() {
		int eventID = 1;
		Assertions.assertThrows(StudyUpException.class, () -> {
			eventServiceImpl.updateEventName(eventID, "This is too long of a name for the function.");
		  });
	}
	
	
	//Test 4 
	@Test
	void testGetActiveEvents_GoodCase() {
		List<Event> events = new ArrayList<>();
		events.add(DataStorage.eventData.get(1));
		assertEquals(events, eventServiceImpl.getActiveEvents());
	}
	
	//Test 5
	@Test
	void testGetPastEvents_GoodCase() {
		List<Event> events = new ArrayList<>();
		events.add(DataStorage.eventData.get(1));
		
		//Create Event2
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(120, 1 , 1));
		event2.setName("Event 2");
		Location location = new Location(-122, 37);
		event2.setLocation(location);
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		assertEquals(events, eventServiceImpl.getPastEvents());
	}
	
	//Test 6
	@Test
	void testAddStudentToEvent_NoEvent_BadCase() {
		Assertions.assertThrows(StudyUpException.class, () -> {
			Student student = new Student();
			eventServiceImpl.addStudentToEvent(student, 3);
		  });
	}
	
	//Test 7
	@Test
	void testAddStudentToEvent_NoStudents_GoodCase() throws StudyUpException {
		//Create Event2
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(120, 1 , 1));
		event2.setName("Event 2");
		Location location = new Location(-122, 37);
		event2.setLocation(location);
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		//Create Student
		Student student2 = new Student();
		student2.setFirstName("John");
		student2.setLastName("Doe");
		student2.setEmail("JohnDoe@email.com");
		student2.setId(1);
		
		assertEquals(event2, eventServiceImpl.addStudentToEvent(student2, 2));
	}
	
	//Test 8
	@Test
	void testAddStudentToEvent_WithStudents_GoodCase() throws StudyUpException {
		Event currEvent = DataStorage.eventData.get(1);
		
		//Create Student
		Student student2 = new Student();
		student2.setFirstName("Jim");
		student2.setLastName("Boe");
		student2.setEmail("JohnDoe@email.com");
		student2.setId(2);
		
		assertEquals(currEvent, eventServiceImpl.addStudentToEvent(student2, 1));
	}
	
	//Test 9
	//Tests whether adding the exact same student(with same name, student ID, and email) will add a duplicate student to the event.
	@Test
	void testAddStudentToEvent_SameStudent_Bug() throws StudyUpException {
		Event currEvent = DataStorage.eventData.get(1);
		List<Student> currStudents = new ArrayList<>(currEvent.getStudents());

		//Create Student
		Student student2 = new Student();
		student2.setFirstName("John");
		student2.setLastName("Doe");
		student2.setEmail("JohnDoe@email.com");
		student2.setId(1);
		
		assertEquals(currStudents, eventServiceImpl.addStudentToEvent(student2, 1).getStudents());
		
		
	}
	
	//Test 10
	@Test
	void testDeleteEvent_GoodCase() {
		Map<Integer, Event> noEvents = new HashMap<Integer, Event>();
		eventServiceImpl.deleteEvent(1);
		assertEquals(noEvents, DataStorage.eventData);
	}
	
	//Test 11
	@Test
	void testGetActiveEvents_NoEvents_GoodCase() {
		List<Event> events = new ArrayList<>();
		eventServiceImpl.deleteEvent(1);
		assertEquals(events, eventServiceImpl.getActiveEvents());
	}
	
	//Test 12
	//An event should only be able to hold 2 students. But addStudentToEvent is not throwing an exception when a third student is added.
	@Test
	void  testAddStudentToEvent_ThreeStudents_Bug() {
		Assertions.assertThrows(StudyUpException.class, () -> {
			//Create Students
			Student student2 = new Student();
			student2.setFirstName("Harry");
			student2.setLastName("Potter");
			student2.setEmail("hp@email.com");
			student2.setId(2);
			
			Student student3 = new Student();
			student3.setFirstName("Ron");
			student3.setLastName("Weasly");
			student3.setEmail("rw@email.com");
			student3.setId(3);
			
			eventServiceImpl.addStudentToEvent(student2, 1);
			eventServiceImpl.addStudentToEvent(student3, 1);
		  });
	}
	
	//Test 13
	//getActiveEvents should only return the events that have not happened yet and not those that have already happened.
	@Test
	void testGetActiveEvents_oldEvent_bug() {
		Event event2 = new Event();
		event2.setEventID(2);
		event2.setDate(new Date(20, 1 , 1));
		event2.setName("Event 2");
		Location location = new Location(-122, 37);
		event2.setLocation(location);
		DataStorage.eventData.put(event2.getEventID(), event2);
		
		List<Event> events = new ArrayList<>();
		events.add(DataStorage.eventData.get(1));
		
		assertEquals(events, eventServiceImpl.getActiveEvents());
	}
	
	//Test 14
	//The updateEventName should accept a name of length 20 but it returns an exception instead.
	@Test
	void testUpdateEventName_MaxLen_Bug() {
		Assertions.assertDoesNotThrow(() -> {
			int eventID = 1;	
			eventServiceImpl.updateEventName(eventID, "This is the max len!");//20 char string
		  });
	}
	

}
