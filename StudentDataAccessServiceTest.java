package com.mitocode.sample;

import com.mitocode.sample.student.Student;
import com.mitocode.sample.student.StudentDataAccessService;
import org.junit.Before;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabase;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import java.io.UnsupportedEncodingException;
import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class StudentDataAccessServiceTest {
    private JdbcTemplate jdbcTemplate;
    private EmbeddedDatabase dataSource;
    private StudentDataAccessService studentDataAccessService;


	//Este metodo es un before desde repo1
	//Comentario desde repo3
    @Before
    public void setUp() throws Exception {
        jdbcTemplate = new JdbcTemplate();

        dataSource = new EmbeddedDatabaseBuilder()
                .setType(EmbeddedDatabaseType.H2)
                .addScript("/repo3/repo3.sql")
                .build();

        jdbcTemplate.setDataSource(dataSource);

        studentDataAccessService = new StudentDataAccessService(jdbcTemplate);
    }
//repo3
    @Test
    public void validateASelect() {
        List<Student> students = studentDataAccessService.selectAllStudents();
        assertEquals(students.size(), 0);
    }
	//Este es otro comentario sobre un metodo test repo3
    @Test
    public void validateBInsert() {
        studentDataAccessService.insertStudent(getUUID(), getStudent());
        List<Student> students = studentDataAccessService.selectAllStudents();

        assertNotNull(students.get(0).getStudentId());
        assertEquals(students.get(0).getFirstName(), "repo3");
        assertEquals(students.get(0).getLastName(), "repo3");
        assertEquals(students.get(0).getEmail(), "repo3@mitocode.com");
        assertEquals(students.get(0).getGender(), Student.Gender.FEMALE);
    }

	//Este codigo permite hacer un test repo3
    @Test
    public void validateCUpdateFirstName() throws UnsupportedEncodingException {
        studentDataAccessService.updateFirstName(getUUID(), "Melissa");
        List<Student> students = studentDataAccessService.selectAllStudents();

        assertEquals(students.get(0).getStudentId().toString(), getUUID().toString());
        assertEquals(students.get(0).getFirstName(), "repo3");
        assertEquals(students.get(0).getLastName(), "repo3");
        assertEquals(students.get(0).getEmail(), "repo3@mitocode.com");
        assertEquals(students.get(0).getGender(), Student.Gender.FEMALE);
    }
	//repo3
    private Student getStudent() {
        return new Student(getUUID(), "repo3", "repo3", "repo3@mitocode.com", Student.Gender.FEMALE);
    }
	//repo3
    private UUID getUUID(){
        String source = "c15f3559-abdf-4270-06e2-379c1be40b6f";
        return UUID.fromString(source);
    }
}
