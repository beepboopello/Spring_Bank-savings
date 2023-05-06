package ptit.savings;

import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;
import ptit.savings.model.Staff;
import ptit.savings.repository.StaffRepository;

@RunWith(SpringRunner.class)
@DataJpaTest
public class StaffRepoTest {


	@Autowired
	StaffRepository staffRepository;

	@Autowired
	private TestEntityManager entityManager;

	@Test
	public void should_null_if_empty(){
		int count = staffRepository.findAll().size();
		Assert.assertEquals(count, 0);
	}

	@Test
	public void should_find_all(){
		Staff a = new Staff("fname", "lname", "email@email.com", "username", "password");
		entityManager.persist(a);

		Staff b = new Staff("fname", "lname", "email@email.com", "username", "password");
		entityManager.persist(b);

		int count = staffRepository.findAll().size();
		Assert.assertEquals(count, 2);
	}

	@Test
	public void should_find_by_username(){
		Staff a = new Staff("fname", "lname", "email@email.com", "username", "password");
		entityManager.persist(a);

		Assert.assertEquals(staffRepository.findByUsername("username").get(0).getUsername(), "username");
	}

	@Test
	public void should_find_by_verified(){
		Staff a = new Staff("fname", "lname", "email@email.com", "username", "password");
		a.setVerified(1);
		entityManager.persist(a);

		Assert.assertEquals(staffRepository.findByVerified(1).get(0).getVerified(), 1);
	}

	@Test
	public void should_find_by_token(){
		Staff a = new Staff("fname", "lname", "email@email.com", "username", "password");
		a.setToken("extremelylongunreadabletoken");
		entityManager.persist(a);

		Assert.assertEquals(staffRepository.findByToken("extremelylongunreadabletoken").get(0).getToken(), "extremelylongunreadabletoken");
	}

}
